# Authorization Server

## 1. 启动服务器

```bash
cd ffvtraceability-auth-server
mvn clean spring-boot:run
```

服务器将在 9000 端口启动。

## 2. OAuth 2.0 授权码浏览器流程测试

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


## 使用 Shell 脚本测试授权码流程

见：`src/ffvtraceability-auth-server/scripts/test.sh`

## 测试资源服务器的端到端测试（授权码流程测试）

见：`src/ffvtraceability-resource-server/README.md`



## 关于 Spring Security OAuth2 Authorization Server 的扩展

### 支持有层级的权限

Spring Security 默认使用的 Schema 对于权限的“粒度”基本没有什么原生的支持。

```sql
CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);
```

可见，默认只是支持扁平化的权限。

我们在不修改 Spring Security 默认的 Schema 的情况下支持有层级的权限（呈现为树形结构）。

我们新增了一个表 `permissions，用于存储所有的基础权限。这些基础权限是系统中可在“权限管理界面”进行设置的权限的集合。

表 `permissions` 包含两列：
* `permission_id` - 权限的唯一标识符
* `description` - 权限的描述信息（可以为 null）

基础权限的示例：

```sql
INSERT INTO permissions (permission_id, description) VALUES 
    ('ITEM_CREATE', '创建物料的权限'),
    ('ITEM_READ', '读取物料的权限'),
    ('ITEM_UPDATE', '更新物料的权限'),
    ('ITEM_DELETE', '删除物料的权限'),
    ('ORDER_PO_CREATE', '创建采购订单的权限'),
    -- 更多权限...
```

在上面的示例中，权限的分隔符是 `_`，表示层级关系。这些基础权限在数据库初始化时插入，一般不需要进行手动管理。


### 用户权限管理 UI 的实现

假设在“用户权限管理”界面，我们可以将某个权限赋予某个用户，或者从用户身上收回某个权限。
只有“管理员”用户可以使用这个界面进行操作。

我们将上面所举例的扁平化的权限在界面上呈现为类似这样的树形结构
（读取 `permissions` 表中的记录，整理为树形结构）：

```
./
├── ITEM
│   ├── CREATE
│   ├── READ
│   ├── UPDATE
│   └── DELETE
├── ORDER
│   ├── PO
│   │   ├── CREATE
│   │   ├── READ
│   │   ├── UPDATE
│   │   └── DEACTIVATE
│   └── SO
│       ├── CREATE
│       ├── READ
│       ├── UPDATE
│       └── DEACTIVATE
```

我们从简单的场景开始讨论。管理员可以对一个用户设置“叶子节点权限”：

* 先选中一个“当前需要设置权限的用户”，我们假设先只支持对一个用户设置权限。（用户信息来自于 `users` 表。）
* 然后，当管理员选中或者取消选中某个“叶子节点”时，向后端发送请求，更新数据库中的该用户的权限。

然后考虑支持更复杂的场景（对一个用户批量赋予/取消权限）：

* 管理员可点选权限树的某个“父节点”，这时候，界面上自动选中其下的所有子节点。自动向后端发送请求，一次性给该用户赋予多个权限（Insert 多行数据）。
* 管理员可取消选中某个“父节点”，这时候，界面上自动取消选中其下的所有子节点。自动向后端发送请求，一次性删除该用户身上的多个权限（Delete 多行数据）。
* 后端进行“批量处理”时，可以忽略 Insert 或 Delete（单条权限记录）操作的“错误”，以容忍可能发生的并发冲突（概率极低）。

所有这些操作，后端最终操作的都是 `authorities` 表，插入或者删除的记录的 `authority` 列的值都是“叶子节点权限”。


