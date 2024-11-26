# OAuth2 授权码流程与安全实践详解

## 1. Web 前端实现分析

### 1.1 浏览器的 CORS 预检机制

浏览器在发送跨域请求前，会先发送预检请求：

```http
OPTIONS /oauth2/token
Host: localhost:9000
Origin: http://localhost:1023
Access-Control-Request-Method: POST
Access-Control-Request-Headers: authorization,content-type
```

关键限制：预检请求不能携带认证信息（如 Authorization 头）。

### 1.2 完整的授权流程

1. 授权请求：
```http
GET /oauth2/authorize?
    response_type=code
    &client_id=ffv-client
    &redirect_uri=http://localhost:1023/api/index.html
    &scope=openid+profile
    &code_challenge=xxx
    &code_challenge_method=S256
Host: localhost:9000
```

2. 授权码回调：
```http
GET /api/index.html?code=xyz123...
Host: localhost:1023
```

3. Token 请求：
```http
# 前端到后端代理
POST /api/oauth2/token
Host: localhost:1023
Content-Type: application/x-www-form-urlencoded

# 后端代理到授权服务器
POST /oauth2/token
Host: localhost:9000
Authorization: Basic ZmZ2LWNsaWVudDpzZWNyZXQ=
```

### 1.3 Spring Security 的实现机制

```java
// OAuth2TokenEndpointFilter 的关键实现
protected void doFilterInternal(HttpServletRequest request...) {
    // 1. 提取客户端认证信息
    Authentication clientAuthentication = 
        authenticationConverter.convert(request);
    
    // 2. 如果没有认证信息，抛出异常
    if (clientAuthentication == null) {
        throw new OAuth2AuthenticationException(
            OAuth2ErrorCodes.INVALID_CLIENT);
    }
    
    // 3. 验证客户端认证
    Authentication authenticatedClient = 
        authenticationManager.authenticate(clientAuthentication);
}
```

### 1.4 为什么前端直接调用会失败？

1. 过滤器链的顺序：
   - OAuth2TokenEndpointFilter 在 CORS 过滤器之前执行
   - 客户端认证失败会立即返回 401
   - CORS 配置根本没机会生效

2. 认证机制：
   - 即使提供了正确的 client_id 和 secret
   - Basic Auth 头部在跨域请求中需要预检
   - 预检请求无法携带认证信息

## 2. Android 客户端实现

### 2.1 公共客户端注册

```sql
INSERT INTO oauth2_registered_client (
    id,
    client_id,
    client_id_issued_at,
    client_secret,  -- 注意：不设置密钥
    client_authentication_methods,  -- 关键：使用 'none'
    authorization_grant_types,
    redirect_uris,
    scopes,
    client_settings
) VALUES (
    'android-client',
    'com.ffv.android',
    CURRENT_TIMESTAMP,
    null,  -- 无密钥
    'none',  -- 无客户端认证
    'authorization_code,refresh_token',
    'com.ffv.android://oauth2/callback',  -- 使用自定义 URL Scheme
    'openid,profile',
    '{"@class":"java.util.Collections$UnmodifiableMap",
      "settings.client.require-proof-key":true,  -- 强制使用 PKCE
      "settings.client.require-authorization-consent":true}'
);
```

### 2.2 Android 实现

1. OAuth2 客户端核心实现：
```kotlin
class OAuth2Client {
    companion object {
        private const val AUTH_ENDPOINT = "http://auth-server/oauth2/authorize"
        private const val TOKEN_ENDPOINT = "http://auth-server/oauth2/token"
        private const val CLIENT_ID = "com.ffv.android"
        private const val REDIRECT_URI = "com.ffv.android://oauth2/callback"
    }

    // PKCE 参数生成
    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(64)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, 
            Base64.URL_SAFE or Base64.NO_PADDING)
    }

    private suspend fun generateCodeChallenge(verifier: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(verifier.toByteArray())
        return Base64.encodeToString(bytes, 
            Base64.URL_SAFE or Base64.NO_PADDING)
    }

    // 启动授权流程
    fun startAuth(context: Context) {
        viewModelScope.launch {
            val codeVerifier = generateCodeVerifier()
            val codeChallenge = generateCodeChallenge(codeVerifier)
            
            // 安全存储 code verifier
            context.getEncryptedSharedPreferences("oauth_prefs")
                .edit()
                .putString("code_verifier", codeVerifier)
                .apply()

            // 构建授权 URL
            val authUri = Uri.parse(AUTH_ENDPOINT).buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("code_challenge", codeChallenge)
                .appendQueryParameter("code_challenge_method", "S256")
                .build()

            // 启动浏览器
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(context, authUri)
        }
    }
}
```

2. 自定义 URL Scheme 配置：
```xml
<!-- AndroidManifest.xml -->
<activity android:name=".OAuthRedirectActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="com.ffv.android"
            android:host="oauth2"
            android:path="/callback" />
    </intent-filter>
</activity>
```

### 2.3 安全特性

1. 无客户端密钥：
- 客户端认证方法设置为 'none'
- 不需要在应用中存储任何密钥

2. PKCE 保护：
- 强制要求使用 PKCE
- 防止授权码拦截攻击

3. 安全存储：
```kotlin
// 使用 EncryptedSharedPreferences 存储敏感数据
context.getEncryptedSharedPreferences("oauth_prefs")
    .edit()
    .putString("access_token", tokens.accessToken)
    .putString("refresh_token", tokens.refreshToken)
    .putLong("expires_at", System.currentTimeMillis() + tokens.expiresIn * 1000)
    .apply()
```

4. 自定义 URL Scheme：
- 使用应用专属的 URL Scheme
- 防止其他应用拦截回调

## 3. 最佳实践总结

### 3.1 Web 应用

1. 使用后端代理：
```java
@RestController
@RequestMapping("/oauth2")
public class TokenController {
    @PostMapping("/token")
    public ResponseEntity<String> getToken(...) {
        // 在后端安全地存储和使用客户端凭证
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
        // ...
    }
}
```

2. 前端实现：
```javascript
// 通过后端代理获取 token
const response = await fetch('/api/oauth2/token', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: new URLSearchParams({
        code: code,
        redirect_uri: REDIRECT_URI,
        code_verifier: codeVerifier
    })
});
```

### 3.2 移动应用

1. 使用公共客户端模式
2. 强制使用 PKCE
3. 利用平台安全特性
4. 实现安全的本地存储

### 3.3 通用原则

1. 避免在不可信环境存储敏感信息
2. 使用 HTTPS 保护通信
3. 实现适当的错误处理
4. 遵循 OAuth2 规范的安全建议

---

关于 OAuth2 客户端安全机制的一些补充说明：


## Web 应用的安全限制

### 浏览器的 CORS 预检机制

浏览器在发送跨域请求前，会先发送预检请求：

```http
OPTIONS /oauth2/token
Host: localhost:9000
Origin: http://localhost:1023
Access-Control-Request-Method: POST
Access-Control-Request-Headers: authorization,content-type
```

关键限制：预检请求不能携带认证信息（如 Authorization 头）。

### Spring Security 的实现机制

```java
// OAuth2TokenEndpointFilter 的关键实现
protected void doFilterInternal(HttpServletRequest request...) {
    // 1. 提取客户端认证信息
    Authentication clientAuthentication = 
        authenticationConverter.convert(request);
    
    // 2. 验证客户端类型和认证方式
    RegisteredClient registeredClient = 
        registeredClientRepository.findByClientId(clientId);
        
    // 3. 如果不是公共客户端但没有提供认证，抛出异常
    if (!registeredClient.getClientAuthenticationMethods()
            .contains(ClientAuthenticationMethod.NONE) 
            && clientAuthentication == null) {
        throw new OAuth2AuthenticationException(
            OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
```

### 为什么 Web 应用不能使用公共客户端配置

1. 过滤器链的顺序：
   - OAuth2TokenEndpointFilter 在 CORS 过滤器之前执行
   - 客户端认证失败会立即返回 401
   - CORS 配置根本没机会生效

2. 认证机制：
   - 即使提供了正确的 client_id 和 secret
   - Basic Auth 头部在跨域请求中需要预检
   - 预检请求无法携带认证信息

## Android 应用的安全机制

### 系统级 URL Scheme 注册

```xml
<activity android:name=".OAuthRedirectActivity">
    <intent-filter android:autoVerify="true">
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="com.ffv.android"
            android:host="oauth2"
            android:path="/callback" />
    </intent-filter>
</activity>
```

### 应用签名验证

```kotlin
fun verifyAppSignature(context: Context): Boolean {
    val packageInfo = context.packageManager.getPackageInfo(
        context.packageName, 
        PackageManager.GET_SIGNING_CERTIFICATES
    )
    
    val signatures = packageInfo.signingInfo.apkContentsSigners
    val expectedSignature = "your_app_signature_hash"
    
    return signatures.any { signature ->
        val signatureHash = signature.toByteArray()
            .toMessageDigest("SHA-256")
            .toHexString()
        signatureHash == expectedSignature
    }
}
```

### Digital Asset Links 验证

```json
// /.well-known/assetlinks.json
[{
  "relation": ["delegate_permission/common.handle_all_urls"],
  "target": {
    "namespace": "android_app",
    "package_name": "com.ffv.android",
    "sha256_cert_fingerprints": ["your_app_fingerprint"]
  }
}]
```

### 为什么一个 Android 应用不能冒用另一个的配置

1. 系统级保护：
- URL Scheme 与应用签名绑定
- 包名唯一性由 Play Store 保证
- 系统级的 Intent 路由验证

2. 多重验证：
```kotlin
// Android 系统的 Intent 路由
class IntentResolver {
    fun resolveActivity(intent: Intent): ComponentName? {
        // 1. 验证目标应用的签名
        // 2. 检查 Intent Filter 的优先级
        // 3. 验证应用的权限
        // 4. 应用 Android App Links 验证
        return resolvedComponent
    }
}
```

3. 授权服务器验证：
```java
@Bean
public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
    return context -> {
        if (context.getAuthorizationGrant().getAttribute("redirect_uri") != null) {
            String redirectUri = context.getAuthorizationGrant()
                .getAttribute("redirect_uri");
            // 验证 redirect_uri 与客户端 ID 的对应关系
            validateRedirectUriWithClientId(redirectUri, 
                context.getRegisteredClient().getClientId());
        }
    };
}
```

## 安全保护总结

### Web 应用
1. 必须使用后端代理处理 token 请求
2. 不能在前端存储客户端凭证
3. 受浏览器同源策略和 CORS 限制
4. 预检请求不能携带认证信息

### Android 应用
1. 系统级 URL Scheme 保护
2. 应用签名验证
3. Digital Asset Links 验证
4. Intent 路由系统保护
5. 包名唯一性保证

### 通用安全机制
1. 客户端类型隔离
2. 重定向 URI 验证
3. 授权服务器的多重验证
4. 平台特定的安全特性
