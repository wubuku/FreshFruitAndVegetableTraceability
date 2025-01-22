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

# Function to check HTTP response
check_response() {
    local curl_exit_code=$1
    local http_status=$2
    local operation=$3
    
    if [ $curl_exit_code -ne 0 ]; then
        echo -e "${RED}✗ $operation failed (curl error: $curl_exit_code)${NC}"
        exit 1
    fi
    
    if [[ $http_status -ge 200 && $http_status -lt 300 ]]; then
        echo -e "${GREEN}✓ $operation succeeded (HTTP Status: $http_status)${NC}"
    else
        echo -e "${RED}✗ $operation failed (HTTP Status: $http_status)${NC}"
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
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "KGM",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "kg",
  "description": "Kilogram - The base unit of mass in the International System of Units (SI)",
  "gs1AI": "3100"
}' | { read http_status; check_response $? "$http_status" "Create KGM unit"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "GRM",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "g",
  "description": "Gram - 1/1000 of a kilogram",
  "gs1AI": "3103"
}' | { read http_status; check_response $? "$http_status" "Create GRM unit"; }

# Create USD (Required by Suppliers)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "USD",
  "uomTypeId": "CURRENCY_MEASURE",
  "abbreviation": "$",
  "numericCode": 840,
  "description": "United States Dollar - The official currency of the United States",
  "gs1AI": "3920"
}' | { read http_status; check_response $? "$http_status" "Create USD unit"; }

# Create SQM (Required by Facilities)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "SQM",
  "uomTypeId": "AREA_MEASURE",
  "abbreviation": "m²",
  "description": "Square Meter - The measurement of area in the International System of Units (SI)",
  "gs1AI": "3340"
}' | { read http_status; check_response $? "$http_status" "Create SQM unit"; }


# Create TNE (Metric Ton)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "TNE",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "t",
  "description": "Metric Ton - A unit of mass equal to 1,000 kilograms",
  "gs1AI": "3100"
}' | { read http_status; check_response $? "$http_status" "Create TNE unit"; }
# 注意：由于 GS1 标准中没有直接表示公吨的 AI，这里使用 3100（整数千克）。
# 在生成 GS1 条码时，需要将公吨值乘以 1000 转换为千克。
# 在读取 GS1 条码时，需要将千克值除以 1000 转换回公吨。

# Create LB (Pound)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "LB",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "lb",
  "description": "Pound - A unit of mass commonly used in North America",
  "gs1AI": "3200"
}' | { read http_status; check_response $? "$http_status" "Create LB unit"; }

# Create OZ (Ounce)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "OZ",
  "uomTypeId": "WEIGHT_MEASURE",
  "abbreviation": "oz",
  "description": "Ounce - A unit of mass commonly used in North America",
  "gs1AI": "3560"
}' | { read http_status; check_response $? "$http_status" "Create OZ unit"; }


# <UomType description="Packaging Type" hasTable="N" uomTypeId="PACKAGE_TYPE_MEASURE"/>
# GS1 AI：
    # "applicationIdentifier": "37",
    # "formatString": "N2+N..8",
    # "label": "COUNT",
    # "description": "Count of trade items or trade item pieces contained in a logistic unit",

# Create EA (Each)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "EA",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "ea",
  "description": "Each - A single unit or piece",
  "gs1AI": "37"
}' | { read http_status; check_response $? "$http_status" "Create EA unit"; }

# Create BX (Box)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "BX",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "bx",
  "description": "Box - A container for packaging items",
  "gs1AI": "37"
}' | { read http_status; check_response $? "$http_status" "Create BX unit"; }

# Create PLT (Pallet)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "PLT",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "plt",
  "description": "Pallet - A flat transport structure to support goods",
  "gs1AI": "37"
}' | { read http_status; check_response $? "$http_status" "Create PLT unit"; }

# Create PK (Pack)
curl -X 'POST' \
  "${API_BASE_URL}/BffUnitsOfMeasure" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "uomId": "PK",
  "uomTypeId": "PACKAGE_TYPE_MEASURE",
  "abbreviation": "pk",
  "description": "Pack - A bundle or package of items",
  "gs1AI": "37"
}' | { read http_status; check_response $? "$http_status" "Create PK unit"; }



# Test Documents
echo -e "\n=== Testing Documents ===\n"

# Create document
curl -X 'POST' \
  "${API_BASE_URL}/BffDocuments" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "comments": "Quality certification document for organic vegetables batch #2024001", 
  "documentLocation": "https://example.com/docs/cert/2024001.pdf",
  "documentText": "Batch #2024001 passed QA inspections"
}' | { read http_status; check_response $? "$http_status" "Create document"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffSuppliers" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "supplierId": "SUPPLIER_001",
  "supplierName": "Vegetables Farm",
  "ggn": "4049929999999",
  "gln": "5420000000008",
  "externalId": "ORGANIC_FARM_01",
  "preferredCurrencyUomId": "USD",
  "description": "Organic Vegetables Farm - specializing in greenhouse vegetables with GLOBALG.A.P. certification"
}' | { read http_status; check_response $? "$http_status" "Create source supplier"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create source facility"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create destination facility"; }

# Create location for source facility
curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities/F001/Locations" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create location for source facility"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffFacilities/DC_FRESH/Locations" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "locationSeqId": "DC_FRESH-WH01-A01",
  "locationTypeEnumId": "STORAGE",
  "areaId": "WH01",
  "aisleId": "A01",
  "geoPointId": "GP002",
  "active": "Y"
}' | { read http_status; check_response $? "$http_status" "Create location for destination facility"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create raw item"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create tomato raw item"; }

# Test Lots
echo -e "\n=== Testing Lots ===\n"

# Create lot (Required by Receiving)
curl -X 'POST' \
  "${API_BASE_URL}/BffLots" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "lotId": "LOT20240315A",
  "gs1Batch": "LOT20240315A",
  "quantity": 100,
  "expirationDate": "2034-12-18T08:53:18.475Z"
}' | { read http_status; check_response $? "$http_status" "Create lot"; }

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
curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create lettuce raw item"; }

# Create organic cucumber
echo "Creating organic cucumber raw item..."
curl -X 'POST' \
  "${API_BASE_URL}/BffRawItems" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
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
}' | { read http_status; check_response $? "$http_status" "Create cucumber raw item"; }

# Create another lot (Required by second receiving item)
echo "Creating another lot..."
curl -X 'POST' \
  "${API_BASE_URL}/BffLots" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "lotId": "LOT20240315B",
  "gs1Batch": "LOT20240315B",
  "quantity": 100,
  "expirationDate": "2034-12-18T08:53:18.475Z"
}' | { read http_status; check_response $? "$http_status" "Create second lot"; }


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

# curl -X 'PUT' \
#   "${API_BASE_URL}/QaInspections/${QA_INSPECTION_ID}/_commands/QaInspectionAction" \
#   -H 'accept: */*' \
#   -H "X-TenantID: X" \
#   -H 'Content-Type: application/json' \
#   -s \
#   -w '%{http_code}\n' \
#   -o /dev/null \
#   -d '{
#   "commandId": "RANDOM197972935792396296493",
#   "value": "Approve",
#   "version": 0
# }' | { read http_status; check_response $? "$http_status" "Update QA inspection"; }


# # Update QA Inspection status
# echo "Updating QA Inspection..."
# curl -X 'PUT' \
#   "${API_BASE_URL}/BffQaInspections/${QA_INSPECTION_ID}" \
#   -H 'accept: application/json' \
#   -H 'Content-Type: application/json' \
#   -H "X-TenantID: X" \
#   -s \
#   -w '%{http_code}\n' \
#   -o /dev/null \
#   -d "{
#   \"statusId\": \"APPROVED\",
#   \"comments\": \"Additional verification required for temperature logs.\"
# }" | { read http_status; check_response $? "$http_status" "Update QA inspection"; }

# Update supplier business contact information
echo "Updating supplier business contact..."
curl -X 'PUT' \
  "${API_BASE_URL}/BffSuppliers/SUPPLIER_001/BusinessContact" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "businessName": "Fresh Organic Farms Office",
  "phoneNumber": "+1 415 555 0123",
  "physicalLocationAddress": "2500 Sand Hill Road",
  "city": "Menlo Park",
  "state": "CA",
  "zipCode": "94025"
}' | { read http_status; check_response $? "$http_status" "Update supplier business contact"; }


# 查询供应商类型枚举
echo "Querying supplier type enumeration..."
curl -X 'GET' \
  "${API_BASE_URL}/Enumerations?enumTypeId=SUPPLIER_TYPE_ENUM" \
  -H 'accept: application/json' \
  -H "X-TenantID: X"


# # 查询采购订单行项的未履行数量
# echo "Querying purchase order item outstanding quantity..."
# curl -X 'GET' \
#   "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/Items/${ORDER_ITEM_SEQ_ID}/OutstandingQuantity" \
#   -H 'accept: application/json' \
#   -H "X-TenantID: X"

# # 查询采购订单的某个产品的未履行数量
# echo "Querying purchase order product outstanding quantity..."
# curl -X 'GET' \
#   "${API_BASE_URL}/BffPurchaseOrders/${ORDER_ID}/getOutstandingQuantityByProduct?productId=${PRODUCT_ID}" \
#   -H 'accept: application/json' \
#   -H "X-TenantID: X"


# # 查询“收货单”信息，并要求返回“收货行项”关联的“采购订单行项”的未履行数量。
# # 注意设置参数 `includesOutstandingOrderQuantity=true`
curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts/${RECEIVING_ID}?includesOutstandingOrderQuantity=true" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X"

# 查询“收货单”信息，并要求返回 QA 检验状态
# 注意设置参数 `derivesQaInspectionStatus=true`
echo "Querying receiving document QA inspection status..."
curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts/${RECEIVING_ID}?includesOutstandingOrderQuantity=true&derivesQaInspectionStatus=true" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X"

# 查询收货单列表，并要求“连带”返回每个收货单的 QA 检验状态
# 注意设置参数 `derivesQaInspectionStatus=true`
# 不建议滥用，可能会导致性能问题。
# 也许后面可以考虑提供一个“批量查询收货单的 QA 检验状态”的接口，由前端组合使用。
curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts?derivesQaInspectionStatus=true" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X"
