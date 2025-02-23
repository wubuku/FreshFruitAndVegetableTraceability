# File Service

## 关于使用 GCS 作为后端存储的说明

### 1. 配置概述

要使用 Google Cloud Storage (GCS) 作为文件服务的后端存储，需要：
1. 正确配置 GCS 相关环境
2. 确保运行环境具有适当的 GCS 访问权限

### 2. 配置方式

#### 2.1 应用配置

在 `application.yml` 中配置：

```yaml
storage:
  type: gcs  # 指定使用 GCS 存储
  gcs:
    project-id: your-project-id
    private-bucket: your-private-bucket
    public-bucket: your-public-bucket
```

关于 bucket 的创建，可以参考下面的命令：

```shell
# 创建私有 bucket，默认就是私有的
gcloud storage buckets create gs://flex-api-private \
    --project=woven-justice-441107-h8 \
    --location=asia-east1 \
    --uniform-bucket-level-access
```

```shell
# 创建 bucket 并设置为公开访问
gcloud storage buckets create gs://flex-api-public \
    --project=woven-justice-441107-h8 \
    --location=asia-east1 \
    --uniform-bucket-level-access

# 设置 bucket 为公开访问
gcloud storage buckets add-iam-policy-binding gs://flex-api-public \
    --member=allUsers \
    --role=roles/storage.objectViewer
```


#### 2.2 运行环境配置

有两种部署方式：

##### A. 使用服务账号 JSON 文件（本地开发环境推荐）

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

##### B. 使用 Google Cloud VM（生产环境推荐）

在 Google Cloud VM 上运行时，可以直接使用 VM 的服务账号，无需配置凭证文件：

```bash
docker run -d \
  --name file-service \
  -p 8080:8080 \
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

示例：

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
  ghcr.io/wubuku/ffvtraceability-file-service:main
```

### 3. 权限配置

#### 3.1 查看当前 VM 服务账号

```bash
# 方法1：使用 gcloud 命令
gcloud compute instances describe INSTANCE_NAME --zone=ZONE \
    --format='get(serviceAccounts[0].email)'

# 方法2：在 VM 内部查询
curl -H "Metadata-Flavor: Google" \
  http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/email
```

#### 3.2 检查服务账号权限

```bash
gcloud projects get-iam-policy PROJECT_ID \
  --flatten="bindings[].members" \
  --format='table(bindings.role)' \
  --filter="bindings.members:SERVICE_ACCOUNT_EMAIL"
```

#### 3.3 配置必要的权限

服务账号需要以下权限：
- `roles/storage.objectViewer` - 读取文件权限
- `roles/storage.objectCreator` - 创建文件权限
- `roles/storage.objectAdmin` - 完整的文件管理权限（如需删除文件）

添加权限命令：
```bash
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.objectViewer"

gcloud projects add-iam-policy-binding PROJECT_ID \
    --member="serviceAccount:SERVICE_ACCOUNT_EMAIL" \
    --role="roles/storage.objectCreator"
```

### 4. 权限验证脚本

可以使用以下脚本验证 GCS 权限配置是否正确：

```bash
#!/bin/bash

# 获取当前服务账号
SERVICE_ACCOUNT=$(curl -s -H "Metadata-Flavor: Google" \
  http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/email)
echo "Current Service Account: $SERVICE_ACCOUNT"

# 检查 IAM 权限
echo "Checking IAM roles..."
gcloud projects get-iam-policy YOUR_PROJECT_ID \
  --flatten="bindings[].members" \
  --format='table(bindings.role)' \
  --filter="bindings.members:$SERVICE_ACCOUNT"

# 测试 GCS 访问
echo "Testing GCS access..."
if gsutil ls gs://your-private-bucket &>/dev/null; then
    echo "✅ Can access private bucket"
else
    echo "❌ Cannot access private bucket"
fi

if gsutil ls gs://your-public-bucket &>/dev/null; then
    echo "✅ Can access public bucket"
else
    echo "❌ Cannot access public bucket"
fi

# 测试写入权限
echo "Testing write permission..."
echo "test" > test.txt
if gsutil cp test.txt gs://your-private-bucket/test.txt &>/dev/null; then
    echo "✅ Can write to private bucket"
    gsutil rm gs://your-private-bucket/test.txt &>/dev/null
else
    echo "❌ Cannot write to private bucket"
fi
rm test.txt

echo "Done checking permissions"
```

将此脚本保存为 `check_gcs_permissions.sh` 并运行：
```bash
chmod +x check_gcs_permissions.sh
./check_gcs_permissions.sh
```

### 5. 最佳实践

1. 在生产环境中，推荐使用 Google Cloud VM 的服务账号，而不是 JSON 凭证文件
2. 遵循最小权限原则，只分配必要的权限
3. 定期审查服务账号权限
4. 在本地开发环境可以使用 JSON 凭证文件
5. 确保 bucket 名称在全局唯一
6. 使用不同的 bucket 区分公开和私有文件


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



