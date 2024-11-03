# Spring Security OAuth2 Resource Server JWT 认证授权


## 资源服务器配置

首先需要添加依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

资源服务器需要配置如何验证 JWT token。主要有两种方式：

### 1. 使用对称密钥（Secret Key）

在 `application.yml` 或 `application.properties` 中配置：

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # 使用对称密钥
          secret-key: your-256-bit-secret-key
```

### 2. 使用非对称密钥（Public Key）

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # 使用 jwk-set-uri（推荐）
          jwk-set-uri: http://auth-server/.well-known/jwks.json
          # 或者使用 issuer-uri（Spring Security 会自动找到 jwks.json）
          issuer-uri: http://auth-server
```

### 3. 完整配置示例

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # JWT 配置
          issuer-uri: http://auth-server
          jwk-set-uri: http://auth-server/.well-known/jwks.json
          # 可选：自定义 JWT 声明名称
          jws-algorithms: RS256
          audiences: your-client-id
          
server:
  port: 8081

logging:
  level:
    org.springframework.security: DEBUG  # 开发时可以开启调试日志
```

### 4. 最佳实践建议

1. **生产环境推荐使用 JWK**：
   - 更安全（使用非对称加密）
   - 支持密钥轮换
   - 无需在资源服务器配置敏感的密钥信息

2. **开发/测试环境**：
   - 可以使用对称密钥简化配置
   - 密钥至少 256 位（32字节）
   - 可以使用 Base64 编码存储密钥

3. **安全配置**：
   - 生产环境不要在配置文件中明文存储密钥
   - 考虑使用配置服务器或密钥管理系统
   - 建议启用 HTTPS


## JWT 转换器（JwtAuthenticationConverter）

默认情况下，如果你的 JWT 来自标准的 Spring Authorization Server，通常**不需要**自定义 JwtAuthenticationConverter。因为默认配置已经能够处理标准的 claims：

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                // 默认使用 JwtAuthenticationConverter
                // 会自动处理 scope 和 authorities
            ))
        .build();
}
```

## 标准 JWT 结构

Spring Authorization Server 生成的标准 JWT 通常包含以下 claims：

```json
{
  "iss": "http://auth-server.com",
  "sub": "user123",
  "aud": "client-id",
  "exp": 1616721821,
  "iat": 1616718221,
  "scope": ["read", "write"],     // 作用域
  "authorities": [                 // Spring Security 权限
    "ROLE_USER",
    "ROLE_ADMIN"
  ]
}
```

## Scope 与 Authorities

虽然最终都会转换为 GrantedAuthority，但 scope 和 authorities 在 OAuth2/OIDC 规范和应用架构中有不同的用途和含义：

### 1. Scope（作用域）
```json
"scope": ["read", "write", "profile"]
```
- 表示客户端的访问范围
- 是 OAuth2 规范的标准部分
- 更粗粒度的权限控制
- 主要用于客户端级别的授权

### 2. Authorities（权限）
```json
"authorities": ["ROLE_USER", "ROLE_ADMIN"]
```
- Spring Security 特有的概念
- 更细粒度的权限控制
- 主要用于用户级别的授权
- 不是 OAuth2 标准的一部分

## 权限转换过程

```java
// 1. Scope 转换
"scope": ["read", "write"]
↓
[
    new SimpleGrantedAuthority("SCOPE_read"),
    new SimpleGrantedAuthority("SCOPE_write")
]

// 2. Authorities 转换
"authorities": ["ROLE_USER", "ROLE_ADMIN"]
↓
[
    new SimpleGrantedAuthority("ROLE_USER"),
    new SimpleGrantedAuthority("ROLE_ADMIN")
]
```

## 自定义场景

只有在以下情况才需要自定义 JwtAuthenticationConverter：

```java
@Bean
JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    
    // 1. 如果你的权限claim使用了不同的名字
    grantedAuthoritiesConverter.setAuthoritiesClaimName("permissions");
    
    // 2. 如果你不想要 SCOPE_ 前缀
    grantedAuthoritiesConverter.setAuthorityPrefix("");
    
    // 3. 如果你需要自定义权限转换逻辑
    grantedAuthoritiesConverter.setConverter(claims -> {
        // 自定义转换逻辑
    });
    
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
}
```

## 实际应用示例

```java
@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .authorizeHttpRequests(authorize -> authorize
                // 基于 scope 的授权（客户端级别）
                .requestMatchers("/api/basic/**").hasAuthority("SCOPE_read")
                .requestMatchers("/api/advanced/**").hasAuthority("SCOPE_write")
                
                // 基于 authorities 的授权（用户级别）
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
            )
            .build();
    }
}

// 多租户系统示例
@PreAuthorize("hasAuthority('SCOPE_read') and hasRole('TENANT_ADMIN')")
public void someMethod() {
    // 客户端有 read 权限
    // 且用户是租户管理员
}

// 微服务架构示例
@PreAuthorize("hasAuthority('SCOPE_order-service') and hasRole('ORDER_MANAGER')")
public void processOrder() {
    // 客户端可以访问订单服务
    // 且用户是订单管理员
}
```

## 区分 Scope 和 Authorities 的好处

1. 符合职责分离原则
2. 支持更灵活的授权策略
3. 便于管理不同级别的权限
4. 与 OAuth2 标准保持兼容

虽然技术上都转换为 GrantedAuthority，但它们在业务含义和使用场景上是有区别的。
这种设计既保持了与 OAuth2 规范的兼容性，又提供了 Spring Security 特有的细粒度权限控制能力。


