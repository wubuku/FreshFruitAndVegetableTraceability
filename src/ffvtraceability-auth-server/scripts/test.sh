#!/bin/bash


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
csrf_token=$(curl -c cookies.txt -b cookies.txt -s http://localhost:9000/login | sed -n 's/.*name="_csrf" type="hidden" value="\([^"]*\).*/\1/p')
echo "🔐 CSRF Token: $csrf_token"

encoded_csrf_token=$(urlencode "$csrf_token")
echo "📝 Encoded CSRF Token: $encoded_csrf_token"

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

echo "🔄 Redirect Location: $location"
echo "🎫 Session ID: $session_id"


# 验证登录是否成功
if [ "$location" = "http://localhost:9000/" ]; then
    echo "✅ Login successful!"
elif [ -z "$location" ]; then
    echo "❌ Error: No redirect location found"
    echo "📋 Response headers:"
    echo "$login_response"
    exit 1
else
    echo "❌ Login failed! Redirected to: $location"
    exit 1
fi

# 保存会话 ID 供后续使用（可选）
echo "export SESSION_ID=$session_id" > session.env


# 设置重定向 URI 并编码
redirect_uri="http://127.0.0.1:3000/callback"
encoded_redirect_uri=$(urlencode "$redirect_uri")
echo "🌐 Redirect URI: $redirect_uri"

# 获取授权页面时使用编码后的 URI
auth_page=$(curl -s \
    -c cookies.txt -b cookies.txt \
    --max-redirs 0 \
    --no-location \
    "http://localhost:9000/oauth2/authorize?\
client_id=ffv-client&\
response_type=code&\
scope=openid%20read%20write&\
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

# 添加较短延迟
echo "⏳ Waiting for authorization code to be processed..."
sleep 0.3

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

# 构建完整的请求体
request_body="grant_type=authorization_code&\
code=${auth_code}&\
redirect_uri=${redirect_uri}&\
code_verifier=${code_verifier}&\
scope=openid%20read%20write"

echo -e "\n📝 Request Body:"
echo "$request_body"

# 编码 code_verifier
encoded_code_verifier=$(urlencode "$code_verifier")

# 编码 auth_code
encoded_auth_code=$(urlencode "$auth_code")

# 获取访问令牌
echo -e "\n🔄 Requesting access token..."
token_response=$(curl -v -X POST "http://localhost:9000/oauth2/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -H "Authorization: Basic $(echo -n 'ffv-client:secret' | base64)" \
    -H "Accept: application/json" \
    -d "grant_type=authorization_code" \
    -d "code=$encoded_auth_code" \
    -d "redirect_uri=$encoded_redirect_uri" \
    -d "code_verifier=$encoded_code_verifier" \
    -d "scope=openid%20read%20write" \
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
