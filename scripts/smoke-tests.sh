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
  "gs1AI": "3102"
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

# Create supplier for destination facility
curl -X 'POST' \
  "${API_BASE_URL}/BffSuppliers" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "supplierId": "FRESH_MART_DC",
  "supplierName": "Fresh Mart Distribution",
  "ggn": "4049929999998",
  "gln": "5420000000009",
  "externalId": "FRESH_MART_DC",
  "preferredCurrencyUomId": "USD",
  "description": "Fresh Mart main distribution center"
}' | { read http_status; check_response $? "$http_status" "Create destination supplier"; }

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

# Test Receiving
echo -e "\n=== Testing Receiving ===\n"

# Create receipt
curl -X 'POST' \
  "${API_BASE_URL}/BffReceipts" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d '{
  "partyIdTo": "FRESH_MART_DC",
  "partyIdFrom": "SUPPLIER_001",
  "originFacilityId": "F001",
  "destinationFacilityId": "DC_FRESH",
  "primaryOrderId": "PO2024031501",
  "receivingItems": [
    {
      "productId": "ORGANIC_TOMATO_01",
      "lotId": "LOT20240315A",
      "locationSeqId": "DC_FRESH-WH01-A01",
      "itemDescription": "Organic Tomato",
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
}' | { read http_status; check_response $? "$http_status" "Create receipt"; }

# Query Receipts
echo "Querying Receipts..."
response=$(curl -X 'GET' \
  "${API_BASE_URL}/BffReceipts?page=0&size=20&documentIdOrItem=ORGANIC_TOMATO_01" \
  -H 'accept: application/json' \
  -H "X-TenantID: X" \
  -s)
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


# Update QA Inspection status
echo "Updating QA Inspection..."
curl -X 'PUT' \
  "${API_BASE_URL}/BffQaInspections/${QA_INSPECTION_ID}" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -s \
  -w '%{http_code}\n' \
  -o /dev/null \
  -d "{
  \"statusId\": \"APPROVED\",
  \"comments\": \"Additional verification required for temperature logs.\"
}" | { read http_status; check_response $? "$http_status" "Update QA inspection"; }

