# Programming


## Generate code from DDDML model files


### Write DDDML model files

Here is the [DDDML model files](../dddml).

### Remove Old Containers and Images

> **Hint**
>
> Sometimes you may need to remove old containers and images, ensure you are using the latest image:
>
> ```shell
> docker rm $(docker ps -aq --filter "ancestor=wubuku/dddappp-java:master")
> docker rmi wubuku/dddappp-java:master
> ```


### Run dddappp Project Creation Tool


In repository root directory, run:

```shell
docker run \
-v .:/myapp \
wubuku/dddappp-java:master \
--dddmlDirectoryPath /myapp/dddml \
--boundedContextName Dddml.FfvTraceability \
--boundedContextJavaPackageName org.dddml.ffvtraceability \
--javaProjectsDirectoryPath /myapp/src \
--javaProjectNamePrefix ffvtraceability \
--pomGroupId dddml.ffvtraceability
```


### Build project

```shell
cd src

mvn clean && mvn package -Dmaven.test.skip=true
```

### Generate database schema

For PostgreSQL, execute the following script:

```sql
CREATE DATABASE test3
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
```

Then execute the following command to generate the database schema:

```shell
java -jar ./ffvtraceability-service-cli/target/ffvtraceability-service-cli-0.0.1-SNAPSHOT.jar ddl -d "./scripts" -c "jdbc:postgresql://127.0.0.1/test3" -u postgres -p 123456
```

For MySQL, execute the following command:

```shell
java -jar ./ffvtraceability-service-cli/target/ffvtraceability-service-cli-0.0.1-SNAPSHOT.jar ddl -d "./scripts" -c "jdbc:mysql://127.0.0.1:3306/test2?enabledTLSProtocols=TLSv1.2&characterEncoding=utf8&serverTimezone=GMT%2b0&useLegacyDatetimeCode=false" -u root -p 123456
```

### Initialize data

```shell
java -jar ./ffvtraceability-service-cli/target/ffvtraceability-service-cli-0.0.1-SNAPSHOT.jar initData -d "file:../data/*.json" --json
```

### Run service

```shell
mvn -pl ffvtraceability-service-rest -am spring-boot:run
```

> 说明：
> `-pl` 是 `--projects` 的缩写，用于指定要构建的模块或项目。
> `-am` 是 `--also-make` 的缩写，用于指定在构建目标模块时，也构建依赖的模块。
> `spring-boot:run`：这是 Spring Boot Maven 插件的一个目标，用于运行 Spring Boot 应用程序。它会编译代码、打包应用并启动嵌入的服务器（如 Tomcat）。


查看 API 文档（Swagger UI）：

```text
http://localhost:1023/api/swagger-ui/index.html
```

### Run tests

```shell
mvn -pl ffvtraceability-service-rest -am test
```


### 多租户支持

我们的应用在数据库访问层使用了 Hibernate ORM 框架，并且采用了基于鉴别器的多租户策略。

Hibernate 其实并[没有对基于鉴别器的多租户策略提供“原生支持”](Hibernate_多租户支持现状.md)，这个策略实际上是我们自己来实现的。


#### 我们的实现


##### 设置上下文中的“当前租户 ID”

我们编写了一个 [TenantFilter](../src/ffvtraceability-service-rest/src/main/java/org/dddml/ffvtraceability/servlet/TenantFilter.java)，它的作用是允许客户端通过 HTTP Header 来设置[租户上下文](../src/ffvtraceability-common/src/generated/java/org/dddml/ffvtraceability/domain/TenantContext.java)中的“当前租户 ID”。

当然，以后我们还可以支持其他方式设置租户上下文中的“当前租户 ID”，比如从 HTTP 请求的域名中解析出租户 ID。


##### 两种方式实现基于鉴别器的多租户策略

在启用 `DiscriminatorBasedMultiTenancyEnabled` 选项的情况下，
我们现在支持以两种方式实现基于鉴别器的多租户策略：

1. 在实体的状态对象/表增加一个普通（即非 ID/Key）的“TenantId”属性/列。
    这是默认的方式。这种做法要求实体的 Id 本身具备全局唯一性（Id Generator 的实现考虑到这一点）。

2. 允许租户 Id 作为实体（聚合根）对应的“表的主键”的一部分。我们通过一个 `ShouldTenantizeId` 选项来启用这种做法。
    这种做法可以比较容易地产生实体的 ID——这个 ID 只需要在租户内具备唯一性即可（Id Generator 的实现较简单）。
    麻烦的地方是我们修改了工具的很多代码，
    才做到可以在 DDDML 模型中不需要显式地指定实体的 Id 类型为一个“包含 TenantId 的”值对象时，
    对应的 table 主键中“自动”包含 TenantId 列。


前一种实现方式的示例见 [StatusItem](../dddml/StatusItem.yaml)。

后一种实现方式的示例见 [SupplierProduct](../dddml/SupplierProduct.yaml)。


不管采用哪种方式，我们都不希望因为“多租户”这个技术问题而过多地“污染”领域模型。
我们基本上做到了这一点。

可以查看 `SupplierProduct` 的 `UpdateAvailableThruDate` 方法，以及 `Disable` 方法的业务逻辑实现文件：
* [UpdateAvailableThruDateLogic.java](../src/ffvtraceability-common/src/main/java/org/dddml/ffvtraceability/domain/supplierproduct/UpdateAvailableThruDateLogic.java)
* [DisableLogic.java](../src/ffvtraceability-common/src/main/java/org/dddml/ffvtraceability/domain/supplierproduct/DisableLogic.java)

可以看到，这些业务逻辑的实现代码十分“干净”，编写它们的时候我们基本不需要考虑到和“多租户”相关的技术细节。



【TBD：工具层面的改进已经基本完成，更多文档待更新】


### Test application

#### 测试“文档”

创建：

```shell
curl -X 'POST' \
  'http://localhost:1023/api/BffDocuments' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "comments": "Quality certification document for organic vegetables batch #2024001", 
  "documentLocation": "https://example.com/docs/cert/2024001.pdf",
  "documentText": "Batch #2024001 passed QA inspections"
}'
```

查询：

```shell
curl -X 'GET' \
  'http://localhost:1023/api/BffDocuments?page=0&size=20' \
  -H 'accept: application/json'
```


更新文档（注意将下面示例中的 `0CGN8Q7LSZANXF4VC9` 替换为实际的要更新的文档 ID）：

```shell
curl -X 'PUT' \
  'http://localhost:1023/api/BffDocuments/0CGN8Q7LSZANXF4VC9' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "comments": "Quality certification document for organic vegetables batch #2024001",
  "documentLocation": "https://example.com/docs/cert/2024022.pdf",
  "documentText": "Batch #2024001 passed QA inspections"
}'
```



#### 测试 "BffRawItem"

测试创建新的 BffRawItem：

```shell
curl -X 'POST' \
  'http://localhost:1023/api/BffRawItems' \
  -H 'accept: */*' \
  -H "X-TenantID:X" \
  -H 'Content-Type: application/json' \
  -d '{
  "productName": "Organic Green Tea",
  "description": "Premium organic green tea leaves from Japanese highlands, rich in antioxidants",
  "gtin": "4912345678901",
  "smallImageUrl": "https://example.com/images/green-tea-small.jpg",
  "mediumImageUrl": "https://example.com/images/green-tea-medium.jpg", 
  "largeImageUrl": "https://example.com/images/green-tea-large.jpg",
  "quantityUomId": "GRM",
  "quantityIncluded": 100,
  "piecesIncluded": 20,
  "statusId": "ACTIVE",
  "supplierId": "SUPPLIER_001"
}'
```

查询 BffRawItem：

```shell
curl -X 'GET' \
  'http://localhost:1023/api/BffRawItems?page=0&size=20' \
  -H 'accept: application/json' \
  -H "X-TenantID:X"
```

#### 测试 BATCH/Lot

创建 Lot：

```shell
curl -X 'POST' \
  'http://localhost:1023/api/BffLots' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "lotId": "LOT-2023.001",
  "gs1Batch": "LOT-2023.001",
  "quantity": 100,
  "expirationDate": "2034-12-18T08:53:18.475Z"
}'
```

查询 Lots：

```shell
curl -X 'GET' \
  'http://localhost:1023/api/BffLots?page=0&size=20' \
  -H 'accept: application/json'
```

更新 Lot：

```shell
curl -X 'PUT' \
  'http://localhost:1023/api/BffLots/LOT-2023.001' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "lotId": "LOT-2023.001",
  "quantity": 120,
  "expirationDate": "2034-12-18T08:53:18.475Z"
}'
```


#### 测试 Receiving

创建一个“收货单”：

```shell
curl -X 'POST' \
  'http://localhost:1023/api/BffReceipts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "partyIdTo": "FRESH_MART_DC",
  "partyIdFrom": "ORGANIC_FARM_01",
  "originFacilityId": "FARM_WAREHOUSE",
  "destinationFacilityId": "DC_FRESH",
  "primaryOrderId": "PO2024031501",
  "receivingItems": [
    {
      "productId": "ORGANIC_TOMATO_01",
      "lotId": "LOT20240315A",
      "locationSeqId": "FRESH_ZONE_A",
      "itemDescription": "Organic Tomato",
      "quantityAccepted": 500.00,
      "quantityRejected": 20.00,
      "casesAccepted": 25,
      "casesRejected": 1
    },
    {
      "productId": "ORGANIC_TOMATO_02",
      "lotId": "LOT20240315B",
      "locationSeqId": "FRESH_ZONE_B",
      "itemDescription": "Organic Tomato",
      "quantityAccepted": 500.00,
      "quantityRejected": 20.00,
      "casesAccepted": 25,
      "casesRejected": 1
    }
  ],
  "referenceDocuments": [
    {
      "documentId": "ASN2024031502"
    }
  ]
}'
```

查询收货单信息：

```shell
curl -X 'GET' \
  'http://localhost:1023/api/BffReceipts?page=0&size=20&documentIdOrItem=ORGANIC_TOMATO_02' \
  -H 'accept: application/json'
```

#### 测试 "BffSuppliers"

添加供应商：

```shell
curl -X 'POST' \
  'http://localhost:1023/api/BffSuppliers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID: X" \
  -d '{
  "supplierId": "SUP20240116001",
  "ggn": "4049929999999",         
  "gln": "5420000000008",         
  "externalId": "ORGANIC_FARM_01",
  "preferredCurrencyUomId": "USD",
  "description": "Organic Vegetables Farm - specializing in greenhouse vegetables with GLOBALG.A.P. certification"
}'

# GLOBALG.A.P. Number - 13位数字
# Global Location Number - 13位数字
```

查询供应商：

```shell
curl -X 'GET' \
  'http://localhost:1023/api/BffSuppliers?page=0&size=20' \
  -H 'accept: application/json'
```

查询单个供应商（注意将下面示例 URL 路径中的 `SUP20240116001` 替换为实际的供应商 ID）：

```shell
curl -X 'GET' \
  'http://localhost:1023/api/BffSuppliers/SUP20240116001' \
  -H 'accept: application/json'
```


#### Test "StatusItem"

下面我们使用实体 [`StatusItem`](../dddml/StatusItem.yaml) 作为示例，来测试“多租户”支持。

##### Create StatusItem

执行下面的命令会失败，因为我们想要在租户 `X` 下创建数据，但是当前上下文中没有找到租户 ID（因为租户上下文没有被正确设置）：

```shell
curl -X POST "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"commandId\":\"CMD_21\",\"requesterId\":\"REQUESTER_ID_21\",\"sequenceId\":\"21\",\"statusCode\":\"TEST_STATUS_CODE_21\",\"statusId\":\"TEST_STATUS_21\",\"tenantId\":\"X\"}"
```

执行下面的命令也会失败，虽然我们通过 HTTP Header 设置了上下文中的租户 ID， 
但是我们的 DDDML 模型中还明确要求 `StatusItem` 的 Id 要包含租户 ID（以租户 ID 开头或结尾），
而下面 POST 请求的 `statusId` 不满足要求：

```shell
curl -X POST "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "Content-Type: application/json" \
-H "X-TenantID:X" \
-d "{\"commandId\":\"CMD_21\",\"requesterId\":\"REQUESTER_ID_21\",\"sequenceId\":\"21\",\"statusCode\":\"TEST_STATUS_CODE_21\",\"statusId\":\"TEST_STATUS_21\",\"tenantId\":\"X\"}"
```

执行下面的命令会成功：

```shell
curl -X POST "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "Content-Type: application/json" \
-H "X-TenantID:X" \
-d "{\"commandId\":\"CMD_21\",\"requesterId\":\"REQUESTER_ID_21\",\"sequenceId\":\"21\",\"statusCode\":\"TEST_STATUS_CODE_21\",\"statusId\":\"X-TEST_STATUS_21\",\"tenantId\":\"X\"}"
```

##### Get StatusItems

执行下面的命令查看特定租户（`X`）的数据：

```shell
curl -X GET "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "X-TenantID:X"
```


#### Test "SupplierProduct"

我们对这个实体实现了 "Tenantized Id" 策略，将模型中定义的实体的 Id 在生成的代码中，自动转换为“包含租户 ID”的形式。

Create:

```shell
curl -X 'POST' \
  'http://localhost:1023/api/SupplierProducts' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "X-TenantID:X" \
  -d '{"commandId":"string","requesterId":"string","supplierProductAssocId":{"productId":"string","partyId":"string","currencyUomId":"string","minimumOrderQuantity":0,"availableFromDate":"2024-11-29T08:53:18.748Z"},"availableThruDate":"2024-11-29T08:53:18.748Z","supplierPrefOrderId":"string","supplierRatingTypeId":"string","active":true}'
```

Get all:

```shell
curl -X 'GET' \
  'http://localhost:1023/api/SupplierProducts' \
  -H 'accept: application/json' -H "X-TenantID:X"
```

Get:

```shell
curl -X 'GET' \
  'http://localhost:1023/api/SupplierProducts/%7B%22productId%22%3A%22string%22%2C%22partyId%22%3A%22string%22%2C%22currencyUomId%22%3A%22string%22%2C%22minimumOrderQuantity%22%3A0%2C%22availableFromDate%22%3A%222024-11-29T08%3A53%3A18.748Z%22%7D' \
  -H 'accept: application/json' -H "X-TenantID:X"
```

Get with filter:

```shell
curl -X 'GET' \
  'http://localhost:1023/api/SupplierProducts?supplierProductAssocId.productId=string&firstResult=0&maxResults=2147483647' \
  -H 'accept: application/json'  -H "X-TenantID:X"
```

Update AvailableThruDate:

```shell
curl -X 'PUT' \
  'http://localhost:1023/api/SupplierProducts/%7B%22productId%22%3A%22string%22%2C%22partyId%22%3A%22string%22%2C%22currencyUomId%22%3A%22string%22%2C%22minimumOrderQuantity%22%3A0%2C%22availableFromDate%22%3A%222024-11-29T08%3A53%3A18.748Z%22%7D/_commands/UpdateAvailableThruDate' \
  -H 'Content-Type: application/json' \
  -H 'accept: application/json' -H "X-TenantID:X" \
  -d '{"commandId":"UPDATE_AVAILABLE_THRU_DATE","requesterId":"REQUESTER_ID_11111","availableThruDate":"2024-11-29T08:53:18.748Z","version":0}'
```


Disable:

```shell
curl -X 'PUT' \
  'http://localhost:1023/api/SupplierProducts/%7B%22productId%22%3A%22string%22%2C%22partyId%22%3A%22string%22%2C%22currencyUomId%22%3A%22string%22%2C%22minimumOrderQuantity%22%3A0%2C%22availableFromDate%22%3A%222024-11-29T08%3A53%3A18.748Z%22%7D/_commands/Disable' \
  -H 'Content-Type: application/json' \
  -H 'accept: application/json' -H "X-TenantID:X" \
  -d '{"commandId":"DISABLE","requesterId":"REQUESTER_ID_22222","version":1}'
```

#### Test "Blog"

Create article:

```shell
curl -X 'POST' \
  'http://localhost:1023/api/Articles' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"commandId":"create_article_21","requesterId":"string","title":"hello_21","body":"world","author":"string","active":true,"tags":["string"],"comments":[{"commentSeqId":1,"commenter":"string","body":"test_comment"}]}'
```

Update body:

```shell
curl -X 'PUT' \
  'http://localhost:1023/api/Articles/21/_commands/UpdateBody' \
  -H 'Content-Type: application/json' \
  -H 'accept: application/json' \
  -d '{"commandId":"UPDATE_BODY_21","requesterId":"REQUESTER_ID_11111","body":"new_world","version":0}'
```
