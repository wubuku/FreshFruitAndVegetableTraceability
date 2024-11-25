# 测试 OAuth2 资源服务器

本文档描述如何测试资源服务器。 为了测试资源服务器，我们需要先启动授权服务器（Auth Server）。
关于 Auth Server 的更多内容请参考 [Auth Server README](../ffvtraceability-auth-server/README.md)。


## 1. 启动授权服务器

首先，我们需要启动授权服务器：

```bash
# 进入授权服务器目录
cd src/ffvtraceability-auth-server

# 启动服务器
mvn spring-boot:run
```

## 2. 启动资源服务器

在另一个终端窗口中，启动资源服务器：

```bash
# 进入资源服务器目录
cd src/ffvtraceability-resource-server

# 启动服务器
mvn spring-boot:run
```

## 注意事项

1. 确保 PostgreSQL 数据库已启动
2. 确保授权服务器和资源服务器使用的是相同的数据库（或数据同步的主从库）
3. 可以修改 application.yml 中的缓存配置来调整缓存行为：
```yaml
spring:
  cache:
    caffeine:
      spec: maximumSize=100,expireAfterWrite=3600s
```

之所以特别提到缓存配置，是因为我们下面会介绍测试观察资源服务器上“权限组”的缓存机制。


## 关于如何获取访问令牌

可以在第三个终端窗口中，运行 Auth Server 的测试脚本获取访问令牌：

```bash
# 进入测试脚本目录
cd src/ffvtraceability-auth-server/scripts

# 运行测试脚本
./test.sh

# 脚本会生成 tokens.env 文件，其中包含访问令牌
# 查看并复制访问令牌
cat tokens.env
# 应该看到类似这样的输出：
# export ACCESS_TOKEN=eyJhbGciOiJSUzI1...
```


## E2E 测试

我们在 `src/ffvtraceability-resource-server/src/test/java/org/dddml/ffvtraceability/resource/E2EAuthFlowTests.java` 中编写了一个 E2E（端到端）测试，可以用来测试资源服务器的整个 OAuth2 流程。

它使用了真实的 HTTP Client 而不是 Mock 测试，所以需要启动授权服务器和资源服务器。



## 关于资源服务使用的缓存机制的测试

资源服务器中使用了 Spring Cache 来缓存“权限组”数据。（见：`src/ffvtraceability-resource-server/src/main/java/org/dddml/ffvtraceability/resource/service/GroupAuthorityService.java`）

我们写了一些测试代码来观察缓存的行为。

### 运行缓存测试客户端

在第四个终端窗口中，使用获取到的访问令牌运行测试客户端：

```bash
# 进入资源服务器目录
cd src/ffvtraceability-resource-server

# 设置访问令牌环境变量（替换为实际的令牌）
export ACCESS_TOKEN=eyJhbGciOiJSUzI1...

# 运行测试客户端
mvn exec:java -Dexec.mainClass="org.dddml.ffvtraceability.resource.CacheTestClient"
```

### 观察缓存行为

在资源服务器的日志输出中，你应该能看到：

1. 第一次请求时的缓存未命中日志：
```
Cache MISS - Loading authorities from database for group: GROUP_ADMIN_GROUP
```

2. 后续请求不会看到缓存未命中日志，说明使用了缓存

3. 等待缓存过期（默认1小时）后，又会看到缓存未命中日志


### 停止测试

可以随时按 `Ctrl+C` 停止测试客户端。


## 故障排除

1. 如果看到数据库连接错误，检查数据库配置
2. 如果看到令牌验证错误，确保使用了正确的访问令牌
3. 如果看不到缓存日志，检查日志级别配置：
```yaml
logging:
  level:
    org.springframework.cache: DEBUG
    org.dddml.ffvtraceability: DEBUG
```
