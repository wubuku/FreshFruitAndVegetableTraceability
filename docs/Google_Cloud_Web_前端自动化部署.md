# Google Cloud Web 前端自动化部署指南

## 导读

本文介绍如何使用 GitHub Actions 将 Web 前端应用自动部署到 Google Cloud。

适用场景：
- 个人开发者使用 GitHub 免费版
- 需要自动化部署流程
- 需要确保部署安全性

### 前置知识

本文假设你：
- 熟悉基本的软件开发流程
- 了解 Git 基本操作
- 有一个可部署的 Web 前端项目
- 对 CI/CD 概念有基本了解

## 1. 基础设施准备

### 1.1 GitHub 仓库设置

GitHub 免费版限制：
- 私有仓库的协作者数量
- GitHub Actions 每月 2000 分钟运行时间
- 存储空间 500MB

### 1.2 Google Cloud 准备

1. 创建账号和项目：
   - 注册 Google Cloud 账号
   - 创建新项目
   - 记录项目 ID

2. 启用必要的 API：
```bash
# 在 Google Cloud Console 执行
gcloud services enable \
    cloudbuild.googleapis.com \
    run.googleapis.com \
    cloudresourcemanager.googleapis.com
```

### 1.3 域名设置

如果您的域名不是在 Google Cloud DNS 注册的，有两种方案可以配置：

#### 方案一：直接使用 Cloud Run URL（最简单）

这种方案不需要任何额外配置：
- 直接使用 Cloud Run 提供的 URL（如 `https://your-app-xxx-xxx.a.run.app`）
- 优点：
  - 零配置
  - 自动 HTTPS
  - 自动证书管理
- 缺点：
  - URL 不够友好
  - 无法使用自定义域名

#### 方案二：配置自定义域名（推荐）

1. **在 Cloud Run 中映射域名**：
```bash
# 将您的域名映射到 Cloud Run 服务
gcloud run domain-mappings create \
    --service=your-service-name \
    --domain=www.your-domain.com \
    --platform=managed \
    --region=asia-east1
```

> **HTTPS 证书说明**
> - Cloud Run 会自动为映射的域名配置 SSL/TLS 证书
> - 证书由 Google 管理的 CA 签发
> - 证书会自动续期，无需手动操作
> - 支持现代的 TLS 1.3 协议
> - 完全免费，无需额外付费

2. **验证域名所有权**：
- 执行上述命令后，Google Cloud 会提供一条 TXT 记录
- 需要在您的域名服务商控制台添加这条记录
- 格式类似：
  ```
  名称: @
  类型: TXT
  值: google-site-verification=xxxxxxxxxxxxxxxxx
  ```

3. **配置 DNS 记录**：
- 在您的域名服务商控制台添加以下记录：

```
# 对于根域名
类型: A
名称: @
值: 获取自 Cloud Run 的 IP 地址列表（通常有多个）

# 对于 www 子域名
类型: A
名称: www
值: 获取自 Cloud Run 的 IP 地址列表（通常有多个）

# 或者使用 CNAME（推荐）
类型: CNAME
名称: www
值: your-app-xxx-xxx.a.run.app
```

> **提示**
> - CNAME 记录更灵活，因为它会自动跟随 Cloud Run 的 IP 变化
> - 但根域名（@）不能使用 CNAME，只能使用 A 记录

4. **等待 DNS 生效**：
- DNS 记录可能需要几分钟到 48 小时不等的时间来全球生效
- 可以使用以下命令查看映射状态：
  ```bash
  gcloud run domain-mappings describe \
      --domain=www.your-domain.com \
      --platform=managed \
      --region=asia-east1
  ```

#### 关于负载均衡

对于大多数前端应用，Cloud Run 内置的负载均衡足够使用：
- 自动处理流量分配
- 自动扩缩容
- 全球 CDN
- DDoS 保护

但如果您需要更高级的负载均衡功能，可以考虑配置 Google Cloud Load Balancer：

1. **何时需要额外的负载均衡器**：
   - 需要跨区域负载均衡
   - 需要更复杂的路由规则
   - 需要 WebSocket 支持
   - 需要集成其他 Google Cloud 服务

2. **配置负载均衡器**：
```bash
# 1. 创建负载均衡器
gcloud compute forwarding-rules create web-frontend \
    --load-balancing-scheme=EXTERNAL \
    --network-tier=PREMIUM \
    --address=your-static-ip \
    --ports=80,443 \
    --target-http-proxy=your-http-proxy

# 2. 配置 SSL 证书（如果需要 HTTPS）
gcloud compute ssl-certificates create www-cert \
    --domains=www.your-domain.com

# 3. 创建后端服务
gcloud compute backend-services create web-backend \
    --protocol=HTTP \
    --port-name=http \
    --health-checks=http-health-check \
    --global
```

> **注意**
> - 使用负载均衡器会产生额外费用
> - 对于大多数前端应用，Cloud Run 的内置功能已经足够
> - 建议先使用 Cloud Run 的基本功能，需要时再添加负载均衡器

### 为什么选择 Cloud Run？

Cloud Run 是一个适合部署前端应用的服务，因为：

1. **成本效益**
- 按实际使用量计费
- 没有访问时不产生费用
- 有慷慨的免费配额：
  ```text
  每月免费额度：
  - 180,000 vCPU-秒
  - 360,000 GiB-秒内存
  - 2 million 请求
  ```

2. **简单部署**
```bash
# 部署一个容器到 Cloud Run
gcloud run deploy my-web-app \
    --image gcr.io/my-project/my-web-app \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated
```

3. **自动扩缩容**
```yaml
# 在服务配置中设置扩缩容规则
spec:
  template:
    metadata:
      annotations:
        autoscaling.knative.dev/minScale: "0"  # 最小实例数（可以到0）
        autoscaling.knative.dev/maxScale: "10" # 最大实例数
```

4. **安全特性**
- 默认 HTTPS
- 自动证书管理
- 内置 DDoS 保护
- IAM 集成

### Cloud Run vs App Engine

对比其他 Google Cloud 服务：

| 特性 | Cloud Run | App Engine |
|------|-----------|------------|
| 部署单位 | 容器 | 源代码 |
| 语言支持 | 任何语言 | 特定运行时 |
| 冷启动 | 较快 | 较慢 |
| 定价 | 按使用量 | 实例时间 |
| 最小实例 | 可以为 0 | 至少 1 个 |
| 自定义 | 高度自定义 | 框架限制 |



### 测试 Cloud Run 配置

#### 1. 部署测试容器

使用 Google 官方的 hello-world 容器镜像来测试：

```bash
# 部署一个测试服务
gcloud run deploy hello-test \
    --image gcr.io/google-samples/hello-app:1.0 \
    --platform managed \
    --region asia-east1 \
    --allow-unauthenticated

# 部署完成后，命令会返回服务URL，格式类似：
# Service URL: https://hello-test-[unique-id].[region].run.app
# 
# 例如：
# https://hello-test-152582749766.asia-east1.run.app
# 
# 这个 URL 是部署时生成的，其中：
# - hello-test 是服务名
# - 152582749766 是部署时生成的唯一标识符
# - asia-east1 是区域名
```

#### 2. 验证服务

有几种方式可以验证服务是否正常运行：

```bash
# 方式1：使用 curl 访问服务URL
# 注意：gcloud describe 命令返回的 URL 可能与部署时显示的 URL 看起来不同
# 例如：https://hello-test-cya7rmdd2q-de.a.run.app
# 这两个 URL 都是有效的，都会指向同一个服务
# URL 格式的差异是由 Cloud Run 的内部路由和负载均衡机制导致的
# 建议使用 gcloud describe 命令获取的 URL，因为这是服务的当前规范 URL
curl $(gcloud run services describe hello-test --platform managed --region asia-east1 --format 'value(status.url)')

# 方式2：获取服务信息
gcloud run services describe hello-test \
    --platform managed \
    --region asia-east1

# 方式3：列出所有运行中的服务
gcloud run services list
```

#### 3. 清理资源

测试完成后，删除测试服务以节省资源：

```bash
# 删除测试服务
gcloud run services delete hello-test \
    --platform managed \
    --region asia-east1 \
    --quiet  # 跳过确认提示
```

> **提示**
> - Cloud Run 的按使用量计费特性意味着：当服务没有流量时，不会产生费用
> - 但为了避免意外的资源使用，建议在测试后删除不需要的服务


## 2. 自动化部署流程

### 2.1 分支策略说明

你可以根据需要选择不同的部署分支策略：

1. **常见的分支策略**：
```plaintext
your-project/
├── main        # 主分支，用于生产环境
├── staging     # 预发布分支，用于测试
└── develop     # 开发分支，用于日常开发
```

2. **如何修改部署分支**：
```yaml
# .github/workflows/deploy.yml
name: Deploy to Google Cloud
on:
  push:
    branches:
      - main     # 可以改为其他分支名
      - staging  # 可以添加多个分支
```

3. **分支与环境对应关系**

为什么需要在两个地方设置环境？

1. **项目目录中的环境文件**（`.env.production` 和 `.env.staging`）
   - 这些文件是给你的应用使用的
   - 包含应用运行需要的配置（如 API 地址）
   - 在构建时会被打包进应用

2. **GitHub 中的环境设置**（Settings → Environments）
   - 这些设置是给 GitHub Actions 使用的
   - 控制部署流程和权限
   - 可以设置部署的保护规则，比如：
     - 谁可以部署到这个环境
     - 是否需要审批
     - 是否需要等待时间
   - 可以存储敏感信息（如部署密钥）

工作流程：
1. 当你推送代码时：
   - GitHub Actions 先检查环境设置
   - 确认是否有权限部署
   - 如果需要，等待审批

2. 部署开始后，需要在 GitHub Actions 配置中指定使用哪个环境文件：
```yaml
# .github/workflows/deploy.yml
jobs:
  deploy:
    environment:
      name: ${{ github.ref == 'refs/heads/main' && 'production' || 'staging' }}
    
    steps:
      # ... 其他步骤 ...
      
      - name: Build
        env:
          # 根据当前环境选择对应的 .env 文件
          NODE_ENV: ${{ github.ref == 'refs/heads/main' && 'production' || 'staging' }}
        run: |
          # 如果使用 npm
          npm run build --mode $NODE_ENV
          
          # 或者如果使用 yarn
          # yarn build --mode $NODE_ENV

      # ... 后续步骤 ...
```

这个配置的含义是：
1. 如果是 main 分支：
   - `NODE_ENV` 被设置为 "production"
   - 使用 `.env.production` 文件
   - 执行 `npm run build --mode production`

2. 如果是其他分支：
   - `NODE_ENV` 被设置为 "staging"
   - 使用 `.env.staging` 文件
   - 执行 `npm run build --mode staging`

注意：具体的构建命令可能因你使用的前端框架而异：
- Vite: `vite build --mode production`
- Create React App: `react-scripts build`
- Vue CLI: `vue-cli-service build`

### 2.2 GitHub Actions 配置

创建部署工作流文件：

```yaml
# .github/workflows/deploy.yml
name: Deploy to Google Cloud
on:
  push:
    branches:
      - main  # 或其他你想触发部署的分支

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2

      - name: Setup Node.js
        uses: actions/setup-node@v2
        with:
          node-version: '16'

      - name: Install Dependencies
        run: npm install

      - name: Build
        env:
          NODE_ENV: ${{ github.ref == 'refs/heads/main' && 'production' || 'staging' }}
        run: npm run build --mode $NODE_ENV

      - name: Auth to Google Cloud
        uses: google-github-actions/auth@v0
        with:
          credentials_json: ${{ secrets.GCP_SA_KEY }}

      - name: Deploy to Cloud Run
        uses: google-github-actions/deploy-cloudrun@v0
        with:
          service: my-web-app
          region: us-central1
          source: ./dist  # 你的构建输出目录
```

### 2.2 应用配置文件

#### 文件位置说明

这些文件需要放在你的项目根目录下：

```plaintext
your-project/
├── src/                  # 你的源代码目录
├── package.json          # npm 配置文件
├── Dockerfile           # 【新建】容器构建配置
├── nginx.conf           # 【新建】nginx 配置
├── .github/
│   └── workflows/
│       └── deploy.yml    # GitHub Actions 配置
└── ... 其他文件和目录
```

#### 为什么需要这些配置文件？

Cloud Run 需要一个容器镜像才能运行你的应用。这些配置文件的作用是：

1. **Dockerfile**: 用于构建容器镜像
   - 第一阶段：构建前端应用
   - 第二阶段：使用 nginx 服务静态文件
```dockerfile
# 第一阶段：构建前端应用
FROM node:16 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# 第二阶段：设置 nginx 服务
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
```

2. **nginx.conf**: 配置 Web 服务器
   - 处理前端路由
   - 提供静态文件服务
```nginx
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # 处理前端路由（如 React Router）
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

#### 部署流程说明

1. GitHub Actions 会：
   - 构建你的前端代码
   - 使用 Dockerfile 创建容器镜像
   - 将镜像推送到 Google Container Registry
   - 在 Cloud Run 上部署这个镜像

2. Cloud Run 会：
   - 运行你的容器
   - 自动处理扩缩容
   - 提供 HTTPS 访问
   - 管理流量路由


## 3. 安全配置

### 3.1 身份认证方案对比

在将 GitHub Actions 与 Google Cloud 集成时，有两种主要的身份认证方案：

> **提示**
> 
> 获取项目 ID：
> ```bash
> gcloud config get-value project
> ```

#### 方案一：服务账号密钥（传统方案）

**优点：**
- 设置简单，容易理解
- 广泛支持，适用于所有 Google Cloud API
- 文档和示例丰富

**缺点：**
- 需要管理密钥文件
- 密钥可能泄露风险
- 需要定期轮换密钥
- 一些组织策略可能禁止创建服务账号密钥

**实现步骤：**
```bash
# 1. 创建服务账号
gcloud iam service-accounts create github-deploy-sa \
    --display-name="GitHub Deploy Service Account"

# 2. 赋予权限
gcloud projects add-iam-policy-binding YOUR_PROJECT_ID \
    --member="serviceAccount:github-deploy-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/run.admin"

# 3. 创建密钥
gcloud iam service-accounts keys create key.json \
    --iam-account=github-deploy-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com
```


#### 方案二：Workload Identity Federation（推荐方案）

**优点：**
- 无需管理服务账号密钥
- 更符合零信任安全模型
- 支持短期令牌，更安全
- 可以精细控制权限
- Google Cloud 官方推荐的最佳实践

**缺点：**
- 初始设置相对复杂
- 需要更多的配置步骤
- 可能需要组织管理员协助设置

**实现步骤：**

下面我们假设：
- Google Cloud 项目 ID 为 `YOUR_PROJECT_ID`
- 授权 `flex-protocol` 组织下的 GitHub 仓库通过 Workload Identity Federation 使用此服务账号访问 Google Cloud 资源

1. 创建 Workload Identity 池和提供者：
```bash
# 创建 Workload Identity 池
gcloud iam workload-identity-pools create "github-pool" \
    --project="YOUR_PROJECT_ID" \
    --location="global" \
    --display-name="GitHub Actions Pool"

# 创建 GitHub 提供者
gcloud iam workload-identity-pools providers create-oidc "github-provider" \
    --project="YOUR_PROJECT_ID" \
    --location="global" \
    --workload-identity-pool="github-pool" \
    --display-name="GitHub provider" \
    --attribute-mapping="google.subject=assertion.sub,attribute.repository=assertion.repository" \
    --issuer-uri="https://token.actions.githubusercontent.com" \
    --attribute-condition="assertion.repository.startsWith('flex-protocol/')"

# 注意替换 `flex-protocol` 为你的组织名


# 获取池名称（后续使用）
export WORKLOAD_IDENTITY_POOL_ID=$(gcloud iam workload-identity-pools describe "github-pool" \
    --project="YOUR_PROJECT_ID" \
    --location="global" \
    --format="value(name)")

# echo $WORKLOAD_IDENTITY_POOL_ID
```

2. 创建服务账号并配置权限：
```bash
# 创建服务账号
gcloud iam service-accounts create github-deploy-sa \
    --display-name="GitHub Deploy Service Account"

# 配置 Workload Identity Federation
gcloud iam service-accounts add-iam-policy-binding "github-deploy-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com" \
    --project="YOUR_PROJECT_ID" \
    --role="roles/iam.workloadIdentityUser" \
    --member="principalSet://iam.googleapis.com/${WORKLOAD_IDENTITY_POOL_ID}/attribute.repository/GITHUB_USERNAME/REPO_NAME"

# 注意替换上面命令中的 GITHUB_USERNAME 和 REPO_NAME！

# 替换 `GITHUB_USERNAME` 和 `REPO_NAME` 后，配置示例：
# gcloud iam service-accounts add-iam-policy-binding "github-deploy-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com" \
#     --project="YOUR_PROJECT_ID" \
#     --role="roles/iam.workloadIdentityUser" \
#     --member="principalSet://iam.googleapis.com/${WORKLOAD_IDENTITY_POOL_ID}/attribute.repository/flex-protocol/aptos-flex-app"

# 赋予部署权限
gcloud projects add-iam-policy-binding YOUR_PROJECT_ID \
    --member="serviceAccount:github-deploy-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/run.admin"
```


3. 配置 GitHub Actions 工作流：
```yaml
# .github/workflows/deploy.yml
jobs:
  deploy:
    permissions:
      contents: 'read'
      id-token: 'write'   # 这个权限是必需的

    steps:
      - uses: 'actions/checkout@v3'

      - id: 'auth'
        uses: 'google-github-actions/auth@v1'
        with:
          workload_identity_provider: 'projects/123456789/locations/global/workloadIdentityPools/github-pool/providers/github-provider'
          service_account: 'github-deploy-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com'

      - name: 'Deploy to Cloud Run'
        uses: 'google-github-actions/deploy-cloudrun@v1'
        with:
          service: 'my-service'
          region: 'us-central1'
          source: ./
```

### 3.2 方案选择建议

1. **对于新项目：**
   - 强烈推荐使用 Workload Identity Federation
   - 符合现代安全最佳实践
   - 无需担心密钥管理和轮换

2. **对于现有项目：**
   - 如果已经在使用服务账号密钥且运行良好，可以继续使用
   - 建议在下一次大更新时迁移到 Workload Identity Federation

3. **特殊情况：**
   - 如果组织策略禁止创建服务账号密钥，必须使用 Workload Identity Federation
   - 如果需要更严格的安全控制，推荐使用 Workload Identity Federation

4. **生产环境考虑：**
   - Workload Identity Federation 完全适合生产环境使用
   - 实际上，它比服务账号密钥更安全
   - 被 Google Cloud 官方推荐用于生产环境


### 3.3 GitHub 仓库安全设置

1. 添加 Secrets：
   - `GCP_SA_KEY`：服务账号的 JSON 密钥
   - `PROJECT_ID`：Google Cloud 项目 ID

2. 分支保护规则：

什么是分支保护？
- 分支保护是 GitHub 的一个安全功能
- 防止代码被意外或未经审查就直接推送到重要分支（如 main 分支）
- 特别适合团队协作，但个人项目也建议使用

如何设置分支保护：
1. 在 GitHub 仓库页面点击 "Settings"
2. 选择左侧的 "Branches"
3. 点击 "Add branch protection rule"
4. 在 "Branch name pattern" 输入 `main`
5. 选择以下保护选项：

```yaml
# GitHub 分支保护设置示例
branches:
  main:  # 主分支名称
    protection:
      # 要求代码审查
      required_pull_request_reviews:
        required_approving_review_count: 1  # 需要至少1个审查者批准
      
      # 要求状态检查通过
      required_status_checks:
        strict: true  # 确保代码是最新的

      # 可选：禁止强制推送
      enforce_admins: true  # 管理员也要遵守这些规则
```

这些设置的作用是：
1. **代码审查要求**
   - 不能直接推送到 main 分支
   - 必须创建 Pull Request
   - 需要至少一个人审查并批准

2. **状态检查**
   - 确保自动化测试通过
   - 确保代码质量检查通过
   - 确保构建成功

3. **保护效果**
   - 防止未经审查的代码上线
   - 防止意外删除或覆盖代码
   - 保持代码质量

即使是个人项目，启用分支保护也有好处：
- 养成良好的开发习惯
- 防止意外操作
- 通过 Pull Request 更好地记录变更

## 4. 最佳实践

### 4.1 环境隔离

1. 创建环境配置：
```yaml
# .github/workflows/deploy.yml
jobs:
  deploy:
    environment:
      name: ${{ github.ref == 'refs/heads/main' && 'production' || 'staging' }}
    # ...
```

2. 环境变量管理：
```yaml
# .env.production
API_URL=https://api.production.com

# .env.staging
API_URL=https://api.staging.com
```

### 4.2 安全检查

添加安全扫描：
```yaml
# .github/workflows/security.yml
name: Security Scan
on: [push, pull_request]

jobs:
  security:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Security Scan
        uses: snyk/actions/node@master
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
```

## 5. 监控与维护

### 5.1 性能监控

1. 设置 Cloud Monitoring：
```yaml
# app.yaml
runtime: nodejs16
env: standard

automatic_scaling:
  target_cpu_utilization: 0.65
  min_instances: 1
  max_instances: 10

vpc_access_connector:
  name: projects/YOUR_PROJECT_ID/locations/us-central1/connectors/my-connector
```

2. 告警配置：
```bash
# 设置 CPU 使用率告警
gcloud alpha monitoring policies create \
    --display-name="High CPU Usage" \
    --conditions="metric.type=\"compute.googleapis.com/instance/cpu/utilization\" \
                 comparison.gt.threshold=0.8 \
                 duration=\"300s\""
```

### 5.2 成本控制

1. 预算告警：
```bash
# 设置每月预算
gcloud billing budgets create \
    --billing-account=YOUR_BILLING_ACCOUNT \
    --display-name="Monthly Budget" \
    --budget-amount=50USD \
    --threshold-rules=percent=0.5,percent=0.9
```

2. 资源限制：
```yaml
# 在 Cloud Run 服务配置中
resources:
  limits:
    cpu: "1"
    memory: "256Mi"
```

## 6. 故障排除

### 6.1 常见问题

1. 部署失败：
   - 检查 GitHub Actions 日志
   - 验证服务账号权限
   - 确认构建配置正确

2. 性能问题：
   - 使用 Lighthouse 分析
   - 检查资源配置
   - 优化构建输出

### 6.2 日志查看

```bash
# 查看 Cloud Run 日志
gcloud logging read "resource.type=cloud_run_revision AND \
    resource.labels.service_name=my-web-app" \
    --limit=10
```

## 7. 安全最佳实践总结

1. 访问控制
   - 使用服务账号
   - 实施最小权限
   - 定期轮换密钥

2. 代码安全
   - 启用依赖扫描
   - 代码审查
   - 自动化安全测试

3. 运行时安全
   - 使用容器安全扫描
   - 配置网络策略
   - 启用审计日志

## 8. 维护清单

- [ ] 每周检查依赖更新
- [ ] 每月审查访问权限
- [ ] 每季度更新密钥
- [ ] 定期备份配置
- [ ] 检查成本报告
- [ ] 审查性能指标
- [ ] 更新文档

## 参考资料

1. [Google Cloud 文档](https://cloud.google.com/docs)
2. [GitHub Actions 文档](https://docs.github.com/en/actions)
