#!/bin/bash

# 设置基础 URL
BASE_URL="http://localhost:9000"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# 测试函数
test_page_access() {
    local endpoint=$1
    local description=$2

    echo -e "\n🔒 Testing: ${description}"
    echo "Endpoint: ${endpoint}"

    # 获取登录页面和 CSRF token
    local login_page=$(curl -c cookies.txt -b cookies.txt -s -H "Accept: text/html" "${BASE_URL}/login")
    local csrf_token=$(echo "$login_page" | grep -o 'name="_csrf".*value="[^"]*"' | sed 's/.*value="\([^"]*\)".*/\1/' | tr -d '\n')

    echo "🔐 CSRF Token: $csrf_token"

    # 使用 CSRF token 登录
    local login_response=$(curl -X POST "${BASE_URL}/login" \
        -c cookies.txt -b cookies.txt \
        -H "Accept: text/html,application/xhtml+xml" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "username=user" \
        -d "password=admin" \
        -d "_csrf=$csrf_token" \
        -v 2>&1)

    # 使用登录后的 cookie 访问目标页面
    local response=$(curl -s -w "%{http_code}" \
        -b cookies.txt \
        -H "Accept: text/html" \
        "${BASE_URL}${endpoint}" \
        -o /dev/null)

    if [ "$response" = "403" ]; then
        echo -e "${GREEN}✓ Test passed: Access denied (403 Forbidden)${NC}"
    else
        echo -e "${RED}✗ Test failed: Expected 403 but got $response${NC}"
    fi

    # 清理 cookies
    rm -f cookies.txt
}

echo "🔐 Testing Protected Pages Access - Normal User"
echo "=================================================="

# 测试管理员页面访问
test_page_access "/group-management" "Access group management page"
test_page_access "/permission-management" "Access permission management page"
test_page_access "/user-management" "Access user management page"
test_page_access "/pre-register" "Access pre-register page"

echo -e "\n✨ Security Test Suite Completed"
echo "==================================================" 