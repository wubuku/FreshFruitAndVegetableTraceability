#!/bin/bash

# 用户凭证配置
USERNAME="admin" # admin / user
PASSWORD="admin"
NEW_PASSWORD="newPassword123!"  # 当需要修改密码时使用的新密码

# Base64URL encode function (no padding)
base64url_encode() {
    # Read binary input and encode to base64, then transform to base64url
    base64 | tr '/+' '_-' | tr -d '='
}

# URL encode function
urlencode() {
    python3 -c "import sys, urllib.parse; print(urllib.parse.quote(sys.argv[1], safe=''))" "$1"
}

# Generate code_verifier (random string)
code_verifier=$(openssl rand -base64 32 | tr -d /=+ | cut -c -43)
echo "🔑 Code Verifier: $code_verifier"

# Generate code_challenge (base64url-encode(sha256(code_verifier)))
code_challenge=$(printf "%s" "$code_verifier" | openssl sha256 -binary | base64url_encode)
echo "🔒 Code Challenge: $code_challenge"

# Verify the code_challenge is not empty
if [ -z "$code_challenge" ]; then
    echo "❌ Error: Failed to generate code_challenge"
    exit 1
fi

# 清理旧的 cookies
rm -f cookies.txt

# 获取登录页面和 CSRF token
login_page=$(curl -c cookies.txt -b cookies.txt -s -H "Accept: text/html" http://localhost:9000/login)
csrf_token=$(echo "$login_page" | grep -o 'name="_csrf".*value="[^"]*"' | sed 's/.*value="\([^"]*\)".*/\1/' | tr -d '\n')

echo "🔐 CSRF Token: $csrf_token"

# 验证是否成功获取到 CSRF token
if [ -z "$csrf_token" ]; then
    echo "❌ Error: Failed to get CSRF token"
    echo "Login page response:"
    echo "$login_page"
    exit 1
fi

# 直接使用原始 CSRF token，不进行 URL 编码
#encoded_csrf_token=$(urlencode "$csrf_token")
#echo "📝 Encoded CSRF Token: $encoded_csrf_token"

# 执行登录并捕获完整响应
login_response=$(curl -X POST http://localhost:9000/login \
    -c cookies.txt -b cookies.txt \
    -H "Accept: text/html,application/xhtml+xml" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=$USERNAME" \
    -d "password=$PASSWORD" \
    -d "_csrf=$csrf_token" \
    -v 2>&1)

# 检查是否重定向到密码修改页面
if echo "$login_response" | grep -q "/password/change"; then
    echo "🔄 Redirected to password change page"
    
    # 获取密码修改页面和新的 CSRF token
    change_password_page=$(curl -s \
        -c cookies.txt -b cookies.txt \
        -H "Accept: text/html" \
        ${session_headers:+-H "X-Auth-Token: $header_session_id"} \
        http://localhost:9000/password/change)
    
    new_csrf_token=$(echo "$change_password_page" | grep -o 'name="_csrf".*value="[^"]*"' | sed 's/.*value="\([^"]*\)".*/\1/' | tr -d '\n')
    state_token=$(echo "$change_password_page" | grep -o 'name="state".*value="[^"]*"' | sed 's/.*value="\([^"]*\)".*/\1/' | tr -d '\n')
    
    echo "🔐 New CSRF Token: $new_csrf_token"
    echo "🔐 State Token: $state_token"
    
    # 提交密码修改
    change_password_response=$(curl -s -X POST http://localhost:9000/password/change \
        -c cookies.txt -b cookies.txt \
        ${session_headers:+-H "X-Auth-Token: $header_session_id"} \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "_csrf=$new_csrf_token" \
        -d "state=$state_token" \
        -d "currentPassword=$PASSWORD" \
        -d "newPassword=$NEW_PASSWORD" \
        -d "confirmPassword=$NEW_PASSWORD" \
        -D - 2>/dev/null)
    
    # 检查密码修改是否成功
    if echo "$change_password_response" | grep -q "error"; then
        echo "❌ Password change failed!"
        echo "$change_password_response"
        exit 1
    fi
    
    echo "✅ Password changed successfully"
    
    # 使用新密码重新登录
    echo "🔄 Logging in with new password..."
    
    # 获取新的登录页面和 CSRF token
    login_page=$(curl -c cookies.txt -b cookies.txt -s -H "Accept: text/html" http://localhost:9000/login)
    csrf_token=$(echo "$login_page" | grep -o 'name="_csrf".*value="[^"]*"' | sed 's/.*value="\([^"]*\)".*/\1/' | tr -d '\n')
    
    # 使用新密码登录
    login_response=$(curl -X POST http://localhost:9000/login \
        -c cookies.txt -b cookies.txt \
        -H "Accept: text/html,application/xhtml+xml" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "username=$USERNAME" \
        -d "password=$NEW_PASSWORD" \
        -d "_csrf=$csrf_token" \
        -v 2>&1)
fi

# 提取会话标识符（同时支持 Cookie 和 Header 方式）
location=$(echo "$login_response" | grep -i "location:" | sed 's/.*Location: //' | tr -d '\r\n')
cookie_session_id=$(echo "$login_response" | grep -i "set-cookie:" | grep -o "JSESSIONID=[^;]*" | cut -d= -f2)
header_session_id=$(echo "$login_response" | grep -i "x-auth-token:" | sed 's/.*X-Auth-Token: //' | tr -d '\r\n')

# 使用可用的会话标识符
session_id=${header_session_id:-$cookie_session_id}

echo "🔄 Redirect Location: $location"
echo "🎫 Session ID: $session_id (${header_session_id:+header}${cookie_session_id:+cookie})"

# 为后续请求准备会话头
if [ -n "$header_session_id" ]; then
    session_headers="-H \"X-Auth-Token: $header_session_id\""
else
    session_headers=""
fi

# 验证登录是否成功
if echo "$location" | grep -q "/error\|/login?error"; then
    echo "❌ Login failed! Redirected to: $location"
    exit 1
elif [ -z "$location" ]; then
    echo "❌ Error: No redirect location found"
    echo "📋 Response headers:"
    echo "$login_response"
    exit 1
else
    echo "✅ Login successful! Redirected to: $location"
fi

# 保存会话 ID 供后续使用（可选）
echo "export SESSION_ID=$session_id" > session.env


# 设置重定向 URI 并编码
redirect_uri="http://127.0.0.1:3000/callback"
encoded_redirect_uri=$(urlencode "$redirect_uri")
echo "🌐 Redirect URI: $redirect_uri"

# 获取授权页面时使用会话信息
auth_page=$(curl -s \
    ${session_headers:+-H "X-Auth-Token: $header_session_id"} \
    -c cookies.txt -b cookies.txt \
    --max-redirs 0 \
    --no-location \
    "http://localhost:9000/oauth2/authorize?\
client_id=ffv-client&\
response_type=code&\
scope=openid%20profile&\
redirect_uri=${encoded_redirect_uri}&\
code_challenge=${code_challenge}&\
code_challenge_method=S256" \
    -D - 2>/dev/null)

# 检查是否需要用户同意
if echo "$auth_page" | grep -q "Consent required"; then
    echo "🔑 Consent required"
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
        ${session_headers:+-H "X-Auth-Token: $header_session_id"} \
        -c cookies.txt -b cookies.txt \
        "http://localhost:9000/oauth2/authorize" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "client_id=ffv-client" \
        -d "state=$state" \
        -d "scope=openid" \
        -d "scope=profile" \
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

# 添加较短延迟
echo "⏳ Waiting for authorization code to be processed..."
sleep 0.1

# 然后再使用授权码

# 保存授权码供后续使用（可选）
echo "export AUTH_CODE=$auth_code" > auth.env

# 检查必需的变量
if [ -z "$auth_code" ] || [ -z "$redirect_uri" ] || [ -z "$code_verifier" ]; then
    echo "❌ Error: Missing required parameters"
    echo "Authorization Code: $auth_code"
    echo "Redirect URI: $redirect_uri"
    echo "Code Verifier: $code_verifier"
    exit 1
fi

# 打印调试信息
echo -e "\n🔍 Debug Information:"
echo "Authorization Code: $auth_code"
echo "Code Verifier: $code_verifier"
echo "Redirect URI: $redirect_uri"
echo "Basic Auth: $(echo -n 'ffv-client:secret' | base64)"

# # 构建完整的请求体
# request_body="grant_type=authorization_code&\
# code=${auth_code}&\
# redirect_uri=${redirect_uri}&\
# code_verifier=${code_verifier}&\
# scope=openid%20profile"

# echo -e "\n📝 Request Body:"
# echo "$request_body"

# 编码 code_verifier
encoded_code_verifier=$(urlencode "$code_verifier")

# 编码 auth_code
encoded_auth_code=$(urlencode "$auth_code")

# 获取访问令牌
echo -e "\n🔄 Requesting access token..."
token_response=$(curl -v -X POST "http://localhost:9000/oauth2/token" \
    ${session_headers:+-H "X-Auth-Token: $header_session_id"} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -H "Authorization: Basic $(echo -n 'ffv-client:secret' | base64)" \
    -H "Accept: application/json" \
    -d "grant_type=authorization_code" \
    -d "code=$encoded_auth_code" \
    -d "redirect_uri=$encoded_redirect_uri" \
    -d "code_verifier=$encoded_code_verifier" \
    -d "scope=openid%20profile" \
    2>&1)

# 打印完整的响应
echo -e "\n📤 Full Response:"
echo "$token_response"

# 提取 JSON 部分
json_response=$(echo "$token_response" | grep -v "^[*<>}]" | tail -n 1)

# 检查响应是否包含错误
if echo "$json_response" | jq -e 'has("error")' > /dev/null; then
    echo -e "\n❌ Token request failed!"
    echo "Error: $(echo "$json_response" | jq -r '.error')"
    echo "Error description: $(echo "$json_response" | jq -r '.error_description // .message')"
    echo "Full JSON response:"
    echo "$json_response" | jq '.'
    exit 1
fi

# 提取令牌
access_token=$(echo "$json_response" | jq -r '.access_token')
refresh_token=$(echo "$json_response" | jq -r '.refresh_token')
id_token=$(echo "$json_response" | jq -r '.id_token')

# 验证是否成功获取到令牌
if [ "$access_token" = "null" ] || [ -z "$access_token" ]; then
    echo "❌ Failed to extract access token!"
    echo "Response:"
    echo "$json_response" | jq '.'
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

# 对于 macOS，使用 gbase64
if [[ "$OSTYPE" == "darwin"* ]]; then
    decode_jwt() {
        local jwt_part=$1
        local pad=$(( 4 - ${#jwt_part} % 4 ))
        if [ $pad -ne 4 ]; then
            jwt_part="${jwt_part}$(printf '=%.0s' $(seq 1 $pad))"
        fi
        jwt_part=$(echo "$jwt_part" | tr '_-' '/+')
        echo "$jwt_part" | gbase64 -d 2>/dev/null
    }
else
    decode_jwt() {
        local jwt_part=$1
        local pad=$(( 4 - ${#jwt_part} % 4 ))
        if [ $pad -ne 4 ]; then
            jwt_part="${jwt_part}$(printf '=%.0s' $(seq 1 $pad))"
        fi
        jwt_part=$(echo "$jwt_part" | tr '_-' '/+')
        echo "$jwt_part" | base64 -d 2>/dev/null
    }
fi

# 显示令牌信息（解码 JWT）
echo -e "\n📝 Access Token Claims:"
if [ -n "$access_token" ]; then
    token_body=$(echo "$access_token" | cut -d"." -f2)
    decode_jwt "$token_body" | jq '.' || echo "❌ Failed to decode access token"
else
    echo "❌ No access token available"
fi

echo -e "\n📝 ID Token Claims:"
if [ -n "$id_token" ]; then
    token_body=$(echo "$id_token" | cut -d"." -f2)
    decode_jwt "$token_body" | jq '.' || echo "❌ Failed to decode ID token"
else
    echo "❌ No ID token available"
fi

