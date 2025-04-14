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

### 6. 关于原材料状态的激活和禁用

原来原材料有激活（禁用）单个原材料、批量激活原材料、批量禁用原材料结构

以上接口全部改用 “BffProducts” 路径前缀的接口。

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
  "supplierId": "13J38CJZZNEEVH3M80",
  "internalId": "Lot No T",
  "productId":"14AG8L4GM6EKAWK277"
}'
```

操作成功直接返回该批次号的Id，如：

```shell
"14EHM643F9U3FXJCR8"
```

添加策略：

如果指定供应商的批次号已经存在那么直接返回LotId;

如果不存在那么将添加给批次号并返回 LotId.

### 2. 根据条件查询批次号信息

新增查询条件：供应商Id，如：

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffLots?page=0&size=20&productId=14AG8L4GM6EKAWK277&supplierId=13J38CJZZNEEVH3M80&keyword=Lot' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

返回结果如下：

```json
{
  "content": [
    {
      "lotId": "14EHM643F9U3FXJCR8",
      "productId": "14AG8L4GM6EKAWK277",
      "supplierId": "13J38CJZZNEEVH3M80",
      "active": "Y",
      "internalId": "Lot No T"
    }
  ],
  "totalElements": 1,
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
      "productId": "14AG8L4GM6EKAWK277",
      "productName": "productName",
      "quantityUomId": "quantityUomId",
      "quantityIncluded": 100,
      "caseUomId": "caseUomId1",
      "supplierId": "13HDSG4J6BKYPHJVZ0",
      "supplierName": "v25031701",
      "facilityId": "13LNXR6X497W03AP81",
      "facilityName": "freshpointvendor1facilityname",
      "quantityOnHandTotal": 1399
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```

### 2. 根据条件查询半成品库存

可以根据半成品类型、供应商 Id、产品 Id、仓库 Id 来查询库存：

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/WIPs?page=0&size=20&productTypeId=RAC_WIP&productName=blueforceitem1&productId=13XM0K65JP235EMN65&facilityId=13XM4J6CJBWD6FK64B' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

其中 productTypeId 为半成品类型，可取值范围：

    * RAC_WIP
    * RTE_WIP
    * PACKED_WIP

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

### 3. 原材料产品库存明细

在手持终端可以按照产品名称、产品Id、供应商Id以及仓库Id进行半成品的库存查询，查询结果按照供应商，仓库，产品汇总库存总数量。
那这些库存按照批次号拆分之后成为库存明细。

客户端需要传递上述查询获得结果列表，所选中的库存信息中的产品Id、供应商Id以及仓库Id来如下调用接口：

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/RawItems/14AG8L4GM6EKAWK277/InventoryItems?page=0&size=20&supplierId=13HDSG4J6BKYPHJVZ0&facilityId=13LNXR6X497W03AP81' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

将会得到该库存的分批次列表：

```json
{
  "content": [
    {
      "productId": "14AG8L4GM6EKAWK277",
      "lotId": "14AMK06WKP804F2VN0",
      "lotNo": "Lot No3",
      "receivingDocumentId": "RC2025040100002",
      "receivedAt": "2025-03-31T21:53:08.842486Z",
      "orderId": "blueforcepo1",
      "qaStatusId": "REJECTED",
      "locationCode": "blureforcewh11",
      "quantityOnHandTotal": 400,
      "quantityUomId": "quantityUomId",
      "quantityIncluded": 100,
      "caseUomId": "caseUomId1"
    },
    {
      "productId": "14AG8L4GM6EKAWK277",
      "lotId": "14AGPQ39F501D6RAZD",
      "lotNo": "Lot No1",
      "receivingDocumentId": "RC2025040100001",
      "receivedAt": "2025-03-31T21:46:13.509342Z",
      "orderId": "blueforcepo1",
      "qaStatusId": "APPROVED",
      "locationCode": "13LNXR6X497W03AP81_DEFAULT",
      "quantityOnHandTotal": 999,
      "quantityUomId": "quantityUomId",
      "quantityIncluded": 100,
      "caseUomId": "caseUomId1"
    }
  ],
  "totalElements": 2,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```

### 4. 根据产品类型返回产品列表（支持分页）

在进行Inventory Adjustment时，先选择产品类型得到产品下拉列表，由于使用原条件查询查询接口返回的信息太多（需要联合查询的表也多）所以响应速度肯定较慢，
所以特别提供该接口。

使用方式：

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffProducts/GetProductsForAdjustInventory?page=0&size=2&productTypeId=RAW_MATERIAL' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

返回结果举例如下：

```json
{
  "content": [
    {
      "productId": "13HDUP50Y9WHFMAMKP",
      "productName": "i25031701",
      "smallImageUrl": "f19bae8f-dbf4-4722-a565-9a63ae9e5f8f",
      "productTypeId": "RAW_MATERIAL",
      "quantityUomId": "KG"
    },
    {
      "productId": "13L16H5CJJCA0JBUCQ",
      "productName": "i25032001",
      "smallImageUrl": "dda1494f-7224-4e4f-b7b3-6c7b49d3a9bc",
      "productTypeId": "RAW_MATERIAL",
      "quantityUomId": "KG"
    }
  ],
  "totalElements": 20,
  "size": 2,
  "number": 0,
  "totalPages": 10
}
```

其中最重要的应该是 productId 和 ProductName.

### 5.根据产品类型和产品名称/Product Number 关键字查询产品（支持分页）

库存查询中，在过滤库存查询条件时，可以指定产品，而产品的选定是通过产品类型、产品名称或者Product Number的关键字来过滤的，特此提供该接口。

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffProducts/GetProductsByKeyword?page=0&size=2&productTypeId=RAW_MATERIAL&productKeyword=freshpoint' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

其中查询参数 productTypeId 为产品类型，productKeyword 是为产品名称或 Product Number 指定的关键字（支持模糊查询）。
返回结果举例如下：

```json
{
  "content": [
    {
      "productId": "14AG8L4GM6EKAWK277",
      "productName": "productName",
      "smallImageUrl": "smallImageUrl",
      "mediumImageUrl": "mediumImageUrl",
      "largeImageUrl": "largeImageUrl",
      "productTypeId": "RAW_MATERIAL",
      "internalId": "internalId1",
      "quantityUomId": "quantityUomId"
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```

其中较重要的属性为：productId,productName,smallImageUrl 以及 internalId（Product number）.

### 6. 根据半成品 Id 和 LotId 获取查询库存

在客户端进行库存调整时，总是先根据产品 Id 和 LotId 来查询库存，得到库存列表，因此提供该接口。

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/WIPs/GroupByProductAndLot?page=0&size=20&productId=141L0K7AH7DL6W4948&lotId=14AMK06WKP804F2VN0' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

返回结果举例如下：

```json
{
  "content": [
    {
      "inventoryItemId": "54f91257311f3f3d44bcd89e4cdc5fc2",
      "productId": "141L0K7AH7DL6W4948",
      "productName": "freshpointitem15",
      "quantityUomId": "KG",
      "quantityIncluded": 10,
      "caseUomId": "BOX",
      "facilityId": "14DAZ36WRKMFF4VQR8",
      "facilityName": "facilityNameX",
      "facilityInternalId": "Facility Number K",
      "lotId": "14AMK06WKP804F2VN0",
      "locationSeqId": "13XM9019U6979XVY4U",
      "locationName": "blueforce1",
      "locationCode": "blureforcewh11",
      "quantityOnHandTotal": 400
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```

* inventoryItemId 库存Id
* productId 产品Id
* productName 产品名称
* quantityUomId 产品主数量单位
* quantityIncluded 一个包装(caseUomId)产品按主数量单位来计算的数量
* caseUomId 包装单位
* facilityId 仓库Id
* facilityName 仓库名称
* facilityInternalId 仓库内部标识（Warehouse Number）
* lotId 批次Id
* locationSeqId 仓位Id
* locationName 仓位名称
* locationCode 仓位内部标识(Location Number)
* quantityOnHandTotal 库存数量

### 7. 根据原材料 Id 和 LotId 获取查询库存

在客户端进行库存调整时，总是先根据产品 Id 和 LotId 来查询库存，得到库存列表，因此提供该接口。

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/RawItems/GroupByProductAndLot?page=0&size=20&productId=14AG8L4GM6EKAWK277&lotId=14AGPQ39F501D6RAZD' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

返回结果举例如下：

```json
{
  "content": [
    {
      "inventoryItemId": "inverntory_itemId",
      "productId": "14AG8L4GM6EKAWK277",
      "productName": "productName",
      "quantityUomId": "quantityUomId",
      "quantityIncluded": 100,
      "caseUomId": "caseUomId1",
      "facilityId": "14DAZ36WRKMFF4VQR8",
      "facilityName": "facilityNameX",
      "facilityInternalId": "Facility Number K",
      "lotId": "14AGPQ39F501D6RAZD",
      "locationSeqId": "13LNXR6X497W03AP81_DEFAULT",
      "locationName": "-",
      "locationCode": "13LNXR6X497W03AP81_DEFAULT",
      "quantityOnHandTotal": 999
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

## 五、Customer 相关接口

相比较 Vendor(Supplier)来说有如下变化（其实我不知道是否也要给 Customer 附带 Facilities）：

* 类型从 SUPPLIER 变为 CUSTOMER
* supplierId->customerId
* supplierName->customerName
* supplierShortName->customerShortName
* supplierTypeEnumId->customerTypeEnumId
* supplierProductTypeDescription->customerProductTypeDescription

### 1. 创建 Customer

举例说明(添加一个 Customer,附带同时创建两个 Facilities)：

```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffCustomers' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "customerName": "CustomerNameX",
  "ggn": "8511684464926",
  "gln": "5251637539544",
  "externalId": "externalId",
  "preferredCurrencyUomId": "pcUomId",
  "description": "description",
  "businessContacts": [
    {
      "businessName": "businessName",
      "phoneNumber": "phoneNumber",
      "physicalLocationAddress": "physicalLocationAddress",
      "city": "city",
      "state": "state",
      "zipCode": "zipCode",
      "country": "country",
      "stateProvinceGeoId": "CA",
      "countryGeoId": "USA",
      "email": "email",
      "contactRole": "contactRole"
    }
  ],
  "customerShortName": "customerShortName",
  "taxId": "taxId",
  "gs1CompanyPrefix": "gs1CompanyPrefix",
  "internalId": "Customer Numberddd",
  "tpaNumber": "tpaNumber",
  "customerTypeEnumId": "customerTypeEnumId",
  "customerProductTypeDescription": "SPTypeDescription",
  "certificationCodes": "certificationCodes",
  "bankAccountInformation": "bankAccountInformation",
  "telephone": "telephone",
  "email": "email",
  "webSite": "webSite",
  "facilities": [
    {
      "parentFacilityId": "parentFacilityId",
      "facilityName": "facilityNameQ",
      "facilitySize": 10,
      "facilitySizeUomId": "facilitySizeUomId",
      "description": "description",
      "geoPointId": "geoPointId",
      "geoId": "geoId",
      "active": "active",
      "gln": "gln1",
      "ffrn": "ffrn2",
      "facilityLevel": 20,
      "internalId": "tFacility Number K",
      "sequenceNumber": 30,
      "businessContacts": [
        {
          "businessName": "businessName",
          "phoneNumber": "phoneNumber",
          "physicalLocationAddress": "physicalLocationAddress",
          "city": "city",
          "state": "state",
          "zipCode": "zipCode",
          "country": "country",
          "stateProvinceGeoId": "CA",
          "countryGeoId": "USA",
          "email": "email",
          "contactRole": "contactRole"
        }
      ]
    },    
    {
      "parentFacilityId": "parentFacilityId2",
      "facilityName": "facilityNamexc2",
      "facilitySize": 100,
      "facilitySizeUomId": "facilitySizeUomId2",
      "description": "description2",
      "geoPointId": "geoPointId2",
      "geoId": "geoId2",
      "active": "active",
      "gln": "gln2",
      "ffrn": "ffrn3",
      "facilityLevel": 120,
      "internalId": "cFacility NumberPP",
      "sequenceNumber": 130,
      "businessContacts": [
        {
          "businessName": "businessName2",
          "phoneNumber": "phoneNumber2",
          "physicalLocationAddress": "physicalLocationAddress",
          "city": "city",
          "state": "state",
          "zipCode": "zipCode",
          "country": "country",
          "stateProvinceGeoId": "CA",
          "countryGeoId": "USA",
          "email": "email",
          "contactRole": "contactRole"
        }
      ]
    }
  ]
}'
```

创建成功直接返回 Customer 的 Id:

```json
"14DA853H1Z0ET6KUNZ"
```

### 2. 根据 Id 查询 Customer 详情

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffCustomers/14DA853H1Z0ET6KUNZ?includesFacilities=true' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

得到结果（举例）如下：

```json
{
  "customerId": "14DA853H1Z0ET6KUNZ",
  "customerName": "customerShortName",
  "ggn": "8511684464926",
  "gln": "5251637539544",
  "externalId": "externalId",
  "preferredCurrencyUomId": "pcUomId",
  "description": "description",
  "statusId": "ACTIVE",
  "businessContacts": [
    {
      "phoneNumber": "phoneNumber",
      "physicalLocationAddress": "physicalLocationAddress",
      "city": "city",
      "state": "California",
      "zipCode": "zipCode",
      "country": "United States",
      "stateProvinceGeoId": "CA",
      "countryGeoId": "USA",
      "email": "email"
    }
  ],
  "customerShortName": "customerShortName",
  "taxId": "taxId",
  "gs1CompanyPrefix": "gs1CompanyPrefix",
  "internalId": "Customer Numberddd",
  "tpaNumber": "tpaNumber",
  "customerTypeEnumId": "customerTypeEnumId",
  "customerProductTypeDescription": "SPTypeDescription",
  "certificationCodes": "certificationCodes",
  "bankAccountInformation": "bankAccountInformation",
  "telephone": "telephone",
  "email": "email",
  "webSite": "webSite",
  "active": "Y",
  "facilities": [
    {
      "facilityId": "14DA8562PEYCZEDMDB",
      "parentFacilityId": "parentFacilityId2",
      "ownerPartyId": "14DA853H1Z0ET6KUNZ",
      "facilityName": "facilityNamexc2",
      "facilitySize": 100,
      "facilitySizeUomId": "facilitySizeUomId2",
      "description": "description2",
      "geoPointId": "geoPointId2",
      "geoId": "geoId2",
      "active": "Y",
      "gln": "gln2",
      "ffrn": "ffrn3",
      "facilityLevel": 120,
      "internalId": "cFacility NumberPP",
      "sequenceNumber": 130,
      "businessContacts": [
        {
          "phoneNumber": "phoneNumber2",
          "physicalLocationAddress": "physicalLocationAddress",
          "city": "city",
          "state": "California",
          "zipCode": "zipCode",
          "country": "United States",
          "stateProvinceGeoId": "CA",
          "countryGeoId": "USA",
          "email": "email"
        }
      ]
    },
    {
      "facilityId": "14DA8560AJJKRHVNGJ",
      "parentFacilityId": "parentFacilityId",
      "ownerPartyId": "14DA853H1Z0ET6KUNZ",
      "facilityName": "facilityNameQ",
      "facilitySize": 10,
      "facilitySizeUomId": "facilitySizeUomId",
      "description": "description",
      "geoPointId": "geoPointId",
      "geoId": "geoId",
      "active": "Y",
      "gln": "gln1",
      "ffrn": "ffrn2",
      "facilityLevel": 20,
      "internalId": "tFacility Number K",
      "sequenceNumber": 30,
      "businessContacts": [
        {
          "phoneNumber": "phoneNumber",
          "physicalLocationAddress": "physicalLocationAddress",
          "city": "city",
          "state": "California",
          "zipCode": "zipCode",
          "country": "United States",
          "stateProvinceGeoId": "CA",
          "countryGeoId": "USA",
          "email": "email"
        }
      ]
    }
  ]
}
```







