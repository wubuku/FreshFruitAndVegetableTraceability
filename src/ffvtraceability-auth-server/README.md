# Authorization Server

## 启动服务器

```bash
cd ffvtraceability-auth-server
mvn clean spring-boot:run
```

服务器将在 9000 端口启动。


## 在 Auth Server 测试页面上测试 OAuth 2.0 授权码流程

### 浏览器访问测试页面

访问 http://localhost:9000/oauth2-test 开始测试流程。

### 详细流程说明

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

### 令牌内容示例

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

### 安全考虑

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

### 调试信息

测试页面 (/oauth2-test) 显示：
- 授权码
- 访问令牌
- 刷新令牌
- ID 令牌
- 解码后的令牌载荷 (Claims)
- 完整的请求/响应信息


## 使用 Shell 脚本测试授权码流程

见：`src/ffvtraceability-auth-server/scripts/test.sh`

## “测试资源服务器”的端到端测试（授权码流程测试）

我们创建了一个供测试用的资源服务器项目，包含了授权码流程的测试。
相关描述见：`src/ffvtraceability-resource-server/README.md`


## 测试 Web 前端 OAuth2 授权码流程


在追溯系统 API 服务中，包含了测试 OAuth2 授权码流程的前端页面。
见这里的描述：`src/ffvtraceability-service-rest/README.md`



## 对 Spring Security OAuth2 Authorization Server 的扩展

下面讨论的是我们对 Spring Security OAuth2 Authorization Server 所做的扩展。

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


## 授权码流程测试脚本解析

脚本见代码库根目录下的 `src/ffvtraceability-auth-server/scripts/test.sh`。

### 1. PKCE 参数生成
```bash
# 生成 code_verifier (随机字符串)
code_verifier=$(openssl rand -base64 32 | tr -d /=+ | cut -c -43)
echo "🔑 Code Verifier: $code_verifier"

# 生成 code_challenge (base64url-encode(sha256(code_verifier)))
code_challenge=$(printf "%s" "$code_verifier" | openssl sha256 -binary | base64url_encode)
echo "🔒 Code Challenge: $code_challenge"
```

### 2. 用户登录流程
```bash
# 获取登录页面和 CSRF token
csrf_token=$(curl -c cookies.txt -b cookies.txt -s http://localhost:9000/login | 
    sed -n 's/.*name="_csrf" type="hidden" value="\([^"]*\).*/\1/p')

# 执行登录请求
curl -X POST http://localhost:9000/login \
    -c cookies.txt -b cookies.txt \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=admin" \
    -d "password=admin" \
    -d "_csrf=$encoded_csrf_token"
```

### 3. 授权请求
```bash
auth_page=$(curl -s \
    -c cookies.txt -b cookies.txt \
    "http://localhost:9000/oauth2/authorize?\
client_id=ffv-client&\
response_type=code&\
scope=openid%20read%20write&\
redirect_uri=${encoded_redirect_uri}&\
code_challenge=${code_challenge}&\
code_challenge_method=S256")
```

### 4. 用户授权确认
```bash
if echo "$auth_page" | grep -q "Consent required"; then
    curl -s \
        -c cookies.txt -b cookies.txt \
        "http://localhost:9000/oauth2/authorize" \
        -d "client_id=ffv-client" \
        -d "state=$state" \
        -d "scope=read" \
        -d "scope=write" \
        -d "scope=openid"
fi
```

### 5. 交换访问令牌
```bash
curl -X POST "http://localhost:9000/oauth2/token" \
    -H "Authorization: Basic $(echo -n 'ffv-client:secret' | base64)" \
    -d "grant_type=authorization_code" \
    -d "code=$encoded_auth_code" \
    -d "redirect_uri=$encoded_redirect_uri" \
    -d "code_verifier=$encoded_code_verifier"
```


## 更多参考信息

见：`docs/OAuth2_授权码流程与安全实践详解.md`



## TODO 更多改进

### 命名问题讨论：permissions 表与 authority 概念

#### 当前的命名挑战

在实现权限管理功能时，我们遇到了一个命名上的挑战。Spring Security 框架中并没有定义一个明确的"authority"实体，而是直接使用字符串来表示用户的权限。这种设计在我们需要为用户进行权限配置管理时带来了一些困扰：

1. 系统中有哪些可用的权限？
2. 这些权限的基本信息（如描述、分类等）应该存储在哪里？

显然，我们需要一个实体（表）来存储这些"可用权限"的定义。但在Spring Security中，"authority"出现的地方通常都是作为字符串类型，这就产生了概念上的不一致。

目前我们使用 `permissions` 表来存储权限定义，但这个命名可能不够精确地反映其与Spring Security权限模型的关系。

#### 命名选项分析

经过讨论，我们考虑了以下几个命名选项：

1. **`permissions`** (当前选择) - 简洁但可能和Spring Security的已有概念放在一起显得有些混乱
2. **`authority_definitions`** - 明确表示这是对authority的定义表
3. **`permission_catalog`** - 强调这是一个权限目录
4. **`available_authorities`** - 表示系统中可用的权限列表
5. **`authority_registry`** - 表示权限的注册表

#### 结论与重构计划

经过分析，我们认为 **`authority_definitions`** 是最准确的命名，因为：

1. 直接使用"authority"术语，与Spring Security概念保持一致
2. "definitions"后缀明确表示这是定义表，不是实际的授权表
3. 清晰地区分于Spring Security的`authorities`表（存储用户-权限关系）
4. 准确反映表的用途 - 存储系统中可用权限的基本定义

在下一个版本的重构中，我们也许应该将`permissions`表重命名为`authority_definitions`，以更好地反映其在系统中的角色。
这个重命名会涉及到相关的实体类、数据库表和参考代码的修改，但将使系统的概念模型更加清晰。

在重构完成前，我们将继续使用`permissions`表，但请注意在注释和文档中会明确说明其用途和与Spring Security权限模型的关系。


### 增加更多认证方式

#### 微信登录支持

见[相关讨论](../../docs/微信登录支持.md)

#### 短信登录支持

见[相关讨论](../../docs/短信登录支持.md)

### 数据模型改进

为了支持更多类型的“用户 ID”，以及支持更多的用户登录（认证）方式，考虑增加实体 `UserIdentification`。

用 DDDML 描述，大致如下：

```yaml
aggregates:
  User:
    id:
      name: Username
      type: id
    properties:
      #Password:
      #  type: ...
      UserIdentifications:
        itemType: UserIdentification

    entities:
      UserIdentification:
        id:
          name: UserIdentificationTypeId
          type: id-ne
        globalId:
          name: UserIdentificationId
          type: UserIdentificationId
          columnNames:
            - USERNAME
            - USER_IDENTIFICATION_TYPE_ID
        properties:
          IdValue:
            columnName: ID_VALUE
            type: id-long
```

