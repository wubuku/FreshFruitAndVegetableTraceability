# 微调脚本


```python
import os
import json
import torch
import wandb
import gc  # 用于主动垃圾收集
from pathlib import Path
import psutil

# ==================== 核心内存优化设置 ====================
# 这些设置对解决接近尾声时崩溃的问题至关重要
os.environ["TOKENIZERS_PARALLELISM"] = "false"   # 避免tokenizer并行导致的内存压力
os.environ["PYTORCH_ENABLE_MPS_FALLBACK"] = "1"  # 启用回退选项，避免未实现的操作报错
os.environ["PYTORCH_MPS_HIGH_WATERMARK_RATIO"] = "0.0"  # 完全禁用内存上限
os.environ["PYTORCH_MPS_LOW_WATERMARK_RATIO"] = "0.0"   # 完全禁用内存下限

# # 硬编码开关，设置为True可跳过初次推理
# SKIP_INITIAL_INFERENCE = True #False

# 定义内存清理函数
def clean_memory():
    """清理GPU和CPU内存缓存，但只在内存压力大时执行"""
    # 只有当内存使用率超过85%时才执行清理
    if psutil.virtual_memory().percent > 85:
        gc.collect()  # 强制Python垃圾收集
        if torch.backends.mps.is_available():
            torch.mps.empty_cache()  # 清理MPS缓存

# 可选：登录wandb进行实验跟踪
# wandb.login(key="你的wandb.ai网站上的token")
# 初始化wandb项目
run = wandb.init(
    project='Lora-DeepSeek-R1-Distill-Qwen-14B-Mac',
    job_type="training",
    anonymous="allow"
)

####################################################################################################
# 1.加载模型

from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig
from peft import prepare_model_for_kbit_training, LoraConfig, get_peft_model

# 内存优化但不过度降低性能的平衡参数
max_seq_length = 2048  # 比原始的3072小，但仍能维持较好的上下文窗口
dtype = torch.float32  # 改为始终使用float32以避免数据类型不匹配问题

# 重要: 使用os.path.expanduser()展开路径中的~符号
model_path = os.path.expanduser("~/Documents/models/deepseek-ai/DeepSeek-R1-Distill-Qwen-14B")
# 确保目录存在
if not os.path.exists(model_path):
    raise ValueError(f"模型路径不存在: {model_path}，请检查下载是否完成或路径是否正确")

print(f"正在从本地路径加载模型: {model_path}")

# 加载分词器
tokenizer = AutoTokenizer.from_pretrained(model_path, trust_remote_code=True)
tokenizer.pad_token = tokenizer.eos_token

# 加载预训练模型 - 添加一些关键的内存优化选项
print("开始加载模型，这可能需要几分钟...")
model = AutoModelForCausalLM.from_pretrained(
    model_path,
    torch_dtype=torch.float32,  # 改为使用float32以避免数据类型不匹配问题
    device_map="mps",  # 使用Apple Silicon的Metal性能着色器
    trust_remote_code=True,
    # 内存优化选项
    low_cpu_mem_usage=True,     # 降低CPU内存使用
)

# 加载后清理内存
clean_memory()
print(f"Model loaded: {model.__class__.__name__}")

####################################################################################################
# 2. 定义提示模板，并在微调前做一次推理

prompt_style = """以下是描述任务的指令，以及提供更多上下文的输入。
  请写出恰当完成该请求的回答。
  在回答之前，请仔细思考问题，并创建一个逐步的思维链，以确保回答合乎逻辑且准确。
  ### Instruction:
  你是一位在临床推理、诊断和治疗计划方面具有专业知识的医学专家。
  请回答以下医学问题。
  ### Question:
  {}
  ### Response:
  <think>{}"""
train_prompt_style = prompt_style + """
  </think>
  {}"""

# 测试用医学问题
question = "一名70岁的男性患者因胸痛伴呕吐16小时就医，心电图显示下壁导联和右胸导联ST段抬高0.1~0.3mV，经补液后血压降至80/60mmHg，患者出现呼吸困难和不能平卧的症状，体检发现双肺有大量水泡音。在这种情况下，最恰当的药物处理是什么？"


# 执行初次推理
#model.eval()
# if not SKIP_INITIAL_INFERENCE:
#     # 只有当SKIP_INITIAL_INFERENCE=False时才执行推理
#     with torch.no_grad():
#         # 准备输入
#         model_input = prompt_style.format(question, "")
#         inputs = tokenizer(model_input, return_tensors="pt").to("mps")
        
#         # 使用智能推理
#         outputs = smart_inference(model, inputs, max_new_tokens=1200, temperature=0.7)
        
#         # 解码输出
#         response = tokenizer.decode(outputs[0], skip_special_tokens=True)
#         print("### 微调前模型推理结果：")
#         print(response.split("### Response:")[1])
# else:
#     print("已跳过初次推理，直接开始训练流程...")

# 清理内存
clean_memory()

####################################################################################################
# 3. 应用LoRA进行微调准备

# 配置LoRA - 保持原始的target_modules但略微减小r值
target_modules = [
    "q_proj", "k_proj", "v_proj", "o_proj", 
    "gate_proj", "up_proj", "down_proj",
]

# 创建LoRA配置 - 使用稍小的r值以节省内存
lora_config = LoraConfig(
    r=16,                      # 从24减小到16，更好地平衡内存使用和性能
    lora_alpha=16,             # 原始alpha值
    target_modules=target_modules,
    lora_dropout=0,            # 原始dropout
    bias="none",               # 原始bias设置
    task_type="CAUSAL_LM",     # 任务类型不变
    inference_mode=False,      # 训练模式
)

# 安全的参数转换函数 - 使用统一的数据类型
def safe_prepare_model_for_kbit_training(model):
    """安全地准备模型进行量化训练，批量处理参数以避免内存峰值"""
    # 启用梯度检查点以减少内存使用
    model.gradient_checkpointing_enable()
    # 确保参数缓存是禁用的，以避免与梯度检查点冲突
    model.config.use_cache = False
    
    print("分批处理参数转换，避免内存峰值...")
    
    # 首先处理注意力层
    print("处理注意力层参数...")
    attention_modules = ["q_proj", "k_proj", "v_proj", "o_proj"]
    for name, module in model.named_modules():
        if any(target in name for target in attention_modules):
            for param in module.parameters():
                if param.requires_grad:
                    # 确保统一使用float32类型
                    param.data = param.data.to(torch.float32)
            # 每个模块后清理内存
            clean_memory()
    
    # 处理前馈层
    print("处理前馈层参数...")
    feedforward_modules = ["gate_proj", "up_proj", "down_proj"]
    for name, module in model.named_modules():
        if any(target in name for target in feedforward_modules):
            for param in module.parameters():
                if param.requires_grad:
                    # 确保统一使用float32类型
                    param.data = param.data.to(torch.float32)
            # 每个模块后清理内存
            clean_memory()
    
    print("参数转换完成！")
    return model

# 使用安全的参数转换函数
try:
    model = safe_prepare_model_for_kbit_training(model)
except Exception as e:
    print(f"参数转换出错: {e}")
    print("尝试使用标准方法...")
    # 再次清理内存
    clean_memory()
    # 尝试标准方法
    model = prepare_model_for_kbit_training(model)

# 应用LoRA配置
model = get_peft_model(model, lora_config)
print(f"Trainable params: {model.print_trainable_parameters()}")

# 清理内存
clean_memory()

####################################################################################################
# 4. 处理数据集

EOS_TOKEN = tokenizer.eos_token

# 格式化提示函数，用于处理数据集中的示例
def formatting_prompts_func(examples):
    # 从examples中提取问题、思维链和回答
    inputs = examples["Question"]      # 医学问题列表
    cots = examples["Complex_CoT"]     # 思维链列表
    outputs = examples["Response"]     # 回答列表
    
    # 存储格式化后的文本
    texts = []
    
    # 遍历每个示例，将问题、思维链和回答组合成指定格式
    for input, cot, output in zip(inputs, cots, outputs):
        # 使用train_prompt_style模板格式化文本，并添加结束符
        text = train_prompt_style.format(input, cot, output) + EOS_TOKEN
        texts.append(text)
        
    # 返回格式化后的文本字典
    return {
        "text": texts,
    }

# 加载数据集并应用格式化
from datasets import load_dataset
# 重要: 使用os.path.expanduser()展开路径中的~符号
data_path = os.path.expanduser("~/Documents/datasets/medical-o1-reasoning-SFT/medical_o1_sft_Chinese.json")
if not os.path.exists(data_path):
    raise ValueError(f"数据集路径不存在: {data_path}，请检查下载是否完成或路径是否正确")

# 使用合理数量的样本 - 初始验证时使用较少样本
dataset = load_dataset(
    "json",  # 指定数据格式为JSON
    data_files=data_path,
    split="train[0:200]",  # 使用前200条数据用于初始验证
    trust_remote_code=True  # 兼容remote code的行为
)

# 如果返回的是DatasetDict，则取出"train"这一部分
if isinstance(dataset, dict):  
    dataset = dataset["train"]


# 分批处理以避免内存峰值,同时缓存处理结果
cache_dir = os.path.join(os.getcwd(), "dataset_cache")
os.makedirs(cache_dir, exist_ok=True)  # 确保缓存目录存在

dataset = dataset.map(
    formatting_prompts_func, 
    batched=True,
    batch_size=10,  # 合理的批处理大小
    num_proc=1,     # 单进程以避免额外内存开销
    cache_file_name=os.path.join(cache_dir, "processed_dataset_cache.arrow")  # 添加缓存文件
)

print(f"Dataset loaded: {len(dataset)} examples")

# 清理内存
clean_memory()

####################################################################################################
# 5. 配置训练参数启动训练

from transformers import TrainingArguments, Trainer
import transformers

# 定义数据整理函数 - 针对MPS优化
def data_collator(features):
    """优化的数据整理函数，减少数据转换开销"""
    texts = [f["text"] for f in features]
    batch = tokenizer(
        texts, 
        padding="longest",  # 只填充到最长序列长度，而不是max_length
        truncation=True, 
        max_length=max_seq_length, 
        return_tensors="pt"
    )
    
    # 创建标签张量：对于因果语言模型，标签通常与输入ID相同
    batch["labels"] = batch["input_ids"].clone()
    
    # 将所有张量直接移到设备上，避免额外转换
    device = "mps" if torch.backends.mps.is_available() else "cpu"
    batch = {k: v.to(device) for k, v in batch.items()}
    
    return batch

# 训练参数 - 根据不同内存大小调整
# 32GB内存: batch_size=1, gradient_steps=8
# 64GB内存: batch_size=1, gradient_steps=4
# 96GB+内存: batch_size=2, gradient_steps=4
training_args = TrainingArguments(
    output_dir="outputs",
    per_device_train_batch_size=1,    # 保持批次大小=1
    gradient_accumulation_steps=16,   # 增加梯度累积步数以保持性能同时提高稳定性
    learning_rate=2e-4,               # 学习率
    lr_scheduler_type="linear",       # 线性学习率调度器
    warmup_steps=5,                   # 预热步数
    max_steps=20,                     # 初始验证只需少量步骤
    logging_steps=5,                  # 减少日志记录频率
    save_steps=10,                    # 每10步保存一次
    fp16=False,                       # 不使用半精度，MPS不支持
    bf16=False,                       # 同样不使用bf16
    remove_unused_columns=False,      # 避免数据集列不匹配错误
    gradient_checkpointing=True,      # 梯度检查点
    weight_decay=0.01,                # 权重衰减
    seed=8137,                        # 随机数种子
    # 内存优化参数
    dataloader_num_workers=0,         # 不使用多进程数据加载
    dataloader_pin_memory=False,      # 不使用固定内存
    optim="adamw_torch",              # 使用标准的AdamW优化器
    report_to="none" if not wandb.run else "wandb",  # 根据wandb是否启用决定报告
    run_name="medical-o1-sft-experiment-mac",  # wandb运行名称
)

# 创建训练器
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=dataset,
    data_collator=data_collator,
)

# 在开始训练前确保模型配置正确
model.config.use_cache = False  # 确保禁用缓存，与梯度检查点兼容

# 在"开始训练"之前添加
# 添加检查点恢复功能
checkpoint_dir = Path("outputs")
resume_from_checkpoint = None

# 检查是否有现有检查点
if checkpoint_dir.exists():
    checkpoints = [d for d in checkpoint_dir.iterdir() if d.is_dir() and "checkpoint" in d.name]
    if checkpoints:
        latest_checkpoint = max(checkpoints, key=lambda x: int(x.name.split("-")[-1]))
        print(f"找到之前的检查点: {latest_checkpoint}")
        resume_from_checkpoint = latest_checkpoint
        print(f"将从检查点 {resume_from_checkpoint} 恢复训练")

# 开始训练
print(f"开始训练: {training_args.max_steps} 步，批次大小: {training_args.per_device_train_batch_size}，梯度累积: {training_args.gradient_accumulation_steps}")

# 添加额外的训练前准备
def prepare_model_for_mps_training(model):
    """特殊处理以解决MPS后端数据类型不匹配问题"""
    print("正在为MPS训练准备模型...")
    
    # 确保所有attention层和feedforward层的参数使用相同的数据类型
    # 这是为了解决特定的tensor<1x688x5120xf16>, tensor<5120xf32>数据类型不匹配问题
    for name, module in model.named_modules():
        if any(x in name for x in ["attention", "mlp", "down_proj", "up_proj", "gate_proj"]):
            # 检查是否有权重和偏置使用不同的数据类型
            for child_name, child in module.named_children():
                if hasattr(child, "weight") and hasattr(child, "bias") and child.bias is not None:
                    # 确保权重和偏置使用相同的数据类型
                    if child.weight.dtype != child.bias.dtype:
                        print(f"修复数据类型不匹配: {name}.{child_name}, 权重={child.weight.dtype}, 偏置={child.bias.dtype}")
                        child.bias.data = child.bias.data.to(dtype=child.weight.dtype)
    
    # 禁用KV缓存，以防止与梯度检查点冲突
    model.config.use_cache = False
    
    print("MPS训练准备完成。")
    return model

# 应用额外的MPS训练准备
model = prepare_model_for_mps_training(model)

try:
    trainer.train(resume_from_checkpoint=resume_from_checkpoint)
    print(f"训练完成。步数: {trainer.state.global_step}")
except RuntimeError as e:
    error_msg = str(e)
    if "MPS backend out of memory" in error_msg:
        print("\n训练过程中出现内存不足。请尝试以下解决方案:")
        print("1. 减小max_seq_length值至1536或1024")
        print("2. 减小LoRA配置中的r值至8")
        print("3. 减少target_modules的数量，仅保留[\"q_proj\", \"k_proj\", \"v_proj\", \"o_proj\"]")
        print("4. 增加gradient_accumulation_steps至16")
    elif "requires the same element type" in error_msg or "does not match" in error_msg:
        print("\n训练过程中出现数据类型不匹配错误:")
        print(error_msg)
        print("\n请尝试以下解决方案:")
        print("1. 确保所有模型参数统一使用torch.float32类型")
        print("2. 减少LoRA配置中的target_modules，仅保留attention模块")
        print("3. 尝试设置环境变量：export PYTORCH_ENABLE_MPS_FALLBACK=1")
    else:
        raise e

# 清理内存
clean_memory()

####################################################################################################
# 6. 微调后的模型做一次推理

# 设置为评估模式
#model.eval()

# with torch.no_grad():
#     # 准备输入
#     model_input = prompt_style.format(question, "")
#     inputs = tokenizer(model_input, return_tensors="pt").to("mps")
    
#     # 使用智能推理
#     outputs = smart_inference(model, inputs, max_new_tokens=1200, temperature=0.7)
    
#     # 解码输出
#     response = tokenizer.decode(outputs[0], skip_special_tokens=True)
#     print("### 微调后模型推理结果：")
#     print(response.split("### Response:")[1])

####################################################################################################
# 7. 保存模型

# 清理内存
clean_memory()

# 保存模型
model_save_path = "DeepSeek-R1-Medical-COT-Qwen-14B-Mac"
model.save_pretrained(model_save_path)
tokenizer.save_pretrained(model_save_path)

print(f"模型已保存到: {os.path.abspath(model_save_path)}")
print("验证完成后，可以移除max_steps限制并使用完整数据集进行训练")
```
