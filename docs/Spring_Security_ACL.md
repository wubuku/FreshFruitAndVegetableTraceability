# Spring Security ACL 实现细粒度权限控制

> 注：本文档提供了 Spring Security ACL 实现细粒度权限控制的实施指南。

## 1. ACL 基础概念

ACL (Access Control List，访问控制列表) 是一种细粒度的权限控制机制，它允许您为单个域对象（比如具体的文档、记录或资源）定义权限。这与基于角色的访问控制（RBAC）不同，RBAC 主要关注的是用户对整个类型资源的访问权限。

### 1.1 主要概念

1. **Domain Object（域对象）**
   - 需要进行权限控制的具体对象
   - 例如：一个文档、一条记录、一个项目等

2. **ACL Entry（访问控制条目）**
   - 定义了特定用户或角色对特定对象的权限
   - 包含以下基本权限：
     - READ (1)：读取权限
     - WRITE (2)：写入权限
     - CREATE (4)：创建权限
     - DELETE (8)：删除权限
     - ADMIN (16)：管理权限

3. **Security Identity (SID)**
   - 代表一个安全主体，可以是用户或角色
   - 分为 PrincipalSid (用户) 和 GrantedAuthoritySid (角色/组)

4. **Permission Mask（权限掩码）**
   - 权限在系统中以整数掩码形式存储（使用位运算）
   - 基本权限使用2的幂值表示：READ(1), WRITE(2), CREATE(4)等
   - 可以扩展定义自定义权限（见后文）

## 2. 完整实施步骤

### 2.1 添加依赖

```xml
<!-- Spring Security ACL -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-acl</artifactId>
</dependency>

<!-- 缓存支持 -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
</dependency>
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache-core</artifactId>
    <version>2.6.11</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
</dependency>
```

### 2.2 创建数据库表

ACL 需要特定的表结构来存储权限信息：

```sql
-- ACL 表结构
CREATE TABLE acl_sid (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    principal BOOLEAN NOT NULL,
    sid VARCHAR(100) NOT NULL,
    CONSTRAINT unique_acl_sid UNIQUE (sid, principal)
);

CREATE TABLE acl_class (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    class VARCHAR(255) NOT NULL,
    CONSTRAINT unique_acl_class UNIQUE (class)
);

CREATE TABLE acl_object_identity (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    object_id_class BIGINT NOT NULL,
    object_id_identity BIGINT NOT NULL,
    parent_object BIGINT,
    owner_sid BIGINT,
    entries_inheriting BOOLEAN NOT NULL,
    CONSTRAINT unique_acl_object_identity UNIQUE (object_id_class, object_id_identity),
    CONSTRAINT fk_acl_object_identity_parent FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
    CONSTRAINT fk_acl_object_identity_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
    CONSTRAINT fk_acl_object_identity_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
);

CREATE TABLE acl_entry (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    acl_object_identity BIGINT NOT NULL,
    ace_order INT NOT NULL,
    sid BIGINT NOT NULL,
    mask INTEGER NOT NULL,
    granting BOOLEAN NOT NULL,
    audit_success BOOLEAN NOT NULL,
    audit_failure BOOLEAN NOT NULL,
    CONSTRAINT unique_acl_entry UNIQUE (acl_object_identity, ace_order),
    CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),
    CONSTRAINT fk_acl_entry_sid FOREIGN KEY (sid) REFERENCES acl_sid (id)
);
```

### 2.3 Spring Boot 配置

创建 ACL 配置类：

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcMutableAclService aclService() {
        JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(
                dataSource, 
                lookupStrategy(), 
                aclCache());
        
        return jdbcMutableAclService;
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(
                aclEhCacheFactoryBean().getObject(),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy());
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setShared(true);
        return factoryBean;
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(
                dataSource,
                aclCache(),
                aclAuthorizationStrategy(),
                permissionGrantingStrategy());
    }
    
    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
            new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator());
        return expressionHandler;
    }
    
    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new AclPermissionEvaluator(aclService());
    }
}
```

## 3. ACL 内部机制

### 3.1 对象标识符处理

ACL系统需要能够唯一标识每个受保护的对象，这是通过ObjectIdentity接口完成的。

Spring Security ACL 使用 ObjectIdentity 来标识受保护的对象。当进行权限检查时，系统会：

1. **获取对象标识符**：
```java
public class DefaultObjectIdentityGenerator implements ObjectIdentityGenerator {
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        Class<?> type = domainObject.getClass();
        
        try {
            Method idMethod = type.getMethod("getId");
            Object identifier = idMethod.invoke(domainObject);
            return new ObjectIdentityImpl(type.getName(), identifier);
        } catch (Exception e) {
            throw new IllegalArgumentException("无法获取对象标识符");
        }
    }
}
```

2. **自定义标识符获取**：
```java
@Component
public class CustomObjectIdentityGenerator implements ObjectIdentityGenerator {
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        if (domainObject instanceof Document) {
            Document doc = (Document) domainObject;
            return new ObjectIdentityImpl(
                Document.class.getName(),
                doc.getCustomId()
            );
        }
        return null;
    }
}
```

### 3.2 支持的 ID 类型

默认支持的标识符类型：
- Long (最常用)
- Integer
- String
- UUID

```java
public class ObjectIdentityImpl implements ObjectIdentity {
    public ObjectIdentityImpl(String type, Serializable identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("Identifier required");
        }
        if (!(identifier instanceof Long 
              || identifier instanceof Integer 
              || identifier instanceof String 
              || identifier instanceof UUID)) {
            throw new IllegalArgumentException(
                "Identifier must be of type: Long, Integer, String, or UUID");
        }
        // ...
    }
}
```

### 3.3 自定义 ID 类型支持

如果需要使用其他类型的ID，可以自定义ObjectIdentity实现：

```java
public class CustomObjectIdentity implements ObjectIdentity {
    private final String type;
    private final YourCustomIdType identifier;

    public CustomObjectIdentity(String type, YourCustomIdType identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    @Override
    public Serializable getIdentifier() {
        return identifier;
    }

    @Override
    public String getType() {
        return type;
    }
}
```

此外，需要修改数据库表结构以支持自定义ID类型：

```sql
CREATE TABLE acl_object_identity (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    object_id_class BIGINT NOT NULL,
    object_id_identity VARCHAR(255) NOT NULL,  -- 修改为支持其他类型
    -- 其他字段...
);
```

## 4. hasPermission 工作原理解析

在 Spring Security ACL 中，`hasPermission` 表达式是许多初学者容易感到困惑的地方。下面将详细解析它的工作机制：

### 4.1 基本用法

```java
@PreAuthorize("hasPermission(#id, 'com.example.Document', 'READ')")
public Document getDocumentById(Long id) {
    return documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
}
```

### 4.2 执行流程

当这个方法被调用时，背后发生了以下过程：

1. **Spring Security 拦截方法调用**
   - 使用 AOP 机制拦截带有 `@PreAuthorize` 注解的方法
   - 在方法执行前评估安全表达式

2. **表达式处理**
   - `hasPermission` 表达式由 `PermissionEvaluator` 接口的实现类处理
   - 在 ACL 环境中，这通常是 `AclPermissionEvaluator`

3. **权限检查流程**
   ```java
   // AclPermissionEvaluator 内部实现（简化版）
   public boolean hasPermission(Authentication authentication, 
                               Serializable targetId, 
                               String targetType, 
                               Object permission) {
       // 1. 构建对象标识
       ObjectIdentity objectIdentity = new ObjectIdentityImpl(targetType, targetId);
       
       // 2. 获取用户的所有安全标识（用户名和角色）
       List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
       
       try {
           // 3. 查询ACL记录
           Acl acl = aclService.readAclById(objectIdentity, sids);
           
           // 4. 将权限转换为Permission对象
           Permission requiredPermission = permissionFactory.buildFromMask(permissionMask);
           
           // 5. 检查是否有权限
           return acl.isGranted(Collections.singletonList(requiredPermission), 
                              sids, false);
       } catch (NotFoundException nfe) {
           return false;
       }
   }
   ```

### 4.3 数据库查询过程

在数据库层面，`hasPermission` 检查会执行以下查询：

1. **查找域对象类**
   ```sql
   SELECT id FROM acl_class WHERE class = 'com.example.Document'
   ```

2. **查找对象标识**
   ```sql
   SELECT id FROM acl_object_identity 
   WHERE object_id_class = [class_id] AND object_id_identity = [document_id]
   ```

3. **查找用户和角色的SID**
   ```sql
   SELECT id FROM acl_sid WHERE sid = 'username' AND principal = 1
   SELECT id FROM acl_sid WHERE sid IN ('ROLE_X', 'ROLE_Y') AND principal = 0
   ```

4. **查询权限条目**
   ```sql
   SELECT * FROM acl_entry 
   WHERE acl_object_identity = [object_identity_id] 
   AND sid IN ([user_sid], [role_sid1], [role_sid2], ...)
   ```

5. **权限验证**
   - 检查返回的权限mask是否包含请求的权限

### 4.4 权限掩码扩展

关于权限掩码（mask）的问题:

1. **预定义权限掩码**
   - Spring Security ACL默认定义了5个基本权限掩码：
     - READ (1 = 2^0)
     - WRITE (2 = 2^1)
     - CREATE (4 = 2^2) 
     - DELETE (8 = 2^3)
     - ADMINISTRATION (16 = 2^4)

2. **掩码工作原理**
   - 每个权限使用一个二进制位表示
   - 可以通过位运算组合多个权限
   - 例如，同时具有READ和WRITE权限的mask值为3 (1+2)

3. **自定义权限掩码**
   - 可以通过扩展`BasePermission`类来定义自定义权限
   - 新权限应该使用未使用的位，例如：
     ```java
     public static final Permission VIEW_HISTORY = new CustomPermission(32); // 2^5
     public static final Permission EXPORT = new CustomPermission(64);      // 2^6
     ```
   - 理论上最多可以定义32个不同权限（使用int类型）

4. **注意事项**
   - 确保自定义权限不与现有权限冲突
   - 记录并维护自定义权限的文档
   - 考虑可读性，不要定义过多自定义权限

### 4.5 在注解中使用自定义权限名称

除了使用自定义权限掩码值外，您还可以在`@PreAuthorize`注解中直接使用自定义权限的名称，就像使用内置的`READ`、`WRITE`等一样。这需要以下步骤：

1. 定义带名称的自定义权限
```java
public class CustomPermission extends BasePermission {
    // 定义标准权限名称的常量
    public static final Permission VIEW_HISTORY = new CustomPermission(32, "VIEW_HISTORY"); // 2^5
    public static final Permission EXPORT = new CustomPermission(64, "EXPORT");            // 2^6
    
    // 注意这个构造函数接受名称参数
    protected CustomPermission(int mask, String name) {
        super(mask);
        // BasePermission内部会注册权限名称到mask的映射
    }
}
```

2. 创建自定义权限工厂
```java
@Bean
public PermissionFactory permissionFactory() {
    return new CustomPermissionFactory(); // 自定义工厂实现
}

3. 在注解中使用自定义权限名称
@PreAuthorize("hasPermission(#id, 'com.example.Document', 'VIEW_HISTORY')")
public DocumentHistory getDocumentHistory(Long id) {
    // 实现获取文档历史的逻辑
}
```
### 4.6 注解VS编程方式使用ACL

Spring Security ACL提供了两种主要的使用方式，各自适用于不同场景：

#### 注解方式（使用`hasPermission`表达式）

适用于单个对象的访问控制：

```java
// 通过ID访问对象
@PreAuthorize("hasPermission(#documentId, 'com.example.Document', 'READ')")
public Document getDocument(Long documentId) { ... }

// 直接使用对象实例
@PreAuthorize("hasPermission(#document, 'WRITE')")
public Document updateDocument(Document document) { ... }
```

#### 编程方式

以下场景需要通过Java代码编程使用ACL：

1. **批量权限检查**
```java
// 过滤有权访问的文档列表
List<Document> accessibleDocs = documentList.stream()
    .filter(doc -> permissionEvaluator.hasPermission(
        authentication, doc, BasePermission.READ))
    .collect(Collectors.toList());
```

2. **权限管理操作**
```java
// 授予、撤销权限
MutableAcl acl = aclService.readAclById(objectIdentity);
acl.insertAce(index, permission, sid, granting);
aclService.updateAcl(acl);
```

3. **字段级别的权限控制**
```java
// 基于权限控制返回不同字段
DocumentDto dto = new DocumentDto();
// 基本字段总是可见
dto.setId(document.getId());
// 敏感字段需要特定权限
if (acl.isGranted(Collections.singletonList(CustomPermission.VIEW_CONFIDENTIAL), 
                  sids, false)) {
    dto.setConfidentialField(document.getConfidentialField());
}
```

4. **复杂的条件性权限**
```java
// 复杂条件下的权限逻辑
boolean canAccess = isWorkingHours() && 
                   !isInRestrictedLocation() && 
                   aclService.hasPermission(...);
```

实际应用中，通常将注解用于简单的访问控制场景，而在需要更细粒度控制、批量操作或权限管理时，则通过编程方式使用ACL服务。

## 5. 实现特定业务场景

### 5.1 域对象定义

首先，定义我们要保护的文档对象：

```java
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
    
    @Column(name = "department_id")
    private Long departmentId;
    
    // 文档的其他属性
    private String confidentialField;
    private String publicField;
    
    // Getters and setters
}
```

### 5.2 自定义权限

```java
public class CustomPermission extends BasePermission {
    // 除了基本权限外，添加自定义权限
    public static final Permission VIEW_METADATA = new CustomPermission(32); // 5th bit (2^5)
    public static final Permission VIEW_CONFIDENTIAL = new CustomPermission(64); // 6th bit (2^6)
    
    protected CustomPermission(int mask) {
        super(mask);
    }
}
```

### 5.3 权限使用与验证

如果 hasPermission 不按预期工作，可以尝试：

1. **启用调试日志**
```properties
logging.level.org.springframework.security.acls=DEBUG
logging.level.org.springframework.jdbc=DEBUG
logging.level.org.springframework.security.access=DEBUG
```

2. **直接检查数据库表**
   - 验证mask值是否匹配预期的权限

## 6. 实现特定业务场景

### 5.1 场景一：管理员决定只有特定用户可以读取某些文档

这种场景是ACL最基本的用例。我们需要将特定的READ权限分配给特定用户：

```java
@RestController
@RequestMapping("/admin/documents")
public class AdminDocumentController {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentPermissionService permissionService;
    
    /**
     * 管理员为特定用户分配对特定文档的读取权限
     */
    @PostMapping("/{documentId}/access/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignDocumentAccessToUser(
            @PathVariable Long documentId,
            @PathVariable String username) {
        
        // 获取文档
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        
        // 授予用户读取权限
        permissionService.grantPermissionToUser(document, username, BasePermission.READ);
        
        return ResponseEntity.ok().body(Map.of(
                "message", "Read permission granted to user " + username + 
                          " for document " + documentId));
    }
    
    /**
     * 撤销用户对文档的读取权限
     */
    @DeleteMapping("/{documentId}/access/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> revokeDocumentAccessFromUser(
            @PathVariable Long documentId,
            @PathVariable String username) {
        
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        
        permissionService.revokePermissionFromUser(document, username, BasePermission.READ);
        
        return ResponseEntity.ok().body(Map.of(
                "message", "Read permission revoked from user " + username + 
                          " for document " + documentId));
    }
}
```

在文档服务中使用权限检查：

```java
@Service
public class DocumentServiceImpl implements DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    /**
     * 获取文档，会自动检查当前用户是否有读取权限
     */
    @PreAuthorize("hasPermission(#id, 'com.example.Document', 'READ')")
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }
    
    /**
     * 列出用户有权访问的所有文档
     */
    public List<Document> getAccessibleDocuments() {
        // 先获取所有文档
        List<Document> allDocuments = documentRepository.findAll();
        
        // 过滤掉用户没有权限的文档
        return allDocuments.stream()
                .filter(doc -> {
                    try {
                        ObjectIdentity oid = new ObjectIdentityImpl(Document.class, doc.getId());
                        // 当前用户是否有READ权限
                        return permissionEvaluator.hasPermission(
                                SecurityContextHolder.getContext().getAuthentication(),
                                oid,
                                BasePermission.READ.getMask());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
```

### 5.2 场景二：管理员决定只有某个用户组可以读取某些文档

对于用户组的权限控制，需要集成Spring Security的组功能：

```java
@Service
public class GroupPermissionService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentPermissionService permissionService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 为用户组分配对文档的读取权限
     */
    @Transactional
    public void grantGroupReadAccess(Long documentId, String groupName) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        
        // 检查组是否存在
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM groups WHERE group_name = ?", 
                Integer.class, 
                groupName);
        
        if (count == null || count == 0) {
            throw new IllegalArgumentException("Group doesn't exist: " + groupName);
        }
        
        // 授予组读取权限
        permissionService.grantPermissionToGroup(document, groupName, BasePermission.READ);
    }
    
    /**
     * 列出可以访问某个文档的所有组
     */
    public List<String> getGroupsWithDocumentAccess(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        
        ObjectIdentity oid = new ObjectIdentityImpl(Document.class, document.getId());
        
        try {
            Acl acl = aclService.readAclById(oid);
            
            List<String> groups = new ArrayList<>();
            
            for (AccessControlEntry entry : acl.getEntries()) {
                Sid sid = entry.getSid();
                if (sid instanceof GrantedAuthoritySid) {
                    String authority = ((GrantedAuthoritySid) sid).getGrantedAuthority();
                    if (authority.startsWith("GROUP_")) {
                        // 去掉前缀 "GROUP_"
                        groups.add(authority.substring(6));
                    }
                }
            }
            
            return groups;
        } catch (NotFoundException e) {
            return Collections.emptyList();
        }
    }
}
```

在用户登录时，需要加载用户所属的组权限：

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // 获取用户的直接权限
        List<GrantedAuthority> authorities = getUserAuthorities(username);
        
        // 获取用户所属组的权限
        authorities.addAll(getGroupAuthorities(username));
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), 
                user.getPassword(), 
                authorities);
    }
    
    private List<GrantedAuthority> getUserAuthorities(String username) {
        List<String> userAuths = jdbcTemplate.queryForList(
                "SELECT authority FROM authorities WHERE username = ?", 
                String.class, 
                username);
        
        return userAuths.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    
    private List<GrantedAuthority> getGroupAuthorities(String username) {
        // 获取用户所属组的权限
        List<String> groupAuths = jdbcTemplate.queryForList(
                "SELECT ga.authority FROM group_authorities ga " +
                "INNER JOIN group_members gm ON ga.group_id = gm.group_id " +
                "WHERE gm.username = ?", 
                String.class, 
                username);
        
        // 获取用户所属的组名
        List<String> groupNames = jdbcTemplate.queryForList(
                "SELECT g.group_name FROM groups g " +
                "INNER JOIN group_members gm ON g.id = gm.group_id " +
                "WHERE gm.username = ?", 
                String.class, 
                username);
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 添加组权限
        for (String auth : groupAuths) {
            authorities.add(new SimpleGrantedAuthority(auth));
        }
        
        // 添加组名作为权限标识（用于ACL中的GrantedAuthoritySid匹配）
        for (String groupName : groupNames) {
            authorities.add(new SimpleGrantedAuthority("GROUP_" + groupName));
        }
        
        return authorities;
    }
}
```

管理员控制器中添加组权限管理功能：

```java
@RestController
@RequestMapping("/admin")
public class GroupPermissionController {
    
    @Autowired
    private GroupPermissionService groupPermissionService;
    
    /**
     * 为组分配对文档的访问权限
     */
    @PostMapping("/documents/{documentId}/groups/{groupName}/access")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> grantGroupAccess(
            @PathVariable Long documentId,
            @PathVariable String groupName) {
        
        groupPermissionService.grantGroupReadAccess(documentId, groupName);
        
        return ResponseEntity.ok().body(Map.of(
                "message", "Read access granted to group " + groupName + 
                          " for document " + documentId));
    }
    
    /**
     * 查看可访问文档的所有组
     */
    @GetMapping("/documents/{documentId}/groups")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGroupsWithAccess(@PathVariable Long documentId) {
        List<String> groups = groupPermissionService.getGroupsWithDocumentAccess(documentId);
        
        return ResponseEntity.ok().body(Map.of(
                "groups", groups
        ));
    }
}
```

### 5.3 场景三：只有某个部门的用户可以读取某些文档的某些字段

这种场景涉及到字段级别的权限控制。我们需要自定义权限并在服务层实现字段过滤：

```java
@Service
public class FieldLevelSecurityService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private MutableAclService aclService;
    
    /**
     * 获取文档，并根据当前用户权限过滤字段
     */
    public DocumentDto getDocumentWithFieldSecurity(Long id, Authentication authentication) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
        
        ObjectIdentity oid = new ObjectIdentityImpl(Document.class, document.getId());
        
        // 获取当前用户的所有SID（包括用户名和角色）
        List<Sid> sids = new ArrayList<>();
        sids.add(new PrincipalSid(authentication.getName()));
        
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            sids.add(new GrantedAuthoritySid(authority.getAuthority()));
        }
        
        try {
            // 查询用户对该文档的ACL
            Acl acl = aclService.readAclById(oid, sids);
            
            // 创建返回DTO
            DocumentDto dto = new DocumentDto();
            dto.setId(document.getId());
            dto.setTitle(document.getTitle());
            
            // 基本信息总是可见
            dto.setPublicField(document.getPublicField());
            
            // 检查是否有机密字段访问权限
            boolean hasConfidentialAccess = false;
            
            try {
                hasConfidentialAccess = acl.isGranted(
                        Collections.singletonList(CustomPermission.VIEW_CONFIDENTIAL), 
                        sids, 
                        false);
            } catch (NotFoundException e) {
                // 权限不存在，默认为false
            }
            
            // 只有有权限的用户才能看到机密字段
            if (hasConfidentialAccess) {
                dto.setConfidentialField(document.getConfidentialField());
            }
            
            return dto;
            
        } catch (NotFoundException e) {
            // 如果没有ACL记录，检查是否有默认读取权限
            boolean hasRead = permissionEvaluator.hasPermission(
                    authentication, 
                    document.getId(), 
                    Document.class.getName(), 
                    BasePermission.READ.getMask());
            
            if (!hasRead) {
                throw new AccessDeniedException("No access to document " + id);
            }
            
            // 有基本读取权限但没有特殊权限，只返回公开字段
            DocumentDto dto = new DocumentDto();
            dto.setId(document.getId());
            dto.setTitle(document.getTitle());
            dto.setPublicField(document.getPublicField());
            return dto;
        }
    }
}
```

### 5.4 权限管理服务实现

下面是一个完整的权限管理服务实现示例，包含为用户、组和部门分配权限的方法：

```java
@Service
public class DocumentPermissionService {
    
    @Autowired
    private MutableAclService aclService;
    
    @Autowired
    private ObjectIdentityRetrievalStrategy objectIdentityRetrieval;
    
    /**
     * 为指定用户授予对文档的特定权限
     */
    @Transactional
    public void grantPermissionToUser(Document document, String username, Permission permission) {
        ObjectIdentity oid = objectIdentityRetrieval.getObjectIdentity(document);
        Sid sid = new PrincipalSid(username);
        
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);
        }
        
        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        aclService.updateAcl(acl);
        
        System.out.println("Added permission " + permission + " for user " + username + 
                " on document " + document.getId());
    }
    
    /**
     * 为用户组授予对文档的权限
     */
    @Transactional
    public void grantPermissionToGroup(Document document, String groupName, Permission permission) {
        ObjectIdentity oid = objectIdentityRetrieval.getObjectIdentity(document);
        // 注意：组权限使用 GrantedAuthoritySid
        Sid sid = new GrantedAuthoritySid("GROUP_" + groupName);
        
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);
        }
        
        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        aclService.updateAcl(acl);
    }
    
    /**
     * 为部门用户授予对文档特定字段的访问权限
     */
    @Transactional
    public void grantDepartmentConfidentialAccess(Document document, Long departmentId) {
        // 这里我们假设有一个部门角色，如 DEPT_123
        String departmentRole = "DEPT_" + departmentId;
        
        ObjectIdentity oid = objectIdentityRetrieval.getObjectIdentity(document);
        Sid sid = new GrantedAuthoritySid(departmentRole);
        
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);
        }
        
        // 授予部门对机密字段的查看权限
        acl.insertAce(acl.getEntries().size(), CustomPermission.VIEW_CONFIDENTIAL, sid, true);
        aclService.updateAcl(acl);
    }
    
    /**
     * 移除用户的权限
     */
    @Transactional
    public void revokePermissionFromUser(Document document, String username, Permission permission) {
        ObjectIdentity oid = objectIdentityRetrieval.getObjectIdentity(document);
        Sid sid = new PrincipalSid(username);
        
        MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
        
        // 查找并删除匹配的访问控制条目
        List<AccessControlEntry> entries = acl.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            AccessControlEntry entry = entries.get(i);
            if (entry.getSid().equals(sid) && entry.getPermission().equals(permission)) {
                acl.deleteAce(i);
                break;
            }
        }
        
        aclService.updateAcl(acl);
    }
}
```

## 6. 最佳实践与注意事项

### 6.1 性能优化

1. **使用缓存**
   - ACL查询是数据库密集型操作，确保启用EhCache缓存
   - 在ACL权限变化时主动清除相关缓存

2. **批量操作**
   - 实现批量权限检查，减少数据库查询
   - 先获取所有需要检查的对象ID，再一次性检查权限

```java
// 批量权限检查示例
public List<Document> filterAccessible(List<Document> documents, Authentication authentication) {
    Map<ObjectIdentity, Document> docMap = new HashMap<>();
    List<ObjectIdentity> oids = new ArrayList<>();
    
    for (Document doc : documents) {
        ObjectIdentity oid = new ObjectIdentityImpl(Document.class, doc.getId());
        oids.add(oid);
        docMap.put(oid, doc);
    }
    
    // 批量查询ACL
    Map<ObjectIdentity, Acl> acls = aclService.readAclsById(oids);
    
    List<Document> accessible = new ArrayList<>();
    for (Map.Entry<ObjectIdentity, Acl> entry : acls.entrySet()) {
        Acl acl = entry.getValue();
        Document doc = docMap.get(entry.getKey());
        
        // 检查权限
        if (acl.isGranted(Collections.singletonList(BasePermission.READ), 
                convertAuthentication(authentication), false)) {
            accessible.add(doc);
        }
    }
    
    return accessible;
}
```

### 6.2 安全考虑

1. **权限继承与集成**
   - 利用ACL的继承机制，可以让子对象继承父对象的权限
   - 例如，部门的文件夹权限可以继承到其中的所有文档

```java
// 设置权限继承
public void setupPermissionInheritance(Document parentDoc, Document childDoc) {
    ObjectIdentity parentOid = new ObjectIdentityImpl(Document.class, parentDoc.getId());
    ObjectIdentity childOid = new ObjectIdentityImpl(Document.class, childDoc.getId());
    
    MutableAcl parentAcl;
    MutableAcl childAcl;
    
    try {
        parentAcl = (MutableAcl) aclService.readAclById(parentOid);
    } catch (NotFoundException nfe) {
        parentAcl = aclService.createAcl(parentOid);
    }
    
    try {
        childAcl = (MutableAcl) aclService.readAclById(childOid);
    } catch (NotFoundException nfe) {
        childAcl = aclService.createAcl(childOid);
    }
    
    // 设置父子关系
    childAcl.setParent(parentAcl);
    childAcl.setEntriesInheriting(true);
    aclService.updateAcl(childAcl);
}
```

2. **权限审计**
   - 实现自定义AuditLogger记录权限变更
   - 记录谁在什么时间对什么对象做了权限变更

### 6.3 实际应用注意事项

1. **前端集成**
   - 在UI上隐藏或禁用用户没有权限的功能
   - 提供权限管理界面，显示和编辑ACL

2. **处理新建对象**
   - 在创建新对象时自动设置创建者的权限
   - 考虑默认继承上级对象的权限

```java
@PostMapping("/documents")
public ResponseEntity<?> createDocument(@RequestBody Document document, Authentication authentication) {
    // 保存文档
    Document saved = documentRepository.save(document);
    
    // 自动设置创建者的所有权限
    ObjectIdentity oid = new ObjectIdentityImpl(Document.class, saved.getId());
    MutableAcl acl = aclService.createAcl(oid);
    
    // 设置所有者
    acl.setOwner(new PrincipalSid(authentication.getName()));
    
    // 添加创建者的完全控制权限
    acl.insertAce(0, BasePermission.READ, new PrincipalSid(authentication.getName()), true);
    acl.insertAce(1, BasePermission.WRITE, new PrincipalSid(authentication.getName()), true);
    acl.insertAce(2, BasePermission.CREATE, new PrincipalSid(authentication.getName()), true);
    acl.insertAce(3, BasePermission.DELETE, new PrincipalSid(authentication.getName()), true);
    acl.insertAce(4, BasePermission.ADMINISTRATION, new PrincipalSid(authentication.getName()), true);
    
    aclService.updateAcl(acl);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

## 7. 故障排除和调试

### 7.1 常见问题及解决方案

1. **"Access is denied" 异常**
   - 检查用户是否有所需权限
   - 验证SID创建方式是否正确（用户用PrincipalSid，角色用GrantedAuthoritySid）
   - 确保ACL记录存在且包含正确的权限

2. **无法找到ACL记录**
   - 确保在分配权限之前创建了ACL
   - 检查对象类型和ID是否正确
   - 启用SQL日志查看实际执行的SQL语句

3. **权限不生效**
   - 检查用户加载时是否包含了所有权限和组信息
   - 验证SecurityContext中是否有完整的Authentication对象
   - 考虑是否有缓存干扰

### 7.2 调试技巧

1. **启用调试日志**
```properties
logging.level.org.springframework.security.acls=DEBUG
logging.level.org.springframework.jdbc=DEBUG
```

2. **检查数据库内容**
```sql
-- 查看用户的SID记录
SELECT * FROM acl_sid WHERE sid = 'username';

-- 查看特定对象的ACL
SELECT aoi.*, ac.class 
FROM acl_object_identity aoi 
JOIN acl_class ac ON aoi.object_id_class = ac.id
WHERE ac.class = 'com.example.Document' AND aoi.object_id_identity = 123;

-- 查看权限条目
SELECT ae.*, s.sid, s.principal
FROM acl_entry ae
JOIN acl_sid s ON ae.sid = s.id
WHERE ae.acl_object_identity = 
    (SELECT id FROM acl_object_identity WHERE object_id_identity = 123);
```

## 8. ACL 的优势与注意事项

### 8.1 优势

1. **细粒度控制**
   - 可以控制到具体对象级别
   - 比如：用户A可以读取文档1，但不能读取文档2

2. **灵活的权限分配**
   - 可以动态分配和撤销权限
   - 支持权限继承

3. **适合复杂业务场景**
   - 特别适合多租户系统
   - 适合需要对象级别权限控制的应用

### 8.2 注意事项

1. **性能考虑**
   - ACL 查询可能会影响性能
   - 建议使用缓存机制

2. **复杂性**
   - 相比简单的 RBAC，ACL 配置更复杂
   - 需要额外的数据库表来存储 ACL 信息

3. **维护成本**
   - 需要管理大量的 ACL 条目
   - 需要定期清理无用的 ACL 记录
