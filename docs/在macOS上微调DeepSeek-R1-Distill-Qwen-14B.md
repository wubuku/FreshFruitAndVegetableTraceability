# 在macOS (Apple Silicon)上微调DeepSeek-R1-Distill-Qwen-14B指南

本指南将帮助您在macOS（Apple Silicon）设备上使用Hugging Face PEFT库微调DeepSeek-R1-Distill-Qwen-14B模型。原指南基于使用Unsloth框架在RTX 4090上微调，但由于Unsloth目前不支持Apple Silicon的MPS后端，本指南已针对Apple Silicon环境做了全面调整，改用标准PEFT库实现。

## 一、硬件要求与准备

### 推荐配置
- 机型：M2 Pro/Max或更高配置的Mac
- 内存：至少32GB，建议64GB以上
- 存储：至少100GB可用空间
- macOS版本：Ventura (13.0)或更新版本

> **注意**：14B参数模型在macOS上是一个挑战，请确保您有足够内存并做好性能预期。此外，DeepSeek-R1-Distill-Qwen系列模型的某些操作可能与MPS后端存在兼容性问题，下文提供了解决方案。

> **⚠️ 特别提醒**：MPS后端在Hugging Face的Accelerate库中**不被识别为支持fp16混合精度训练**的设备。本指南已针对此问题提供了解决方案，确保将训练参数中的`fp16=False`。

## 二、环境配置

### 1. 安装必要的软件

```bash
# 安装Homebrew（如果尚未安装）
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 安装Python相关工具
brew install python@3.11 cmake

# 安装git-lfs（用于模型下载）
brew install git-lfs
git lfs install
```

### 2. 创建虚拟环境

```bash
# 创建项目目录
mkdir -p ~/Documents/llm-finetune
cd ~/Documents/llm-finetune

# 创建虚拟环境
python3 -m venv peft-env
source peft-env/bin/activate
```

### 3. 安装依赖

```bash
# 确保pip是最新版本
pip install --upgrade pip

# 安装PyTorch with MPS支持
pip install torch torchvision torchaudio

# 安装NumPy 1.x版本解决兼容性问题
pip install numpy==1.26.4

# 安装核心依赖库
pip install transformers datasets peft accelerate huggingface_hub wandb psutil
```

#### PyTorch安装详解

上述命令 `pip install torch torchvision torchaudio` 一次性安装了三个相关的Python包：

1. **torch**：
   - PyTorch的核心库
   - 由Meta(原Facebook)开发的流行深度学习框架
   - 提供张量计算和动态神经网络功能

2. **torchvision**：
   - PyTorch的计算机视觉扩展库
   - 包含常用的数据集、模型架构和图像处理工具

3. **torchaudio**：
   - PyTorch的音频处理扩展库
   - 提供音频加载、处理和特征提取功能

**在Apple Silicon上**，此命令会自动安装支持MPS (Metal Performance Shaders)的PyTorch版本，使得深度学习任务能够利用M系列芯片的GPU加速。

> ⚠️ **重要说明**：本指南目前使用标准的Hugging Face PEFT库进行LoRA微调，而非Unsloth。这是因为经过测试，**Unsloth目前不完全支持Apple Silicon的MPS后端**。


#### 安装Apple的CoreML工具（可选）

```shell
pip install coremltools  # 安装Apple的CoreML工具
```

coremltools 的安装本身并不会自动提升 PyTorch 训练性能。这个工具主要用于：
* 模型转换：将训练好的模型转换为 Apple CoreML 格式，以便在 iOS、macOS 等 Apple 设备上高效运行
* 模型优化：为 Apple 设备优化模型结构和性能
* 模型部署：帮助将模型部署到 Apple 设备上

对于当前的微调训练任务，coremltools 实际上并没有直接帮助，因为：
* 我们使用的是 PyTorch 的 MPS (Metal Performance Shaders) 后端进行训练
* LoRA 微调过程主要依赖 PyTorch 的原生功能
* coremltools 更多是用于模型推理阶段，而不是训练阶段


### 4. 原始方法：Unsloth安装（参考，不适用于Apple Silicon）

<details>
<summary>👉 展开查看原始Unsloth安装方法（仅供参考，不推荐使用）</summary>

```bash
# 克隆Unsloth
git clone https://github.com/unslothai/unsloth.git
cd unsloth

# 安装Unsloth（Apple Silicon优化版本）
# 注意：在zsh中需要用引号避免方括号被解析为通配符
pip install ".[mps]"

# 如果出现 "zsh: no matches found: .[mps]" 错误，请使用以下命令之一：
# pip install ".[mps]"    # 使用引号
# pip install .\[mps\]    # 使用转义符
# pip install -e . --extra-index-url="mps"  # 使用-e选项
```

#### 关于Unsloth安装方式的说明

上述安装方式使用了源码安装的方法，而不是直接通过pip安装。这样做的原因有：

1. **获取最新MPS支持**：
   - 直接从GitHub获取最新的源码，包含对Apple Silicon的最新优化
   - 可以访问可能尚未在PyPI正式发布的功能

2. **可编辑模式**：
   - 安装在"可编辑模式"，允许在需要时修改源码
   - 对于调试和自定义微调流程很有帮助

3. **版本灵活性**：
   - 可以通过Git切换到特定版本或分支

**替代方案**：您也可以直接通过pip安装：
```bash
# 直接从PyPI安装（简单但可能不是最新版本）
pip install unsloth[mps]
```

**⚠️ 提示：** 经测试，虽然Unsloth可以在MacOS上安装，但实际使用时会遇到兼容性问题，因此本指南采用标准PEFT库实现LoRA微调。
</details>

### 5. 常见环境问题与解决方案

#### Python版本冲突问题

如果您在安装Python 3.11时遇到类似以下错误：

```
Error: The `brew link` step did not complete successfully
Could not symlink bin/2to3-3.11
Target /usr/local/bin/2to3-3.11 already exists.
```

这表明系统中存在多个Python版本冲突。解决方法：

1. **使用特定版本路径**：
   ```bash
   # 使用Homebrew安装的Python 3.11的完整路径创建虚拟环境
   /usr/local/Cellar/python@3.11/3.11.11/bin/python3.11 -m venv peft-env
   ```

2. **强制创建链接**（可选）：
   ```bash
   brew link --overwrite python@3.11
   ```

3. **验证正在使用的Python版本**：
   ```bash
   python --version  # 应显示3.11.x
   ```

#### 虚拟环境使用须知

请注意，激活虚拟环境（`source peft-env/bin/activate`）**仅对当前终端会话有效**：

- 关闭终端窗口后，激活状态会丢失
- 新开的终端窗口需要重新激活环境
- 多个终端窗口需要分别激活

**实用建议**：

1. **创建别名**：在`.zshrc`中添加：
   ```bash
   echo 'alias activate-unsloth="cd ~/Documents/unsloth-finetune && source peft-env/bin/activate"' >> ~/.zshrc
   ```

2. **创建启动脚本**：
   ```bash
   echo '#!/bin/bash
   cd ~/Documents/unsloth-finetune
   source peft-env/bin/activate
   exec "$@"' > ~/start-unsloth.sh
   chmod +x ~/start-unsloth.sh
   ```
   使用：`~/start-unsloth.sh python your_script.py`

#### PyTorch下载超时问题

如果您在安装PyTorch时遇到类似以下错误:

```
HTTPSConnectionPool(host='files.pythonhosted.org', port=443): Read timed out.
```

这通常是因为PyTorch包体积较大(约150MB)，在网络状况不佳时容易下载超时。解决方法:

1. **使用镜像源安装**:
   ```bash
   pip install torch torchvision torchaudio -i https://pypi.tuna.tsinghua.edu.cn/simple
   ```

2. **增加pip超时时间**:
   ```bash
   pip --default-timeout=1000 install torch torchvision torchaudio
   ```

3. **分步下载**:
   ```bash
   # 先只安装torch核心库
   pip install torch -i https://pypi.tuna.tsinghua.edu.cn/simple
   # 成功后再安装其他组件
   pip install torchvision torchaudio -i https://pypi.tuna.tsinghua.edu.cn/simple
   ```

4. **确认Apple Silicon兼容性**:
   对于M系列芯片，确保下载的是ARM架构的包:
   ```bash
   pip install torch torchvision torchaudio --extra-index-url https://download.pytorch.org/whl/cpu
   ```

## 三、下载模型与数据

### 1. 设置HuggingFace缓存目录

```bash
# 创建缓存目录
mkdir -p ~/Documents/models
mkdir -p ~/Documents/datasets

# 设置环境变量
export HF_HOME=~/Documents/models
export TRANSFORMERS_CACHE=~/Documents/models
export HUGGINGFACE_HUB_CACHE=~/Documents/models
```

### 2. 下载模型

#### 准备工作：安装必要的库

```bash
# 根据您选择的下载方式，安装对应的库
# 方式一：ModelScope（需要安装modelscope库）
pip install modelscope

# 方式二：HuggingFace API（需要安装huggingface_hub）
pip install huggingface_hub
```

> **⚠️ 重要警告**：在Python脚本中，`~/` 这样的路径简写**不会**被自动展开为用户主目录！必须使用绝对路径或`os.path.expanduser()`函数进行转换，否则会导致路径错误！

选择以下两种方式之一：

#### 使用ModelScope（如果需要镜像加速）
```python
# 创建并运行Python脚本
cat > download_model.py << 'EOF'
import os
from modelscope import snapshot_download

# 正确: 使用os.path.expanduser展开用户主目录
model_cache_dir = os.path.expanduser('~/Documents/models')

# 下载模型到展开后的路径
snapshot_download('deepseek-ai/DeepSeek-R1-Distill-Qwen-14B', cache_dir=model_cache_dir)

# 错误示例 - 不要这样做: ❌
# snapshot_download('deepseek-ai/DeepSeek-R1-Distill-Qwen-14B', cache_dir='~/Documents/models')
EOF

python download_model.py
```

#### 或使用HuggingFace API
```bash
# 设置镜像（可选，如果下载慢）
export HF_ENDPOINT=https://hf-mirror.com

# 下载模型 - 用绝对路径或os.path.expanduser()
python -c "import os; from huggingface_hub import snapshot_download; snapshot_download('deepseek-ai/DeepSeek-R1-Distill-Qwen-14B', cache_dir=os.path.expanduser('~/Documents/models'))"
```

### 3. 下载医学数据集

```bash
# 设置镜像（可选）
export HF_ENDPOINT=https://hf-mirror.com

# 下载数据集
huggingface-cli download --resume-download --repo-type dataset FreedomIntelligence/medical-o1-reasoning-SFT --local-dir ~/Documents/datasets/medical-o1-reasoning-SFT
```

### 4. 验证数据集

```bash
# 查看数据集的最后一条记录
tail -6 ~/Documents/datasets/medical-o1-reasoning-SFT/medical_o1_sft_Chinese.json
```


## 四、微调脚本配置

**Unsloth 目前不支持 Apple Silicon 的 MPS 后端**。我们需要使用标准的 Hugging Face PEFT (Parameter-Efficient Fine-Tuning) 库来进行 LoRA 微调。

> **⚠️提示**：在Python脚本中处理文件路径应使用`os.path.expanduser()`函数处理包含`~`的路径，如本示例中所示。此类路径错误是导致模型加载失败的最常见原因之一。


首先，安装必要的库：

```bash
# 安装核心库 
pip install transformers datasets peft accelerate wandb

# 注意：NumPy版本兼容性问题 - 确保安装1.x系列版本以避免兼容性错误
pip install numpy==1.26.4
```

可以使用这个脚本做一次在微调前的推理：[在macOS上微调DeepSeek-R1-Distill-Qwen-14B_推理脚本.md](在macOS上微调DeepSeek-R1-Distill-Qwen-14B_推理脚本.md)

创建微调脚本：[在macOS上微调DeepSeek-R1-Distill-Qwen-14B_微调脚本.md](在macOS上微调DeepSeek-R1-Distill-Qwen-14B_微调脚本.md)


## 五、执行微调过程

```bash
# 激活虚拟环境（如果尚未激活）
cd ~/Documents/llm-finetune
source peft-env/bin/activate

# 激活unsloth虚拟环境
# source unsloth-env/bin/activate

# 安装NumPy 1.x版本解决兼容性问题
pip install numpy==1.26.4

# 安装内存监控工具(可选)
pip install psutil

# 运行微调脚本
python r1-lora-peft-mac.py
```

## 六、性能优化建议

### 1. 内存配置指南

| Mac内存配置 | max_seq_length | batch_size | gradient_steps | LoRA r值 | target_modules |
|------------|----------------|------------|----------------|---------|----------------|
| 32GB | 1024-1536 | 1 | 8-16 | 8 | 仅attention (q,k,v,o) |
| 64GB | 2048 | 1 | 4-8 | 16 | 所有7个模块 |
| 96GB+ | 2048-3072 | 1-2 | 4 | 32 | 所有7个模块 |

### 2. 解决内存溢出问题

如果遇到`MPS backend out of memory`错误，请尝试以下步骤（按优先级排序）：

1. **减少序列长度**：
   - 将`max_seq_length`降至1024（优先级最高）
   - 这是最有效的内存节省方式

2. **降低LoRA参数**：
   - 将LoRA的`r`值从32降至16或8
   - 减少`target_modules`，仅保留`["q_proj", "k_proj", "v_proj", "o_proj"]`

3. **调整批处理设置**：
   - 确保`per_device_train_batch_size=1`
   - 增加`gradient_accumulation_steps`值（8或更高）

4. **添加内存优化环境变量**：
   ```bash
   export PYTORCH_MPS_HIGH_WATERMARK_RATIO=0.0
   export PYTORCH_ENABLE_MPS_FALLBACK=1
   ```

5. **减少训练数据**：
   - 使用`split="train[0:100]"`等策略限制样本数量
   - 在代码中频繁调用`gc.collect()`和`torch.mps.empty_cache()`

### 3. MPS兼容性问题解决方案

Qwen2和DeepSeek-R1-Distill-Qwen系列模型在MPS上可能遇到以下兼容性问题：

1. **常见错误类型**：
   - `validateComputeFunctionArguments` 相关错误
   - `gather_kernel_1: missing buffer binding at index 2` 错误
   - **数据类型不匹配错误**：例如 `'mps.add' op requires the same element type for all operands and results`
   - **use_cache与gradient_checkpointing冲突**：`use_cache=True` 与 `gradient_checkpointing=True` 不兼容
   - 其他MPS操作未实现的错误
   - **fp16混合精度训练不兼容**：`ValueError: fp16 mixed precision requires a GPU (not 'mps')`

2. **解决策略**：
   - **统一数据类型**：确保所有张量都使用相同的数据类型（使用float32而非float16）
   - **检查权重和偏置类型匹配**：确保每个层的权重和偏置使用相同的数据类型
   - **显式禁用缓存**：设置`model.config.use_cache = False`以避免与梯度检查点冲突
   - **智能禁用gradient_checkpointing**：在推理前临时禁用gradient_checkpointing以使用KV缓存
   - **推理回退到CPU**：当MPS出现不可恢复的错误时自动回退到CPU处理
   - **禁用fp16训练**：确保将`fp16=False`设置在`TrainingArguments`中
   - **添加环境变量**：使用`PYTORCH_ENABLE_MPS_FALLBACK=1`允许未实现的操作回退到CPU

3. **性能影响**：
   - 推理在CPU上速度会慢一些，但结果准确
   - 训练过程仍能充分利用MPS加速
   - 合理配置参数可以减少数据类型转换的性能损失

### 4. 训练过程中的MPS数据类型不匹配错误修复

如果您遇到类似以下错误：
```
'mps.add' op requires the same element type for all operands and results
%7 = "mps.add"(%6, %arg2) : (tensor<1x688x5120xf16>, tensor<5120xf32>) -> tensor<*xf32>
```

这是由于MPS后端要求所有操作数具有相同的数据类型，但模型中存在不同类型的张量（如示例中的f16和f32）。本指南中已经实现了以下修复：

1. **统一模型加载时的数据类型**：
   ```python
   model = AutoModelForCausalLM.from_pretrained(
       model_path,
       torch_dtype=torch.float32,  # 使用float32而非float16
       device_map="mps",
       # ...
   )
   ```

2. **特殊的MPS训练准备函数**：
   ```python
   def prepare_model_for_mps_training(model):
       # 确保所有注意力层和前馈层的参数使用相同数据类型
       for name, module in model.named_modules():
           # 检查权重和偏置是否使用不同的数据类型
           # ...
       return model
   ```

3. **禁用KV缓存与梯度检查点一起使用**：
   ```python
   model.config.use_cache = False  # 避免与梯度检查点冲突
   ```

4. **确保数据加载器返回统一类型的张量并包含标签**：
   ```python
   def data_collator(features):
       # ...
       # 创建标签张量
       batch["labels"] = batch["input_ids"].clone()
       
       # 确保所有张量为float32类型
       for key, value in batch.items():
           if isinstance(value, torch.Tensor) and value.dtype == torch.float16:
               batch[key] = value.to(dtype=torch.float32)
       return batch
   ```

### 5. 其他常见训练错误

1. **缺少标签错误**：如果您遇到以下错误：
   ```
   ValueError: The model did not return a loss from the inputs, only the following keys: logits.
   ```
   
   这意味着模型在训练时没有收到`labels`字段，无法计算损失函数。解决方法是修改数据整理函数，确保它为模型提供输入的副本作为标签：
   ```python
   batch["labels"] = batch["input_ids"].clone()
   ```

2. **数据集列不匹配错误**：这是PEFT模型的一个常见问题，解决方法是在`TrainingArguments`中设置：
   ```python
   remove_unused_columns=False
   ```

以上修复措施应该能够解决MPS后端上的常见训练问题。

## 七、训练日志解读与常见问题

训练日志示例：

> wandb: ⭐️ View project at https://wandb.ai/anony-mouse-733470366999568055/Lora-DeepSeek-R1-Distill-Qwen-14B-Mac?apiKey=c16d51f0be89758603632573346321aab91cbb6f
> wandb: 🚀 View run at https://wandb.ai/anony-mouse-733470366999568055/Lora-DeepSeek-R1-Distill-Qwen-14B-Mac/runs/9yxzquav?apiKey=c16d51f0be89758603632573346321aab91cbb6f


### 1. 理解训练损失和指标

微调结束后，可以看到类似下面的训练日志：

```
21 {'loss': 2.4172, 'grad_norm': 0.2653520107269287, 'learning_rate': 4e-05, 'epoch': 0.04}
22 {'loss': 2.4697, 'grad_norm': 0.2983250916004181, 'learning_rate': 8e-05, 'epoch': 0.08}
23 {'loss': 2.5866, 'grad_norm': 0.30050748586654663, 'learning_rate': 0.00012, 'epoch': 0.12}
24 {'loss': 2.508, 'grad_norm': 0.3454345464706421, 'learning_rate': 0.00016, 'epoch': 0.16}
25 {'loss': 2.5712, 'grad_norm': 0.4334571063518524, 'learning_rate': 0.0002, 'epoch': 0.2}
26 {'loss': 2.328, 'grad_norm': 0.49836465716362, 'learning_rate': 0.0001866666666666667, 'epoch': 0.24}
27 {'loss': 2.2054, 'grad_norm': 0.4785458743572235, 'learning_rate': 0.00017333333333333334, 'epoch': 0.28}
28 {'loss': 2.2087, 'grad_norm': 0.40370047092437744, 'learning_rate': 0.00016, 'epoch': 0.32}
29 {'loss': 1.9788, 'grad_norm': 0.3458884060382843, 'learning_rate': 0.00014666666666666666, 'epoch': 0.36}
30 {'loss': 2.1806, 'grad_norm': 0.2959587872028351, 'learning_rate': 0.00013333333333333334, 'epoch': 0.4}
31 {'loss': 1.8023, 'grad_norm': 0.2639026343822479, 'learning_rate': 0.00012, 'epoch': 0.44}
32 {'loss': 1.9279, 'grad_norm': 0.26907879114151, 'learning_rate': 0.00010666666666666667, 'epoch': 0.48}
33 {'loss': 1.8455, 'grad_norm': 0.28270483016967773, 'learning_rate': 9.333333333333334e-05, 'epoch': 0.52}
34 {'loss': 1.8335, 'grad_norm': 0.24587252736091614, 'learning_rate': 8e-05, 'epoch': 0.56}
35 {'loss': 1.7411, 'grad_norm': 0.21421942114830017, 'learning_rate': 6.666666666666667e-05, 'epoch': 0.6}
36 {'loss': 1.67, 'grad_norm': 0.21373698115348816, 'learning_rate': 5.333333333333333e-05, 'epoch': 0.64}
37 {'loss': 1.7019, 'grad_norm': 0.20923933386802673, 'learning_rate': 4e-05, 'epoch': 0.68}
38 {'loss': 1.6447, 'grad_norm': 0.2079884558916092, 'learning_rate': 2.6666666666666667e-05, 'epoch': 0.72}
39 {'loss': 1.6787, 'grad_norm': 0.19983869791030884, 'learning_rate': 1.3333333333333333e-05, 'epoch': 0.76}
40 {'loss': 1.6625, 'grad_norm': 0.21983186900615692, 'learning_rate': 0.0, 'epoch': 0.8}
41 {'train_runtime': 98940.9247, 'train_samples_per_second': 0.002, 'train_steps_per_second': 0.0, 'train_loss': 2.048112761974335, 'epoch': 0.8}
42 训练完成。步数: 20
```

#### 损失值(Loss)变化趋势分析

从日志中可以将损失变化划分为三个阶段：

```
初始阶段（步骤21-25）: 2.4172 → 2.4697 → 2.5866 → 2.508 → 2.5712 (预热阶段，损失波动上升)
中期阶段（步骤26-33）: 2.328 → 2.2054 → 2.2087 → 1.9788 → 2.1806 → 1.8023 → 1.9279 → 1.8455 (转折，开始下降)
后期阶段（步骤34-40）: 1.8335 → 1.7411 → 1.67 → 1.7019 → 1.6447 → 1.6787 → 1.6625 (稳定下降)
```

**损失变化特点**：
- **初始阶段**：损失值在2.4-2.6之间波动上升，这与学习率预热期间的正常现象一致
- **中期阶段**：损失开始显著下降，但仍有波动，表明模型正在适应数据
- **后期阶段**：损失更加稳定地下降，最终稳定在约1.65左右
- **总体降幅**：从初始的约2.5降至1.6，降幅约36%，表明微调非常有效

#### 梯度范数(grad_norm)演变

梯度范数显示了一个清晰的"山峰"模式：

```
初始阶段: 0.265 → 0.298 → 0.300 → 0.345 → 0.433 (逐渐上升)
中期阶段: 0.498 → 0.478 → 0.403 → 0.345 → 0.295 → 0.263 → 0.269 → 0.282 (达到峰值后下降)
后期阶段: 0.245 → 0.214 → 0.213 → 0.209 → 0.207 → 0.199 → 0.219 (稳定在较低水平)
```

**梯度变化特点**：
- 梯度范数先上升后下降，呈"山峰"形状，最高达到约0.5，最终稳定在0.2左右
- 这种模式与学习率变化相呼应，表明参数调整幅度先增强后减弱
- 后期梯度范数保持在适中水平（0.2左右），表明模型逐渐收敛到局部最优解

#### 学习率(learning_rate)策略剖析

学习率采用了典型的"预热-平台-衰减"三阶段策略：

```
预热阶段: 4e-05 → 8e-05 → 0.00012 → 0.00016 → 0.0002 (线性增加)
平台阶段: 0.000187 → 0.000173 → 0.00016 (短暂平台后开始下降)
衰减阶段: 0.000147 → ... → 1.33e-05 → 0.0 (线性衰减)
```

**学习率策略特点**：
- 预热阶段（步骤21-25）：学习率从4e-05线性增加到0.0002
- 平台过渡（步骤26-28）：学习率短暂维持在高位
- 衰减阶段（步骤29-40）：学习率线性降低直至0
- 这种策略有效避免了训练初期的不稳定性，同时允许模型在后期精细调整

#### 训练效率指标解读

从训练的最终统计可以得到以下效率指标：

```
{'train_runtime': 98940.9247, 'train_samples_per_second': 0.002, 'train_steps_per_second': 0.0, 'train_loss': 2.048112761974335, 'epoch': 0.8}
```

**效率分析**：
- 总训练时间：约27.5小时（98940秒）
- 处理速度：0.002样本/秒
- 完成了0.8个训练周期
- 平均训练损失：2.048
- 每步耗时：约4947秒（82.5分钟）

#### 综合评估

1. **微调成功度**：
   - 损失显著下降（约36%）表明微调非常有效
   - 梯度范数的变化合理，没有出现梯度爆炸或消失
   - 学习率策略执行良好，遵循了最佳实践

2. **模型收敛状况**：
   - 模型在训练结束时仍在稳定改进（损失仍有下降趋势）
   - 最终几个步骤的损失波动较小，表明模型接近局部最优解
   - 损失值降至1.6左右表明模型对训练数据有了很好的拟合

3. **训练时间与资源利用**：
   - 在Apple Silicon上微调14B模型耗时约27.5小时，符合预期
   - 每步训练时间（约82.5分钟）反映了Mac设备上大模型微调的现实性能

这次微调DeepSeek-R1-Distill-Qwen-14B模型在Apple Silicon设备上取得了良好的效果，验证了本文档中的方法和参数设置是有效的。从指标变化来看，模型参数调整是稳健的，没有出现过拟合或训练不稳定的迹象。

### 2. 常见警告解析

#### MallocStackLogging警告

如果您在使用wandb时看到类似以下警告：

```
wandb-core(2754) MallocStackLogging: can't turn off malloc stack logging because it was not enabled.
```

**解释**：这是Weights & Biases (wandb)工具在MacOS上的一个已知问题，wandb尝试关闭一个本来就没有启用的内存跟踪功能。

**影响**：完全没有影响，可以安全忽略。这不会影响训练过程、性能或结果质量。

#### CUDA相关警告

如果看到类似以下警告：

```
UserWarning: CUDA initialization: CUDA unknown error - this may be due to an incorrectly set up environment
```

**解释**：这是因为PyTorch检测到无法初始化CUDA环境，在Apple Silicon上属于正常情况。

**影响**：无需担心，代码会自动使用MPS后端而非CUDA。

### 3. 合理的训练时间预期

在Apple Silicon设备上进行大语言模型微调时，训练时间会比专业GPU慢很多：

- **14B模型训练一个step的时间**：
  - 32GB Mac (M1/M2 Pro/Max): 约500-800秒/step
  - 64GB Mac (M1/M2 Max/Ultra): 约300-500秒/step
  - 对比: RTX 4090: 约10-30秒/step

- **完成20步训练预计总时间**：
  - 32GB Mac: 约28-44小时
  - 64GB Mac: 约17-28小时
  - 对比: RTX 4090: 约3-10小时

**提示**：鉴于长时间训练的特性，请确保您的Mac有良好的散热条件，并考虑分阶段训练，定期保存检查点。

### 4. Mac与NVIDIA GPU的训练效率差异原因

即使Mac具有较大的统一内存(例如64GB)，但训练效率仍低于只有24GB显存的RTX 4090，主要原因包括：

1. **内存架构差异**：
   - RTX 4090: 24GB**专用**高速GDDR6X显存(~1TB/s带宽)
   - Mac: 统一内存同时被系统和其他应用程序占用

2. **处理器优化程度**：
   - NVIDIA GPU专为深度学习优化，具有大量Tensor核心
   - Apple Silicon的ML加速器针对推理更优化，对训练的优化较少

3. **软件栈成熟度**：
   - CUDA生态系统非常成熟，针对训练有深度优化
   - MPS后端相对较新，优化程度不如CUDA

4. **数据类型限制**：
   - RTX 4090支持高效的FP16/BF16训练
   - MPS训练必须使用FP32以避免兼容性问题，这使内存需求加倍

因此，即使Mac拥有更大的总内存，但由于上述因素，在LLM微调任务上仍需使用更保守的参数设置。


### 5. 模型微调前后推理结果对比

微调前：

```
  <think>
好的，我现在需要仔细分析这个病例，确定最合适的药物处理方案。首先，患者是一个70岁的男性，主诉是胸痛伴呕吐，持续了16个小时。心电图显示下壁导联和右胸导联的ST段抬高，幅度在0.1到0.3mV之间。这可能提示下壁心肌梗死，因为这些导联通常对应右心房、右心室前壁以及后壁的部分区域。

接下来，患者在补液后血压下降到80/60mmHg，这提示可能存在休克，特别是心源性休克，因为心肌梗死可能导致心脏泵血功能下降。随后，患者出现呼吸困难和不能平卧的症状，体检发现双肺有大量水泡音，这表明肺水肿的存在。肺水肿通常与心脏功能不全有关，尤其是右心衰竭时，液体可能积聚在肺部。

综合以上信息，患者的状况可能涉及以下几个方面：

1. **急性心肌梗死（下壁）**：ST段抬高提示心肌梗死，需要紧急处理以减少心肌损伤和改善心脏功能。
2. **心源性休克**：血压下降到80/60mmHg，可能是因为心脏输出量减少导致的。
3. **肺水肿**：双肺水泡音提示肺水肿，可能由心脏衰竭引起，需要紧急处理以防止呼吸衰竭。

接下来，我需要考虑可能的药物治疗方案：

- **硝酸甘油**：用于缓解胸痛，扩张冠状动脉，减少心脏负荷。但需注意血压变化，因为补液后血压已下降，使用时可能需要调整剂量。
- **吗啡**：用于缓解胸痛和焦虑，同时可以扩张外周血管，减轻心脏负担，但需注意呼吸抑制。
- **利尿剂**：用于处理肺水肿，减少肺部液体积聚，改善呼吸困难。
- **ACEI类药物**：如依那普利，有助于减轻心脏负荷和延缓心室重构，但需在血压允许的情况下使用。
- **β受体阻滞剂**：用于降低心率和血压，减少心脏耗氧量，但需在没有禁忌证的情况下使用，如无严重心动过速或低血压。
- **正性肌力药物**：如多巴胺或多巴酚丁胺，用于改善心脏输出，提升血压，适用于休克状态。

考虑到患者当前的情况，尤其是心源性休克和肺水肿，优先处理应该是稳定循环和改善心脏功能。因此，可能需要使用升压药物来维持血压，同时处理肺水肿。

在处理过程中，还需要考虑其他可能的并发症，如心律失常，可能需要监测和处理。此外，补液后的血压下降可能提示液体过负荷，因此在补液时需要谨慎，可能需要调整液体量或使用利尿剂来管理。

综上所述，最恰当的药物处理可能包括使用升压药物如多巴胺或多巴酚丁胺来维持血压，同时使用利尿剂处理肺水肿，并辅以硝酸甘油和ACEI类药物来减轻心脏负荷。同时，吗啡可能用于缓解胸痛和焦虑，但需注意呼吸抑制。此外，抗凝治疗也是必要的，以防止血栓扩展和并发症。
</think>

### 分步解释：

1. **评估心电图结果**：下壁导联和右胸导联的ST段抬高提示下壁心肌梗死，可能影响右心室功能。

2. **处理胸痛和改善心脏供血**：使用硝酸甘油扩张冠状动脉，缓解胸痛，但需监测血压，避免过低。

3. **缓解呼吸困难和肺水肿**：应用利尿剂（如呋塞米）减少肺部液体，改善呼吸情况。

4. **提升血压和心脏输出**：使用升压药物（如多巴胺）以维持血压，改善心脏泵血功能。

5. **减轻心脏负荷**：使用ACEI类药物（如依那普利）帮助降低血压和减轻心脏负担。

6. **缓解疼痛和焦虑**：使用吗啡减轻胸痛，但需注意可能的呼吸抑制作用。

7. **抗凝治疗**：预防血栓扩展，通常使用肝素。

8. **监测和处理并发症**：如心律失常、低血压等，确保患者生命体征稳定。

### 最终药物处理建议：

- **立即处理**：
  - **硝酸甘油**：静脉滴注，根据血压调整剂量。
  - **吗啡**：静脉注射，缓解疼痛。
  - **利尿剂**：如呋塞米，静脉注射，处理肺水肿。
  - **升压药物**：如多巴胺，静脉滴注，维持血压≥80/60mmHg。

- **后续治疗**：
  - **ACEI类药物**：如依那普利，改善心脏功能。
  - **β受体阻滞剂**：如美托洛尔，降低心率，但需在血压稳定后使用。
  - **抗凝治疗**：如肝素，预防血栓。

- **监测与支持**：
  - 持续监测血压、心率、呼吸和液体平衡。
  - 评估是否需要机械通气，尤其是如果呼吸困难严重。
  - 考虑进一步的介入治疗，如PCI（经皮冠状动脉介入术）以恢复血流。

```

微调后：

```
  <think>
  该患者70岁，胸痛16小时，伴呕吐，心电图显示下壁导联和右胸导联ST段抬高0.1~0.3mV，补液后血压降至80/60mmHg，出现呼吸困难和不能平卧，体检发现双肺有大量水泡音。

首先，心电图显示下壁导联和右胸导联ST段抬高，这提示可能有下壁心肌梗死。同时，血压下降和呼吸困难可能与心肌梗死导致心功能不全有关。补液后血压降至80/60mmHg，这可能表明存在心功能不全或心源性休克。

双肺大量水泡音提示肺水肿，这可能与心功能不全有关。因此，该患者的症状和体征提示有心源性休克和肺水肿。在这种情况下，最恰当的药物处理是使用利尿剂，如速尿，以减轻肺水肿和改善呼吸困难。

此外，补液后血压下降可能表明存在血容量过载或心功能不全。在这种情况下，补液可能不是最佳选择。相反，使用利尿剂可以帮助降低心室前负荷，减轻心脏负担，从而改善血压和呼吸困难。

综上所述，针对该患者的症状和体征，使用利尿剂如速尿是最恰当的药物处理。
</think>

在这种情况下，最恰当的药物处理是使用利尿剂，如速尿，以减轻肺水肿和改善呼吸困难。
```


关于微调效果，AI（Claude 3.7 Sonnet thinking）给出了如下分析😂：

> 通过对比微调前后的模型输出，我可以看到明显的差异和微调效果：
>
> 1. 简洁度提升：
>    - 微调前：输出非常详细，包含大量医学推理和多种药物选择，回答长达数百字
>    - 微调后：输出极其简洁，仅有一句话直接指出最恰当的药物处理方案
> 2. 焦点集中：
>    - 微调前：提供了全面的药物方案，包括硝酸甘油、吗啡、ACEI类药物、β受体阻滞剂等多种选择
>    - 微调后：直接聚焦于最关键的药物（利尿剂/速尿），不再列举其他可能的治疗方案
> 3. 思维过程变化：
>    - 微调前：`<think>` 部分非常详细，展示了全面的医学推理过程
>    - 微调后：`<think>` 部分仍有一定推理，但更加简洁直接，直接指向最终结论
> 4. 信息密度：
>    - 微调前：信息密度较低，内容冗余，虽然全面但需要阅读更多文本
>    - 微调后：信息密度高，直接提供关键治疗建议
> 5. 实用性：
>    - 微调后的模型似乎更专注于提供一个明确的、可操作的治疗方案，而不是列举所有可能性
> 6. 总体评价：
>    - 微调效果显著，模型回答风格从全面详尽转变为简洁直接。这种变化是否是改进取决于具体使用场景：
>
> - 如果目标是获得快速、明确的临床决策建议，微调后的模型表现更好
> - 如果需要全面的医学推理和多种治疗方案的详细说明，原始模型可能更适合
>
> 值得注意的是，微调后的模型保留了关键的医学推理能力（在`<think>`部分），但最终输出极度精简，这表明微调可能特别强调了提供简洁明了的最终答案的能力。



## 八、微调后模型的正确使用与常见问题

### 1. 安全终止卡住的推理进程

如果您在微调后进行推理测试时遇到进程卡住的情况（例如超过2小时没有输出），可以安全地使用`Ctrl+C`终止当前进程。这不会影响已经完成的训练和保存的模型权重。

微调的成果（适配器权重）已经完全保存在`outputs/checkpoint-XX`目录中（其中XX是最后一个检查点的步数，通常是您设置的`max_steps`值）。

### 2. 理解LoRA微调的输出结构

使用LoRA/PEFT进行微调时，输出的不是完整模型，而是一组适配器权重。这些权重需要与原始基础模型结合使用。检查点目录通常包含：

- `adapter_config.json` - 适配器配置信息
- `adapter_model.safetensors` - 适配器权重文件
- `training_args.bin` - 训练参数
- 其他训练状态文件

### 3. 常见问题与解决方案

#### 推理卡住或无响应

如果推理过程卡住或长时间无响应：

1. **检查适配器路径**：确保适配器路径指向正确的检查点目录
2. **设置生成限制**：添加严格的`max_new_tokens`和`max_time`限制
3. **使用CPU回退**：如果MPS持续失败，可以直接使用CPU进行推理
4. **分批处理长文本**：对于长输入，考虑分批处理以减少内存压力

#### 数据类型不匹配错误

如果遇到数据类型不匹配错误：

1. **统一数据类型**：确保所有张量使用相同的数据类型（推荐float32）
2. **避免混合精度**：在MPS上避免使用混合精度训练和推理
3. **检查模型配置**：确保模型配置与适配器配置兼容

#### 内存不足错误

如果遇到内存不足错误：

1. **减少生成参数**：降低`max_new_tokens`和`temperature`
2. **禁用采样**：设置`do_sample=False`以减少内存使用
3. **清理缓存**：定期调用`torch.mps.empty_cache()`
4. **减少批量大小**：使用较小的批量大小进行推理

### 4. 保存完整模型（可选）

如果您希望将适配器与基础模型合并并保存为完整模型，可以使用以下代码：

```python
# 合并适配器和基础模型
merged_model = model.merge_and_unload()

# 保存完整模型
merged_model_path = "DeepSeek-R1-Medical-Merged"
merged_model.save_pretrained(merged_model_path)
tokenizer.save_pretrained(merged_model_path)
print(f"合并后的模型已保存到: {os.path.abspath(merged_model_path)}")
```

请注意，合并后的模型将占用更多磁盘空间，但在推理时不再需要原始基础模型。




---

## 附：原版文章内容


参考文章：[《单卡 RTX 4090 用 unsloth 和医学数据微调 DeepSeek-R1-Distill-Qwen-14B》](https://mp.weixin.qq.com/s?__biz=MzUxMTczMTY1Ng==&mid=2247483742&idx=1&sn=b90af9f3a12f182f5e48859b3b72a40a&scene=21#wechat_redirect)

在文章[《单卡4090微调DeepSeek-R1-32B》](https://mp.weixin.qq.com/s?__biz=MzUxMTczMTY1Ng==&mid=2247483746&idx=1&sn=b7e16a70ffb903b64f28424677fb22c0&chksm=f96e73c5ce19fad3b1b26f56826a3e2470bcfbc8675995c6a47b2b9e466d19ba6ec2c6a32277&cur_album_id=3854852837976768515&scene=189#wechat_redirect)中，提供的训练代码如下：


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

## 总结：macOS版本与原版代码的主要调整

对比原始RTX 4090上使用Unsloth框架的代码与适配macOS的版本，主要进行了以下调整：

### 1. 框架与架构调整
- **框架替换**：完全放弃了Unsloth框架，改用标准的Hugging Face PEFT库实现LoRA微调
- **后端适配**：从CUDA后端转换到Apple Silicon的MPS后端，并针对MPS的特性进行了大量兼容性修复
- **训练器变更**：从Unsloth的`SFTTrainer`替换为标准的Hugging Face `Trainer`

### 2. 内存优化策略
- **添加主动内存管理**：定义了`clean_memory()`函数，在关键节点主动清理GPU和CPU内存
- **环境变量优化**：添加了多个MPS相关环境变量控制内存使用和错误处理
- **批量处理参数转换**：实现了`safe_prepare_model_for_kbit_training`函数，分批处理参数转换以避免内存峰值

### 3. 训练参数调整
- **批次大小降低**：从`per_device_train_batch_size=2`降低到`1`
- **梯度累积增加**：从`gradient_accumulation_steps=4`增加到`8`
- **序列长度缩短**：从`max_seq_length=4096`降低为`2048`
- **LoRA参数减小**：从`r=32`减小到`r=16`以节省内存
- **数据精度调整**：禁用半精度(`fp16=False`、`bf16=False`)，统一使用`float32`避免MPS数据类型不匹配错误
- **禁用多进程**：设置`dataloader_num_workers=0`避免多进程带来的额外内存开销

### 4. 兼容性修复与智能回退
- **数据类型统一**：实现了确保所有张量使用相同数据类型(`float32`)的特殊处理
- **智能推理回退**：添加了`smart_inference`函数，在MPS后端失败时自动回退到CPU推理
- **梯度检查点与KV缓存冲突解决**：在不同阶段适当启用/禁用`gradient_checkpointing`和`use_cache`

### 5. 错误处理与用户体验
- **详细错误分析**：对常见错误类型提供具体的修复建议
- **分级配置指南**：根据不同Mac内存配置提供相应的参数调整建议
- **路径处理改进**：强调并解决了Python脚本中`~/`路径不会自动展开的问题

这些调整不仅解决了Apple Silicon架构与MPS后端的兼容性问题，还通过精心优化的内存管理和参数调整，使得在内存有限的Mac设备上也能稳定地微调14B参数级别的大语言模型。



## Mac设备微调大语言模型的其他参考信息

### 非CUDA环境下的大模型微调实践

[有云技术团队的实践](https://mp.weixin.qq.com/s?__biz=MjM5NjUxNDIwNw==&mid=2654069282&idx=1&sn=ac26cc4dc52b0d63c11cfee95edd2187&chksm=bc35d7c842283b695ab733fa22829780c7e39ca62ae460fa4fd2cae2594bf175a0b4291eeb4e#rd)展示了如何在Mac M系列芯片上使用LoRA技术对DeepSeek Coder模型进行微调，为使用 Mac 的开发者提供参考。

#### 1. 项目概述

**目标**：
- 使用LoRA技术微调DeepSeek Coder 7B模型
- 优化模型在代码生成任务上的表现
- 在Mac M系列芯片上实现微调训练和推理

**技术栈**：
- 基础模型：deepseek-ai/deepseek-coder-7b-base
- 训练框架：PyTorch + Transformers + PEFT
- 硬件平台：Mac M系列芯片(MPS后端)

#### 2. 实现流程

**数据准备**：
```python
training_data = [
    {
        "instruction": "实现一个二分查找算法",
        "input": "",
        "output": """def binary_search(arr, target):
    left, right = 0, len(arr) - 1
    while left <= right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1"""
    },
    {
        "instruction": "编写一个快速排序函数",
        "input": "",
        "output": """def quicksort(arr):
    if len(arr) <= 1:
        return arr
    pivot = arr[len(arr) // 2]
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]
    right = [x for x in arr if x > pivot]
    return quicksort(left) + middle + quicksort(right)"""
    }
]
```

**LoRA配置**：
```python
LORA_CONFIG = LoraConfig(
    task_type=TaskType.CAUSAL_LM,      # 任务类型:因果语言模型
    r=8,                               # LoRA矩阵的秩,控制可训练参数量
    lora_alpha=32,                     # 缩放因子,控制LoRA更新的重要性
    lora_dropout=0.1,                  # Dropout比例,防止过拟合
    target_modules=[                   # 需要训练的模块
        "q_proj",                      # 查询矩阵投影层
        "v_proj",                      # 值矩阵投影层  
        "k_proj",                      # 键矩阵投影层
        "o_proj",                      # 输出投影层
    ],
    bias="none",                       # 是否训练偏置项
    modules_to_save=None               # 需要完整保存的模块
)
```

**训练参数优化**：
```python
training_args = TrainingArguments(
    output_dir="./deepseek-finetuned",
    per_device_train_batch_size=1,     # 受限于M系列芯片内存
    gradient_accumulation_steps=64,    # 累积梯度以模拟大批量
    learning_rate=5e-5,
    num_train_epochs=3,
    gradient_checkpointing=True,       # 减少内存使用
    max_grad_norm=0.3,                 # 梯度裁剪,防止梯度爆炸
    warmup_steps=50                    # 预热步数
)
```

#### 3. Mac M系列适配技巧

**内存管理**：
```python
# 设置MPS内存水位
os.environ['PYTORCH_MPS_HIGH_WATERMARK_RATIO'] = '0.0'
os.environ['PYTORCH_MPS_LOW_WATERMARK_RATIO'] = '0.0'

# 定期清理内存
if torch.backends.mps.is_available():
    torch.mps.empty_cache()
```

**性能优化策略**：

1. **模型优化**：
   - KV Cache优化：启用注意力缓存机制,优化缓存更新策略
   - 计算图优化：使用torch.inferencemode(),启用静态图优化
   - 生成参数调优：自适应batch size,优化采样策略

2. **内存优化**：
   - 批处理策略：动态batch size调整,梯度累积步数优化
   - 梯度检查点：选择性激活检查点,平衡内存与速度
   - 显存管理：及时释放无用张量,使用CPU offload,启用混合精度训练

3. **数据加载优化**：
   - 预处理加速：多进程数据加载,数据预取与缓存
   - 内存管理：使用内存映射,渐进式数据加载
   - IO优化：使用异步加载,优化数据格式

4. **训练流程优化**：
   - 监控与调整：实时性能分析,自动化参数调优
   - 容错机制：检查点保存,训练状态恢复

#### 4. 推理优化配置

```python
generation_config = {
    "max_length": 2048,               # 生成文本的最大长度
    "temperature": 0.7,               # 采样温度,控制生成的随机性
    "top_p": 0.95,                    # 核采样阈值,控制词表采样范围
    "top_k": 50,                      # 保留概率最高的k个token
    "do_sample": True,                # 是否使用采样策略
    "num_beams": 1,                   # beam search的束宽
    "repetition_penalty": 1.1,        # 重复惩罚因子,避免重复生成
    "early_stopping": True,           # 是否提前停止生成
    "pad_token_id": tokenizer.pad_token_id,  # padding token的ID
    "eos_token_id": tokenizer.eos_token_id,  # 结束符token的ID
}
```

#### 5. 主要挑战与解决方案

**MPS后端内存管理**：
- 内存泄漏问题：MPS后端缓存积累、张量碎片化、自动回收不及时
- 解决方案：定期手动清理缓存、监控内存使用、优化张量生命周期

**消费级硬件优化**：
- 计算能力限制：单GPU显存不足、计算速度瓶颈
- 解决方案：模型量化、梯度累积、优化器内存优化

**训练和推理性能平衡**：
- 训练效率：批处理大小受限、梯度累积开销大
- 推理延迟：首次推理延迟高、响应时间不稳定
- 解决方案：动态批处理、KV缓存优化、预热模型

#### 6. 参考资源

- DeepSeek Coder官方文档
- PEFT库文档
- PyTorch MPS文档
- LoRA论文


### 基于Apple MLX框架的大模型微调

[Apple MLX框架](https://github.com/ml-explore/mlx)是苹果官方开源的针对Apple Silicon芯片优化的深度学习框架，为开发者提供了在Mac等设备上直接进行模型训练和部署的能力。[文章](https://mp.weixin.qq.com/s?__biz=MzA5NDc3ODQxNQ==&mid=2247484394&idx=1&sn=aeeb89f13a5269cbda8ca6f31d198b89&chksm=915cb4f81cab6148fda2f458f969edec253f789c4279ba67cd946f6d0fdbc3eb2a93f32c389a#rd)介绍基于MLX框架在M1设备上微调Mistral-7B模型的实践过程。

#### 1. MLX框架特性

- **熟悉的API**：提供类似NumPy的Python API和类似PyTorch的高级API
- **可组合函数转换**：支持自动微分、自动矢量化和计算图优化
- **惰性计算**：只在需要时才具体化数组，提高计算效率
- **动态图构建**：无需重编译即可支持形状变化，简化调试
- **多设备支持**：可在CPU和GPU上运行，充分利用硬件
- **统一内存模型**：数组位于共享内存，无需设备间数据移动

#### 2. 微调环境准备

```bash
# 获取MLX示例代码
git clone https://github.com/ml-explore/mlx-examples
cd mlx-examples/lora

# 安装依赖
pip install -r requirements.txt

# 下载Mistral-7B模型
curl -O https://files.mistral-7b-v0-1.mistral.ai/mistral-7B-v0.1.tar
tar -xf mistral-7B-v0.1.tar

# 转换模型格式为MLX格式
python convert.py \
  --torch-model mistral-7B-v0.1 \
  --mlx-model mistral-7b-v0.1-mlx
```

#### 3. 训练数据格式

MLX示例中使用WikiSQL数据集进行文本到SQL的转换训练，数据格式如下：

```json
{
    "text": "table: 1-1000181-1\ncolumns: State/territory, Text/background colour, Format, Current slogan, Current series, Notes\nQ: Tell me what the notes are for South Australia \nA: SELECT Notes FROM 1-1000181-1 WHERE Current slogan = 'SOUTH AUSTRALIA'"
}
```

数据集结构：
- `table`: 表名称
- `columns`: 列名称列表
- `Q`: 用户自然语言问题
- `A`: 对应的SQL查询语句

#### 4. 内存优化与参数调整

在M1设备上进行微调时，内存管理是关键挑战。以下是优化参数：

```bash
# 优化内存使用的训练命令
python lora.py \
   --model mistral-7b-v0.1-mlx \
   --train \
   --batch-size 1 \          # 降低批次大小减少内存占用
   --lora-layers 4           # 减少微调层数
```

**关键参数解析**：
- `--batch-size`：默认为4，降低至1或2可显著减少内存消耗
- `--lora-layers`：默认为16，可降至8或4减少反向传播内存，但可能影响微调质量
- 数据集长度：较长样本需要更多内存，可考虑分解为更小序列

#### 5. 性能与训练过程

在32GB的M1 Mac上，使用优化参数后的训练性能：
- 处理速度：约110 tokens/秒
- 损失变化：从初始2.265降至1.325（800次迭代后）

**不同参数配置的损失变化对比**：

| 配置 | 迭代次数 | Loss |
|------|---------|------|
| batch=1, lora=4 | 800 | 1.325 |
| batch=2, lora=8 | 800 | 1.213 |
| batch=2, lora=8, 更大数据集 | 800 | 1.360 |


#### 6. 模型评估与输出示例

微调后的模型用于自然语言到SQL的转换任务：

```bash
python lora.py --model mistral-7b-v0.1-mlx \
               --adapter-file adapters_2_8.npz \
               --num-tokens 50 \
               --prompt "table: 1-10015132-16
columns: Player, No., Nationality, Position, Years in Toronto, School/Club Team
Q: What is terrence ross' nationality
A: "
```

**模型输出**：
```
A: SELECT Nationality FROM 1-10015132-16 WHERE Player = 'Terrence Ross'
```

#### 7. 经验总结与建议

1. **内存管理策略**：
   - 降低批次大小是最直接有效的内存减少方法
   - 降低LoRA层数可减少内存，但会影响模型质量
   - 32GB内存设备建议最大使用8个LoRA层

2. **数据集考量**：
   - 训练集大小对结果影响显著
   - 对于复杂任务，1000条样本通常不足，建议使用5000-10000条

3. **适用场景**：
   - RAG系统自定义：针对特定领域知识微调
   - 小型专业模型：如文本到SQL转换器
   - 开发和测试：无需高端GPU即可进行原型开发

#### 8. 技术价值与意义

- **生态多样化**：为非NVIDIA设备提供大模型微调能力
- **开发门槛降低**：降低硬件要求，使更多开发者能参与大模型开发
- **苹果设备优势**：充分利用Apple Silicon统一内存架构
- **应用场景扩展**：支持在边缘设备上进行个性化模型适配

MLX框架为Apple设备提供了一条不依赖NVIDIA CUDA的大模型微调路径，虽然在处理大规模训练时仍有局限，但对于特定任务的适应性微调已经展现出实用价值，未来随着框架优化和Apple硬件升级，这一方案的可行性将进一步增强。

#### 9. 参考资源

- [MLX GitHub仓库](https://github.com/ml-explore/mlx)
- [MLX LoRA微调示例](https://github.com/ml-explore/mlx-examples/tree/main/lora)
- [八一菜刀：基于Apple MLX框架的M1设备上大模型微调实践](https://mp.weixin.qq.com/s?__biz=MzA5NDc3ODQxNQ==&mid=2247484394&idx=1&sn=aeeb89f13a5269cbda8ca6f31d198b89&chksm=915cb4f81cab6148fda2f458f969edec253f789c4279ba67cd946f6d0fdbc3eb2a93f32c389a#rd)


### Apple设备深度学习框架

#### MPS与MLX框架对比

MPS后端和MLX框架虽然都利用Apple硬件能力，但它们在架构和设计理念上有明显区别：

##### 底层硬件调用
- **共同点**：二者都使用苹果的Metal图形API来访问计算能力
- **共同点**：都充分利用了Apple Silicon芯片的统一内存架构

##### 架构层次
- **MPS (Metal Performance Shaders)**：
  - 是一个适配层，让PyTorch等现有框架能在苹果硬件上运行
  - 本质上是将现有框架的运算翻译成Metal指令
  - 类似"翻译器"角色，存在一定的转换开销

- **MLX框架**：
  - 苹果从头开发的框架，直接为Apple Silicon架构优化
  - 无需"翻译"层，直接生成针对苹果硬件的最优指令
  - 更深度整合了苹果硬件的特性（特别是统一内存）

##### 技术性能对比

```
性能优化层次：
┌─────────────────────────┐
│     应用代码 (Python)    │
├───────────┬─────────────┤
│  PyTorch  │    MLX      │
├───────────┤             │
│ MPS后端   │             │
├───────────┴─────────────┤
│        Metal API        │
├─────────────────────────┤
│    Apple Silicon硬件    │
└─────────────────────────┘
```

- **内存效率**：MLX通常比PyTorch+MPS有更高的内存利用率
- **计算性能**：MLX在相同硬件上通常能提供更好的性能，特别是在矩阵运算上
- **启动时间**：MLX通常有更快的启动和编译时间

#### Apple Metal计算资源管理

Metal不仅限于GPU，它是Apple设计的底层图形和计算API，能够访问Apple Silicon芯片上的多种计算资源：

##### Metal的全面计算能力
- **CPU**：包括性能核心和能效核心
- **GPU**：集成的图形处理单元
- **神经引擎(Neural Engine)**：专用于机器学习加速的硬件
- **AMX(Apple Matrix accelerators)**：矩阵计算加速器
- **媒体引擎**：视频编解码专用处理单元

##### 自动资源分配能力

```
┌──────────────────────────────────────────┐
│           应用程序 (MLX/PyTorch等)        │
└───────────────────┬──────────────────────┘
                    ↓
┌──────────────────────────────────────────┐
│                 Metal框架                 │
└───────┬───────────┬────────────┬─────────┘
        ↓           ↓            ↓         ↓
   ┌─────────┐ ┌─────────┐ ┌──────────┐ ┌─────────┐
   │   CPU   │ │   GPU   │ │ 神经引擎  │ │  AMX    │
   └─────────┘ └─────────┘ └──────────┘ └─────────┘
```

Metal的任务分配策略：

1. **智能调度**：根据工作负载特性自动选择适当的计算单元
2. **异构计算**：可以同时利用多种计算资源协同工作
3. **专用加速**：特定运算会自动路由到最适合的硬件单元

##### 框架差异在资源利用上

- **MPS(Metal Performance Shaders)**：
  - 主要优化GPU计算，对其他计算单元支持有限
  - 通过Metal访问硬件，但优化层次较浅

- **MLX**：
  - 深度整合所有计算资源，包括CPU、GPU、神经引擎
  - 更智能地分配不同类型的计算任务到最适合的硬件
  - 专为机器学习优化，了解不同计算模式的特点

在训练大语言模型时，Metal能够自动将不同类型的计算任务分配给最适合的硬件单元：
- 矩阵乘法可能路由到GPU或AMX
- 注意力机制可以利用GPU的并行能力
- 激活函数可能在CPU上执行
- 特定ML算子可以利用神经引擎加速

这种全面的计算资源调度是Apple设备上能够进行大模型微调的关键技术基础，也是为什么即使没有高端NVIDIA GPU，Apple设备仍能有效支持机器学习工作负载的根本原因。

### Mac设备上微调大语言模型的通用注意事项

通过分析上述两篇关于Mac设备上微调大模型的实践文章(DeepSeek+MPS与MLX框架)，可以总结出以下通用的关键注意事项：

#### 1. 内存管理是核心挑战

- **内存水位控制**：设置`PYTORCH_MPS_HIGH_WATERMARK_RATIO`和`PYTORCH_MPS_LOW_WATERMARK_RATIO`参数控制内存使用上限
- **手动清理缓存**：定期使用`torch.mps.empty_cache()`或MLX等效命令清理GPU内存
- **内存泄漏监控**：特别关注MPS后端缓存积累、张量碎片化问题，长时间训练时尤为重要

#### 2. 训练参数优化

- **批次大小降低**：将batch size降至1，这是最直接有效的内存节省方法
- **梯度累积**：使用合理的`gradient_accumulation_steps`(如8-16)来模拟大批量训练效果，但不必过高(如64)，过高会影响收敛速度和训练效率
- **微调层数限制**：
  - 针对特定关键层(q_proj, v_proj, k_proj, o_proj, gate_proj, up_proj, down_proj)进行LoRA微调
  - 32GB内存设备可以尝试使用更多层和更大r值(如r=16)，16GB设备建议4层或更少且r值更小(如r=8)

#### 3. 技术策略共识

- **梯度检查点**：采用`gradient_checkpointing=True`以平衡性能和内存占用
- **专注于LoRA**：LoRA技术是目前Mac设备上微调大模型的最佳选择，添加参数少、内存占用低
- **数据类型管理**：在MPS后端上**必须禁用fp16和bf16**，统一使用float32以避免数据类型不匹配错误，这点与CUDA环境不同
- **数据集规模**：Mac设备上微调需谨慎控制数据集大小，建议根据任务复杂度选择适当数量，初始验证时可以只使用少量样本(如200条)

#### 4. 性能与质量平衡

- **训练与推理权衡**：需要在训练效率与模型效果间做出权衡，参数越小训练越快但效果可能较差
- **训练时间预期**：相比NVIDIA GPU，需要接受更长的训练时间
- **模型规模选择**：
  - 16GB内存设备：最大建议7B模型
  - 32GB内存设备：可以微调14B级别模型，但需谨慎调整参数
  - 64GB及以上内存设备：可更稳定地微调14B以上模型

#### 5. 框架选择考量

- **PyTorch+PEFT**：本文实践采用的方案，兼容性更好，生态更丰富，但需要针对MPS后端进行多项兼容性修复
- **MLX框架**：苹果专门开发的替代方案，针对Apple Silicon优化，性能可能更好，但生态较新，工具链不够丰富
- **统一内存优势**：两种方法都能充分利用Apple Silicon芯片的统一内存架构

#### 6. 实用建议

- **预热阶段**：为模型设置适当的预热步数(5-10步)以稳定初始训练
- **KV缓存与梯度检查点冲突**：在训练时必须设置`model.config.use_cache = False`以避免与梯度检查点冲突
- **智能推理回退**：实现智能推理函数，在MPS后端失败时自动回退到CPU
- **功耗管理**：注意Mac设备在长时间训练中的散热问题，可能需要外部散热支持
- **分批训练**：将长时间训练分为多个短训练阶段，定期保存检查点，避免因内存问题训练失败
- **路径处理正确性**：在Python脚本中使用`os.path.expanduser()`展开包含`~`的路径，避免路径错误

Mac设备上的大模型微调虽然面临资源限制，但通过合理的参数设置和优化策略，完全可以实现有效的微调，尤其适合原型开发、概念验证和特定领域的小规模定制化需求。
