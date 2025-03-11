# 用unsloth和医学数据微调DeepSeek-R1-Distill-Qwen-14B

参考文章：[《单卡 RTX 4090 用 unsloth 和医学数据微调 DeepSeek-R1-Distill-Qwen-14B》](https://mp.weixin.qq.com/s?__biz=MzUxMTczMTY1Ng==&mid=2247483742&idx=1&sn=b90af9f3a12f182f5e48859b3b72a40a&scene=21#wechat_redirect)

在后续文章[《单卡4090微调DeepSeek-R1-32B》](https://mp.weixin.qq.com/s?__biz=MzUxMTczMTY1Ng==&mid=2247483746&idx=1&sn=b7e16a70ffb903b64f28424677fb22c0&chksm=f96e73c5ce19fad3b1b26f56826a3e2470bcfbc8675995c6a47b2b9e466d19ba6ec2c6a32277&cur_album_id=3854852837976768515&scene=189#wechat_redirect)中，提供的训练代码如下：


```python
import wandb
# 登录 wandb.ai 用于实验跟踪
wandb.login(key="放置你的wandb.ai网站上的token")
# 初始化wandb项目
run = wandb.init(
    project='Lora-R1-Distill-Qwen on Medical COT Dataset',
    job_type="training",
    anonymous="allow"
)

####################################################################################################
# 1.加载模型

# 使用 unsloth 优化的 FastLanguageModel 加载模型
from unsloth import FastLanguageModel
max_seq_length = 4096 # 最大序列长度
dtype = None          # 数据类型，None表示自动选择
load_in_4bit = True   # 使用4bit量化加载模型以节省显存

# 加载预训练模型和分词器
model, tokenizer = FastLanguageModel.from_pretrained(
    #model_name = "unsloth/DeepSeek-R1-Distill-Qwen-7B",
    model_name = "/models/deepseek-ai/DeepSeek-R1-Distill-Qwen-32B",
    local_files_only=True,  # 避免联网
    max_seq_length = max_seq_length,
    dtype = dtype,
    load_in_4bit = load_in_4bit,
    #token = hf_token, 
)
print(model)

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

# 设置模型为推理模式
FastLanguageModel.for_inference(model) 
inputs = tokenizer([prompt_style.format(question, "")], return_tensors="pt").to("cuda")

# 生成回答
outputs = model.generate(
    input_ids=inputs.input_ids,
    attention_mask=inputs.attention_mask,
    max_new_tokens=1200,
    use_cache=True,
)
response = tokenizer.batch_decode(outputs)
print("### 微调前模型推理结果：")
print(response[0].split("### Response:")[1])

####################################################################################################
# 3. 处理数据集

EOS_TOKEN = tokenizer.eos_token  # 添加结束符标记
#格式化提示函数,用于处理数据集中的示例
def formatting_prompts_func(examples):
    # 从examples中提取问题、思维链和回答
    inputs = examples["Question"]      # 医学问题列表
    cots = examples["Complex_CoT"]     # 思维链列表 
    outputs = examples["Response"]     # 回答列表
    
    # 存储格式化后的文本
    texts = []
    
    # 遍历每个示例,将问题、思维链和回答组合成指定格式
    for input, cot, output in zip(inputs, cots, outputs):
        # 使用train_prompt_style模板格式化文本,并添加结束符
        text = train_prompt_style.format(input, cot, output) + EOS_TOKEN
        texts.append(text)
        
    # 返回格式化后的文本字典
    return {
        "text": texts,
    }

# 加载数据集并应用格式化
from datasets import load_dataset,load_from_disk
dataset = load_dataset(
    "json",  # 指定数据格式为 JSON
    data_files="/datasets/FreedomIntelligence/medical-o1-reasoning-SFT/medical_o1_sft_Chinese.json",
    #split="train[0:500]",  # 只取前 500 条数据
    trust_remote_code=True  # 兼容 remote code 的行为
)

# 如果返回的是 DatasetDict，则取出 "train" 这一部分
if isinstance(dataset, dict):  
    dataset = dataset["train"]
    
dataset = dataset.map(formatting_prompts_func, batched = True,)
print(dataset)  # 查看数据集结构

####################################################################################################
# 4. 配置训练参数启动训练

model = FastLanguageModel.get_peft_model(
    model, 
    r=32,
    target_modules=[
        "q_proj", "k_proj", "v_proj", "o_proj", 
        "gate_proj", "up_proj", "down_proj",    
    ],
    lora_alpha=16,
    lora_dropout=0,  
    bias="none",  
    use_gradient_checkpointing="unsloth", 
    random_state=8137,
    use_rslora=False,  
    loftq_config=None,
)
print(model)

# 配置训练参数和初始化训练器
from trl import SFTTrainer  
from transformers import TrainingArguments  
from unsloth import is_bfloat16_supported  

# 初始化 SFT 训练器
trainer = SFTTrainer(
    model=model,  
    tokenizer=tokenizer,  
    train_dataset=dataset,  
    dataset_text_field="text",  # 数据集字段的名称
    max_seq_length=max_seq_length,  
    dataset_num_proc=2,  # 数据集处理的并行进程数，提高CPU利用率
    args=TrainingArguments(
        per_device_train_batch_size=2, 
        gradient_accumulation_steps=4,   
        warmup_steps=5,  # 预热步数,逐步增加学习率
        learning_rate=2e-4,  # 学习率
        lr_scheduler_type="linear",  # 线性学习率调度器
        # max_steps=200,    # 最大训练步数（一步 = 处理一个batch的数据）
        fp16=not is_bfloat16_supported(),  # 如果不支持bf16则使用fp16
        bf16=is_bfloat16_supported(),      # 如果支持则使用bf16
        logging_steps=10,  # 每10步记录一次日志
        optim="adamw_8bit",  # 使用8位AdamW优化器节省显存，几乎不影响训练效果
        weight_decay=0.01,   # 权重衰减系数,用于正则化，防止过拟合
        seed=8137,  # 随机数种子
        output_dir="outputs",  # 保存模型检查点和训练日志
        run_name="medical-o1-sft-experiment",  # 显式设置 wandb 运行名称，避免警告
    ),
)

# 开始训练
print(f"trainer.args.max_steps: {trainer.args.max_steps}")
print(f"trainer.args.num_train_epochs: {trainer.args.num_train_epochs}")
trainer.train()
print(f"Total training steps: {trainer.state.max_steps}")
print(f"Total epochs: {trainer.state.epoch}")

####################################################################################################
# 5. 微调后的模型做一次推理

FastLanguageModel.for_inference(model)  
inputs = tokenizer([prompt_style.format(question, "")], return_tensors="pt").to("cuda")

# 生成回答
outputs = model.generate(
    input_ids=inputs.input_ids, # 输入token的id序列
    attention_mask=inputs.attention_mask,  # 注意力掩码,用于标记有效输入位置
    max_new_tokens=1200, # 生成的最大新token数量
    use_cache=True, # 是否使用KV缓存加速生成
)

response = tokenizer.batch_decode(outputs)
print("### 微调后模型推理结果：")
print(response[0].split("### Response:")[1])

####################################################################################################
# 6. 保存模型

new_model_local = "DeepSeek-R1-Medical-COT-Qwen-32B"
model.save_pretrained(new_model_local) 
tokenizer.save_pretrained(new_model_local)

# 保存合并后的16bit模型
model.save_pretrained_merged(new_model_local, tokenizer, save_method = "merged_16bit",)

# 保存为 GGUF 模型
# model.save_pretrained_gguf("DeepSeek-R1-Qwen-32B-Medical-COT-GGUF", tokenizer,)
```
