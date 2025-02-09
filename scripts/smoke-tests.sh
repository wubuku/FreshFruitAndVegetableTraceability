#!/bin/bash

# Exit on error
set -e

# Configuration
API_BASE_URL="http://localhost:1023/api"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo "Starting smoke tests..."

# Helper function to handle "already exists" errors
handle_exists_error() {
    local http_status=$1
    local response=$2
    local operation=$3
    
    if [ $http_status -eq 400 ] && echo "$response" | grep -q "exists"; then
        echo -e "${GREEN}✓ $operation (already exists)${NC}"
        return 0
    fi
    return 1
}

# Modified check_response function to use handle_exists_error
check_response() {
    local curl_exit_code=$1
    local http_status=$2
    local operation=$3
    local response=$4
    
    if [ $curl_exit_code -ne 0 ]; then
        echo -e "${RED}✗ $operation failed (curl error: $curl_exit_code)${NC}"
        exit 1
    fi
    
    # Check for "already exists" case first
    if handle_exists_error "$http_status" "$response" "$operation"; then
        return 0
    fi
    
    if [[ $http_status -ge 200 && $http_status -lt 300 ]]; then
        echo -e "${GREEN}✓ $operation succeeded (HTTP Status: $http_status)${NC}"
    else
        echo -e "${RED}✗ $operation failed (HTTP Status: $http_status)${NC}"
        echo "Error response: $response"
        exit 1
    fi
}

#
# NOTE: 已启用多租户！
#
# 初始化数据（含租户数据）：
# java -jar ./ffvtraceability-service-cli/target/ffvtraceability-service-cli-0.0.1-SNAPSHOT.jar initData -d "file:../data/*.xml" --xml
#
# 启用多租户后，下面所有测试 HTTP 请求都使用 X-TenantID 头来指定当前租户。（下面的测试指定租户 ID 为 `X`）
#

# 获取当前租户的信息
curl -X 'GET' \
  "${API_BASE_URL}/BffTenants/current" \
  -H 'accept: application/json' \
  -H "X-TenantID: X"
# 返回结果类似：
# {"tenantId":"X","partyId":"FRESH_MART_DC","description":"Tenant X" ...}
# 当前租户可以绑定到某个 party（业务实体），可以使用返回的 partyId 来访问当前租户所属的业务实体拥有的"设施"等。

# 返回北美洲的州和省
curl -X 'GET' \
  "${API_BASE_URL}/BffGeo/NorthAmericanStatesAndProvinces" \
  -H 'accept: application/json' \
  -H "X-TenantID: X"

# Test Units of Measure (Base data)
echo -e "\n=== Testing Units of Measure ===\n"


# Create KGM (Required by Raw Items)
echo "Creating KGM unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "KGM",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "kg",
  "description": "Kilogram - The base unit of mass in the International System of Units (SI)",
  "gs1AI": "3100"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create KGM unit" "$response_body"

# Query Units of Measure
echo "Querying Units of Measure..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffUnitsOfMeasure?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffUnitsOfMeasure?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query Units of Measure"

# Create GRM
echo "Creating GRM unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "GRM",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "g",
  "description": "Gram - 1/1000 of a kilogram",
  "gs1AI": "3103"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create GRM unit" "$response_body"

# Create USD (Required by Suppliers)
echo "Creating USD unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "USD",
  "uomTypeId": "CURRENCY_MEASURE",
  "abbreviation": "$",
  "numericCode": 840,
  "description": "United States Dollar - The official currency of the United States",
  "gs1AI": "3920"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create USD unit" "$response_body"

# Create SQM (Required by Facilities)
echo "Creating SQM unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "SQM",
  "uomTypeId": "AREA_MEASURE",
  "abbreviation": "m²",
  "description": "Square Meter - The measurement of area in the International System of Units (SI)",
  "gs1AI": "3340"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create SQM unit" "$response_body"

# Create TNE (Metric Ton)
echo "Creating TNE unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "TNE",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "t",
  "description": "Metric Ton - A unit of mass equal to 1,000 kilograms",
  "gs1AI": "3100"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create TNE unit" "$response_body"

# Create LB (Pound)
echo "Creating LB unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "LB",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "lb",
  "description": "Pound - A unit of mass commonly used in North America",
  "gs1AI": "3200"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create LB unit" "$response_body"

# Create OZ (Ounce)
echo "Creating OZ unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "OZ",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "oz",
  "description": "Ounce - A unit of mass commonly used in North America",
  "gs1AI": "3560"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create OZ unit" "$response_body"

# Create EA (Each)
echo "Creating EA unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "EA",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "ea",
  "description": "Each - A single unit or piece",
  "gs1AI": "37"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create EA unit" "$response_body"

# Create BX (Box)
echo "Creating BX unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "BX",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "bx",
  "description": "Box - A container for packaging items",
  "gs1AI": "37"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create BX unit" "$response_body"

# Create PLT (Pallet)
echo "Creating PLT unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "PLT",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "plt",
  "description": "Pallet - A flat transport structure to support goods",
  "gs1AI": "37"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create PLT unit" "$response_body"

# Create PK (Pack)
echo "Creating PK unit..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "uomId": "PK",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "pk",
  "description": "Pack - A bundle or package of items",
  "gs1AI": "37"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create PK unit" "$response_body"



# Test Documents
echo -e "\n=== Testing Documents ===\n"

# Create document
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffDocuments" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "comments": "Quality certification document for organic vegetables batch #2024001", 
  "documentLocation": "https://example.com/docs/cert/2024001.pdf",
  "documentText": "Batch #2024001 passed QA inspections"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create document" "$response_body"

# Query documents
echo "Querying Documents..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffDocuments?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffDocuments?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query documents"

# Test Suppliers
echo -e "\n=== Testing Suppliers ===\n"

# Create supplier for source facility
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffSuppliers" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "supplierId": "SUPPLIER_001",
  "supplierName": "Vegetables Farm",
  "ggn": "4049929999999",
  "gln": "5420000000008",
  "externalId": "ORGANIC_FARM_01",
  "preferredCurrencyUomId": "USD",
  "description": "Organic Vegetables Farm - specializing in greenhouse vegetables with GLOBALG.A.P. certification"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create source supplier" "$response_body"

# Query Suppliers
echo "Querying Suppliers..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffSuppliers?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffSuppliers?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query suppliers"

# Query specific supplier
echo "Querying specific supplier..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffSuppliers/SUPPLIER_001" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffSuppliers/SUPPLIER_001" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query specific supplier"

# -----------------------------------------------------------------------------------
# 下面的数据可以通过 XML 数据文件导入：
# -----------------------------------------------------------------------------------
# Create party for destination facility
# curl -X 'POST' \
#   "${API_BASE_URL}/BffParties" \
#   -H 'accept: */*' \
#   -H 'Content-Type: application/json' \
#   -H "X-TenantID: X" \
#   -s \
#   -w '%{http_code}\n' \
#   -o /dev/null \
#   -d '{
#   "partyId": "FRESH_MART_DC",
#   "partyName": "Fresh Mart Distribution",
#   "ggn": "4049929999998",
#   "gln": "5420000000009",
#   "externalId": "FRESH_MART_DC",
#   "preferredCurrencyUomId": "USD",
#   "description": "Fresh Mart main distribution center"
# }' | { read http_status; check_response $? "$http_status" "Create MY PARTY"; }


# Test Facilities
echo -e "\n=== Testing Facilities ===\n"

# Create source facility
echo "Creating source facility..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "facilityId": "F001",
  "ownerPartyId": "SUPPLIER_001",
  "facilityName": "Fresh Produce Processing Center",
  "facilitySize": 5000,
  "facilitySizeUomId": "SQM",
  "description": "Modern food processing facility with cold storage capabilities",
  "active": "Y",
  "gln": "1234567890123",
  "ffrn": "12345678901"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create source facility" "$response_body"

# Query Facilities
echo "Querying Facilities..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query facilities"

# Query specific facility
echo "Querying specific facility..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/F001" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/F001" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query specific facility"

# Create destination facility
echo "Creating destination facility..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "facilityId": "DC_FRESH",
  "ownerPartyId": "FRESH_MART_DC",
  "facilityName": "Fresh Mart Distribution Center",
  "facilitySize": 10000,
  "facilitySizeUomId": "SQM",
  "description": "Main distribution center for Fresh Mart",
  "active": "Y",
  "gln": "1234567890124",
  "ffrn": "12345678902"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create destination facility" "$response_body"


# Create location for source facility
echo "Creating location for source facility..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities/F001/Locations" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "locationSeqId": "F001-WH01-A01-01",
  "locationTypeEnumId": "STORAGE",
  "areaId": "WH01",
  "aisleId": "A01",
  "sectionId": "01",
  "levelId": "01",
  "positionId": "01",
  "geoPointId": "GP001",
  "active": "Y"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create location for source facility" "$response_body"


# Query Locations for F001
echo "Querying Locations for F001..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/F001/Locations" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/F001/Locations" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query locations for F001"

# Query specific location
echo "Querying specific location..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/F001/Locations/F001-WH01-A01-01" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/F001/Locations/F001-WH01-A01-01" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query specific location"

# Create location for destination facility
echo "Creating location for destination facility..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities/DC_FRESH/Locations" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "locationSeqId": "DC_FRESH-WH01-A01",
  "locationTypeEnumId": "STORAGE",
  "areaId": "WH01",
  "aisleId": "A01",
  "geoPointId": "GP002",
  "active": "Y"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create location for destination facility" "$response_body"


# Query Locations for DC_FRESH
echo "Querying Locations for DC_FRESH..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/DC_FRESH/Locations" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffFacilities/DC_FRESH/Locations" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query locations for DC_FRESH"

# Test Raw Items
echo -e "\n=== Testing Raw Items ===\n"

# Create raw item
echo "Creating raw item..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "productId": "PROD001",
  "productName": "Organic Red Apple",
  "description": "Fresh organic apples from certified orchards",
  "gtin": "0614141123452",
  "smallImageUrl": "https://example.com/images/apple-small.jpg",
  "mediumImageUrl": "https://example.com/images/apple-medium.jpg",
  "largeImageUrl": "https://example.com/images/apple-large.jpg",
  "quantityUomId": "KGM",
  "quantityIncluded": 1.0,
  "piecesIncluded": 1,
  "statusId": "ACTIVE",
  "supplierId": "SUPPLIER_001"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create raw item" "$response_body"

# Query Raw Items
echo "Querying Raw Items..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffRawItems?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffRawItems?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query raw items"

# Query raw items by product ID
echo "Querying raw items by product ID..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffRawItems?productId=PROD001&firstResult=0&maxResults=100" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffRawItems?productId=PROD001&firstResult=0&maxResults=100" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query raw items by product ID"

# Create another raw item (Required by Receiving)
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "productId": "ORGANIC_TOMATO_01",
  "productName": "Organic Tomato",
  "description": "Fresh organic tomatoes",
  "gtin": "0614141123453",
  "quantityUomId": "KGM",
  "quantityIncluded": 1.0,
  "piecesIncluded": 1,
  "statusId": "ACTIVE",
  "supplierId": "SUPPLIER_001"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create tomato raw item" "$response_body"

# Test Lots
echo -e "\n=== Testing Lots ===\n"

# Create lot
echo "Creating lot..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffLots" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "lotId": "LOT20240315A",
  "gs1Batch": "LOT20240315A",
  "quantity": 100,
  "expirationDate": "2034-12-18T08:53:18.475Z"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create lot" "$response_body"


# Query Lots
echo "Querying Lots..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffLots?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffLots?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query lots"


# ---------------------------------------------------------------------------------------
# 订单测试
echo -e "\n=== Testing Purchase Orders ===\n"

# Create additional raw items (Required by Orders)
echo -e "\n=== Creating Additional Raw Items ===\n"

# Create organic lettuce
echo "Creating organic lettuce raw item..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "productId": "ORGANIC_LETTUCE_01",
  "productName": "Organic Lettuce",
  "description": "Fresh organic lettuce from certified farms",
  "gtin": "0614141123454",
  "quantityUomId": "KGM",
  "quantityIncluded": 1.0,
  "piecesIncluded": 1,
  "statusId": "ACTIVE",
  "supplierId": "SUPPLIER_001"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create lettuce raw item" "$response_body"

# Create organic cucumber
echo "Creating organic cucumber raw item..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "productId": "ORGANIC_CUCUMBER_01",
  "productName": "Organic Cucumber",
  "description": "Fresh organic cucumbers from certified farms",
  "gtin": "0614141123455",
  "quantityUomId": "KGM",
  "quantityIncluded": 1.0,
  "piecesIncluded": 1,
  "statusId": "ACTIVE",
  "supplierId": "SUPPLIER_001"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create cucumber raw item" "$response_body"

# Create another lot (Required by second receiving item)
echo "Creating another lot..."
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffLots" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "lotId": "LOT20240315B",
  "gs1Batch": "LOT20240315B",
  "quantity": 100,
  "expirationDate": "2034-12-18T08:53:18.475Z"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Create second lot" "$response_body"


# 测试查询订单列表
echo "Querying purchase orders..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffPurchaseOrders?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffPurchaseOrders?page=0&size=20" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query purchase orders"

# 创建订单并提取 orderId
echo "Creating purchase order..."
ORDER_ID=$(curl -X 'POST' \
  "${API_BASE_URL}/BffPurchaseOrders" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -d '{
  "orderName": "Organic Produce Order",
  "originFacilityId": "F001",
  "memo": "Weekly organic produce order",
  "supplierId": "SUPPLIER_001",
  "orderItems": [
    {
      "productId": "ORGANIC_TOMATO_01",
      "quantity": 500,
      "unitPrice": 2.99,
      "itemDescription": "Organic Tomatoes, Grade A",
      "comments": "Require fresh produce",
      "estimatedShipDate": "2024-03-20T08:00:00Z",
      "estimatedDeliveryDate": "2024-03-21T08:00:00Z"
    },
    {
      "productId": "ORGANIC_LETTUCE_01",
      "quantity": 300,
      "unitPrice": 1.99,
      "itemDescription": "Organic Lettuce",
      "comments": "Keep refrigerated",
      "estimatedShipDate": "2024-03-20T08:00:00Z",
      "estimatedDeliveryDate": "2024-03-21T08:00:00Z"
    }
  ]
}' | tr -d '"')

# 检查订单ID是否成功获取
if [ -z "$ORDER_ID" ]; then
  echo -e "${RED}ERROR: Failed to get Order ID from response${NC}"
  exit 1
fi

echo "Created purchase order with ID: $ORDER_ID"
check_response $? "$http_code" "Create purchase order"

# 更新订单行项
echo "Updating order item..."
curl -X 'PUT' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/Items/1" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
   "productId": "ORGANIC_TOMATO_01",
   "quantity": 550,
   "unitPrice": 2.99,
   "itemDescription": "Organic Tomatoes, Grade A",
   "comments": "Updated: Require extra fresh produce",
   "estimatedShipDate": "2024-03-20T08:00:00Z",
   "estimatedDeliveryDate": "2024-03-21T08:00:00Z"
}' | { read http_status; check_response $? "$http_status" "Update order item"; }

# 添加新的订单行项
echo "Adding new order item..."
curl -X 'POST' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/Items" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
   "productId": "ORGANIC_CUCUMBER_01",
   "quantity": 200,
   "unitPrice": 1.49,
   "itemDescription": "Organic Cucumbers",
   "comments": "New item added",
   "estimatedShipDate": "2024-03-20T08:00:00Z",
   "estimatedDeliveryDate": "2024-03-21T08:00:00Z"
}' | { read http_status; check_response $? "$http_status" "Add order item"; }


# ---------------------------------------------------------------------------------------
# Test Receiving
echo -e "\n=== Testing Receiving ===\n"

# 创建收货单并提取 receivingDocumentId
echo "Creating receipt..."
RECEIVING_ID=$(curl -X 'POST' \
  "${API_BASE_URL}/BffReceipts" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -d '{
  "partyIdTo": "FRESH_MART_DC",
  "partyIdFrom": "SUPPLIER_001",
  "originFacilityId": "F001",
  "destinationFacilityId": "DC_FRESH",
  "primaryOrderId": "'${ORDER_ID}'",
  "receivingItems": [
    {
      "productId": "ORGANIC_TOMATO_01",
      "lotId": "LOT20240315A",
      "locationSeqId": "DC_FRESH-WH01-A01",
      "itemDescription": "Organic Tomatoes",
      "quantityAccepted": 500.00,
      "quantityRejected": 20.00,
      "casesAccepted": 25,
      "casesRejected": 1
    }
  ],
  "referenceDocuments": [
    {
      "documentLocation": "https://example.com/docs/asn/ASN2024031502.pdf"
    }
  ]
}' | tr -d '"')

# 检查收货单ID是否成功获取
if [ -z "$RECEIVING_ID" ]; then
  echo -e "${RED}ERROR: Failed to get Receiving ID from response${NC}"
  exit 1
fi

echo "Created receipt with ID: $RECEIVING_ID"
check_response $? "$http_code" "Create receipt"

# 添加收货行项
echo "Adding receiving item..."
curl -X 'POST' \
  "${API_BASE_URL}/BffReceipts/${RECEIVING_ID}/Items" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "productId": "ORGANIC_LETTUCE_01",
  "lotId": "LOT20240315B",
  "locationSeqId": "DC_FRESH-WH01-B02",
  "itemDescription": "Organic Lettuce",
  "quantityAccepted": 290.00,
  "quantityRejected": 10.00,
  "casesAccepted": 15,
  "casesRejected": 1
}' | { read http_status; check_response $? "$http_status" "Add receiving item"; }


# Query Receipts
echo "Querying Receipts..."
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts?page=0&size=20&documentIdOrItem=ORGANIC_TOMATO_01" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query receipts"

echo -e "\n${GREEN}All smoke tests completed successfully!${NC}"

# Test QA Inspections
echo -e "\n=== Testing QA Inspections ===\n"

# 从上一个查询结果中提取 receivingDocumentId 和 receiptId
RECEIVING_DOC_ID=$(echo "$response" | jq -r '.content[0].documentId')
RECEIPT_ID=$(echo "$response" | jq -r '.content[0].receivingItems[0].receiptId')
echo "Using Receiving Document ID: ${RECEIVING_DOC_ID}"
echo "Using Receipt ID: ${RECEIPT_ID}"

# Create QA Inspection
echo "Creating QA Inspection..."
QA_INSPECTION_ID=$(curl -X 'POST' \
  "${API_BASE_URL}/BffQaInspections" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -d "{
  \"receiptId\": \"${RECEIPT_ID}\",
  \"statusId\": \"ON_HOLD\",
  \"comments\": \"Initial quality inspection passed. All parameters within acceptable range.\"
}" | tr -d '"')

echo "Created QA Inspection ID: ${QA_INSPECTION_ID}"
check_response $? "$http_code" "Create QA inspection"

# Query QA Inspections by receiving document
echo "Querying QA Inspections..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffQaInspections?receivingDocumentId=${RECEIVING_DOC_ID}" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
http_code=$(curl -X 'GET' \
  "${API_BASE_URL}/BffQaInspections?receivingDocumentId=${RECEIVING_DOC_ID}" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}' \
  -o /dev/null)

echo "$response"
check_response $? "$http_code" "Query QA inspections"


# 重新计算订单的 fulfillmentStatus
echo "Recalculating fulfillment status..."
# 捕获 输出 FULFILLMENT_STATUS
FULFILLMENT_STATUS=$(curl -X 'POST' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/recalculateFulfillmentStatus" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -d '{}' | tr -d '"')
echo "Order ${ORDER_ID}, fulfillment status: ${FULFILLMENT_STATUS}"

# 查询订单，包含行项的履行状态。注意参数 includesItemFulfillments=true
echo "Querying order item fulfillments..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}?includesItemFulfillments=true" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s
)
echo "$response"
# 输出类似：
# {"orderId":"11CMNR3KCJRZ4PXE95","orderName":"Organic Produce Order","orderDate":"2025-01-12T13:12:53.591547Z","originFacilityId":"F001","memo":"Weekly organic produce order","supplierId":"SUPPLIER_001","supplierName":"Vegetables Farm","createdAt":"2025-01-12T13:12:53.599303Z","orderItems":[{"orderItemSeqId":"2" ...

# 查询订单行项，包含（当前行项的）履行状态。注意参数 includesFulfillments=true
# 提取 orderItems 中的 orderItemSeqId
ORDER_ITEM_SEQ_ID=$(echo "$response" | jq -r '.orderItems[0].orderItemSeqId')
echo "Using Order Item Seq ID: ${ORDER_ITEM_SEQ_ID}"

response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/Items/${ORDER_ITEM_SEQ_ID}?includesFulfillments=true" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s
)
echo "$response"
# 输出类似：{"orderItemSeqId":"1","productId":"ORGANIC_TOMATO_01","productName":"Organic Tomato","gtin":"0614141123453","quantity":550.000000,"unitPrice":2.990,"itemDescription":"Organic Tomatoes, Grade A","comments":"Updated: Require extra fresh produce","estimatedShipDate":"2024-03-20T08:00:00Z","estimatedDeliveryDate":"2024-03-21T08:00:00Z","fulfillments":[{"receiptId":"11CCL74RZCU00FZCVG-1","allocatedQuantity":500.000000,"receivedAt":"2025-01-12T04:30:43.862706Z","qaStatusId":"DRAFTED"},{"receiptId":"11CD2111RBF6YQ3N86-1","allocatedQuantity":50.000000,"receivedAt":"2025-01-12T05:39:41.546006Z","qaStatusId":"DRAFTED"}]}
# 提取出 productId
PRODUCT_ID=$(echo "$response" | jq -r '.productId')
echo "Using Product ID: ${PRODUCT_ID}"

# # Update QA Inspection status
# echo "Updating QA Inspection..."
# response=$(curl -X 'PUT' \
#   "${API_BASE_URL}/BffQaInspections/${QA_INSPECTION_ID}" \
#   -H 'accept: application/json' \
#   -H 'Content-Type: application/json' \
#   -H "X-TenantID: X" \
#   -s \
#   -w '\n%{http_code}' \
#   -d "{
#   \"statusId\": \"APPROVED\",
#   \"comments\": \"Additional verification required for temperature logs.\"
# }")
# http_status=$(echo "$response" | tail -n1)
# response_body=$(echo "$response" | sed '$d')
# check_response $? "$http_status" "Update QA inspection" "$response_body"


# Update supplier business contact
echo "Updating supplier business contact..."
response=$(curl -X 'PUT' \
  "${API_BASE_URL}/BffSuppliers/SUPPLIER_001/BusinessContact" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "businessName": "Fresh Organic Farms Office",
  "phoneNumber": "+1 415 555 0123",
  "physicalLocationAddress": "2500 Sand Hill Road",
  "city": "Menlo Park",
  "stateProvinceGeoId": "CA",
  "zipCode": "94025"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Update supplier business contact" "$response_body"


# 更新之前出现过的所有 Facility 的联系方式

# Update facility business contact information
echo "Updating facility business contacts..."

# Update DC_FRESH facility contact
response=$(curl -X 'PUT' \
  "${API_BASE_URL}/BffFacilities/DC_FRESH/BusinessContact" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "businessName": "Fresh Mart Distribution Center",
  "phoneNumber": "+1 408 555 0123",
  "physicalLocationAddress": "2800 Distribution Way",
  "city": "San Jose",
  "stateProvinceGeoId": "CA",
  "zipCode": "95134",
  "country": "USA",
  "email": "operations@freshmart-dc.example.com",
  "contactRole": "Distribution Center Manager"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Update DC_FRESH business contact" "$response_body"

# Update F001 facility contact
response=$(curl -X 'PUT' \
  "${API_BASE_URL}/BffFacilities/F001/BusinessContact" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "businessName": "Fresh Produce Processing Center",
  "phoneNumber": "+1 831 555 0456",
  "physicalLocationAddress": "1200 Agriculture Road",
  "city": "Salinas",
  "stateProvinceGeoId": "CA",
  "zipCode": "93901",
  "country": "USA",
  "email": "operations@fresh-produce.example.com",
  "contactRole": "Processing Center Supervisor"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Update F001 business contact" "$response_body"


# 查询采购订单行项的未履行数量
echo "Querying purchase order item outstanding quantity..."
curl -X 'GET' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/Items/${ORDER_ITEM_SEQ_ID}/OutstandingQuantity" \
  -H 'accept: application/json' \
  -H "X-TenantID: X"

echo ""

# 查询采购订单的某个产品的未履行数量
echo "Querying purchase order product outstanding quantity..."
curl -X 'GET' \
  "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/getOutstandingQuantityByProduct?productId=${PRODUCT_ID}" \
  -H 'accept: application/json' \
  -H "X-TenantID: X"

echo ""
#exit 0


# 查询"收货单"信息，并要求返回"收货行项"关联的"采购订单行项"的未履行数量。
# 注意设置参数 `includesOutstandingOrderQuantity=true`
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts/${RECEIVING_ID}?includesOutstandingOrderQuantity=true" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
echo "$response_body"
check_response $? "$http_status" "Query receipt with outstanding quantity" "$response_body"

# 查询"收货单"信息，并要求返回 QA 检验状态
# 注意设置参数 `derivesQaInspectionStatus=true`
echo "Querying receiving document QA inspection status..."
curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts/${RECEIVING_ID}?includesOutstandingOrderQuantity=true&derivesQaInspectionStatus=true" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X"

# 查询收货单列表，并要求"连带"返回每个收货单的 QA 检验状态
# 注意设置参数 `derivesQaInspectionStatus=true`
# 不建议滥用，可能会导致性能问题。
# 也许后面可以考虑提供一个"批量查询收货单的 QA 检验状态"的接口，由前端组合使用。
curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts?derivesQaInspectionStatus=true" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X"

# ------------------------------------------------------------------------------------
# 为前面出现过的 Lot 记录，创建对应的 Primary Tlc
echo -e "\n=== Creating Primary TLC ===\n"

# 为 LOT20240315A (有机番茄) 创建 Primary TLC
# 先检查是否已经存在，如果存在，则跳过。
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffLots/getPrimaryTlc?gtin=0614141123453&gs1Batch=LOT20240315A" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
echo "$response"
if [ -n "$response" ]; then
  echo "Primary TLC already exists, skipping creation."
else
  echo "Creating Primary TLC for organic tomatoes..."
  response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffLots/createPrimaryTlc" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "gs1Batch": "LOT20240315A",
  "quantity": 500,
  "expirationDate": "2034-12-18T08:53:18.475Z",
  "gtin": "0614141123453",
  "sourceFacilityId": "F001"
  }')
  http_status=$(echo "$response" | tail -n1)
  response_body=$(echo "$response" | sed '$d')
  check_response $? "$http_status" "Create Primary TLC for tomatoes" "$response_body"
fi

# 为 LOT20240315B (有机生菜) 创建 Primary TLC
# 先检查是否已经存在，如果存在，则跳过。
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffLots/getPrimaryTlc?gtin=0614141123454&gs1Batch=LOT20240315B" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
echo "$response"
if [ -n "$response" ]; then
  echo "Primary TLC already exists, skipping creation."
else
  echo "Creating Primary TLC for organic lettuce..."
  response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffLots/createPrimaryTlc" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "gs1Batch": "LOT20240315B",
  "quantity": 290,
  "expirationDate": "2034-12-18T08:53:18.475Z",
  "gtin": "0614141123454",
  "sourceFacilityId": "F001"
  }')
  # 从响应中分离出 HTTP 状态码和响应内容
  http_status=$(echo "$response" | tail -n1)
  response_body=$(echo "$response" | sed '$d')
  check_response $? "$http_status" "Create Primary TLC for lettuce" "$response_body"
fi


# 触发生成 CTE receiving 事件
response=$(curl -X 'POST' \
  "${API_BASE_URL}/BffReceipts/${RECEIVING_ID}/synchronizeCteReceivingEvents" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '\n%{http_code}' \
  -d '{
  "documentId": "${RECEIVING_ID}"
}')
http_status=$(echo "$response" | tail -n1)
response_body=$(echo "$response" | sed '$d')
check_response $? "$http_status" "Synchronize CTE receiving events" "$response_body"

# 查询 CTE receiving 事件
echo "Querying CTE receiving events..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/ReceivingEvents?firstResult=0&maxResults=2147483647" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
echo "$response"


# response=$(curl -X 'PUT' \
#   "${API_BASE_URL}/QaInspections/${QA_INSPECTION_ID}/_commands/QaInspectionAction" \
#   -H 'accept: */*' \
#   -H "X-TenantID: X" \
#   -H 'Content-Type: application/json' \
#   -s \
#   -w '\n%{http_code}' \
#   -d '{
#   "commandId": "RANDOM197972935792396296493",
#   "value": "Approve",
#   "version": 0
# }')
# http_status=$(echo "$response" | tail -n1)
# response_body=$(echo "$response" | sed '$d')
# check_response $? "$http_status" "Update QA inspection" "$response_body"




# 查询供应商类型枚举
echo "Querying supplier type enumeration..."
curl -X 'GET' \
  "${API_BASE_URL}/Enumerations?enumTypeId=SUPPLIER_TYPE_ENUM" \
  -H 'accept: application/json' \
  -H "X-TenantID: X"
