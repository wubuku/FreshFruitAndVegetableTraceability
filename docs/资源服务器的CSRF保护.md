# 资源服务器的 CSRF 保护


禁用掉 CSRF 是否安全？


## CSRF 简介

**CSRF (Cross-Site Request Forgery) 跨站请求伪造**是一种网络安全漏洞：
1. 攻击者诱导已登录用户访问恶意网站
2. 恶意网站向受害者已登录的网站发送未经授权的请求
3. 受害者浏览器会自动附带该网站的认证信息（如 cookie）
4. 服务器无法分辨这个请求是否是用户本人发起的

## RESTful API 与 CSRF

**对于纯 RESTful API 项目，通常可以安全地禁用 CSRF 保护**，原因如下：

1. **现代前后端分离架构特点**：
   - 使用 JWT 等无状态令牌认证
   - 令牌通常存储在 JavaScript 内存中，而不是 cookie
   - 令牌通过 Authorization header 发送，而不是自动附加的 cookie

2. **CSRF 攻击的前提条件**：
   - 服务器使用 cookie 进行身份验证
   - 浏览器会自动附加 cookie 到请求中

3. **为什么 RESTful API 相对安全**：
   - API 调用需要手动添加 Authorization header
   - 恶意网站无法访问到存储在内存中的令牌
   - 没有自动附加的认证信息，CSRF 攻击难以实现

## 代码分析

所以在您的代码中：
```java:src/ffvtraceability-resource-server/src/main/java/org/dddml/ffvtraceability/resource/config/ResourceServerConfig.java
.csrf(csrf -> csrf.disable())
```

这行代码是合理的，因为：
1. 您的项目使用了 OAuth2 资源服务器
2. 使用 JWT 进行认证
3. 是一个纯 RESTful API 项目

## 注意事项

如果您的 API 服务还要支持基于 cookie 的认证，或者需要处理浏览器直接发起的表单提交，那么可能还是需要启用 CSRF 保护。

