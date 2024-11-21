# Spring Security CORS 配置指南

## 什么是 CORS？

**CORS (Cross-Origin Resource Sharing) 跨源资源共享**是一种浏览器安全机制：
1. 浏览器默认禁止网页向不同源（协议、域名、端口）的服务器发送请求
2. 服务器需要通过特定的 HTTP 响应头来明确允许跨源请求
3. 这是浏览器的同源策略（Same-Origin Policy）的一种延伸机制

## Spring Security 中的 CORS 配置

### 默认配置

Spring Security 提供了简单的默认配置：
```java
.cors(withDefaults())
```

默认配置特点：
- 非常严格
- 不允许任何跨源请求
- 不设置任何 CORS 响应头
- 实际上相当于完全禁用了跨源访问

### 自定义配置示例

通常需要自定义 CORS 配置：
```java
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");  // 开发环境配置
        configuration.addAllowedMethod("*");                      // 允许所有 HTTP 方法
        configuration.addAllowedHeader("*");                      // 允许所有请求头
        configuration.setAllowCredentials(true);                  // 允许发送认证信息

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())  // 启用 CORS 支持
            // ... 其他配置 ...
        return http.build();
    }
}
```

## 配置建议

### 生产环境配置

```java
// 通过配置文件管理允许的域名
configuration.addAllowedOrigin("https://your-production-domain.com");
```

### 精确的方法控制

```java
// 只允许需要的 HTTP 方法
configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
```

### 精确的请求头控制

```java
// 只允许必要的请求头
configuration.setAllowedHeaders(Arrays.asList(
    "Authorization",
    "Content-Type",
    "Accept"
));
```

### 性能优化

```java
// 设置浏览器缓存 CORS 检查结果的时间（秒）
configuration.setMaxAge(3600L);
```

## 配置生效机制

CORS 配置需要两个部分配合才能生效：

1. **CorsConfigurationSource Bean 的定义**
2. **SecurityFilterChain 中的 .cors() 启用**

工作流程：
1. `.cors(withDefaults())` 查找 `CorsConfigurationSource` 类型的 Bean
2. 如果找到自定义 Bean，使用该配置
3. 如果没找到，使用默认配置

## 验证配置

### 通过浏览器开发者工具

检查响应头是否包含：
```http
Access-Control-Allow-Origin: http://localhost:3000
Access-Control-Allow-Methods: *
Access-Control-Allow-Headers: *
Access-Control-Allow-Credentials: true
```

### 通过代码测试

```java
@RestController
// @CrossOrigin  // 如果需要这个注解，说明 Security 配置可能没生效
public class TestController {
    // ...
}
```

## 常见问题

### 配置未生效的可能原因

1. Bean 名称冲突
2. Spring Security 配置顺序问题
3. 其他地方有覆盖配置

### Spring MVC 的额外配置

不建议同时使用 Spring MVC 的 CORS 配置：
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 避免使用这种配置方式
    }
}
```

## 最佳实践

1. 使用配置文件管理允许的源
2. 根据实际需求限制允许的方法和请求头
3. 区分开发和生产环境的配置
4. 定期审查 CORS 配置，确保安全性
5. 保持单一配置方式，优先使用 Spring Security 的 CORS 配置
