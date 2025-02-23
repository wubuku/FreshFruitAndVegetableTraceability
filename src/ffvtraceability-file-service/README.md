# File Service

## 关于使用 GCS 作为后端存储的说明

### 1. 配置概述

要使用 Google Cloud Storage (GCS) 作为文件服务的后端存储，需要：
1. 正确配置 GCS 相关环境
2. 确保运行环境具有适当的 GCS 访问权限

### 2. 配置步骤

#### 2.1 创建和配置 Bucket

以下命令示例中使用的配置：
- Project ID: `woven-justice-441107-h8`（替换为你的项目 ID）
- Location: `asia-east1`（可选择离你最近的区域）
- Bucket 名称: 
  - 私有：`flex-api-private`
  - 公开：`flex-api-public`

> 注意：Bucket 名称必须是全局唯一的，建议使用有意义的前缀，如项目名或组织名。

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

在 `application.yml` 中配置：
```yaml
storage:
  type: gcs  # 指定使用 GCS 存储
  gcs:
    project-id: your-project-id
    private-bucket: your-private-bucket
    public-bucket: your-public-bucket
```

### 3. 部署方式

#### 3.1 本地开发环境

使用服务账号 JSON 文件：
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

在 Google Cloud VM 上运行时，需要确保 VM 有正确的访问权限。有两种方式：

##### A. 使用访问作用域（Access Scopes）

```bash
# 检查当前 VM 的作用域
gcloud compute instances describe INSTANCE_NAME \
    --zone=ZONE \
    --format='get(serviceAccounts[].scopes)'
```

实际案例：当发现 VM 只有 `devstorage.read_only` 作用域时，需要更新为 `devstorage.full_control`：

```bash
# 1. 停止 VM
gcloud compute instances stop instance-20250124-134353 --zone=us-central1-c

# 2. 更新 VM 的访问作用域（保留其他必要的作用域）
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

> 注意：更新 VM 的访问作用域需要重启 VM。如果想避免重启，建议使用方式 B（cloud-platform 作用域和 IAM 权限）。

##### B. 使用 cloud-platform 作用域和 IAM 权限（推荐）

这种方式更灵活，因为：
- 不需要管理具体的 API 作用域
- 可以通过 IAM 精细控制权限
- 不需要重启 VM 就能更新权限

```bash
# 创建 VM 时设置
gcloud compute instances create INSTANCE_NAME \
    --zone=ZONE \
    --scopes=cloud-platform

# 确保服务账号有正确的 IAM 权限
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.admin"
```

### 4. 权限配置和验证

#### 4.1 查看和配置权限

1. 查看当前 VM 服务账号：
```bash
curl -H "Metadata-Flavor: Google" \
  http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/email
```

2. 检查服务账号权限：
```bash
gcloud projects get-iam-policy PROJECT_ID \
  --flatten="bindings[].members" \
  --format='table(bindings.role)' \
  --filter="bindings.members:SERVICE_ACCOUNT_EMAIL"
```

3. 配置必要的权限：
```bash
# 添加读取权限
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.objectViewer"

# 添加创建权限
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.objectCreator"

# 添加完整管理权限（如需删除文件）
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.objectAdmin"
```

### 5. 部署示例

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


部署后可以使用 curl 命令测试，示例：

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

可以使用返回的 url 直接访问文件。也可以像下面这样通过拼接 url 来访问文件（注意文件 Id `{FILE_ID}` 需要替换为上传文件的返回结果中的 `id` 字段的值）：

```
https://files.gmeme.xyz/api/files/{FILE_ID}/media
```


## Maven 

### Package

```shell
# in `src` directory
mvn package -pl ffvtraceability-file-service
```

### Run

```shell
java -jar ./ffvtraceability-file-service/target/ffvtraceability-file-service-0.0.1-SNAPSHOT.jar
```



