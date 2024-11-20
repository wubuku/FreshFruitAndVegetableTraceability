#!/bin/bash

# 设置基础 URL
BASE_URL="http://localhost:9000"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试函数
test_api() {
    local method=$1
    local endpoint=$2
    local description=$3
    local data=$4

    echo -e "\n🔒 Testing: ${description}"
    echo "Method: ${method}, Endpoint: ${endpoint}"

    if [ -n "$data" ]; then
        response=$(curl -L -s -w "%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data" \
            -o /dev/null)
    else
        response=$(curl -L -s -w "%{http_code}" -X $method "$BASE_URL$endpoint" -o /dev/null)
    fi

    if [ "$response" = "401" ] || [ "$response" = "403" ]; then
        if [ "$response" = "401" ]; then
            echo -e "${GREEN}✓ Test passed: Got status code 401 (Unauthorized)${NC}"
        else
            echo -e "${GREEN}✓ Test passed: Got status code 403 (Forbidden)${NC}"
        fi
    else
        echo -e "${RED}✗ Test failed: Expected 401 or 403 but got $response${NC}"
        echo -e "${YELLOW}Note: 401 means unauthorized, 403 means forbidden${NC}"
    fi
}

echo "🔐 Testing API Security - Unauthorized Access Attempts"
echo "=================================================="
echo -e "${YELLOW}Note: Both 401 (Unauthorized) and 403 (Forbidden) are acceptable responses${NC}"
echo "      401 - No authentication provided"
echo "      403 - Authentication failed or insufficient permissions"
echo "=================================================="

# 测试组管理 API
test_api "GET" "/api/groups/list" "Get groups list without authentication"
test_api "POST" "/api/groups/create" "Create group without authentication" '{"groupName":"test_group"}'
test_api "POST" "/api/groups/1/members" "Add group member without authentication" '{"username":"test_user"}'
test_api "DELETE" "/api/groups/1/members/test_user" "Remove group member without authentication"
test_api "POST" "/api/groups/1/toggle-enabled" "Toggle group status without authentication"

# 测试用户管理 API
test_api "GET" "/api/users/list" "Get users list without authentication"
test_api "POST" "/api/users/test_user/toggle-enabled" "Toggle user status without authentication"
test_api "POST" "/api/users/test_user/require-password-change" "Require password change without authentication"
test_api "POST" "/api/users/pre-register" "Pre-register user without authentication" '{"username":"test_user"}'

echo -e "\n✨ Security Test Suite Completed"
echo "==================================================" 