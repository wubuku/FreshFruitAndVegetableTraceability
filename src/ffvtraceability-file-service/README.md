# File Service

## GCS 存储后端配置指南

本文档介绍如何使用 Google Cloud Storage (GCS) 作为文件服务的存储后端。

### 1. 前置要求

成功配置 GCS 存储后端需要：
1. Google Cloud 项目访问权限
2. 适当的 GCS 访问权限
3. 相关命令行工具（gcloud）

### 2. 存储配置

#### 2.1 创建存储 Bucket

本服务需要两个 bucket：一个用于私有文件，另一个用于公开访问的文件。以下示例使用：
- Project ID: `woven-justice-441107-h8`（请替换为你的项目 ID）
- Location: `asia-east1`（选择适合你的区域）
- Bucket 名称: 
  - 私有：`flex-api-private`
  - 公开：`flex-api-public`

> 注意：Bucket 名称在全局范围内必须唯一，建议使用项目名或组织名作为前缀。

1. 创建私有 bucket（默认就是私有的）：
```shell
gcloud storage buckets create gs://flex-api-private \
    --project=woven-justice-441107-h8 \
    --location=asia-east1 \
    --uniform-bucket-level-access
```

2. 创建公开访问的 bucket：
```shell
gcloud storage buckets create gs://flex-api-public \
    --project=woven-justice-441107-h8 \
    --location=asia-east1 \
    --uniform-bucket-level-access

# 设置 bucket 为公开访问
gcloud storage buckets add-iam-policy-binding gs://flex-api-public \
    --member=allUsers \
    --role=roles/storage.objectViewer
```

#### 2.2 应用配置

配置 application.yml 文件：
```yaml
storage:
  type: gcs  # 指定使用 GCS 存储
  gcs:
    project-id: your-project-id
    private-bucket: your-private-bucket
    public-bucket: your-public-bucket
```

### 3. 访问权限配置

在 Google Cloud 中，访问权限的控制分为两个层面：
1. VM 实例级别的访问作用域（Access Scopes）
2. 服务账号级别的 IAM 权限

#### 3.1 本地开发环境

本地开发环境有两种方式配置 GCS 认证：

##### A. 使用 gcloud CLI（推荐）

应用会按照以下顺序自动查找凭证（Application Default Credentials）：
1. `GOOGLE_APPLICATION_CREDENTIALS` 环境变量指定的 JSON 文件
2. gcloud 默认用户凭证（`~/.config/gcloud/application_default_credentials.json`）
3. Google Cloud 环境的元数据服务器

如果你已经安装了 gcloud CLI，只需执行：
```bash
# 登录并创建默认凭证
gcloud auth application-default login
```

然后在 application.yml 中配置：
```yaml
storage:
  type: gcs
  gcs:
    project-id: your-project-id
    private-bucket: your-private-bucket
    public-bucket: your-public-bucket
```

这种方式的优点：
- 不需要手动管理服务账号 JSON 文件
- 使用你的 Google 账号权限
- 与其他 gcloud 命令使用相同的认证
- 凭证会自动保存在 `~/.config/gcloud/application_default_credentials.json`

##### B. 使用服务账号 JSON 文件

如果需要使用特定的服务账号，可以下载 JSON 凭证文件：

```bash
docker run -d \
  --name file-service \
  -p 8080:8080 \
  -v /path/to/gcp-credentials.json:/app/credentials.json \
  -e GOOGLE_APPLICATION_CREDENTIALS=/app/credentials.json \
  -e STORAGE_TYPE=gcs \
  -e STORAGE_GCS_PROJECT_ID="your-project-id" \
  -e STORAGE_GCS_PRIVATE_BUCKET="your-private-bucket" \
  -e STORAGE_GCS_PUBLIC_BUCKET="your-public-bucket" \
  -e SPRING_DATASOURCE_USERNAME="your-db-username" \
  -e SPRING_DATASOURCE_PASSWORD="your-db-password" \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI="https://your-auth-server/realms/your-realm" \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI="https://your-auth-server/realms/your-realm/.well-known/jwks.json" \
  [其他配置...] \
  your-docker-image:tag
```

#### 3.2 Google Cloud VM 环境（推荐）

在 VM 环境中，需要同时配置：
1. VM 的访问作用域
2. 服务账号的 IAM 权限

有两种配置方案：

##### 方案一：使用特定的访问作用域

这种方式直接在 VM 级别控制对 GCS 的访问权限：

1. 检查当前 VM 的访问作用域：
```bash
gcloud compute instances describe INSTANCE_NAME \
    --zone=ZONE \
    --format='get(serviceAccounts[].scopes)'
```

2. 如果作用域不足（例如只有 `devstorage.read_only`），需要更新为 `devstorage.full_control`：
```bash
# 1. 停止 VM
gcloud compute instances stop instance-20250124-134353 --zone=us-central1-c

# 2. 更新访问作用域（保留其他必要的作用域）
gcloud compute instances set-service-account instance-20250124-134353 \
    --zone=us-central1-c \
    --scopes=https://www.googleapis.com/auth/devstorage.full_control,\
https://www.googleapis.com/auth/logging.write,\
https://www.googleapis.com/auth/monitoring.write,\
https://www.googleapis.com/auth/service.management.readonly,\
https://www.googleapis.com/auth/servicecontrol,\
https://www.googleapis.com/auth/trace.append

# 3. 重启 VM
gcloud compute instances start instance-20250124-134353 --zone=us-central1-c
```

##### 方案二：使用 cloud-platform 作用域 + IAM（推荐）

这是 Google Cloud 推荐的现代方式：
1. 使用 `cloud-platform` 作用域启用 IAM 权限控制
2. 然后通过 IAM 精确控制具体权限

首先配置 VM 使用 `cloud-platform` 作用域：
```bash
# 检查作用域
gcloud compute instances describe INSTANCE_NAME \
    --zone=ZONE \
    --format='get(serviceAccounts[].scopes)'

# 如果需要，更新作用域（需要重启 VM）
gcloud compute instances set-service-account INSTANCE_NAME \
    --zone=ZONE \
    --scopes=cloud-platform
```

> 说明：虽然 `cloud-platform` 作用域理论上允许访问所有 API，但实际的访问权限是由 IAM 控制的。这种方式的优点是：
> 1. 可以通过 IAM 随时调整具体权限，无需重启 VM
> 2. 可以实现更精细的权限控制
> 3. 符合 Google Cloud 的最佳实践

2. 然后配置服务账号的 IAM 权限：
```bash
# 1. 获取服务账号邮箱
curl -H "Metadata-Flavor: Google" \
  http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/email

# 2. 配置必要的权限
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.objectAdmin"
```

> 重要说明：
> 1. 如果没有正确的访问作用域，即使配置了 IAM 权限也不会生效
> 2. 更新访问作用域需要重启 VM
> 3. 配置 IAM 权限不需要重启 VM

### 4. 验证配置

完成配置后，可以使用以下命令验证：

1. 验证服务账号身份：
```bash
curl -H "Metadata-Flavor: Google" \
  http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/email
```

2. 验证 GCS 访问：
```bash
# 获取访问令牌
TOKEN=$(curl -s -H "Metadata-Flavor: Google" \
  http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/token \
  | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)

# 测试访问 bucket
curl -H "Authorization: Bearer $TOKEN" \
  "https://storage.googleapis.com/storage/v1/b/YOUR_BUCKET/o"
```

> 提示：如果验证失败，请按以下顺序检查：
> 1. VM 的访问作用域是否正确（使用第一个验证命令）
> 2. 服务账号是否有正确的 IAM 权限
> 3. 确保 bucket 存在且配置正确

### 5. 部署和测试

#### 5.1 部署服务

以下是一个完整的部署示例，使用 MySQL 作为数据库：

```bash
docker run -d \
  --name gmeme-file-service \
  -p 8080:8080 \
  -e STORAGE_TYPE=gcs \
  -e STORAGE_GCS_PROJECT_ID="woven-justice-441107-h8" \
  -e STORAGE_GCS_PRIVATE_BUCKET="gmeme-private" \
  -e STORAGE_GCS_PUBLIC_BUCKET="gmeme-public" \
  -e SPRING_DATASOURCE_URL='jdbc:mysql://10.95.80.3:3306/gmeme_files?characterEncoding=utf8&serverTimezone=GMT%2b0&useLegacyDatetimeCode=false' \
  -e SPRING_DATASOURCE_USERNAME=flex-api-test \
  -e SPRING_DATASOURCE_PASSWORD='YOUR_PASSWORD' \
  -e SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver \
  -e SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect \
  ghcr.io/wubuku/ffvtraceability-file-service:main
```

配置说明：
1. GCS 配置：设置存储类型为 GCS，并配置项目 ID 和 bucket 名称
2. MySQL 配置：
   - 数据库地址：`10.95.80.3:3306`
   - 数据库名：`gmeme_files`
   - 字符集：`utf8`
   - 时区：`GMT+0`
3. 其他配置根据实际需求调整

#### 5.2 功能测试

部署完成后，可以使用以下命令测试文件上传功能：

```shell
curl -X POST \
  -F "file=@/PATH/TO/YOUR/FILE" \
  -F "isPublic=true" \
  https://files.gmeme.xyz/api/files/upload -v
```

返回的结果类似（注意，在下面的例子中，`{FILE_ID}` 占位符表示的是文件 Id，`ORIGINAL_FILE_NAME` 是文件的原始名称，`GCS_FILE_NAME` 是文件在 GCS 中的文件名称，我们隐去了它们的实际的值）：

```json
{"id":"{FILE_ID}","originalFilename":"ORIGINAL_FILE_NAME","storageFilename":"public/FILE_ID.jpg","contentType":"image/jpeg","size":1156066,"userId":"anonymous","uploadTime":"2025-02-23T12:50:55.037351296Z","url":"https://storage.googleapis.com/gmeme-public/GCS_FILE_NAME","urlExpireTime":null,"public":true}
```

文件访问方式：
1. 直接使用返回的 URL
2. 通过 API 接口访问：

```
https://files.gmeme.xyz/api/files/{FILE_ID}/media
```

### 6. 本地开发

如果需要在本地开发环境构建和运行项目：

#### Maven Package

```shell
# in `src` directory
mvn package -pl ffvtraceability-file-service
```

#### Maven Run

```shell
java -jar ./ffvtraceability-file-service/target/ffvtraceability-file-service-0.0.1-SNAPSHOT.jar
```




