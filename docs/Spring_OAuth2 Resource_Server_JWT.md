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



## Spring Security + OAuth2 Resource Server 自授权最小实现


### 依赖配置

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

### 核心代码

#### SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secretKey;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()));  // 会自动使用配置的 secret-key

        return http.build();
    }

    // 使用同一个密钥创建 encoder，用于生成 token
    @Bean
    public JwtEncoder jwtEncoder() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }
}
```

使用 Spring Security OAuth2 Resource Server 的默认配置，需要配置对称密钥：

```yaml
# application.yml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          secret-key: your-256-bit-secret-key
```

#### AuthController.java
```java
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private JwtEncoder jwtEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // 验证用户名密码
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // 生成JWT
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(36000L))
            .subject(authentication.getName())
            .claim("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
```

#### ApiController.java
```java
@RestController
@RequestMapping("/api")
public class ApiController {
    
    @GetMapping("/user-info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
            "username", jwt.getSubject(),
            "authorities", jwt.getClaims().get("authorities")
        );
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin-only")
    public String adminOnly() {
        return "Only admins can see this";
    }
}
```

#### LoginRequest.java
```java
public class LoginRequest {
    private String username;
    private String password;
    
    // getters and setters
}
```



### API 使用说明

#### 1. 获取令牌
```http
POST /auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "password"
}
```

#### 2. 使用令牌访问 API
```http
GET /api/user-info
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 实现特点

1. **简单直接**
   - 无需外部授权服务器
   - 自包含的认证授权功能
   - 基于 JWT 的无状态设计

2. **标准安全特性**
   - 支持基于权限的访问控制
   - JWT 令牌验证
   - 可配置的令牌过期时间

3. **易于扩展**
   - 可以方便地添加新的安全规则
   - 支持自定义认证逻辑
   - 为将来迁移到完整 OAuth2 架构预留空间

### 注意事项

1. 生产环境使用时需要：
   - 使用强密钥
   - 配置适当的令牌过期时间
   - 实现用户管理功能
   - 添加必要的安全头部
   - 考虑实现令牌刷新机制

2. 安全建议：
   - 使用 HTTPS
   - 实现请求限流
   - 添加审计日志
   - 定期轮换密钥


## 主要面向 Android 等原生应用的 RESTful APIs 自包含认证授权服务

本文介绍如何在 Spring Boot 项目中实现一个简单的自包含认证授权服务，主要面向 Android 等原生应用的 RESTful APIs。

### 核心配置

#### application.yml
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          secret-key: your-256-bit-secret-key  # 生产环境请使用更安全的方式存储
```

#### SecurityConfig.java
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 认证相关代码

#### LoginRequest.java
```java
public class LoginRequest {
    private String username;
    private String password;
    
    // getters and setters
}
```

#### AuthController.java
```java
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private JwtEncoder jwtEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        // 验证用户名密码
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // 生成JWT
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(36000L))  // 10小时过期
            .subject(authentication.getName())
            .claim("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
            .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
        return new TokenResponse(token);
    }
}
```

#### TokenResponse.java
```java
public class TokenResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn = 36000L;  // 10小时，与token生成时一致

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // getters
}
```

### API 示例

#### ApiController.java
```java
@RestController
@RequestMapping("/api")
public class ApiController {
    
    @GetMapping("/user-info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
            "username", jwt.getSubject(),
            "authorities", jwt.getClaims().get("authorities")
        );
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin-only")
    public String adminOnly() {
        return "Only admins can see this";
    }
}
```

### API 使用说明

#### 1. 登录获取令牌
```http
POST /auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "password"
}
```

响应示例：
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 36000
}
```

#### 2. 使用令牌访问 API
```http
GET /api/user-info
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Android 客户端示例

```kotlin
class ApiClient {
    private val retrofit: Retrofit = // Retrofit 配置
    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    
    private var tokenResponse: TokenResponse? = null
    
    suspend fun login(username: String, password: String) {
        tokenResponse = authApi.login(LoginRequest(username, password))
    }
    
    suspend fun getUserInfo(): UserInfo {
        tokenResponse?.let { token ->
            return apiService.getUserInfo("Bearer ${token.accessToken}")
        } ?: throw IllegalStateException("Not logged in")
    }
}

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse
}

interface ApiService {
    @GET("api/user-info")
    suspend fun getUserInfo(@Header("Authorization") authHeader: String): UserInfo
}
```

### 注意事项

1. **安全配置**
   - 使用 HTTPS
   - 使用强密钥
   - 合理设置令牌过期时间
   - 实现请求限流

2. **生产环境建议**
   - 使用配置服务器或密钥管理系统
   - 实现令牌刷新机制
   - 添加设备绑定机制
   - 实现用户管理功能

3. **监控和日志**
   - 记录认证事件
   - 监控异常登录
   - 实现审计日志

### 扩展建议

1. **令牌刷新**
   - 添加 refresh_token 支持
   - 实现令牌自动刷新

2. **设备管理**
   - 添加设备注册
   - 实现设备绑定
   - 支持多设备管理

3. **安全增强**
   - 添加 rate limiting
   - 实现密钥轮换
   - 支持双因素认证


## 扩展功能实现

### JWT 失效方案

#### 基于数据库的 JWT 失效方案

##### 1. 创建失效令牌表
```sql
CREATE TABLE revoked_tokens (
    jti VARCHAR(255) PRIMARY KEY,
    revoked_at TIMESTAMP NOT NULL,
    revoked_by VARCHAR(255) NOT NULL,
    reason VARCHAR(255),
    expires_at TIMESTAMP NOT NULL
);
```

##### 2. 令牌失效服务
```java
@Service
@Transactional
public class TokenRevocationService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void revokeToken(String jti, String revokedBy, String reason, Instant expiresAt) {
        String sql = """
            INSERT INTO revoked_tokens (jti, revoked_at, revoked_by, reason, expires_at)
            VALUES (?, ?, ?, ?, ?)
            """;
            
        jdbcTemplate.update(sql,
            jti,
            Timestamp.from(Instant.now()),
            revokedBy,
            reason,
            Timestamp.from(expiresAt)
        );
    }
    
    public boolean isTokenRevoked(String jti) {
        String sql = """
            SELECT COUNT(1) FROM revoked_tokens 
            WHERE jti = ? AND expires_at > ?
            """;
            
        int count = jdbcTemplate.queryForObject(sql,
            Integer.class,
            jti,
            Timestamp.from(Instant.now())
        );
        
        return count > 0;
    }
    
    // 清理过期记录（可以通过定时任务调用）
    public void cleanupExpiredTokens() {
        String sql = "DELETE FROM revoked_tokens WHERE expires_at <= ?";
        jdbcTemplate.update(sql, Timestamp.from(Instant.now()));
    }
}
```

##### 3. JWT 认证转换器
```java
@Component
public class ResourceServerJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    @Autowired
    private TokenRevocationService revocationService;
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 检查令牌是否被撤销
        String jti = jwt.getId();
        if (jti != null && revocationService.isTokenRevoked(jti)) {
            throw new BadJwtException("Token has been revoked");
        }
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // 添加直接权限
        getClaimAsSet(jwt, "directAuthorities")
            .stream()
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
            
        // 从组恢复权限
        getClaimAsSet(jwt, "groups")
            .stream()
            .map(this::getGroupAuthorities)
            .flatMap(Set::stream)
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
        
        return new JwtAuthenticationToken(jwt, authorities);
    }
    
    // ... 其他现有方法 ...
}
```

##### 4. 管理接口
```java
@RestController
@RequestMapping("/admin/tokens")
public class TokenManagementController {
    
    @Autowired
    private TokenRevocationService revocationService;
    
    @PostMapping("/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokeToken(
            @RequestParam String jti,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant expiresAt,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal Jwt currentUserJwt) {
            
        revocationService.revokeToken(
            jti,
            currentUserJwt.getSubject(),
            reason,
            expiresAt
        );
        
        return ResponseEntity.ok().build();
    }
}
```

##### 5. 定时清理任务（可选）
```java
@Configuration
@EnableScheduling
public class SchedulingConfig {
    
    @Autowired
    private TokenRevocationService revocationService;
    
    @Scheduled(cron = "0 0 2 * * *")  // 每天凌晨2点执行
    public void cleanupExpiredTokens() {
        revocationService.cleanupExpiredTokens();
    }
}
```

#### 使用说明

1. **生成 JWT 时确保包含 JTI**：
```java
public String generateToken(Authentication authentication) {
    return Jwts.builder()
        .setId(UUID.randomUUID().toString())  // 添加 JTI
        .setClaims(claims)
        .setSubject(authentication.getName())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .compact();
}
```

2. **撤销令牌**：
```http
POST /admin/tokens/revoke?jti=123&expiresAt=2024-12-31T23:59:59Z&reason=Security%20concern
Authorization: Bearer <admin-token>
```

#### 优势

1. 简单的实现方式
2. 不需要额外的基础设施
3. 支持审计追踪
4. 自动清理过期记录

#### 注意事项

1. 数据库查询性能
   - 建议在 `jti` 列上创建索引
   - 定期清理过期记录
   - 考虑添加应用层缓存（如果需要）

2. 并发处理
   - 使用事务确保数据一致性
   - 考虑数据库锁的影响

3. 存储空间
   - 监控表的增长情况
   - 及时清理过期记录


#### 基于数据库的 JWT 失效方案（带本地缓存）

##### 1. 添加 Caffeine 缓存依赖
```xml
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

##### 2. 改进的令牌失效服务
```java
@Service
@Transactional
public class TokenRevocationService {
    
    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> revocationCache;
    
    public TokenRevocationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // 配置本地缓存
        this.revocationCache = Caffeine.newBuilder()
            .maximumSize(10_000)                 // 最多缓存1万个记录
            .expireAfterWrite(1, TimeUnit.HOURS) // 缓存1小时后过期
            .build();
    }
    
    public void revokeToken(String jti, String revokedBy, String reason, Instant expiresAt) {
        String sql = """
            INSERT INTO revoked_tokens (jti, revoked_at, revoked_by, reason, expires_at)
            VALUES (?, ?, ?, ?, ?)
            """;
            
        jdbcTemplate.update(sql,
            jti,
            Timestamp.from(Instant.now()),
            revokedBy,
            reason,
            Timestamp.from(expiresAt)
        );
        
        // 更新缓存
        revocationCache.put(jti, Boolean.TRUE);
    }
    
    public boolean isTokenRevoked(String jti) {
        // 先检查缓存
        Boolean cached = revocationCache.getIfPresent(jti);
        if (cached != null) {
            return cached;
        }
        
        // 缓存未命中，查询数据库
        String sql = """
            SELECT COUNT(1) FROM revoked_tokens 
            WHERE jti = ? AND expires_at > ?
            """;
            
        int count = jdbcTemplate.queryForObject(sql,
            Integer.class,
            jti,
            Timestamp.from(Instant.now())
        );
        
        boolean isRevoked = count > 0;
        // 只缓存已撤销的令牌，未撤销的不缓存（避免缓存穿透）
        if (isRevoked) {
            revocationCache.put(jti, true);
        }
        
        return isRevoked;
    }
    
    public void cleanupExpiredTokens() {
        String sql = "DELETE FROM revoked_tokens WHERE expires_at <= ?";
        int deleted = jdbcTemplate.update(sql, Timestamp.from(Instant.now()));
        
        // 如果有记录被删除，清空缓存
        if (deleted > 0) {
            revocationCache.invalidateAll();
        }
    }
}
```

##### 3. 缓存监控（可选）
```java
@RestController
@RequestMapping("/admin/system")
public class SystemMonitorController {
    
    @Autowired
    private TokenRevocationService revocationService;
    
    @GetMapping("/cache/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getCacheStats() {
        Cache<String, Boolean> cache = revocationService.getRevocationCache();
        CacheStats stats = cache.stats();
        
        return Map.of(
            "hitCount", stats.hitCount(),
            "missCount", stats.missCount(),
            "hitRate", stats.hitRate(),
            "size", cache.estimatedSize()
        );
    }
}
```

#### 缓存策略说明

1. **缓存配置**
   - 最大缓存数量：10,000条
   - 缓存过期时间：1小时
   - 仅缓存已撤销的令牌

2. **缓存更新策略**
   - 撤销令牌时直接更新缓存
   - 清理过期记录时清空缓存
   - 只缓存阳性结果（已撤销的令牌）

3. **性能优化**
   - 避免缓存穿透（不缓存未撤销的令牌）
   - 定期清理过期记录
   - 缓存统计监控

#### 使用示例

1. **检查令牌状态**：
```java
@Component
public class ResourceServerJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    @Autowired
    private TokenRevocationService revocationService;
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String jti = jwt.getId();
        // 快速检查令牌是否被撤销（使用缓存）
        if (jti != null && revocationService.isTokenRevoked(jti)) {
            throw new BadJwtException("Token has been revoked");
        }
        
        // ... 其余代码保持不变 ...
    }
}
```

#### 优势

1. **高性能**
   - 本地缓存，无网络开销
   - 只缓存必要的数据
   - 自动过期管理

2. **低复杂度**
   - 无需额外服务
   - 简单的缓存策略
   - 易于维护

3. **可靠性**
   - 缓存未命中时自动回退到数据库
   - 定期清理过期数据
   - 支持缓存统计和监控

#### 注意事项

1. **内存使用**
   - 监控缓存大小
   - 适当调整最大缓存数量
   - 注意 JVM 内存配置

2. **缓存一致性**
   - 缓存过期时间要小于令牌过期时间
   - 集群环境下考虑缓存同步问题
   - 关键操作时主动清除缓存

3. **监控**
   - 定期检查缓存命中率
   - 监控内存使用情况
   - 观察数据库查询频率
