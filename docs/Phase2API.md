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

添加批次号时需要传递供应商的Id（SupplierId）、产品Id（productId）和批次号(internalId)：

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

### 2. 根据条件查询产品（非原材料）库存

可以根据产品类型（除原材料外）、产品 Id、仓库 Id 来查询库存：

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/Products?page=0&size=20&productTypeId=RAC_WIP&productName=blueforceitem1&productId=13XM0K65JP235EMN65&facilityId=13XM4J6CJBWD6FK64B' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```

其中 productTypeId 为产品类型，可取值范围：

    * RAC_WIP
    * RTE_WIP
    * PACKED_WIP
    * FINISHED_GOOD

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

### 4. 半成品库存明细

BOM做完提供

### 5. 成品库存明细

生产完成或者发货确定了客户的时候才能提供

### 6. 根据产品类型返回产品列表（支持分页）

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
      "internalId": "25031701",
      "quantityUomId": "KG"
    },
    {
      "productId": "13L16H5CJJCA0JBUCQ",
      "productName": "i25032001",
      "smallImageUrl": "dda1494f-7224-4e4f-b7b3-6c7b49d3a9bc",
      "productTypeId": "RAW_MATERIAL",
      "internalId": "25032001",
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

### 7.根据产品类型和产品名称/Product Number 关键字查询产品（支持分页）

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

### 8. 根据产品(除原材料外) Id 和 LotId 查询库存（支持分页）

在客户端进行库存调整时，总是先根据产品 Id 和 LotId 来查询库存，得到库存列表，因此提供该接口。

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/Products/GroupByProductAndLot?page=0&size=20&productId=141L0K7AH7DL6W4948&lotId=14AMK06WKP804F2VN0' \
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

### 9. 根据原材料 Id 和 LotId 获取查询库存（支持分页）

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

### 10. 库存调整-位置调整

对应手持终端的 "Location adjustment".

```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffInventoryTransfers/LocationAdjustment' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "inventoryItemId": "daa0afcf71a678c53248843173cdecf7",
  "adjustmentQuantity": 30,
  "facilityIdTo": "13LN5R1QKHUWBKN3PS",
  "locationSeqIdTo": "13LN6K2WGW493HJ9SF",
  "comments": "comments"
}'
```

* inventoryItemId:所选中库存的Id;
* adjustmentQuantity:转移数量，必须大于0;
* facilityIdTo:目的仓库Id;
* locationSeqIdTo:目的仓位Id;
* comments:备注信息

操作成功后直接返回库存转移【之前】目的仓位的库存信息：

```json
{
  "inventoryItemId": "c7fc0ac13f5e74c904ed498a031c8f27",
  "productId": "141L0K7AH7DL6W4948",
  "productName": "freshpointitem15",
  "quantityUomId": "KG",
  "facilityId": "13M82U5EQYQXS101SJ",
  "facilityName": "w2503201",
  "facilityInternalId": "25032201",
  "lotId": "14AMK06WKP804F2VN0",
  "locationSeqId": "13M8AD4G2KD3WAY1JE",
  "locationName": "l25032201",
  "locationCode": "25032201",
  "quantityOnHandTotal": 64
}
```

注意：如果 quantityOnHandTotal 为 0，表示当前库位不存在相同批次的产品。

```json
{
  "productId": "141L0K7AH7DL6W4948",
  "facilityId": "13QPR94UDK1ALTS3F7",
  "facilityName": "freshpointwh13",
  "facilityInternalId": "freshpointvendor1number3",
  "locationSeqId": "13QPT9H0W107CQLEPV",
  "locationName": "freshpointl122",
  "locationCode": "freshpointl1number3",
  "quantityOnHandTotal": 0
}
```

也就不会存在 inventoryItemId、productName、quantityUomId 这些不太重要的信息。

productName、quantityUomId 等本身就可以从源信息获取。

### 11. 库存调整-数量调整、退回供应商、作废
库存调整类型为数量调整、退回供应商、作废时，使用以下统一接口。

针对这几种调整库存的方式，提供了 varianceReasonId 字段进行区分，注意不同的调整库存方式有不同的调整数量要求：

* 数量调整：QUANTITY_ADJUSTMENT，数量：不能为 0，可正可负
* 作废：SCRAP，数量：必须大于 0； 
* 返回供应商：RETURN_TO_VENDOR，数量：必须大于 0

1. 数量调整：
```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffPhysicalInventories' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "generalComments": "generalComments",
  "variances": [
    {
      "inventoryItemId": "c39293f3420b8d95225e2ea76e520d07",
      "varianceReasonId": "QUANTITY_ADJUSTMENT",
      "quantityOnHandVar": -4,
      "comments": "comments"
    }
  ]
}'
```
参数说明：

generalComments 为对整个调整的备注说明；

variances 为调整的明细，按照当前的设置，该列表只有一条数据，但是为了以后扩展这里使用列表形式；

inventoryItemId 为要调整的库存的 Id，经查询获取；

varianceReasonId 调整类型，数量调整为：QUANTITY_ADJUSTMENT；

quantityOnHandVar 调整数量，不能为 0，负数为减少库存，正数为增加库存，此处与设计不同，设计中为调整后的库存，但是为了接口的统一，这里改为调整数量；

comments：针对改库存的说明，在只有一条数据的情况下可以与 generalComments 相同。

操作成功后返回此次操作的 Id:
```json
"14GBUL4QQG1CNA8J8P"
```
用不到的情况下可以忽略该返回值。
2. 作废
```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffPhysicalInventories' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "generalComments": "批量作废",
  "variances": [
    {
      "inventoryItemId": "c39293f3420b8d95225e2ea76e520d07",
      "varianceReasonId": "SCRAP",
      "quantityOnHandVar": 4,
      "comments": "作废"
    }
  ]
}'
```
3. 返回供应商
```shell
{
  "generalComments": "退回去",
  "variances": [
    {
      "inventoryItemId": "c39293f3420b8d95225e2ea76e520d07",
      "varianceReasonId": "RETURN_TO_VENDOR",
      "quantityOnHandVar": 4,
      "comments": "都臭了"
    }
  ]
}
```
### 12. ~~（已废弃）原材料库存按批次分组(支持分页)~~
该接口为在手机客户端得到原材料根据产品、供应商，仓库汇总后的库存列表后，继续进一步按照批次号汇总的库存列表。
也就是对于确定产品Id、供应商Id和仓库Id的库存信息按照批次号（更小粒度）进行分解：

![替代文字](https://haozhuo-store-pulic.oss-cn-shanghai.aliyuncs.com/RawItemInventoryByLotNo.png "可选标题")

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/141L0K7AH7DL6W4948/InventoriesByLotNo?page=0&size=20&supplierId=13XLVW1AUECBR9GH0L&facilityId=13XM4J6CJBWD6FK64B' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
* productId 产品Id 例子中为：141L0K7AH7DL6W4948
* supplierId 供应商Id
* facilityId 仓库Id

得到结果举例如下：
```json
{
  "content": [
    {
      "productId": "141L0K7AH7DL6W4948",
      "productInternalId": "freshpointvendor1number55",
      "lotId": "14E7JY726AQ3US1MJA",
      "lotNo": "1",
      "facilityId": "13XM4J6CJBWD6FK64B",
      "quantityUomId": "KG",
      "quantityIncluded": 12,
      "caseUomId": "BOX",
      "quantityOnHandTotal": 150
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```
其中 content 为列表，也就是再按批次细分得到的列表。

* productInternalId 产品内部编码
* lotNo 批次号
* quantityOnHandTotal 库存数量
* quantityUomId 主数量单位
* caseUomId 包装单位
* quantityIncluded 一包装单位含有多少主数量单位的数量

### 13. 根据条件查询库存流水（支持分页）
当前支持按照产品类型、产品Id以及仓库Id查询库存流水。
```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffInventoryItems/Details?page=0&size=2&productTypeId=RAW_MATERIAL&productId=141L0K7AH7DL6W4948&facilityId=13XM4J6CJBWD6FK64B' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
得到结果举例如下：
```json
{
  "content": [
    {
      "inventoryItemId": "2649b7a09651e238f7d5aa962e65e53c",
      "inventoryItemDetailSeqId": "f26e44e377b1c43418f2b242803550e4",
      "productId": "141L0K7AH7DL6W4948",
      "productName": "freshpointitem15",
      "productTypeId": "RAW_MATERIAL",
      "productInternalId": "freshpointvendor1number55",
      "quantityUomId": "KG",
      "lotId": "14FN1C418HTJ4DLUEX",
      "lotNo": "1",
      "facilityId": "13XM4J6CJBWD6FK64B",
      "facilityName": "blureforcewh1",
      "locationSeqId": "13XM4J6CJBWD6FK64B_DEFAULT",
      "locationName": "-",
      "quantityOnHandDiff": 40,
      "rawIemQuantityIncluded": 12,
      "rawItemCaseUomId": "BOX",
      "shipmentId": "RC2025041400002",
      "receiptId": "RC2025041400002-2",
      "reasonEnumId": "RECEIVING",
      "createdAt": "2025-04-17T07:02:08.567085Z",
      "createdBy": "anonymousUser"
    },
    {
      "inventoryItemId": "c39293f3420b8d95225e2ea76e520d07",
      "inventoryItemDetailSeqId": "553371da8ea18aa3b1d2e5e650004a2c",
      "productId": "141L0K7AH7DL6W4948",
      "productName": "freshpointitem15",
      "productTypeId": "RAW_MATERIAL",
      "productInternalId": "freshpointvendor1number55",
      "quantityUomId": "KG",
      "lotId": "14E7JY726AQ3US1MJA",
      "lotNo": "1",
      "facilityId": "13XM4J6CJBWD6FK64B",
      "facilityName": "blureforcewh1",
      "locationSeqId": "13XM4J6CJBWD6FK64B_DEFAULT",
      "locationName": "-",
      "quantityOnHandDiff": -50,
      "rawIemQuantityIncluded": 12,
      "rawItemCaseUomId": "BOX",
      "physicalInventoryId": "14GC8S5M74LMQR3TEX",
      "reasonEnumId": "RETURN_TO_VENDOR",
      "createdAt": "2025-04-16T04:24:14.29097Z",
      "createdBy": "anonymousUser"
    }
  ],
  "totalElements": 13,
  "size": 2,
  "number": 0,
  "totalPages": 7
}
```
* quantityOnHandDiff 库存变动数量，复数为减少，正数为增加；
* reasonEnumId 库存变动原因
* createdAt 库存变动时间
* createdBy 操作人Id
* facilityId 仓库Id
* facilityName 仓库名称
* locationSeqId 仓位Id
* locationName 仓位名称
* locationCode 仓位内部编码
* lotNo 批次号
* productInternalId 产品内部编码
* physicalInventoryId、inventoryTransferId、receiptId等为相关单据的Id，根据 reasonEnumId 的不同，这些字段分别有值；
* rawItemCaseUomId 如果是原材料那么用该字段表示原材料的包装类型，否则使用 productCaseUomId；
* rawIemQuantityIncluded 如果是原材料那么用该字段表示一个包装单位内包含多少主数量单位的数量，否则使用 productQuantityIncluded。



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

增加以下三个属性：
* creditRating
* shippingAddress
* paymentMethodEnumId
 
其中 customerTypeEnumId 的取值范围为：
* WHOLESALER
* RETAILER
* FOOD_SERVICE
* E_COMMERCE
* END_USER

paymentMethodEnumId 的取值范围为：
* CASH
* CREDIT_CARD
* BANK_TRANSFER
* E_PAYMENT

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
  "shippingAddress": "shippingAddress",
  "paymentMethodEnumId": "paymentMethodEnumId",
  "creditRating": "creditRating",
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
  "customerId": "14FLR27KG1NF7QBR8Y",
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
  "shippingAddress": "shippingAddress",
  "paymentMethodEnumId": "paymentMethodEnumId",
  "creditRating": "creditRating",
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
      "facilityId": "14FLR24DXWQCKQ4LYJ",
      "parentFacilityId": "parentFacilityId2",
      "ownerPartyId": "14FLR27KG1NF7QBR8Y",
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
      "facilityId": "14FLR26GZMXYH0LKEM",
      "parentFacilityId": "parentFacilityId",
      "ownerPartyId": "14FLR27KG1NF7QBR8Y",
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
## 六、 BOM 

### 1. 创建 BOM
```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffBoms' \
  -H 'accept: */*' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "productId": "14F8LJ5BHUMW4VCCAS",
  "components": [
    {
      "productId": "14E2MB11RM6MV95X3E",
      "quantity": 100,
      "scrapFactor": 4.23
    },
    {
      "productId": "14F91P4G9RGPPJG80F",
      "quantity": 300,
      "scrapFactor": 2
    },
    {
      "productId": "14F9DW17Q9K170VP2Q",
      "quantity": 254,
      "scrapFactor": 7.1
    }
  ]
}'
```
创建成功，响应状态为 200。

对请求体 JSON 结构进行说明如下：

为产品 Id 为 productId 的产品设置 BOM，productId 指向的产品不能为原材料；

components 为组成 BOM 的【直接】构件列表；

每一个元素中的 productId 为组成 BOM 的构件的产品的 Id；

quantity 为该构建产品所需的数量（必须大于0）；

scrapFactor 为构建产品的报废百分比（取值大于0小于100）；

另外目前创建 BOM 遵循以下限制：

1. 不能为原材料创建 BOM（无意义）；
2. 只能使用原材料创建 RAC WIP 类型的产品的 BOM；
3. 只能使用原材料和 RAC WIP 类型的产品创建 RTE WIP 类型产品的 BOM；
4. 只能使用原材料、RAC WIP、RTE WIP 类型的产品创建 PACK WIP 类型产品的 BOM；
5. 只能使用原材料、RAC WIP、RTE WIP、PACK WIP 类型的产品创建 FINISHED GOOD 类型产品的 BOM；

### 2. 根据条件查询 BOM

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffBoms?page=0&size=20&productTypeId=RAC_WIP&productId=14F91P4G9RGPPJG80F&internalId=rac25041502' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
当前支持过滤条件：
1. productTypeId 产品类型Id
2. productId 产品Id
3. internalId 产品内部标识

返回结果举例如下：
```json
{
  "content": [
    {
      "productId": "14F91P4G9RGPPJG80F",
      "productTypeId": "RAC_WIP",
      "smallImageUrl": "c486209b-1a61-402d-b8dd-a12394671a81",
      "mediumImageUrl": "medium image url",
      "largeImageUrl": "large image url",
      "quantityUomId": "KG",
      "internalId": "rac25041502",
      "productName": "rac25041502",
      "fromDate": "2025-04-19T12:59:28.309837Z"
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```
其中：
1. fromDate 表示创建时间
2. internalId 标识产品内部标识
### 3. 获取指定产品的 BOM 详情
```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffBoms/14F91P4G9RGPPJG80F' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
其中 14F91P4G9RGPPJG80F 为产品的Id；

返回结果举例如下：

```json
{
  "productId": "14F8LJ5BHUMW4VCCAS",
  "productTypeId": "FINISHED_GOOD",
  "smallImageUrl": "small image url",
  "mediumImageUrl": "medium image url",
  "largeImageUrl": "large_image_url",
  "quantityUomId": "KG",
  "internalId": "25041502",
  "productName": "P25041502",
  "fromDate": "2025-04-19T13:34:02.939311Z",
  "components": [
    {
      "productId": "14E2MB11RM6MV95X3E",
      "productTypeId": "RAW_MATERIAL",
      "smallImageUrl": "c2616454-ebb2-402f-a709-1aefcfa63da3",
      "quantityUomId": "LB",
      "internalId": "25041401",
      "productName": "i25041401",
      "sequenceNum": 1,
      "fromDate": "2025-04-19T13:34:02.939311Z",
      "quantity": 100,
      "scrapFactor": 4.23
    },
    {
      "productId": "14F91P4G9RGPPJG80F",
      "productTypeId": "RAC_WIP",
      "smallImageUrl": "c486209b-1a61-402d-b8dd-a12394671a81",
      "mediumImageUrl": "medium image url",
      "largeImageUrl": "large image url",
      "quantityUomId": "KG",
      "internalId": "rac25041502",
      "productName": "rac25041502",
      "sequenceNum": 2,
      "fromDate": "2025-04-19T12:59:28.309837Z",
      "quantity": 300,
      "scrapFactor": 2,
      "components": [
        {
          "productId": "13X7PC3NRZ17ZFKZS1",
          "productTypeId": "RAW_MATERIAL",
          "smallImageUrl": "0179890d-d78e-4e65-9497-95c7a0eec3cd",
          "quantityUomId": "KG",
          "internalId": "25033102",
          "productName": "i25033102",
          "sequenceNum": 1,
          "fromDate": "2025-04-19T12:59:28.309837Z",
          "quantity": 100,
          "scrapFactor": 4.23
        }
      ]
    },
    {
      "productId": "14F9DW17Q9K170VP2Q",
      "productTypeId": "RTE_WIP",
      "smallImageUrl": "c64b2124-32e3-4b6d-960d-e6a151cff477",
      "quantityUomId": "LB",
      "internalId": "rte25041501",
      "productName": "rte25041501",
      "sequenceNum": 3,
      "fromDate": "2025-04-19T13:34:03Z",
      "quantity": 254,
      "scrapFactor": 7.1
    }
  ]
}
```
结果为一自顶而下的树状结构，一直延伸到最底层。

### 4. 删除 BOM
可以删除指定产品的 BOM 信息，并且只是移除其与直接关联产品的关系，如果其直接关联构建产品也有自己的 BOM 信息，则改信息不会被删除。
```shell
curl -X 'DELETE' \
  'http://localhost:8001/api/BffBoms/14F8LJ5BHUMW4VCCAS' \
  -H 'accept: */*' \
  -H 'X-TenantID: X'
```
请求路径中的 14F8LJ5BHUMW4VCCAS 为产品 Id.

✅ 成功删除时返回 HTTP 200 OK 状态码（无响应体）

### 5. 更新 BOM

请参照 【3. 获取指定产品的 BOM 详情】中 Id 为 14F8LJ5BHUMW4VCCAS 的产品的 BOM 由三个原材料或者WIP构建，
现在我们删除与产品"14E2MB11RM6MV95X3E"和“14F91P4G9RGPPJG80F”的关联，并且增加与产品“13X7PC3NRZ17ZFKZS1”的关联，
并且修改与产品“14F9DW17Q9K170VP2Q”的关联（修改数量和报废率），如下：
```shell
curl -X 'PUT' \
  'http://localhost:8001/api/BffBoms/14F8LJ5BHUMW4VCCAS' \
  -H 'accept: */*' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '[
    {
        "productId": "14F9DW17Q9K170VP2Q",
        "quantity": 55,
        "scrapFactor": 43.2
    },
    {
        "productId": "13X7PC3NRZ17ZFKZS1",
        "quantity": 66,
        "scrapFactor": 3.2
    }
]'
```
操作成功（返回 HTTP 200 OK 状态码）后，我们再获取原产品“14F8LJ5BHUMW4VCCAS”的 BOM 信息将得到以下结果：
```json
{
  "productId": "14F8LJ5BHUMW4VCCAS",
  "productTypeId": "FINISHED_GOOD",
  "smallImageUrl": "small image url",
  "mediumImageUrl": "medium image url",
  "largeImageUrl": "large_image_url",
  "quantityUomId": "KG",
  "internalId": "25041502",
  "productName": "P25041502",
  "fromDate": "2025-04-20T08:47:54.300194Z",
  "components": [
    {
      "productId": "14F9DW17Q9K170VP2Q",
      "productTypeId": "RTE_WIP",
      "smallImageUrl": "c64b2124-32e3-4b6d-960d-e6a151cff477",
      "quantityUomId": "LB",
      "internalId": "rte25041501",
      "productName": "rte25041501",
      "sequenceNum": 1,
      "fromDate": "2025-04-20T08:47:54.300194Z",
      "quantity": 55,
      "scrapFactor": 43.2
    },
    {
      "productId": "13X7PC3NRZ17ZFKZS1",
      "productTypeId": "RAW_MATERIAL",
      "smallImageUrl": "0179890d-d78e-4e65-9497-95c7a0eec3cd",
      "quantityUomId": "KG",
      "internalId": "25033102",
      "productName": "i25033102",
      "sequenceNum": 2,
      "fromDate": "2025-04-20T08:57:16.071297Z",
      "quantity": 66,
      "scrapFactor": 3.2
    }
  ]
}
```
可以看到该产品的 BOM 已经如愿更新。

## 七、销售订单 Sales order

### 1. 创建销售订单
使用 curl 创建销售订单如下：
```shell
curl -X 'POST' \
  'http://localhost:8001/api/BffSalesOrders' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "orderId": "salesorderId",
  "orderDate": "2025-04-21T06:33:11.514Z",
  "memo": "memo",
  "customerId": "14MESS61QA9BPQY0TH",
  "orderItems": [
    {
      "orderItemSeqId": "orderItemSeqId1",
      "productId": "14F8LJ5R5RFJA2U7VZ",
      "quantity": 100
    },
    {
      "productId": "14J32466F5ZZJY72S4",
      "quantity": 200
    }
  ]
}'
```
创建成功将返回该订单的Id(订单号)，如：
```json
"SO2025042100001"
```
* orderId：销售订单号，如果不提供系统会自动生成以 SO 开头的销售订单号，如：“SO2025042100001”；
* memo：订单备注信息
* customerId：Customer Id;
* orderItems：订单行项；
* orderItemSeqId：订单行项 Id，如果不提供系统将自动生成；
* productId：产品 Id；
* quantity：数量；

### 2. 条件查询销售订单

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffSalesOrders?page=0&size=20&orderIdOrItem=14F8LJ5R5RFJA2U7VZ&customerId=14MG1V3N96FQ6YF88U&orderDateFrom=2025-04-21T00%3A00%3A00.000Z&orderDateTo=2025-04-22T00%3A00%3A00.000Z&includesProductDetails=true' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
目前可供使用的查询条件有：
* orderIdOrItem 可以模糊查询以 orderIdOrItem 开头的销售订单Id,销售订单行项Id,产品Id以及产品GTIN；
* customerId 
* orderDateFrom & orderDateTo 下单时间范围
* includesProductDetails 是否在结果中展示产品详情

展示结果举例如下：
```json
{
  "content": [
    {
      "orderId": "salesorderId",
      "orderDate": "2025-04-21T06:33:11.514Z",
      "memo": "memo",
      "fulfillmentStatusId": "NOT_FULFILLED",
      "customerId": "14MG1V3N96FQ6YF88U",
      "customerName": "customerShortName",
      "createdAt": "2025-04-24T03:28:47.023102Z",
      "createdBy": "anonymousUser",
      "orderItems": [
        {
          "orderItemSeqId": "1",
          "productId": "14F8LJ5R5RFJA2U7VZ",
          "productName": "P25041501",
          "product": {
            "productId": "14F8LJ5R5RFJA2U7VZ",
            "productName": "P25041501",
            "productTypeId": "FINISHED_GOOD",
            "brandName": "brand1",
            "description": "description1",
            "quantityUomId": "KG",
            "quantityIncluded": 21,
            "piecesIncluded": 1,
            "productWeight": 31,
            "active": "Y",
            "defaultShipmentBoxTypeId": "14F8MM34CHBD13A6Y8",
            "defaultShipmentBoxType": {
              "shipmentBoxTypeId": "14F8MM34CHBD13A6Y8"
            },
            "caseUomId": "PCS",
            "internalId": "25041501",
            "hsCode": "hscode1",
            "organicCertifications": "organicCertification1",
            "materialCompositionDescription": "materialComposition1",
            "countryOfOrigin": "countryOfOrgin1",
            "shelfLifeDescription": "shelfLife1",
            "handlingInstructions": "handingInstructions1",
            "storageConditions": "storageConditions1",
            "certificationCodes": "certificationCode1",
            "individualsPerPackage": 11,
            "dimensionsDescription": "dimensions1"
          },
          "quantity": 100,
          "fulfillmentStatusId": "NOT_FULFILLED"
        },
        {
          "orderItemSeqId": "2",
          "productId": "14J32466F5ZZJY72S4",
          "productName": "p25041901",
          "product": {
            "productId": "14J32466F5ZZJY72S4",
            "productName": "p25041901",
            "productTypeId": "FINISHED_GOOD",
            "smallImageUrl": "131562e0-c0c6-4301-9dcc-5dc6c86ac63c",
            "quantityUomId": "LB",
            "quantityIncluded": 10,
            "piecesIncluded": 1,
            "active": "Y",
            "defaultShipmentBoxTypeId": "14J3245MHGULTZCYZ6",
            "defaultShipmentBoxType": {
              "shipmentBoxTypeId": "14J3245MHGULTZCYZ6"
            },
            "caseUomId": "PCS",
            "internalId": "25041901"
          },
          "quantity": 200,
          "fulfillmentStatusId": "NOT_FULFILLED"
        }
      ]
    }
  ],
  "totalElements": 1,
  "size": 20,
  "number": 0,
  "totalPages": 1
}
```

### 3. 获取销售订单详情

```shell
curl -X 'GET' \
  'http://localhost:8001/api/BffSalesOrders/{salesOrderId}?includesProductDetails=true' \
  -H 'accept: application/json' \
  -H 'X-TenantID: X'
```
* 路径参数 {salesOrderId} 为销售订单Id;

* includesProductDetails 是否在销售订单行项中显示产品详情。

返回结果示例如下：

```json
{
  "orderId": "salesorderId",
  "orderDate": "2025-04-21T06:33:11.514Z",
  "memo": "memo",
  "fulfillmentStatusId": "NOT_FULFILLED",
  "customerId": "14MG1V3N96FQ6YF88U",
  "customerName": "customerShortName",
  "createdAt": "2025-04-24T03:28:47.023102Z",
  "createdBy": "anonymousUser",
  "orderItems": [
    {
      "orderItemSeqId": "1",
      "productId": "14F8LJ5R5RFJA2U7VZ",
      "productName": "P25041501",
      "product": {
        "productId": "14F8LJ5R5RFJA2U7VZ",
        "productName": "P25041501",
        "productTypeId": "FINISHED_GOOD",
        "brandName": "brand1",
        "description": "description1",
        "quantityUomId": "KG",
        "quantityIncluded": 21,
        "piecesIncluded": 1,
        "productWeight": 31,
        "active": "Y",
        "defaultShipmentBoxTypeId": "14F8MM34CHBD13A6Y8",
        "defaultShipmentBoxType": {
          "shipmentBoxTypeId": "14F8MM34CHBD13A6Y8"
        },
        "caseUomId": "PCS",
        "internalId": "25041501",
        "hsCode": "hscode1",
        "organicCertifications": "organicCertification1",
        "materialCompositionDescription": "materialComposition1",
        "countryOfOrigin": "countryOfOrgin1",
        "shelfLifeDescription": "shelfLife1",
        "handlingInstructions": "handingInstructions1",
        "storageConditions": "storageConditions1",
        "certificationCodes": "certificationCode1",
        "individualsPerPackage": 11,
        "dimensionsDescription": "dimensions1"
      },
      "quantity": 100,
      "fulfillmentStatusId": "NOT_FULFILLED"
    },
    {
      "orderItemSeqId": "2",
      "productId": "14J32466F5ZZJY72S4",
      "productName": "p25041901",
      "product": {
        "productId": "14J32466F5ZZJY72S4",
        "productName": "p25041901",
        "productTypeId": "FINISHED_GOOD",
        "smallImageUrl": "131562e0-c0c6-4301-9dcc-5dc6c86ac63c",
        "quantityUomId": "LB",
        "quantityIncluded": 10,
        "piecesIncluded": 1,
        "active": "Y",
        "defaultShipmentBoxTypeId": "14J3245MHGULTZCYZ6",
        "defaultShipmentBoxType": {
          "shipmentBoxTypeId": "14J3245MHGULTZCYZ6"
        },
        "caseUomId": "PCS",
        "internalId": "25041901"
      },
      "quantity": 200,
      "fulfillmentStatusId": "NOT_FULFILLED"
    }
  ]
}
```

### 4. 修改订单
我们以上面创建的订单号为 “saleorderId”的订单为例，它目前有两个行项：
1. 产品Id为"14F8LJ5R5RFJA2U7VZ"，数量为100；
2. 产品Id为“14J32466F5ZZJY72S4”，数量为200。

现在：
1. 将下单时间改为："2025-04-24T07:36:06.201Z"；
2. 备注信息改为"updatedMemo"；
3. 删除产品Id为“14J32466F5ZZJY72S4”的行项；
4. 新增产品Id为“14F8LJ5BHUMW4VCCAS”的行项，数量为700；
5. 将产品Id为“14F8LJ5R5RFJA2U7VZ”的行项的数量改为200；

```shell
curl -X 'PUT' \
  'http://localhost:8001/api/BffSalesOrders/salesorderId' \
  -H 'accept: */*' \
  -H 'X-TenantID: X' \
  -H 'Content-Type: application/json' \
  -d '{
  "orderDate": "2025-04-24T07:36:06.201Z",
  "memo": "updatedMemo",
  "customerId": "14MG1V3N96FQ6YF88U",
  "orderItems": [
    {
      "orderItemSeqId": "1",
      "productId": "14F8LJ5R5RFJA2U7VZ",
      "quantity": 200
    },
    {
      "productId": "14F8LJ5BHUMW4VCCAS",
      "quantity": 700    
    }
  ]
}'
```
操作成功（返回 HTTP 200 OK 状态码）后，再次查询订单号为"salesorderId"的订单，可以看到如下结果：
```json
{
  "orderId": "salesorderId",
  "orderDate": "2025-04-24T07:36:06.201Z",
  "memo": "updatedMemo",
  "fulfillmentStatusId": "NOT_FULFILLED",
  "customerId": "14MG1V3N96FQ6YF88U",
  "customerName": "customerShortName",
  "createdAt": "2025-04-24T03:28:47.023102Z",
  "createdBy": "anonymousUser",
  "orderItems": [
    {
      "orderItemSeqId": "14QF8GFGC3WMYFEQVG",
      "productId": "14F8LJ5BHUMW4VCCAS",
      "productName": "P25041502",
      "product": {
        "defaultShipmentBoxType": {}
      },
      "quantity": 700,
      "fulfillmentStatusId": "NOT_FULFILLED"
    },
    {
      "orderItemSeqId": "1",
      "productId": "14F8LJ5R5RFJA2U7VZ",
      "productName": "P25041501",
      "product": {
        "defaultShipmentBoxType": {}
      },
      "quantity": 200,
      "fulfillmentStatusId": "NOT_FULFILLED"
    }
  ]
}
```













