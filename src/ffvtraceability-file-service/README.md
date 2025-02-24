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

### 7. CORS 配置

本服务支持跨域资源共享（CORS）配置，可以通过 application.yml 进行设置。

#### 7.1 基础配置

在 application.yml 中添加 CORS 配置：
```yaml
spring:
  mvc:
    cors:
      # 允许的源，使用 * 表示允许所有源
      allowed-origins: "*"
      # 允许的 HTTP 方法
      allowed-methods: "*"
      # 允许的请求头
      allowed-headers: "*"
      # 暴露的响应头
      exposed-headers: "*"
      # 是否允许发送认证信息（cookies, auth headers）
      allow-credentials: false
      # 预检请求的有效期（秒）
      max-age: 3600
```

> 重要说明：
> 1. 如果设置 `allowed-origins: "*"`，则 `allow-credentials` 必须为 false
> 2. 如果需要发送认证信息，必须指定具体的域名，例如：
>    ```yaml
>    allowed-origins: http://your-domain.com,https://your-domain.com
>    allow-credentials: true
>    ```

#### 7.2 安全配置

本服务使用 Spring Security 进行安全控制，需要确保 OPTIONS 请求（预检请求）能够通过。有两种配置方式：

##### A. 使用 SecurityConfig（当前方式）
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())  // 启用 CORS
            .authorizeHttpRequests(auth -> {
                // 首先允许 OPTIONS 请求
                auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/**")).permitAll();
                // ... 其他安全配置 ...
            });
        return http.build();
    }
}
```

##### B. 使用注解方式
```java
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)  // 在控制器级别配置
public class FileController {
    
    @PostMapping("/upload")
    @CrossOrigin(origins = "http://specific-domain.com")  // 在方法级别配置
    public ResponseEntity<?> upload() {
        // ...
    }
}
```

> 注意：推荐使用 SecurityConfig 方式，因为：
> 1. 集中管理 CORS 配置，易于维护
> 2. 与安全配置结合更紧密
> 3. 可以通过配置文件动态调整

#### 7.3 常见问题

1. 403 错误：
   - 检查 SecurityConfig 中是否正确配置了 OPTIONS 请求
   - 确保 OPTIONS 请求的配置在其他安全规则之前

2. CORS 预检失败：
   - 检查请求头是否在 allowed-headers 列表中
   - 检查请求方法是否在 allowed-methods 列表中
   - 检查源域名是否在 allowed-origins 列表中

3. 认证信息无法发送：
   - 不能同时使用 `allowed-origins: "*"` 和 `allow-credentials: true`
   - 必须明确指定允许的域名

4. 500 错误：
   - 检查 Spring Security 配置是否正确
   - 检查是否有其他过滤器干扰
   - 查看服务器日志获取具体错误信息

#### 7.4 测试 CORS 配置

使用以下命令测试 CORS 配置：

```bash
# 测试预检请求（OPTIONS）
curl -X OPTIONS -H "Origin: http://example.com" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: content-type" \
  -v https://your-api-domain/api/files/upload

# 测试实际请求
curl -X POST -H "Origin: http://example.com" \
  -F "file=@/path/to/file.jpg" \
  -F "isPublic=true" \
  -v https://your-api-domain/api/files/upload
```

预检请求成功时，应该看到以下响应头：
```
access-control-allow-origin: *
access-control-allow-methods: POST
access-control-allow-headers: content-type
access-control-expose-headers: *
access-control-max-age: 3600
```

> 生产环境安全建议：
> 1. 限制允许的源域名：
>    ```yaml
>    allowed-origins: https://your-frontend.com,https://admin.your-frontend.com
>    ```
> 2. 限制允许的 HTTP 方法：
>    ```yaml
>    allowed-methods: GET,POST,PUT,DELETE,OPTIONS
>    ```
> 3. 明确指定允许的请求头：
>    ```yaml
>    allowed-headers: Origin,Content-Type,Accept,Authorization
>    ```
> 4. 定期审查 CORS 配置，确保安全性
> 5. 考虑使用 Spring Security 的 CSRF 保护（对于非 GET 请求）

#### 7.5 CORS vs CSRF

##### CORS（跨域资源共享）
- 用于控制不同域之间的资源访问
- 是浏览器的安全机制
- 通过 HTTP 头部控制是否允许跨域请求
- 主要解决：来自不同域的前端应用能否调用 API

例如：
- 前端域名：`https://app.example.com`
- API 域名：`https://api.example.com`
- 需要配置 CORS 才能让前端调用 API

##### CSRF（跨站请求伪造）
- 用于防止恶意网站冒充用户发送请求
- 是应用程序的安全机制
- 通过令牌验证请求是否来自合法的前端应用
- 主要解决：确保请求来自你的合法前端，而不是恶意网站

CSRF 攻击示例：
1. HTML 表单自动提交：
```html
<!-- evil.com 的页面 -->
<form action="https://bank.com/transfer" method="POST" id="hack-form">
    <input type="hidden" name="to" value="hacker-account" />
    <input type="hidden" name="amount" value="1000000" />
</form>
<script>
    document.getElementById('hack-form').submit(); // 自动提交
</script>
```

2. AJAX 请求：
```javascript
// evil.com 的脚本
fetch('https://bank.com/transfer', {
    method: 'POST',
    credentials: 'include', // 会带上 cookie
    body: JSON.stringify({
        to: 'hacker-account',
        amount: 1000000
    })
});
```

为什么会成功：
1. 浏览器会自动带上目标网站的 cookie
2. 传统的 session-cookie 认证无法分辨请求来源
3. 用户在目标网站是已登录状态

防护方式：
1. CSRF Token：
```java
// 后端生成 token
String csrfToken = generateToken();
response.setCookie("CSRF-TOKEN", csrfToken);

// 前端发请求时带上 token
fetch('/api/transfer', {
    method: 'POST',
    headers: {
        'X-CSRF-TOKEN': document.cookie.match('CSRF-TOKEN=([^;]+)')[1]
    }
});
```

2. SameSite Cookie：
```java
// 设置 cookie 的 SameSite 属性
response.setHeader("Set-Cookie", "session=123; SameSite=Strict");
```

3. 使用 JWT 等自定义头：
```javascript
// 前端请求时手动添加 Authorization 头
fetch('/api/transfer', {
    headers: {
        'Authorization': 'Bearer ' + jwt
    }
});
```

> 关于 CSRF 保护：
> 1. GET 请求通常不需要 CSRF 保护
> 2. 文件上传等 API 如果只用于前端上传，可以禁用 CSRF
> 3. 涉及敏感操作的 API（如支付）应该启用 CSRF 保护
> 4. 使用 JWT 等 token 认证时，通常不需要 CSRF 保护

> 注意：我们的文件服务使用 JWT 认证，所以默认是安全的，因为：
> 1. JWT token 存在 Authorization 头中，而不是 cookie
> 2. 恶意网站无法获取或设置其他域的请求头
> 3. 这就是为什么我们可以安全地禁用 CSRF 保护

