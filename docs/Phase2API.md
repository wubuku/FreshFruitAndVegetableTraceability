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
  'http://localhost:8001/api/BffInventoryItems/Proucts?page=0&size=20&productTypeId=RAC_WIP&productName=blueforceitem1&productId=13XM0K65JP235EMN65&facilityId=13XM4J6CJBWD6FK64B' \
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
* physicalInventoryId、inventoryTransferId、receiptId等为相关单据的Id，根据 reasonEnumId 的不同，这些字段分别有值。



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







