# Authorization Server

## 1. 启动服务器

```bash
cd ffvtraceability-auth-server
mvn clean spring-boot:run
```

服务器将在 9000 端口启动。

## 2. OAuth 2.0 授权码流程测试

### 2.1 浏览器访问测试页面

访问 http://localhost:9000/oauth2-test 开始测试流程。

### 2.2 详细流程说明

1. **初始化 PKCE 参数**
```javascript
// 生成随机的 code_verifier (43字节)
const array = new Uint8Array(32);
window.crypto.getRandomValues(array);
const codeVerifier = base64URLEncode(array);

// 生成 code_challenge (SHA-256 哈希后的 base64url 编码)
const encoder = new TextEncoder();
const data = encoder.encode(codeVerifier);
const hash = await window.crypto.subtle.digest('SHA-256', data);
const codeChallenge = base64URLEncode(new Uint8Array(hash));
```

2. **发起授权请求**
```javascript
const params = new URLSearchParams({
    response_type: 'code',
    client_id: 'ffv-client',
    redirect_uri: 'http://localhost:9000/oauth2-test-callback',
    scope: 'openid read write',
    code_challenge: codeChallenge,
    code_challenge_method: 'S256'
});

window.location.href = '/oauth2/authorize?' + params.toString();
```

3. **用户登录认证**
- 系统跳转到登录页面
- 用户输入用户名和密码 (admin/admin)
- Spring Security 验证凭据
- 登录成功后继续授权流程

4. **授权确认**
- 如果需要用户同意，显示授权确认页面
- 用户确认授权范围 (scopes)
- 系统生成授权码

5. **获取授权码**
- 系统重定向到回调地址，附带授权码
- 回调页面获取授权码并保存
```javascript
const urlParams = new URLSearchParams(window.location.search);
const code = urlParams.get('code');
```

6. **交换访问令牌**
```javascript
const tokenResponse = await fetch('/oauth2/token', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Basic ' + btoa('ffv-client:secret')
    },
    body: new URLSearchParams({
        grant_type: 'authorization_code',
        code: code,
        redirect_uri: 'http://localhost:9000/oauth2-test-callback',
        code_verifier: codeVerifier,
        scope: 'openid read write'
    })
});
```

7. **解析令牌信息**
```javascript
const tokenData = await tokenResponse.json();
// 访问令牌
console.log('Access Token:', tokenData.access_token);
// 刷新令牌
console.log('Refresh Token:', tokenData.refresh_token);
// ID 令牌 (OpenID Connect)
console.log('ID Token:', tokenData.id_token);

// 解码 JWT 令牌
function decodeJWT(token) {
    const parts = token.split('.');
    const payload = base64URLDecode(parts[1]);
    return JSON.parse(payload);
}
```

### 2.3 令牌内容示例

**Access Token Claims:**
```json
{
  "sub": "admin",
  "aud": "ffv-client",
  "nbf": 1731915436,
  "scope": [
    "read",
    "openid",
    "write"
  ],
  "iss": "http://localhost:9000",
  "exp": 1731919036,
  "iat": 1731915436,
  "jti": "c5f3eac0-61e6-4a94-9bf8-dd5bc684d177",
  "authorities": [
    "ROLE_USER",
    "ROLE_ADMIN",
    "DIRECT_ADMIN_AUTH"
  ]
}
```

**ID Token Claims:**
```json
{
  "sub": "admin",
  "aud": "ffv-client",
  "azp": "ffv-client",
  "auth_time": 1731915436,
  "iss": "http://localhost:9000",
  "exp": 1731917236,
  "iat": 1731915436,
  "jti": "ba9509c9-3b7b-4635-abac-beb2c178c912",
  "sid": "D4a00T_VVb_xRj4fQQygxI77NWP-LEzMN8F9KuqYifE"
}
```


### 2.4 安全考虑

1. **PKCE (Proof Key for Code Exchange)**
   - 防止授权码拦截攻击
   - 客户端生成随机 code_verifier
   - 使用 SHA-256 哈希生成 code_challenge
   - 令牌请求时验证 code_verifier

2. **状态管理**
   - 使用 sessionStorage 存储 code_verifier
   - 令牌信息安全存储
   - 适当的页面跳转和状态维护

3. **令牌安全**
   - 访问令牌有限时效
   - 刷新令牌用于获取新的访问令牌
   - ID 令牌用于身份验证

### 2.5 调试信息

测试页面 (/oauth2-test) 显示：
- 授权码
- 访问令牌
- 刷新令牌
- ID 令牌
- 解码后的令牌载荷 (Claims)
- 完整的请求/响应信息

