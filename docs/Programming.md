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

### Test application

#### 多租户支持

我们编写了一个 [TenantFilter](../src/ffvtraceability-service-rest/src/main/java/org/dddml/ffvtraceability/servlet/TenantFilter.java)，它的作用是允许客户端通过 HTTP Header 来设置[租户上下文](../src/ffvtraceability-common/src/generated/java/org/dddml/ffvtraceability/domain/TenantContext.java)中的“当前租户 ID”。

当然，以后我们还可以支持其他方式设置租户上下文中的“当前租户 ID”，比如从 HTTP 请求的域名中解析出租户 ID。

下面我们使用实体 [`StatusItem`](../dddml/StatusItem.yaml) 作为示例，来测试“多租户”支持。

#### Create StatusItem

执行下面的命令会失败，因为我们想要在租户 `X` 下创建数据，但是当前上下文中没有找到租户 ID（因为租户上下文没有被正确设置）：

```shell
curl -X POST "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"commandId\":\"CMD_17\",\"requesterId\":\"REQUESTER_ID_17\",\"sequenceId\":\"17\",\"statusCode\":\"TEST_STATUS_CODE_17\",\"statusId\":\"TEST_STATUS_17\",\"tenantId\":\"X\"}"
```

执行下面的命令也会失败，虽然我们通过 HTTP Header 设置了上下文中的租户 ID， 
但是我们的 DDDML 模型中还明确要求 `StatusItem` 的 Id 要包含租户 ID（以租户 ID 开头或结尾），
而下面 POST 请求的 `statusId` 不满足要求：

```shell
curl -X POST "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "Content-Type: application/json" \
-H "X-TenantID:X" \
-d "{\"commandId\":\"CMD_17\",\"requesterId\":\"REQUESTER_ID_17\",\"sequenceId\":\"17\",\"statusCode\":\"TEST_STATUS_CODE_17\",\"statusId\":\"TEST_STATUS_17\",\"tenantId\":\"X\"}"
```

执行下面的命令会成功：

```shell
curl -X POST "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "Content-Type: application/json" \
-H "X-TenantID:X" \
-d "{\"commandId\":\"CMD_17\",\"requesterId\":\"REQUESTER_ID_17\",\"sequenceId\":\"17\",\"statusCode\":\"TEST_STATUS_CODE_17\",\"statusId\":\"X-TEST_STATUS_17\",\"tenantId\":\"X\"}"
```

#### Get StatusItems

执行下面的命令查看特定租户（`X`）的数据：

```shell
curl -X GET "http://localhost:1023/api/StatusItems" -H "accept: application/json" -H "X-TenantID:X"
```


