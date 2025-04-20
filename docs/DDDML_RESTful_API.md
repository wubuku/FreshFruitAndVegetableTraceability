# 使用 DDDML 开发 RESTful API

本教程详细介绍了如何使用 DDDML (Domain-Driven Design Markup Language) 通过 YAML 文件定义“服务”，并生成服务的 RESTful API 实现。

## 1. DDDML 概述

DDDML 是一种专为领域驱动设计 (DDD) 建模的标记语言，它允许开发者通过 YAML 格式声明式地定义业务领域模型（包括“服务”），然后自动生成相应的代码实现。
在本文档中，我们主要介绍如何使用 DDDML 定义“服务”，以生成服务的 RESTful API 实现。

## 2. YAML 文件基本结构

DDDML YAML 文件主要包含以下几个顶级部分：

- **valueObjects**：定义值对象，不具有独立身份的领域对象
- **services**：定义领域服务及其方法
- **aggregates**：定义聚合，具有身份和生命周期的实体集合（本教程不涉及）

### 2.1 值对象定义

值对象是没有独立身份（即 ID）的领域对象，通常通过其属性来识别。

```yaml
valueObjects:
  BffProductDto:
    metadata:
      JpaProjectionInterfaceName: BffProductProjection
    properties:
      ProductId:
        type: id
      ProductName:
        type: name
      Description:
        type: description
      UomId:
        type: id
```

**关键点：**
- `metadata` 部分可以添加特定于实现的元数据
- `properties` 部分定义值对象的属性
- 每个属性有一个 `type` 定义，如 `id`, `name`, `description` 等

### 2.2 值对象元数据配置

可以通过 `metadata` 配置值对象的特殊行为：

```yaml
valueObjects:
  BffQaInspectionDto:
    metadata:
      JpaProjectionInterfaceName: BffQaInspectionProjection
      JpaProjectionPropertyTypes:
        InspectedAt: java.time.Instant
        CreatedAt: java.time.Instant
      JakartaValidationEnabled: true
      NoFlattenedProperties: true
```

**常用元数据：**
- `JpaProjectionInterfaceName`：指定值对象的 JPA 投影接口名称。我们可能期望在编写 JPA Repository 时，生成这里定义的“值对象”对应的投影接口。
- `JpaProjectionPropertyTypes`：指定特定属性在 JPA 投影接口中的类型。
- `JakartaValidationEnabled`：启用 Jakarta 验证
- `NoFlattenedProperties`：禁止生成（类型为“值对象”的）属性的扁平化代码。


## 3. 属性类型

DDDML 允许项目自定义“抽象”类型，只要这些类型可以映射为生成的代码的“实现”类型即可（如何配置这些“映射”信息不在本文档讨论范围）。
下面是当前项目中定义的部分“抽象”类型：

- `id`：标识符，通常是字符串
- `id-long`：长整型标识符
- `id-vlong`：超长整型标识符
- `name`：名称类型
- `description`：描述类型
- `comment`：注释类型
- `fixed-point`：定点数，适用于货币等精确计算
- `numeric`：数值类型
- `date-time`：日期时间类型
- `indicator`：指示器类型，通常是布尔值
- `short-varchar`：短文本
- `long-varchar`：长文本
- `very-long`：超长文本
- `very-short`：超短文本

## 4. 服务定义

服务是领域操作的集合。通过定义服务，可以生成 RESTful API 方法：

```yaml
services:
  BffDocumentService:
    restfulResourceName: "BffDocuments"
    methods:
      GetDocuments:
        metadata:
          IsPageable: true
          ReturnPageEnvelope: true
        isQuery: true
        result:
          itemType: BffDocumentDto

      GetDocument:
        metadata:
          RestfulPathVariable: DocumentId
        isQuery: true
        result:
          type: BffDocumentDto
        parameters:
          DocumentId:
            type: id
```

**关键点：**
- `restfulResourceName` 定义 REST 资源的名称
- `methods` 定义服务的方法
- 每个方法可以有 `metadata`, `isQuery`, `result`, `parameters` 等属性

### 4.1 HTTP 方法定义

可以通过 `httpMethod` 显式指定 HTTP 方法：

```yaml
CreateDocument:
  httpMethod: POST
  metadata:
    HttpRequestBody: Document
  parameters:
    Document:
      type: BffDocumentDto
  result:
    type: id # DocumentId
```

### 4.2 查询方法定义

查询方法可以设置 `isQuery: true`，默认情况下会生成 HTTP GET 方法：

```yaml
GetUnitsOfMeasure:
  metadata:
    IsPageable: true
    ReturnPageEnvelope: true
  isQuery: true
  parameters:
    Active:
      type: indicator
    UomTypeId:
      type: id
      optional: true
  result:
    itemType: BffUomDto
```

**查询方法关键点：**
- `isQuery: true` 表示这是一个查询方法
- `IsPageable: true` 表示支持分页
- `ReturnPageEnvelope: true` 表示返回包含分页信息的封装（封包）对象
- `result` 定义返回结果类型
- `parameters` 定义查询参数

### 4.3 参数定义

方法参数可以定义各种属性：

```yaml
parameters:
  DocumentId:
    type: id
    optional: false # is required
  Active:
    type: indicator
    optional: true # NOT required
```

**参数属性：**
- `type`：参数类型
- `optional`：是否可选
- `itemType`：如果参数是集合，定义集合元素类型

### 4.4 结果定义

方法返回值可以有多种形式：

```yaml
# 返回单个对象
result:
  type: BffDocumentDto

# 返回集合
result:
  itemType: BffDocumentDto

# 返回基本类型
result:
  type: id # 返回字符串ID
```

## 5. REST API 路径和变量

DDDML 提供了多种方式定义 REST API 路径：

### 5.1 资源路径

通过 `restfulResourceName` 定义资源基础路径：

```yaml
services:
  BffGeoService:
    restfulResourceName: "BffGeo"
```

生成 Java 代码中的路径映射：

```java
@RequestMapping(path = "BffGeo", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffGeoServiceResource {
```

### 5.2 方法级路径

可以为特定方法定义子路径：

```yaml
GetCountries:
  restfulResourceName: "Countries"
  isQuery: true
```

生成的 Java 代码：

```java
@GetMapping("Countries")
public BffGeoDto[] getCountries() {
```

### 5.3 路径变量

使用 `RestfulPathVariable` 定义路径变量：

```yaml
GetDocument:
  metadata:
    RestfulPathVariable: DocumentId
  parameters:
    DocumentId:
      type: id
```

生成的 Java 代码：

```java
@GetMapping("{documentId}")
public BffDocumentDto getDocument(
    @PathVariable("documentId") String documentId
) {
```

当需要定义多个路径变量时，可以使用 `RestfulPathVariables`（复数形式）：

```yaml
GetPurchaseOrderItem:
  # 获取指定采购订单下指定行项的详情
  restfulResourceName: "{OrderId}/Items/{OrderItemSeqId}"
  metadata:
    RestfulPathVariables:
      - OrderId
      - OrderItemSeqId
  isQuery: true
  result:
    type: BffPurchaseOrderItemDto
  parameters:
    OrderId:
      type: id
    OrderItemSeqId:
      type: id
    IncludesFulfillments:
      # 是否包含（当前行项的）履行状态
      type: bool
      optional: true # NOT required
    IncludesProductDetails:
      type: bool
      optional: true
```

生成的 Java 代码会包含所有指定的路径变量：

```java
@GetMapping("{orderId}/Items/{orderItemSeqId}")
public BffPurchaseOrderItemDto getPurchaseOrderItem(
    @PathVariable("orderId") String orderId,
    @PathVariable("orderItemSeqId") String orderItemSeqId,
    @RequestParam(value = "includesFulfillments", required = false) Boolean includesFulfillments,
    @RequestParam(value = "includesProductDetails", required = false) Boolean includesProductDetails
) {
```

这种方式在定义复杂的嵌套资源路径时特别有用，可以清晰地指定多个路径参数，如上例中的订单ID和订单项序列ID，形成多层次的RESTful资源路径结构。

### 5.4 复杂路径模式

可以使用 `{变量}` 语法在 `restfulResourceName` 中定义更复杂的路径：

```yaml
ActivateShipmentBoxType:
  restfulResourceName: "{ShipmentBoxTypeId}/active"
  metadata:
    RestfulPathVariable: ShipmentBoxTypeId
```

生成的 Java 代码：

```java
@PutMapping("{shipmentBoxTypeId}/active")
public void activateShipmentBoxType(
    @PathVariable("shipmentBoxTypeId") String shipmentBoxTypeId,
    @RequestBody Boolean active
) {
```

## 6. 请求体定义

可以通过 `HttpRequestBody` 指定请求体参数：

```yaml
CreateDocument:
  httpMethod: POST
  metadata:
    HttpRequestBody: Document
  parameters:
    Document:
      type: BffDocumentDto
```

生成的 Java 代码：

```java
@PostMapping
public String createDocument(
    @RequestBody BffDocumentDto document
) {
```

## 7. 集合与关联

DDDML 支持定义对象之间的关联和集合：

### 7.1 简单集合

使用 `isList: true` 定义集合：

```yaml
valueObjects:
  BffPhysicalInventoryDto:
    properties:
      Variances:
        itemType: BffInventoryVarianceDto
        isList: true
```

### 7.2 自引用关联

可以定义自引用的层次结构：

```yaml
Children:
  type: BffProductAssociationDto
  isList: true
```

注意，此时需要设置 `NoFlattenedProperties: true`，否则在生成扁平化的代码时，会因为“无限”递归而导致生成失败。

## 8. 批量操作

DDDML 支持定义批量操作方法：

```yaml
BatchAddUnitsOfMeasure:
  httpMethod: POST
  restfulResourceName: "batchAddUnitsOfMeasure"
  metadata:
    HttpRequestBody: UnitsOfMeasure
  parameters:
    UnitsOfMeasure:
      itemType: BffUomDto
```

生成的 Java 代码：

```java
@PostMapping("batchAddUnitsOfMeasure")
public void batchAddUnitsOfMeasure(
    @RequestBody BffUomDto[] unitsOfMeasure
) {
```

## 9. 生成代码模式

DDDML 生成的代码遵循以下模式：

1. RESTful 资源控制器：处理 HTTP 请求
2. 命令/查询对象：封装请求参数
3. 应用服务：处理业务逻辑
4. DTO 转换：在表示层和领域层之间转换数据

生成的代码示例：

```java
@PostMapping
public String createDocument(
    @RequestBody BffDocumentDto document
) {
    BffDocumentServiceCommands.CreateDocument createDocument = new BffDocumentServiceCommands.CreateDocument();
    createDocument.setDocument(document);
    
    createDocument.setRequesterId(SecurityContextUtil.getRequesterId());
    return bffDocumentApplicationService.when(createDocument);
}
```

## 10. 验证和安全

DDDML 支持启用 Jakarta 验证和安全注解：

```yaml
valueObjects:
  BffBusinessContactDto:
    metadata:
      JakartaValidationEnabled: true
```

生成的 Java 代码会包含验证注解：

```java
@PostMapping
public String createCustomer(
    @RequestBody @Valid BffCustomerDto customer
) {
```

## 11. 实际案例分析

### 11.1 物理库存调整示例

以物理库存调整为例，展示完整的定义和生成流程：

```yaml
# BffPhysicalInventories.yaml
valueObjects:
  BffPhysicalInventoryDto:
    metadata:
      JpaProjectionInterfaceName: BffPhysicalInventoryProjection
      JpaProjectionPropertyTypes:
        PhysicalInventoryDate: java.time.Instant
        CreatedAt: java.time.Instant
    properties:
      PhysicalInventoryId:
        type: id
      PhysicalInventoryDate:
        type: date-time
      GeneralComments:
        type: comment
      CreatedAt:
        type: date-time
      Variances:
        itemType: BffInventoryVarianceDto
        isList: true

services:
  BffPhysicalInventoryService:
    restfulResourceName: BffPhysicalInventories
    methods:
      CreatePhysicalInventory:
        httpMethod: POST
        metadata:
          HttpRequestBody: PhysicalInventory
        parameters:
          PhysicalInventory:
            type: BffPhysicalInventoryDto
        result:
          type: id
```

生成的 Java 代码：

```java
@RequestMapping(path = "BffPhysicalInventories", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffPhysicalInventoryServiceResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffPhysicalInventoryApplicationService bffPhysicalInventoryApplicationService;

    @PostMapping
    public String createPhysicalInventory(
        @RequestBody BffPhysicalInventoryDto physicalInventory
    ) {
        BffPhysicalInventoryServiceCommands.CreatePhysicalInventory createPhysicalInventory = new BffPhysicalInventoryServiceCommands.CreatePhysicalInventory();
        createPhysicalInventory.setPhysicalInventory(physicalInventory);
        
        createPhysicalInventory.setRequesterId(SecurityContextUtil.getRequesterId());
        return bffPhysicalInventoryApplicationService.when(createPhysicalInventory);
    }
}
```

### 11.2 质量检验示例

质量检验服务定义了更复杂的 API 操作：

```yaml
services:
  BffQaInspectionService:
    restfulResourceName: BffQaInspections
    methods:
      GetQaInspections:
        isQuery: true
        parameters:
          ReceivingDocumentId:
            type: id
            optional: false
          ReceiptId:
            type: id-long
        result:
          itemType: BffQaInspectionDto

      BatchAddOrUpdateQaInspections:
        httpMethod: POST
        restfulResourceName: "batchAddOrUpdateQaInspections"
        metadata:
          HttpRequestBody: QaInspections
        parameters:
          QaInspections:
            itemType: BffQaInspectionDto
```

## 12. 最佳实践

1. **命名规范**：
   - 使用有意义的名称
   - DTO 后缀表示数据传输对象
   - Service 后缀表示服务

2. **属性类型**：
   - 使用合适的预定义类型
   - 为日期时间类型指定 Java 类型

3. **路径设计**：
   - 使用有意义的资源名称
   - 遵循 REST 资源层次设计原则

4. **批量操作**：
   - 为频繁操作提供批量接口
   - 使用一致的命名规范

5. **元数据配置**：
   - 使用 JpaProjectionPropertyTypes 指定 Java 类型
   - 启用验证和安全注解

## 13. 结论

DDDML 提供了一种声明式的方式来定义领域模型和 RESTful API，通过简洁的 YAML 语法，可以快速创建符合 DDD 原则的应用程序。本教程介绍了 DDDML 的基本概念和用法，希望能帮助开发人员更好地理解和使用 DDDML 来构建高质量的应用程序。

## 附录：常用元数据参考

| 元数据名称 | 描述 | 适用范围 |
|------------|------|----------|
| JpaProjectionInterfaceName | JPA 投影接口名称 | 值对象 |
| JpaProjectionPropertyTypes | 属性的 Java 类型 | 值对象 |
| JakartaValidationEnabled | 启用 Jakarta 验证 | 值对象 |
| NoFlattenedProperties | 禁止属性扁平化 | 值对象 |
| IsPageable | 支持分页 | 方法 |
| ReturnPageEnvelope | 返回分页封装对象 | 方法 |
| RestfulPathVariable | 路径变量 | 方法 |
| HttpRequestBody | 请求体参数 | 方法 | 