# OAuth2 授权码流程与安全实践详解

## 导读

本文介绍 OAuth2 授权码流程实现中的一些安全细节。

1. 什么场景需要用到 OAuth2？
   - 当你的应用需要访问第三方服务（如：使用GitHub登录）
   - 当你的系统包含多个应用，需要统一的认证方案
   - 当你需要让用户安全地授权，而不是直接共享密码

2. 为什么要使用授权码流程？
   - 它是最安全的 OAuth2 授权流程
   - 适用于各种类型的应用（Web、移动端、桌面端）
   - 支持长期访问（通过刷新令牌）

3. 本文相关内容包括：
   - OAuth2 授权码流程的工作原理
   - Web 应用和移动应用的实现方案
   - 相关的安全最佳实践

## 0. 基础概念解析

### 0.1 OAuth2 中的客户端类型

在开始详细讨论之前，我们需要理解 OAuth2 中的两种主要客户端类型：

1. 机密客户端（Confidential Clients）
   - 能够安全存储客户端凭证（client_id 和 client_secret）
   - 典型场景：服务器端应用
   - 认证方式：通常使用 client_secret 进行客户端认证

2. 公共客户端（Public Clients）
   - 无法安全存储客户端凭证
   - 典型场景：纯前端应用、桌面应用
   - 认证方式：通常只使用 client_id，不使用 client_secret

### 0.2 关于客户端认证和 PKCE

#### PKCE (Proof Key for Code Exchange) 工作原理

PKCE 是一种动态证明机制，通过以下步骤防止授权码被截获：

1. 客户端生成 code_verifier：
   - 生成一个高熵的随机字符串
   - 长度建议在43-128字符之间
   - 字符集：[A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~"

2. 客户端计算 code_challenge：
   - 使用 SHA-256 对 code_verifier 进行哈希
   - 将结果进行 Base64URL 编码
   - code_challenge_method 设置为 "S256"

PKCE 通过这种方式确保：
- 只有原始发起授权请求的客户端才能使用授权码
- 即使授权码被截获，没有 code_verifier 也无法使用

#### 关于 PKCE 的误解

一个常见的误解是将 PKCE（Proof Key for Code Exchange）机制与公共客户端绑定在一起。实际上：

1. 客户端认证与 PKCE 是两个独立的安全机制：
   - 客户端认证：验证客户端的身份（who you are）
   - PKCE：防止授权码被截获（protect the code）

2. PKCE 可以（也应该）被所有类型的客户端使用：
   ```http
   # 即使是使用 client_secret 的机密客户端，也可以使用 PKCE
   GET /oauth2/authorize?
       response_type=code
       &client_id=my-client
       &code_challenge=xxx        # PKCE challenge
       &code_challenge_method=S256
   ```

### 0.3 授权码流程是如何工作的？

想象一个实际场景：你想让一个第三方应用访问你的GitHub仓库。

1. 基本流程：
```
用户 -> 第三方应用 -> GitHub授权页面 -> 同意授权 -> 第三方应用获得访问权限
```

2. 详细步骤：
   - 你点击"使用GitHub登录"
   - 跳转到GitHub的授权页面
   - 你登录GitHub并同意授权
   - GitHub给第三方应用一个授权码
   - 第三方应用使用这个授权码换取访问令牌
   - 后续使用访问令牌来访问你的GitHub数据

3. 为什么要这么复杂？
   - 你不需要把GitHub密码告诉第三方应用
   - 你可以随时在GitHub中撤销授权
   - 第三方应用只能访问你允许的数据
   - 整个过程是安全的

### 0.4 Web 应用与移动应用的实现选择

Web 应用的实现方式：

1. 后端代理方式（标准推荐方式）：
   - 在自己的后端服务器上设置代理端点
   - 所有 OAuth2 相关请求通过后端代理
   - 客户端凭证安全存储在后端
   - 适用于所有 Web 应用

2. 认证代理服务方式：
   - 使用专业的认证代理服务（如 Auth0）
   - 代理服务处理跨域和令牌交换
   - 适用于无后端或 Serverless 架构

移动应用的实现方式：

1. 公共客户端方式：
   - 不使用客户端密钥
   - 使用 PKCE 增强安全性
   - 直接与授权服务器通信
   - 适用于原生移动应用

2. 后端代理方式（可选）：
   - 类似 Web 应用的实现
   - 提供额外的安全层
   - 适用于有特殊安全需求的场景

注意：纯前端应用不能直接实现为公共客户端，因为浏览器的同源策略限制会阻止直接访问授权服务器的 token endpoint。这是由于：

1. CORS 预检请求的限制：
```http
OPTIONS /oauth2/token
Host: auth-server.com
Origin: http://frontend-app.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: authorization
```

2. OAuth2 认证流程的限制：
   - token endpoint 的客户端认证发生在 CORS 处理之前
   - 预检请求无法携带认证信息
   - 导致无法完成跨域请求

## 1. Web 前端实现分析

### 1.1 浏览器的同源策略与跨域安全

浏览器的同源策略（Same-Origin Policy）是一个关键的安全机制：

1. 同源的定义：
   - 协议相同（http/https）
   - 域名相同
   - 端口相同

2. 跨域请求的限制：
   - 默认禁止跨域请求
   - 需要服务器明确允许（通过 CORS）
   - 某些请求需要预检（Preflight）

### 1.2 CORS 在 OAuth2 中的影响

在实现OAuth2授权码流程时，我们经常会遇到跨域问题：

1. 典型场景：
   - 前端应用运行在 localhost:3000
   - 授权服务器在 auth-server.com
   - 后端API在 api.myapp.com

2. 浏览器的同源策略：
   - 不同域之间的请求会受到限制
   - 特别是携带认证信息的请求
   - 这就是为什么我们需要了解CORS机制

3. CORS预检机制：

浏览器在发送跨域请求前，会先发送预检请求：

```http
OPTIONS /oauth2/token
Host: localhost:9000
Origin: http://localhost:1023
Access-Control-Request-Method: POST
Access-Control-Request-Headers: authorization,content-type
```

关键限制：预检请求不能携带认证信息（如 Authorization 头）。

### 1.3 完整的授权流程

OAuth2 授权抽象流程：

```
     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |                              |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                              +---------------+
     |        |
     |        |                              +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                              |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                              +---------------+
     |        |
     |        |                              +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                              |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                              +---------------+
```

* （A）用户打开客户端以后，客户端要求用户给予授权。
* （B）用户同意给予客户端授权。
* （C）客户端使用上一步获得的授权，向认证服务器申请令牌。
* （D）认证服务器对客户端进行认证以后，确认无误，同意发放令牌。
* （E）客户端使用令牌，向资源服务器申请获取资源。
* （F）资源服务器确认令牌无误，同意向客户端开放资源。

B 是关键，即用户怎样才能给于客户端授权。有了这个授权以后，客户端就可以获取令牌，进而凭令牌获取资源。

> 参考：[OAuth 2.0 的四种方式](https://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html)

授权码模式（authorization code）是功能最完整、流程最严密的授权模式。它的特点就是通过客户端的后台服务器，与"服务提供商"的认证服务器进行互动。

授权码流程的具体步骤：

```
     +----------+
     | Resource |
     |  Owner   |
     |          |
     +----------+
          ^
          |
         (B)
     +----|-----+          Client Identifier      +---------------+
     |         -+----(A)-- & Redirection URI --->|               |
     |  User-   |    Client Identifier ^          | Authorization |
     |  Agent  -+----(B)-- & Redirection URI --->|     Server    |
     |          |              v                  |               |
     |         -|----(C)-- Authorization Code ----+               |
     +-|----|--+                                 +---------------+
       |    |                                         ^      v
      (A)  (C)                                        |      |
       |    |                                         |      |
       ^    v                                         |      |
     +---------+                                      |      |
     |         |>---(D)-- Authorization Code ---------'      |
     |  Client |          & Redirection URI                  |
     |         |                                             |
     |         |<---(E)----- Access Token -------------------'
     +---------+       (w/ Optional Refresh Token)
```

关键步骤说明：
1. (A) 客户端通过用户代理（浏览器）向授权服务器发起授权请求
   - response_type=code
   - client_id
   - redirect_uri（可选）
   - scope（可选）
   - state（推荐，防止 CSRF）
   - code_challenge（如果使用 PKCE）
   - code_challenge_method（如果使用 PKCE）

2. (B) 用户在授权服务器上进行身份认证并授权

3. (C) 授权服务器通过重定向将授权码返回给客户端
   - code
   - state（如果请求中包含）

4. (D) 客户端向授权服务器请求访问令牌
   - grant_type=authorization_code
   - code（上一步获得的授权码）
   - redirect_uri（如果步骤 A 中提供了）
   - client_id
   - client_secret（如果是机密客户端）
   - code_verifier（如果使用 PKCE）

5. (E) 授权服务器返回访问令牌
   - access_token
   - token_type
   - expires_in
   - refresh_token（可选）
   - scope（如果与请求的不同）

现在让我们看看具体的技术实现...

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

### 1.4 Spring Security 的实现机制

为什么要了解这部分实现？因为它解释了：
- 为什么前端不能直接调用授权服务器
- 为什么需要后端代理
- 整个安全机制是如何工作的

让我们看看关键代码：

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

### 1.5 为什么前端直接调用会失败？

让我们通过一个具体的时序来理解这个问题：

1. 没有后端代理时的流程：
```
浏览器                    授权服务器
   |                         |
   |---OPTIONS 预检请求----->|  // 不能带认证信息
   |                         |
   |<----拒绝预检请求--------|  // 因为缺少认证信息
   |                         |
   |--POST /oauth2/token-X-->|  // 主请求根本没机会发送
   |                         |
```

2. 使用后端代理的流程：
```
浏览器          后端代理          授权服务器
   |               |                |
   |--POST /api--->|                |  // 同源请求，无需预检
   |               |---POST /token-->|  // 服务器间通信，无CORS限制
   |               |<-----令牌-------|
   |<----令牌------ |                |
```

这就解释了为什么我们需要后端代理：
- 避免跨域问题
- 安全存储客户端凭证
- 简化前端实现

为什么 Web 应用不能使用公共客户端配置：

1. 过滤器链的顺序：
   - OAuth2TokenEndpointFilter 在 CORS 过滤器之前执行
   - 客户端认证失败会立即返回 401
   - CORS 配置根本没机会生效

2. 认证机制：
   - 即使提供了正确的 client_id 和 secret
   - Basic Auth 头部在跨域请求中需要预检
   - 预检请求无法携带认证信息

## 2. Android 客户端实现

移动应用有其特殊性：

Android 的安全模型基于以下核心概念：

1. 应用沙箱：
   - 每个应用运行在独立的进程中
   - 有自己的虚拟机实例
   - 有独立的文件系统权限

2. 应用签名：
   - 所有 APK 必须经过签名
   - 签名确保应用来源和完整性
   - 系统根据签名识别应用身份

3. 权限系统：
   - 声明式权限模型
   - 运行时权限检查
   - 组件间通信控制

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

### 2.3 Android 平台的安全机制

1. 系统级 URL Scheme 保护：
- URL Scheme 与应用签名绑定
- 包名唯一性由 Play Store 保证
- 系统级的 Intent 路由验证

2. 应用签名验证：
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

3. Digital Asset Links 验证：
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

4. 多重验证机制：
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

5. 授权服务器验证：
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

## 3. 安全最佳实践总结

### 3.1 Web 应用安全要点

1. 必须使用后端代理处理 token 请求
2. 不能在前端存储客户端凭证
3. 受浏览器同源策略和 CORS 限制
4. 预检请求不能携带认证信息

### 3.2 Android 应用安全要点

1. 系统级 URL Scheme 保护
2. 应用签名验证
3. Digital Asset Links 验证
4. Intent 路由系统保护
5. 包名唯一性保证

### 3.3 通用安全机制

1. 客户端类型隔离
2. 重定向 URI 验证
3. 授权服务器的多重验证
4. 平台特定的安全特性
