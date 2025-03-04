# Spring Security

> 注意：以下内容基于与 AI 对话的结果整理而成，细节上没有经过验证，仅供参考。

## Authorities

ROLE_ 前缀是 Spring Security 的一个特殊约定，但 authorities 不一定要以 ROLE_ 开头。Authorities 可以是任何权限标识符。
常见的非 ROLE_ 权限示例：

```sql
-- 基于操作的权限
INSERT INTO authorities VALUES ('user', 'READ');
INSERT INTO authorities VALUES ('editor', 'WRITE');
INSERT INTO authorities VALUES ('admin', 'DELETE');

-- 基于资源的权限
INSERT INTO authorities VALUES ('user', 'READ_PROFILE');
INSERT INTO authorities VALUES ('manager', 'READ_REPORTS');
INSERT INTO authorities VALUES ('admin', 'MANAGE_USERS');

-- 基于功能的权限
INSERT INTO authorities VALUES ('user', 'USE_API');
INSERT INTO authorities VALUES ('premium', 'ACCESS_PREMIUM_FEATURES');
```

在代码中的使用区别：

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            // 使用 ROLE_ 前缀的权限
            .requestMatchers("/admin/**").hasRole("ADMIN")  // 自动添加 ROLE_ 前缀
            
            // 使用普通权限
            .requestMatchers("/api/**").hasAuthority("USE_API")  // 不添加前缀
            .requestMatchers("/reports/**").hasAuthority("READ_REPORTS")
            .requestMatchers("/users/**").hasAuthority("MANAGE_USERS")
        );
        return http.build();
    }
}
```

或者在方法级别：

```java
@RestController
public class UserController {
    
    @PreAuthorize("hasRole('ADMIN')")  // 使用角色
    public void adminMethod() {}
    
    @PreAuthorize("hasAuthority('MANAGE_USERS')")  // 使用权限
    public void manageUsers() {}
    
    @PreAuthorize("hasAuthority('READ_REPORTS') and hasRole('MANAGER')")  // 组合使用
    public void readReports() {}
}
```

主要区别：
* hasRole() 会自动添加 ROLE_ 前缀
* hasAuthority() 使用原始权限字符串
* 角色通常表示用户的身份
* 权限通常表示具体的操作权限

选择建议：
* 使用 ROLE_ 来表示用户的角色/身份
* 使用普通权限来表示具体的操作权限
* 可以混合使用两种方式


## Group Authorities

Group Authorities 更符合我们通常理解的"角色"概念，因为：
1. 一个角色通常包含多个权限
2. 角色之间可以有层次关系
3. 更容易管理和维护权限
4. 更符合 RBAC（基于角色的访问控制）模型


### **传统意义上的"角色"**

```sql
-- 更符合直觉的角色定义
INSERT INTO groups (group_name) VALUES 
    ('管理员组'),
    ('编辑组'),
    ('普通用户组');

-- 角色对应的权限集合
INSERT INTO group_authorities (group_id, authority) VALUES 
    (1, 'MANAGE_USERS'),    -- 管理员可以管理用户
    (1, 'MANAGE_CONTENT'),  -- 管理员可以管理内容
    (1, 'VIEW_REPORTS'),    -- 管理员可以查看报表
    (2, 'EDIT_CONTENT'),    -- 编辑可以编辑内容
    (2, 'PUBLISH_CONTENT'), -- 编辑可以发布内容
    (3, 'READ_CONTENT');    -- 普通用户只能读内容
```

### **Spring Security 的 ROLE_ 设计**

```sql
-- 这更像是一个简单的权限标记
INSERT INTO authorities VALUES 
    ('user1', 'ROLE_ADMIN'),  -- 本质上只是一个标记
    ('user2', 'ROLE_USER');   -- 并没有体现角色的层次结构
```


实际应用建议：
```sql
-- 1. 定义基础权限
INSERT INTO authorities VALUES 
    ('READ_PROFILE'),
    ('EDIT_PROFILE'),
    ('MANAGE_USERS');

-- 2. 定义角色（组）
INSERT INTO groups VALUES 
    (1, '系统管理员'),
    (2, '部门主管'),
    (3, '普通员工');

-- 3. 为角色分配权限
INSERT INTO group_authorities VALUES 
    (1, 'MANAGE_USERS'),
    (1, 'EDIT_PROFILE'),
    (2, 'EDIT_PROFILE'),
    (3, 'READ_PROFILE');

-- 4. 用户归属角色
INSERT INTO group_members VALUES 
    ('alice', 1),  -- alice 是系统管理员
    ('bob', 2),    -- bob 是部门主管
    ('charlie', 3);-- charlie 是普通员工
```

这样的设计：
1. 更符合实际业务场景
2. 更容易理解和维护
3. 更灵活的权限管理
4. 更好的可扩展性


## Spring Security 授权注解

### 注解来源和依赖

#### 1. `@RolesAllowed` - JSR-250 标准注解

**包路径：**
```java
javax.annotation.security.RolesAllowed  // Java EE
jakarta.annotation.security.RolesAllowed // Jakarta EE
```

**Maven 依赖：**
```xml
<dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>2.1.1</version>
</dependency>
```

#### 2. `@Secured` - Spring Security 的原生注解

**包路径：**
```java
org.springframework.security.access.annotation.Secured
```

**Maven 依赖：**
```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-core</artifactId>
</dependency>
```

#### 3. `@PreAuthorize` - Spring Security 的原生注解

**包路径：**
```java
org.springframework.security.access.prepost.PreAuthorize
```

### 启用注解支持

#### Spring Security 5.x 配置

```java
@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,    // 启用 @PreAuthorize, @PostAuthorize
    securedEnabled = true,    // 启用 @Secured
    jsr250Enabled = true      // 启用 @RolesAllowed
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ...
}
```

### Spring Security 6.x 配置

```java
@Configuration
@EnableMethodSecurity(
    prePostEnabled = true,    // 默认为 true
    securedEnabled = true,    // 默认为 false
    jsr250Enabled = true      // 默认为 false
)
public class SecurityConfig {
    // ...
}
```

### 注解使用示例

#### 基本使用

```java
// 使用 @Secured
@Secured("ROLE_ADMIN")
public void adminMethod() { }

// 使用 @RolesAllowed
@RolesAllowed("ROLE_ADMIN")
public void adminMethod() { }

// 使用 @PreAuthorize
@PreAuthorize("hasRole('ADMIN')")
public void adminMethod() { }
```

#### 多角色/权限控制

```java
// 使用 and 组合
@PreAuthorize("hasAnyRole('ADMIN') and hasAnyAuthority('USER')")  

// 使用 or 组合
@PreAuthorize("hasAnyRole('ADMIN') or hasAnyAuthority('USER')")   

// 更复杂的组合
@PreAuthorize("(hasAnyRole('ADMIN','MANAGER') and hasAnyAuthority('READ','WRITE')) or hasRole('SUPER_ADMIN')")
```

#### 角色前缀说明

Spring Security 中的 `hasRole()` 和 `hasAnyRole()` 会自动添加 "ROLE_" 前缀：

```java
// 错误方式 - 会变成检查 "ROLE_ROLE_ADMIN" 角色
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")  

// 正确方式 - 会自动变成检查 "ROLE_ADMIN"
@PreAuthorize("hasAnyRole('ADMIN')")       

// 使用 hasAuthority - 直接检查 "ROLE_ADMIN"
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  
```

源码实现：
```java
private static String defaultRolePrefix = "ROLE_";

public final boolean hasRole(String role) {
    return hasAuthority(defaultRolePrefix + role);
}

public final boolean hasAnyRole(String... roles) {
    return hasAnyAuthorityName(defaultRolePrefix, roles);
}
```

### 最佳实践建议

1. 推荐使用 `@PreAuthorize`，因为：
   - 表达能力最强
   - 可以使用 SpEL 表达式
   - 可以访问方法参数
   - Spring Security 默认启用

2. 角色命名规范：
   - 使用 `hasRole()` 时不要手动添加 "ROLE_" 前缀
   - 使用 `hasAuthority()` 时需要完整的权限名称

3. 保持团队使用风格的一致性，避免混用不同注解

