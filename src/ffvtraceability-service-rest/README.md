# RESTful API 服务


## 运行服务

在目录 `src` 下运行：
```shell
mvn -pl ffvtraceability-service-rest -am spring-boot:run
```

## 运行测试

在目录 `src` 下运行：

```shell
mvn -pl ffvtraceability-service-rest -am test
```


## 测试前端 OAuth2 授权码流程

见文件 `src/ffvtraceability-service-rest/src/main/resources/static/index.html`。


### Web 前端 OAuth2 授权码流程优化

开始我们使用 service-rest 模块的 TokenController 作为代理来访问 auth-server 的 token endpoint。
后面我们进行了优化，下面是对这一过程的讨论记录。

#### 问题背景

原有的授权码流程中，前端页面通过 service-rest 模块的 TokenController 作为代理来访问 auth-server 的 token endpoint。这种方式存在以下问题：

1. 多余的网络跳转
2. 缺乏必要的安全机制
3. 架构复杂度增加


#### 优化方案

##### 新增 WebTokenController

在 auth-server 中直接提供 Web 前端使用的 token endpoint：

```java
@RestController
@RequestMapping("/web-clients/oauth2")
public class WebTokenController {
    @PostMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("code") String code,
            @RequestParam("code_verifier") String codeVerifier,
            @RequestParam("redirect_uri") String redirectUri,
            HttpServletRequest request) {
        // 验证客户端
        // 转发到本地 token endpoint
        // 返回响应
    }
}
```

##### 配置更新

1. application.yml 中添加 web-clients 配置：

```yaml
auth-server:
  web-clients:
    allowed-client-ids: ${WEB_CLIENT_IDS:ffv-client,other-web-client}
    client-secrets: ${WEB_CLIENT_SECRETS:secret,other-secret}
```

2. 添加限流保护：

```java
@Configuration
public class WebSecurityConfig {
    @Bean
    public FilterRegistrationBean<Filter> rateLimitingFilter() {
        // 配置限流过滤器
        // 每个客户端每秒10个请求的限制
    }
}
```

3. CORS 配置优化：

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList(origins));
    configuration.setAllowCredentials(true);
    // ... 其他配置
}
```

##### 前端适配

更新前端代码，直接调用 auth-server 的新端点：

```javascript
const response = await fetch('http://localhost:9000/web-clients/oauth2/token', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: new URLSearchParams({
        client_id: CLIENT_ID,
        code: code,
        redirect_uri: REDIRECT_URI,
        code_verifier: codeVerifier
    })
});
```

#### 优化效果

1. 架构简化：
   - 移除了不必要的代理层
   - 减少了网络跳转
   - 简化了系统架构

2. 安全增强：
   - 添加了客户端白名单验证
   - 实现了请求限流保护
   - 集中式的安全控制

3. 性能提升：
   - 减少了网络延迟
   - 降低了系统负载
   - 提高了响应速度

#### 后续工作

1. 移除原有的 TokenController
2. 更新相关文档
3. 监控新端点的使用情况
4. 根据实际使用情况调整限流参数

#### 注意事项

1. 确保 CORS 配置正确
2. 妥善保管客户端密钥
3. 定期检查限流日志
4. 保持与其他 OAuth2 客户端的兼容性
