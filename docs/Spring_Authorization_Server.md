# Spring Authorization Server

## Spring Authorization Server 最小化生产实现

本文介绍如何使用 Spring Authorization Server 和 Spring Security 标准组件实现一个认证授权服务，主要面向 Android 等原生应用的 RESTful APIs。

### 1. 项目配置

#### 1.1 Maven 依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>auth-server</artifactId>
    <version>1.0.0</version>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot 基础依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Spring Security 和 OAuth2 相关依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-oauth2-authorization-server</artifactId>
            <version>1.2.1</version>
        </dependency>
        
        <!-- JDBC 相关依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```

#### 1.2 应用配置
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_server
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  
  sql:
    init:
      mode: always
      schema-locations: 
        - classpath:org/springframework/security/core/userdetails/jdbc/users.ddl
        - classpath:schema.sql

server:
  port: 9000

logging:
  level:
    org.springframework.security: INFO
    org.springframework.security.oauth2: INFO
```

### 2. 数据库初始化

#### 2.1 Spring Security 标准用户表
Spring Security 的标准用户表结构已包含在依赖中，会自动创建：
- users (username, password, enabled)
- authorities (username, authority)

#### 2.2 OAuth2 相关表
```sql
-- schema.sql
-- OAuth2 注册客户端表
CREATE TABLE IF NOT EXISTS oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);

-- OAuth2 授权表
CREATE TABLE IF NOT EXISTS oauth2_authorization (
    id varchar(100) NOT NULL,
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorization_grant_type varchar(100) NOT NULL,
    authorized_scopes varchar(1000) DEFAULT NULL,
    attributes text DEFAULT NULL,
    state varchar(500) DEFAULT NULL,
    authorization_code_value text DEFAULT NULL,
    authorization_code_issued_at timestamp DEFAULT NULL,
    authorization_code_expires_at timestamp DEFAULT NULL,
    authorization_code_metadata text DEFAULT NULL,
    access_token_value text DEFAULT NULL,
    access_token_issued_at timestamp DEFAULT NULL,
    access_token_expires_at timestamp DEFAULT NULL,
    access_token_metadata text DEFAULT NULL,
    access_token_type varchar(100) DEFAULT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    refresh_token_value text DEFAULT NULL,
    refresh_token_issued_at timestamp DEFAULT NULL,
    refresh_token_expires_at timestamp DEFAULT NULL,
    refresh_token_metadata text DEFAULT NULL,
    user_code_value text DEFAULT NULL,
    user_code_issued_at timestamp DEFAULT NULL,
    user_code_expires_at timestamp DEFAULT NULL,
    user_code_metadata text DEFAULT NULL,
    device_code_value text DEFAULT NULL,
    device_code_issued_at timestamp DEFAULT NULL,
    device_code_expires_at timestamp DEFAULT NULL,
    device_code_metadata text DEFAULT NULL,
    PRIMARY KEY (id)
);

-- OAuth2 授权确认表
CREATE TABLE IF NOT EXISTS oauth2_authorization_consent (
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorities varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);
```


### 3. 核心配置

#### 3.1 用户管理配置
```java
@Configuration
public class UserManagementConfig {
    
    @Bean
    public JdbcUserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        
        // 创建初始管理员用户（如果不存在）
        if (!users.userExists("admin")) {
            UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin_initial_password"))
                .roles("ADMIN")
                .build();
            users.createUser(admin);
        }
        
        return users;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### 3.2 授权服务器配置
```java
@Configuration
public class AuthorizationServerConfig {
    
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            RegisteredClientRepository registeredClientRepository) throws Exception {
            
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .tokenEndpoint(tokenEndpoint ->
                tokenEndpoint
                    .accessTokenRequestConverter(
                        new OAuth2ResourceOwnerPasswordAuthenticationConverter())
                    .authenticationProvider(
                        new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                            authenticationManager(http),
                            tokenGenerator(),
                            registeredClientRepository)));
        
        return http.build();
    }
    
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()));
        
        return http.build();
    }
    
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // 预配置企业移动应用客户端
        RegisteredClient mobileClient = RegisteredClient.withId("mobile-client")
            .clientId("mobile-client")
            .clientSecret("{noop}mobile-secret")  // 生产环境应使用加密的密钥
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope("api.read")
            .scope("api.write")
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(12))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .reuseRefreshTokens(false)
                .build())
            .build();

        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);
        repository.save(mobileClient);
        
        return repository;
    }
    
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
    
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .issuer("http://localhost:9000")
            .build();
    }
    
    private static KeyPair generateRsaKey() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
```

#### 3.3 API 示例
```java
@RestController
@RequestMapping("/api")
public class ApiController {
    
    private final JdbcUserDetailsManager userDetailsManager;
    
    public ApiController(JdbcUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }
    
    @GetMapping("/user/profile")
    public Map<String, Object> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        UserDetails user = userDetailsManager.loadUserByUsername(username);
        
        return Map.of(
            "username", username,
            "authorities", user.getAuthorities()
        );
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/users")
    public void createUser(@RequestBody CreateUserRequest request) {
        UserDetails user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(request.getRoles().toArray(new String[0]))
            .build();
            
        userDetailsManager.createUser(user);
    }
}
```


### 4. 认证流程和客户端集成

#### 4.1 认证流程说明

##### 4.1.1 首次登录（Password 模式）
```http
POST http://localhost:9000/oauth2/token
Authorization: Basic bW9iaWxlLWNsaWVudDptb2JpbGUtc2VjcmV0
Content-Type: application/x-www-form-urlencoded

grant_type=password&username=user1&password=pwd1&scope=api.read api.write
```

响应示例：
```json
{
    "access_token": "eyJhbGciOiJSUzI1...",
    "refresh_token": "eyJhbGciOiJSUzI1...",
    "token_type": "Bearer",
    "expires_in": 43200,
    "scope": "api.read api.write"
}
```

##### 4.1.2 令牌续期（Refresh Token 模式）
```http
POST http://localhost:9000/oauth2/token
Authorization: Basic bW9iaWxlLWNsaWVudDptb2JpbGUtc2VjcmV0
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token&refresh_token=eyJhbGciOiJSUzI1...
```

#### 4.2 Android 客户端实现

##### 4.2.1 数据模型
```kotlin
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Long
)

data class AuthState(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresAt: Long
)
```

##### 4.2.2 认证管理器
```kotlin
class AuthManager(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) {
    private val clientAuth = Credentials.basic("mobile-client", "mobile-secret")
    
    // 首次登录或重新登录
    suspend fun login(username: String, password: String) {
        try {
            val response = authApi.login(
                authorization = clientAuth,
                grantType = "password",
                username = username,
                password = password
            )
            saveAuthState(response)
        } catch (e: Exception) {
            throw AuthException("Login failed", e)
        }
    }
    
    // 使用刷新令牌续期
    suspend fun refreshToken() {
        val currentRefreshToken = tokenStore.getRefreshToken()
            ?: throw AuthException("No refresh token available")
            
        try {
            val response = authApi.refresh(
                authorization = clientAuth,
                grantType = "refresh_token",
                refreshToken = currentRefreshToken
            )
            saveAuthState(response)
        } catch (e: Exception) {
            // 刷新失败，可能需要重新登录
            tokenStore.clearTokens()
            throw AuthException("Token refresh failed", e)
        }
    }
    
    // 确保有效的访问令牌
    suspend fun ensureValidToken(): String {
        val authState = tokenStore.getAuthState()
            ?: throw AuthException("No authentication state")
            
        return when {
            // 访问令牌仍然有效
            !isTokenExpired(authState) -> authState.accessToken
            
            // 尝试使用刷新令牌
            else -> {
                refreshToken()
                tokenStore.getAuthState()?.accessToken
                    ?: throw AuthException("Failed to refresh token")
            }
        }
    }
    
    private fun saveAuthState(response: TokenResponse) {
        val expiresAt = System.currentTimeMillis() + (response.expiresIn * 1000)
        val authState = AuthState(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            tokenType = response.tokenType,
            expiresAt = expiresAt
        )
        tokenStore.saveAuthState(authState)
    }
    
    private fun isTokenExpired(authState: AuthState): Boolean {
        // 提前5分钟认为令牌过期，以避免边界情况
        return System.currentTimeMillis() >= (authState.expiresAt - 300_000)
    }
}
```

##### 4.2.3 令牌存储
```kotlin
@Singleton
class TokenStore @Inject constructor(
    private val encryptedPreferences: SharedPreferences
) {
    fun saveAuthState(authState: AuthState) {
        encryptedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, authState.accessToken)
            putString(KEY_REFRESH_TOKEN, authState.refreshToken)
            putString(KEY_TOKEN_TYPE, authState.tokenType)
            putLong(KEY_EXPIRES_AT, authState.expiresAt)
        }.apply()
    }
    
    fun getAuthState(): AuthState? {
        val accessToken = encryptedPreferences.getString(KEY_ACCESS_TOKEN, null)
            ?: return null
        val refreshToken = encryptedPreferences.getString(KEY_REFRESH_TOKEN, null)
            ?: return null
        val tokenType = encryptedPreferences.getString(KEY_TOKEN_TYPE, null)
            ?: return null
        val expiresAt = encryptedPreferences.getLong(KEY_EXPIRES_AT, 0)
        
        return AuthState(accessToken, refreshToken, tokenType, expiresAt)
    }
    
    fun getRefreshToken(): String? {
        return encryptedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }
    
    fun clearTokens() {
        encryptedPreferences.edit().clear().apply()
    }
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_TYPE = "token_type"
        private const val KEY_EXPIRES_AT = "expires_at"
    }
}
```

##### 4.2.4 API 客户端
```kotlin
class ApiClient(
    private val authManager: AuthManager,
    private val apiService: ApiService
) {
    suspend fun <T> executeAuthorized(
        apiCall: suspend (String) -> T
    ): T {
        val token = authManager.ensureValidToken()
        return try {
            apiCall("Bearer $token")
        } catch (e: Exception) {
            when {
                e is HttpException && e.code() == 401 -> {
                    // 令牌可能在其他设备被撤销，尝试刷新
                    val newToken = authManager.ensureValidToken()
                    apiCall("Bearer $newToken")
                }
                else -> throw e
            }
        }
    }
    
    suspend fun getUserProfile(): UserProfile {
        return executeAuthorized { token ->
            apiService.getUserProfile(token)
        }
    }
}
```

#### 4.3 使用示例
```kotlin
class MainViewModel(
    private val authManager: AuthManager,
    private val apiClient: ApiClient
) : ViewModel() {
    
    // 登录
    fun login(username: String, password: String) = viewModelScope.launch {
        try {
            authManager.login(username, password)
            // 登录成功，更新UI
        } catch (e: AuthException) {
            // 处理登录失败
        }
    }
    
    // 获取用户信息
    fun getUserProfile() = viewModelScope.launch {
        try {
            val profile = apiClient.getUserProfile()
            // 处理用户信息
        } catch (e: Exception) {
            when (e) {
                is AuthException -> {
                    // 认证错误，可能需要重新登录
                }
                else -> {
                    // 其他错误处理
                }
            }
        }
    }
}
```



### 5. 安全和最佳实践

#### 5.1 令牌管理
```kotlin
// 使用 EncryptedSharedPreferences 安全存储令牌
@Provides
@Singleton
fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
        
    return EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
```

#### 5.2 自动令牌刷新的网络拦截器
```kotlin
class AuthInterceptor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 跳过认证端点
        if (originalRequest.url.encodedPath.contains("/oauth2/token")) {
            return chain.proceed(originalRequest)
        }
        
        // 确保有有效的访问令牌
        val accessToken = runBlocking {
            authManager.ensureValidToken()
        }
        
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
            
        return chain.proceed(authenticatedRequest)
    }
}
```

#### 5.3 生产环境配置建议

##### 5.3.1 服务端配置
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.company.com  # 使用正式域名
          jws-algorithms: RS256  # 指定签名算法
  
  datasource:
    url: jdbc:postgresql://db.company.com:5432/auth_db
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
      
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${KEY_STORE_PASSWORD}
    key-store-type: PKCS12
```

##### 5.3.2 安全配置
```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // ... 其他配置
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers
                .frameOptions().deny()
                .xssProtection().block(true)
                .contentSecurityPolicy("default-src 'self'"))
            .build();
    }
}
```

#### 5.4 客户端安全建议

1. **令牌存储**
   - 使用 EncryptedSharedPreferences 存储令牌
   - 应用退出时清除访问令牌
   - 定期验证令牌有效性

2. **网络安全**
   - 使用 SSL 证书固定
   - 实现请求重试机制
   - 处理网络错误

3. **用户体验**
   - 实现无缝令牌刷新
   - 提供离线支持
   - 优雅处理认证失败

#### 5.5 监控和日志

1. **服务端监控**
   ```java
   @Component
   public class AuthenticationEventListener {
       
       @EventListener
       public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
           // 记录成功登录
       }
       
       @EventListener
       public void onAuthenticationFailure(AuthenticationFailureEvent event) {
           // 记录失败尝试
       }
   }
   ```

2. **客户端日志**
   ```kotlin
   class AuthLogger {
       fun logAuthEvent(event: AuthEvent) {
           when (event) {
               is AuthEvent.LoginSuccess -> {
                   // 记录成功登录
               }
               is AuthEvent.TokenRefresh -> {
                   // 记录令牌刷新
               }
               is AuthEvent.AuthError -> {
                   // 记录认证错误
               }
           }
       }
   }
   ```

#### 5.6 故障排除清单

1. **常见问题**
   - 令牌过期处理
   - 网络超时处理
   - 并发请求处理
   - 设备时间不准确

2. **解决方案**
   - 实现令牌预刷新
   - 使用适当的重试策略
   - 实现请求队列
   - 服务器时间同步

3. **应急预案**
   - 紧急令牌撤销机制
   - 降级服务支持
   - 备用认证方案
