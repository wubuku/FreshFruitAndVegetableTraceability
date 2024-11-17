# Authorization Server

## 1. 启动服务器

```bash
cd ffvtraceability-auth-server
mvn clean spring-boot:run
```

服务器将在 9000 端口启动。

## 2. OAuth 2.0 授权码流程测试

### 2.1 生成 PKCE 参数
```bash
# 生成 code verifier (43-128位随机字符)
code_verifier=$(openssl rand -base64 32 | tr -d /=+ | cut -c -43)
echo "Code Verifier: $code_verifier"

# 生成 code challenge
code_challenge=$(echo -n $code_verifier | openssl sha256 -binary | base64 | tr -d /=+ )
echo "Code Challenge: $code_challenge"
```

### 2.2 登录认证
```bash
# 清理旧的 cookies
rm -f cookies.txt

# 获取登录页面和 CSRF token
csrf_token=$(curl -c cookies.txt -b cookies.txt -s http://localhost:9000/login | sed -n 's/.*name="_csrf" type="hidden" value="\([^"]*\).*/\1/p')
echo "CSRF Token: $csrf_token"

encoded_csrf_token=$(echo -n "$csrf_token" | perl -pe 's/([^A-Za-z0-9])/sprintf("%%%02X", ord($1))/seg')
echo "Encoded CSRF Token: $encoded_csrf_token"

# 执行登录
login_response=$(curl -X POST http://localhost:9000/login \
    -c cookies.txt -b cookies.txt \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=admin" \
    -d "password=admin" \
    -d "_csrf=$encoded_csrf_token" \
    -v 2>&1)

# 提取 Location 头和会话 ID
location=$(echo "$login_response" | grep -i "location:" | sed 's/.*Location: //' | tr -d '\r\n')
session_id=$(echo "$login_response" | grep -i "set-cookie:" | grep -o "JSESSIONID=[^;]*" | cut -d= -f2)

echo "Redirect Location: $location"
echo "Session ID: $session_id"


# # 验证登录是否成功
# if [ "$location" = "http://localhost:9000/" ]; then
#     echo "Login successful!"
# elif [ -z "$location" ]; then
#     echo "Error: No redirect location found"
#     echo "Response headers:"
#     echo "$login_response"
#     exit 1
# else
#     echo "Login failed! Redirected to: $location"
#     exit 1
# fi

## 保存会话 ID 供后续使用（可选）
#echo "export SESSION_ID=$session_id" > session.env

```


### 2.3 获取授权码
```bash
# 设置重定向 URI
redirect_uri="http://127.0.0.1:3000/callback"
echo "🌐 Redirect URI: $redirect_uri"

# 获取授权页面（禁用自动重定向）
auth_page=$(curl -s \
    -c cookies.txt -b cookies.txt \
    --max-redirs 0 \
    --no-location \
    "http://localhost:9000/oauth2/authorize?\
client_id=ffv-client&\
response_type=code&\
scope=openid%20read%20write&\
redirect_uri=${redirect_uri}&\
code_challenge=${code_challenge}&\
code_challenge_method=S256" \
    -D - 2>/dev/null)

# 检查是否需要用户同意
if echo "$auth_page" | grep -q "Consent required"; then
    # 从授权页面提取 state 值
    state=$(echo "$auth_page" | sed -n 's/.*name="state" value="\([^"]*\).*/\1/p')
    
    if [ -z "$state" ]; then
        echo "❌ Error: Could not extract state from auth page"
        echo "$auth_page"
        exit 1
    fi
    
    echo "🔐 State: $state"
    
    # 提交授权确认
    auth_response=$(curl -s \
        -c cookies.txt -b cookies.txt \
        "http://localhost:9000/oauth2/authorize" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "client_id=ffv-client" \
        -d "state=$state" \
        -d "scope=read" \
        -d "scope=write" \
        -d "scope=openid" \
        -d "submit=Submit+Consent" \
        -D - 2>/dev/null)
else
    # 直接使用重定向响应
    auth_response="$auth_page"
fi

# 从重定向 URL 中提取授权码
location=$(echo "$auth_response" | grep -i "^location:" | sed 's/.*Location: //' | tr -d '\r\n')
auth_code=$(echo "$location" | grep -o 'code=[^&]*' | cut -d= -f2)

if [ -z "$auth_code" ]; then
    echo "❌ Authorization failed!"
    echo "Response headers:"
    echo "$auth_response"
    exit 1
fi

echo "✅ Authorization successful!"
echo "🎫 Authorization Code: $auth_code"

# 保存授权码供后续使用（可选）
echo "export AUTH_CODE=$auth_code" > auth.env
```

### 2.4 获取访问令牌
```bash
# 检查必需的变量
if [ -z "$auth_code" ] || [ -z "$redirect_uri" ] || [ -z "$code_verifier" ]; then
    echo "❌ Error: Missing required parameters"
    echo "Authorization Code: $auth_code"
    echo "Redirect URI: $redirect_uri"
    echo "Code Verifier: $code_verifier"
    exit 1
fi

# 获取访问令牌
echo "🔄 Requesting access token..."
token_response=$(curl -s -X POST http://localhost:9000/oauth2/token \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -H "Authorization: Basic $(echo -n 'ffv-client:secret' | base64)" \
    -d "grant_type=authorization_code" \
    -d "code=$auth_code" \
    -d "redirect_uri=$redirect_uri" \
    -d "code_verifier=$code_verifier")

# 检查响应是否包含错误
if echo "$token_response" | jq -e 'has("error")' > /dev/null; then
    echo "❌ Token request failed!"
    echo "Error: $(echo "$token_response" | jq -r '.error')"
    echo "Error description: $(echo "$token_response" | jq -r '.error_description')"
    exit 1
fi

# 提取令牌
access_token=$(echo "$token_response" | jq -r '.access_token')
refresh_token=$(echo "$token_response" | jq -r '.refresh_token')
id_token=$(echo "$token_response" | jq -r '.id_token')

# 验证是否成功获取到令牌
if [ "$access_token" = "null" ] || [ -z "$access_token" ]; then
    echo "❌ Failed to extract access token!"
    echo "Response:"
    echo "$token_response" | jq '.'
    exit 1
fi

echo "✅ Token request successful!"
echo "🔑 Access Token: ${access_token:0:50}..."
echo "🔄 Refresh Token: ${refresh_token:0:50}..."
echo "🎫 ID Token: ${id_token:0:50}..."

# 保存令牌供后续使用（可选）
echo "export ACCESS_TOKEN=$access_token" > tokens.env
echo "export REFRESH_TOKEN=$refresh_token" >> tokens.env
echo "export ID_TOKEN=$id_token" >> tokens.env

# 显示令牌信息（解码 JWT）
echo -e "\n📝 Access Token Claims:"
echo "$access_token" | cut -d"." -f2 | base64 -d 2>/dev/null | jq '.'

echo -e "\n📝 ID Token Claims:"
echo "$id_token" | cut -d"." -f2 | base64 -d 2>/dev/null | jq '.'
```


## 3. 使用令牌

### 3.1 检查令牌信息
```bash
echo $access_token | cut -d"." -f2 | base64 -d | jq '.'
```

### 3.2 访问用户信息
```bash
curl -s -H "Authorization: Bearer $access_token" \
    http://localhost:9000/userinfo | jq '.'
```

### 3.3 刷新令牌
```bash
refresh_response=$(curl -s -X POST http://localhost:9000/oauth2/token \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -H "Authorization: Basic $(echo -n 'ffv-client:secret' | base64)" \
    -d "grant_type=refresh_token" \
    -d "refresh_token=$refresh_token")

new_access_token=$(echo $refresh_response | jq -r '.access_token')
new_refresh_token=$(echo $refresh_response | jq -r '.refresh_token')

echo "New Access Token: $new_access_token"
echo "New Refresh Token: $new_refresh_token"
```

## 4. 清理
```bash
rm -f cookies.txt login.html
```

## 5. 故障排除

### 5.1 检查服务状态
```bash
curl -s http://localhost:9000/.well-known/openid-configuration | jq '.'
```

### 5.2 检查数据库连接
```bash
psql -h localhost -U postgres -d ffvtraceability -c "SELECT COUNT(*) FROM users;"
```

## 6. 安全注意事项

1. 所有密码都使用 BCrypt 加密存储
2. PKCE 是必需的，可防止授权码拦截攻击
3. 生产环境中应使用 HTTPS
4. 定期轮换客户端密钥
5. 命令行中的敏感信息应该使用环境变量处理