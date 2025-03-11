# 推理脚本

## 推理脚本代码

不加载微调后的模型进行推理：

```python
import torch
import time
import os
import gc
import threading
from transformers import AutoModelForCausalLM, AutoTokenizer

print(f"PyTorch版本: {torch.__version__}")
print(f"MPS可用: {torch.backends.mps.is_available()}")

# 优化MPS性能的环境变量
os.environ["PYTORCH_ENABLE_MPS_FALLBACK"] = "1"  
os.environ["PYTORCH_MPS_HIGH_WATERMARK_RATIO"] = "0.0"  
os.environ["PYTORCH_MPS_LOW_WATERMARK_RATIO"] = "0.0"   

# 清理内存
gc.collect()
torch.mps.empty_cache()

# 模型路径
model_path = "/Users/yangjiefeng/Documents/models/deepseek-ai/DeepSeek-R1-Distill-Qwen-14B"

print("====== 阶段1: 加载模型 ======")
tokenizer = AutoTokenizer.from_pretrained(model_path, trust_remote_code=True)
tokenizer.pad_token = tokenizer.eos_token

print("开始加载模型...")
start_time = time.time()

model = AutoModelForCausalLM.from_pretrained(
    model_path,
    torch_dtype=torch.float16,  # 使用float16减少内存占用
    device_map="mps",  # 直接指定使用MPS
    trust_remote_code=True,
    low_cpu_mem_usage=True,
)

print(f"模型加载用时: {time.time() - start_time:.2f}秒")
print(f"模型类型: {model.__class__.__name__}")

# 打印模型参数所在设备
devices = {}
for name, param in model.named_parameters():
    device = param.device
    if device not in devices:
        devices[device] = 0
    devices[device] += param.numel()

print("模型参数分布:")
total_params = sum(devices.values())
for device, count in devices.items():
    print(f"  {device}: {count/1e9:.2f}B 参数 ({count/total_params*100:.1f}%)")

print("====== 阶段2: 准备推理 ======")
model.eval()
model.config.use_cache = True
torch.set_grad_enabled(False)

# 清理内存
gc.collect()
torch.mps.empty_cache()

print("====== 阶段3: 执行推理 ======")


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

print("准备输入...")
inputs = tokenizer([prompt_style.format(question, "")], return_tensors="pt")

main_device = next(model.parameters()).device
print(f"模型主要在设备: {main_device}")
inputs = {k: v.to(main_device) for k, v in inputs.items()}

# 全局变量，用于跟踪生成是否完成
generation_completed = False

def print_progress():
    """每10秒打印一次进度提示"""
    if not generation_completed:
        elapsed = time.time() - start_time
        print(f"生成中... 已经运行 {elapsed:.1f} 秒")
        threading.Timer(10.0, print_progress).start()

print("开始生成...")
print("注意：生成过程中将每10秒显示一次运行时间，请耐心等待...")
start_time = time.time()

# 启动进度打印线程
threading.Timer(10.0, print_progress).start()

try:
    # 执行生成 - 不使用callback参数
    outputs = model.generate(
        **inputs,
        max_new_tokens=1200,
        temperature=0.7,
        do_sample=True,
        use_cache=True,
    )
    
    # 标记生成已完成
    generation_completed = True
    
    # 计算生成速度
    generation_time = time.time() - start_time
    tokens_generated = outputs.shape[1] - inputs["input_ids"].shape[1]
    tokens_per_second = tokens_generated / generation_time
    
    print(f"\n生成完成!")
    print(f"生成了 {tokens_generated} 个tokens，用时 {generation_time:.2f} 秒")
    print(f"生成速度: {tokens_per_second:.2f} tokens/秒")
    
    # 解码输出
    generated_text = tokenizer.decode(outputs[0], skip_special_tokens=True)
    
    print("\n====== 生成结果 ======\n")
    print(generated_text)
    
    # 尝试分割出回答部分
    if "### Response:" in generated_text:
        answer = generated_text.split("### Response:")[1]
        print("\n====== 提取的回答 ======\n")
        print(answer)
    
except Exception as e:
    print(f"生成过程中出错: {e}")
    import traceback
    traceback.print_exc()

# 标记生成已完成，停止进度提示
generation_completed = True

# 清理资源
print("\n====== 清理资源 ======")
del model
del tokenizer
gc.collect()
torch.mps.empty_cache()
print("推理完成，资源已释放")
```

## 加载微调后的模型

要在已有推理脚本的基础上加载微调后的模型，只需添加以下几行代码：

```python
# 在导入部分添加
from peft import PeftModel

# 原始代码中加载模型后，添加以下代码来加载适配器
adapter_path = "outputs/checkpoint-20"  # 适配器路径，指向训练后的检查点
model = PeftModel.from_pretrained(model, adapter_path)
```

这样就完成了微调模型的加载，其余推理代码可以保持不变。适配器会自动将微调后的权重应用到原始模型上。

