# 第二阶段API

## 一、原材料相关API

参考 Swagger :

http://15.156.171.105:8001/api/swagger-ui/index.html

### 1. 添加原材料

变化为：部分在原材料实体中的属性转移到供应与产品的关联关系实体中，如：
brandName（Brand）,Gtin（GTIN），IndividualsPerPackage
（Quantity Per Package）,QuantityIncluded
（Gross Weight Per Package）
,ProductWeight（Net Weight Per Package）,CaseUomId（Packaging Type）,
OrganicCertifications（Organic Certification）,MaterialCompositionDescription（Material Composition）,
CountryOfOrigin（Country of Origin）,CertificationCodes（Certification code）等。

并增加 suppliers 属性用来表示原材料与多个供应商关联：

```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffRawItems' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "productName": "productName",
  "internalName": "internalName",
  "description": "description",
  "smallImageUrl": "smallImageUrl",
  "mediumImageUrl": "mediumImageUrl",
  "largeImageUrl": "largeImageUrl",
  "quantityUomId": "quantityUomId",
  "weightUomId": "weightUomId",
  "shippingWeight": 10,
  "productWeight": 20,
  "heightUomId": "heightUomId",
  "productHeight": 30,
  "shippingHeight": 40,
  "widthUomId": "widthUomId",
  "productWidth": 50,
  "shippingWidth": 60,
  "depthUomId": "depthUomId",
  "productDepth": 70,
  "shippingDepth": 80,
  "diameterUomId": "diameterUomId",
  "productDiameter": 90,
  "defaultShipmentBoxTypeId": "SBoxTypeId",
  "internalId": "internalId1",
  "produceVariety": "produceVariety",
  "hsCode": "hsCode",
  "shelfLifeDescription": "shelfLifeDescription",
  "handlingInstructions": "handlingInstructions",
  "dimensionsDescription": "dimensionsDescription",
  "suppliers": [
    {
      "supplierId": "FDaHai",
      "brandName": "brandName",
      "gtin": "gtin",
      "quantityIncluded": 100,
      "productWeight": 200,
      "caseUomId": "caseUomId1",
      "organicCertifications": "organicCertifications",
      "materialCompositionDescription": "materialCompositionDescription",
      "countryOfOrigin": "countryOfOrigin",
      "certificationCodes": "certificationCodes",
      "individualsPerPackage": 300
    },    
    {
      "supplierId": "ODaHaisupplierId",
      "brandName": "brandName2",
      "gtin": "gtin2",
      "quantityIncluded": 1001,
      "productWeight": 2001,
      "caseUomId": "caseUomId2",
      "organicCertifications": "organicCertifications2",
      "materialCompositionDescription": "materialCompositionDescription2",
      "countryOfOrigin": "countryOfOrigin2",
      "certificationCodes": "certificationCodes2",
      "individualsPerPackage": 3001
    }
  ]
}'
```
添加成功将返回原材料的Id。如：148H6N2U8Q40JKPS9Q.

### 2. 更新原材料


```shell
curl -X 'PUT' \
  'http://localhost:8001/api/BffRawItems/{productId}' \
  -H 'accept: */*' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "productName": "XproductName",
  "internalName": "XinternalName",
  "description": "Xdescription",
  "smallImageUrl": "XsmallImageUrl",
  "mediumImageUrl": "XmediumImageUrl",
  "largeImageUrl": "XlargeImageUrl",
  "quantityUomId": "XquantityUomId",
  "weightUomId": "XweightUomId",
  "shippingWeight": 910,
  "productWeight": 920,
  "heightUomId": "XheightUomId",
  "productHeight": 930,
  "shippingHeight": 940,
  "widthUomId": "XwidthUomId",
  "productWidth": 950,
  "shippingWidth": 960,
  "depthUomId": "XdepthUomId",
  "productDepth": 970,
  "shippingDepth": 980,
  "diameterUomId": "XdiameterUomId",
  "productDiameter": 990,
  "defaultShipmentBoxTypeId": "XSBoxTypeId",
  "internalId": "XinternalId1",
  "produceVariety": "XproduceVariety",
  "hsCode": "XhsCode",
  "shelfLifeDescription": "XshelfLifeDescription",
  "handlingInstructions": "XhandlingInstructions",
  "dimensionsDescription": "XdimensionsDescription",
  "suppliers": [
    {
      "supplierId": "13B99T7LZLMFDHPB8B",
      "brandName": "XbrandName",
      "gtin": "Xgtin",
      "quantityIncluded": 8100,
      "productWeight": 8200,
      "caseUomId": "XcaseUomId1",
      "organicCertifications": "XorganicCertifications",
      "materialCompositionDescription": "XmaterialCompositionDescription",
      "countryOfOrigin": "XcountryOfOrigin",
      "certificationCodes": "XcertificationCodes",
      "individualsPerPackage": 8300
    },    
    {
      "supplierId": "ODaHaisupplierId",
      "brandName": "CbrandName2",
      "gtin": "Cgtin2",
      "quantityIncluded": 7001,
      "productWeight": 7001,
      "caseUomId": "CcaseUomId2",
      "organicCertifications": "CorganicCertifications2",
      "materialCompositionDescription": "CmaterialCompositionDescription2",
      "countryOfOrigin": "CcountryOfOrigin2",
      "certificationCodes": "CcertificationCodes2",
      "individualsPerPackage": 7001
    }
  ]
}'
```
路径参数 {productId} 为原材料 Id;

另外注意：

suppliers 数组为该原材料最终所关联的供应商与产品之间的关联关系列表。

如果原来有，更新的时候某个关联关系没有了，并不是将其删除而是将其的可用状态改为“禁用”；

如果原来有，更新的时候还有，那么按最新提供的信息更新该关联关系，不改变其可用状态；

如果原来没有，更新的时候新增，那么将创建新的关联关系，可用状态为“启用”。

也就是说不存在删除关联关系，只有禁用。

更新成功将会得到 200 的响应码。

### 3. 根据原材料 Id 获取原材料的详情信息
请求调用方式如下：
```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffRawItems/{productId}' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
路径参数 {productId} 为原材料 Id.

返回结果举例如下：
```json
{
  "productId": "14AG8L4GM6EKAWK277",
  "productName": "productName",
  "description": "description",
  "smallImageUrl": "smallImageUrl",
  "mediumImageUrl": "mediumImageUrl",
  "largeImageUrl": "largeImageUrl",
  "quantityUomId": "quantityUomId",
  "piecesIncluded": 1,
  "weightUomId": "weightUomId",
  "shippingWeight": 10,
  "heightUomId": "heightUomId",
  "productHeight": 30,
  "shippingHeight": 40,
  "widthUomId": "widthUomId",
  "productWidth": 50,
  "shippingWidth": 60,
  "depthUomId": "depthUomId",
  "productDepth": 70,
  "shippingDepth": 80,
  "diameterUomId": "diameterUomId",
  "productDiameter": 90,
  "active": "Y",
  "defaultShipmentBoxTypeId": "SBoxTypeId",
  "internalId": "internalId1",
  "produceVariety": "produceVariety",
  "hsCode": "hsCode",
  "shelfLifeDescription": "shelfLifeDescription",
  "handlingInstructions": "handlingInstructions",
  "dimensionsDescription": "dimensionsDescription",
  "suppliers": [
    {
      "productId": "14AG8L4GM6EKAWK277",
      "supplierId": "13HDSG4J6BKYPHJVZ0",
      "supplierShortName": "v25031701",
      "version": 0,
      "currencyUomId": "USD",
      "minimumOrderQuantity": 0,
      "availableFromDate": "2025-04-10T08:43:24.509842Z",
      "availableThruDate": "2125-04-10T08:43:24.509842Z",
      "brandName": "brandName",
      "gtin": "gtin",
      "quantityIncluded": 100,
      "productWeight": 200,
      "active": "Y",
      "caseUomId": "caseUomId1",
      "organicCertifications": "organicCertifications",
      "materialCompositionDescription": "materialCompositionDescription",
      "countryOfOrigin": "countryOfOrigin",
      "certificationCodes": "certificationCodes",
      "individualsPerPackage": 300
    },
    {
      "productId": "14AG8L4GM6EKAWK277",
      "supplierId": "13J38CJZZNEEVH3M80",
      "supplierShortName": "v25031801",
      "version": 0,
      "currencyUomId": "USD",
      "minimumOrderQuantity": 0,
      "availableFromDate": "2025-04-10T08:43:24.509842Z",
      "availableThruDate": "2125-04-10T08:43:24.509842Z",
      "brandName": "brandName2",
      "gtin": "gtin2",
      "quantityIncluded": 1001,
      "productWeight": 2001,
      "active": "Y",
      "caseUomId": "caseUomId2",
      "organicCertifications": "organicCertifications2",
      "materialCompositionDescription": "materialCompositionDescription2",
      "countryOfOrigin": "countryOfOrigin2",
      "certificationCodes": "certificationCodes2",
      "individualsPerPackage": 3001
    }
  ]
}
```
客户端并不需要关注 suppliers 列表中的：version,currencyUomId,minimumOrderQuantity,availableFromDate,availableThruDate等属性。

### 4. 禁用原材料的供应商与产品之间的关联关系

请求调用方式如下：
```shell
curl -X 'PUT' \
  'http://localhost:8001/api/BffRawItems/{productId}/SupplierRawItems/{supplierId}/deactivate' \
  -H 'accept: */*' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{}'
```
路径参数 {productId} 为原材料 Id,{supplierId} 为供应商 Id.

禁用策略：

不管当前该关联关系的可用状态是“禁用”还是“启用”，都将其可用状态设置为“禁用”。


### 5. 启用原材料的供应商与产品之间的关联关系

请求调用方式如下：
```shell
curl -X 'PUT' \
  'http://localhost:8001/api/BffRawItems/{productId}/SupplierRawItems/{supplierId}/active' \
  -H 'accept: */*' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{}'
```
路径参数 {productId} 为原材料 Id,{supplierId} 为供应商 Id.

禁用策略：

不管当前该关联关系的可用状态是“禁用”还是“启用”，都将其可用状态设置为“启用”。

## 二、批次号相关API

### 1. 添加批次号
添加批次号时需要传递供应商的Id（SupplierId）和批次号(internalId)：
```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffLots' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "supplierId": "13HDSG4J6BKYPHJVZ0",
  "internalId": "Lot No"
}'
```
操作成功直接返回该批次号的Id，如：
```shell
14AGQF7D2VWBEZH298
```
添加策略：

如果指定供应商的批次号已经存在那么直接返回LotId;

如果不存在那么将添加给批次号并返回 LotId.

### 2. 根据条件查询批次号信息

新增查询条件：供应商Id，如：
```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffLots?page=0&size=20&supplierId=13HDSG4J6BKYPHJVZ0' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
返回结果如下：
```json
{
  "content": [
    {
      "lotId": "14AGPQ39F501D6RAZD",
      "supplierId": "13HDSG4J6BKYPHJVZ0",
      "active": "Y",
      "internalId": "Lot No1"
    },
    {
      "lotId": "14AGQF7D2VWBEZH298",
      "supplierId": "13HDSG4J6BKYPHJVZ0",
      "active": "Y",
      "internalId": "Lot No"
    }
  ],
  "totalElements": 2,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```
lotId 为批次号的 Id

supplierId 为供应商 Id

internalId 为批次号

## 三、库存相关接口
### 1. 根据条件查询原材料库存

可以根据产品名称（支持模糊查询）、供应商 Id、产品 Id、仓库 Id 来查询库存：
```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/RawItems?page=0&size=20&productName=blueforceitem1&supplierId=13HDSG4J6BKYPHJVZ0&productId=13XM0K65JP235EMN65&facilityId=13XM4J6CJBWD6FK64B' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
其中 productName 为产品名称（支持模糊查询）；

supplierId 为供应商 Id；

productId 为产品 Id；

facilityId 为仓库 Id.

返回结果举例如下：
```json
{
  "content": [
    {
      "productId": "13XM0K65JP235EMN65",
      "productName": "blueforceitem1",
      "quantityUomId": "LB",
      "supplierId": "13HDSG4J6BKYPHJVZ0",
      "supplierName": "v25031701",
      "facilityId": "13XM4J6CJBWD6FK64B",
      "facilityName": "blureforcewh1",
      "quantityOnHandTotal": 400
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```
1. 根据条件查询半成品库存

可以根据产品类型、供应商 Id、产品 Id、仓库 Id 来查询库存：
```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/RawItems?page=0&size=20&productName=blueforceitem1&supplierId=13HDSG4J6BKYPHJVZ0&productId=13XM0K65JP235EMN65&facilityId=13XM4J6CJBWD6FK64B' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
其中 productTypeId 为产品类型，可取值范围：

    * RAC_WIP
    * RTE_WIP
    * PACKED_WIP

supplierId 为供应商 Id；

productId 为产品 Id；

facilityId 为仓库 Id.

返回结果举例如下：
```json
{
  "content": [
    {
      "productId": "13XM0K65JP235EMN65",
      "productName": "blueforceitem1",
      "quantityUomId": "LB",
      "supplierId": "13HDSG4J6BKYPHJVZ0",
      "facilityId": "13XM4J6CJBWD6FK64B",
      "facilityName": "blureforcewh1",
      "quantityOnHandTotal": 400
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```

## 四、产品相关接口

* 原以“BffRawItems”开头的接口全部用来满足原材料（RAW_MATERIAL）的管理；
* 增加以“BffProducts”开头的接口来满足半成品和成品的管理；
* 半成品和成品的产品类型（productTypeId）种类如下：

    * 半成品包括：RAC_WIP、RTE_WIP、PACKED_WIP 三种
    * 成品为 FINISHED_GOOD
* 总的指导原则：除了新建原材料，更新原材料，查询原材料详情信息之外其余都是用统一的“BffProducts”接口。






