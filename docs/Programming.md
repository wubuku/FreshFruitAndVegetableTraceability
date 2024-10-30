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

mvn package
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

Swagger UI:

```text
http://localhost:1023/api/swagger-ui/index.html
```


### 多租户支持

我们的应用在数据库访问层使用了 Hibernate ORM 框架，并且采用了基于鉴别器的多租户策略。

Hibernate 其实并[没有对基于鉴别器的多租户策略提供“原生支持”](Hibernate_多租户支持现状.md)，这个策略实际上是我们自己来实现的。


#### 我们的实现

我们编写了一个 [TenantFilter](../src/ffvtraceability-service-rest/src/main/java/org/dddml/ffvtraceability/servlet/TenantFilter.java)，它的作用是允许客户端通过 HTTP Header 来设置[租户上下文](../src/ffvtraceability-common/src/generated/java/org/dddml/ffvtraceability/domain/TenantContext.java)中的“当前租户 ID”。

当然，以后我们还可以支持其他方式设置租户上下文中的“当前租户 ID”，比如从 HTTP 请求的域名中解析出租户 ID。

【TBD：工具层面的改进已经基本完成，文档待更新】


### Test application

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
  -d '{"commandType":"string","commandId":"string","requesterId":"string","supplierProductAssocId":{"productId":"string","partyId":"string","currencyUomId":"string","minimumOrderQuantity":0,"availableFromDate":"2024-10-29T08:53:18.748Z"},"availableThruDate":"2024-10-29T08:53:18.748Z","supplierPrefOrderId":"string","supplierRatingTypeId":"string","active":true}'
```

Get:

```
curl -X 'GET' \
  'http://localhost:1023/api/SupplierProducts/string%2Cstring%2Cstring%2C0.000000%2C2024-10-29T08%3A53%3A18.748Z' \
  -H 'accept: application/json' -H "X-TenantID:X"
```

Get with filter:

```shell
curl -X 'GET' \
  'http://localhost:1023/api/SupplierProducts?supplierProductAssocId.productId=string&firstResult=0&maxResults=2147483647' \
  -H 'accept: application/json'  -H "X-TenantID:X"
```


