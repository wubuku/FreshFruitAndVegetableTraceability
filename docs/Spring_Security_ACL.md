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

-- 性能优化索引
CREATE INDEX idx_acl_entry_oid ON acl_entry (acl_object_identity);
CREATE INDEX idx_acl_entry_sid ON acl_entry (sid);
CREATE INDEX idx_acl_object_identity_class ON acl_object_identity (object_id_class);
```

#### 2.2.1 表字段说明

**acl_object_identity 表中的重要字段**：

- **parent_object**: 指向父对象的ACL标识符，用于实现权限继承关系。例如，一个文件夹（父对象）与其内部文件（子对象）的关系，子对象可以继承父对象的权限。

- **entries_inheriting**: 布尔值，指定该对象是否继承父对象的权限。当设置为`true`时，系统在检查权限时会同时检查父对象的权限条目。这对于构建层级化的权限结构（如文件夹/文件系统）非常重要。

**注意**: `object_id_identity`字段默认支持的数据类型包括：
- Long (最常用)
- Integer 
- String (需要修改字段类型为VARCHAR)
- UUID (需要修改字段类型)

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
        
        // 设置缓存失效策略
        ehCacheFactoryBean.setCacheEventListeners(Collections.singletonMap(
                CacheEventType.EXPIRED, new LoggingCacheEventListener()));
        
        // 配置缓存属性
        net.sf.ehcache.config.CacheConfiguration cacheConfig = new net.sf.ehcache.config.CacheConfiguration();
        
        // 缓存最大元素数量
        cacheConfig.setMaxEntriesLocalHeap(10000);
        
        // 缓存元素过期时间（秒）
        cacheConfig.setTimeToLiveSeconds(1800); // 30分钟
        
        // 空闲时间过期（秒）
        cacheConfig.setTimeToIdleSeconds(600); // 10分钟
        
        ehCacheFactoryBean.setCacheConfiguration(cacheConfig);
        
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
        BatchBasicLookupStrategy lookupStrategy = new BatchBasicLookupStrategy(
                dataSource,
                aclCache(),
                aclAuthorizationStrategy(),
                permissionGrantingStrategy());
        
        // 设置批处理大小（默认为50）
        lookupStrategy.setBatchSize(100);
        
        return lookupStrategy;
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

/**
 * 自定义的批量查询优化LookupStrategy
 */
class BatchBasicLookupStrategy extends BasicLookupStrategy {
    private int batchSize = 50;
    
    public BatchBasicLookupStrategy(DataSource dataSource, 
                                    AclCache aclCache,
                                    AclAuthorizationStrategy aclAuthorizationStrategy,
                                    AuditLogger auditLogger) {
        super(dataSource, aclCache, aclAuthorizationStrategy, auditLogger);
    }
    
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    @Override
    protected Map<ObjectIdentity, Acl> lookupObjectIdentities(
            List<ObjectIdentity> objectIdentities, List<Sid> sids) {
        Assert.notEmpty(objectIdentities, "ObjectIdentities required");
        
        Map<ObjectIdentity, Acl> result = new HashMap<>();
        
        // 将查询分批处理，每批不超过batchSize个对象
        for (int i = 0; i < objectIdentities.size(); i += batchSize) {
            int endIdx = Math.min(i + batchSize, objectIdentities.size());
            List<ObjectIdentity> batch = objectIdentities.subList(i, endIdx);
            
            // 对当前批次执行查询
            Map<ObjectIdentity, Acl> batchResult = super.lookupObjectIdentities(batch, sids);
            result.putAll(batchResult);
        }
        
        return result;
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
/**
 * 批量权限检查优化示例
 */
public class BatchPermissionService {
    
    @Autowired
    private MutableAclService aclService;
    
    /**
     * 批量检查多个文档的权限
     * @param docIds 需要检查的文档ID列表
     * @param permission 待检查的权限
     * @param authentication 当前用户认证信息
     * @return 文档ID到权限检查结果的映射
     */
    public Map<Long, Boolean> batchCheckPermission(List<Long> docIds, 
                                                Permission permission,
                                                Authentication authentication) {
        // 获取当前用户的所有SID
        List<Sid> sids = getSids(authentication);
        
        // 构建对象标识符列表
        List<ObjectIdentity> oids = docIds.stream()
                .map(id -> new ObjectIdentityImpl(Document.class, id))
                .collect(Collectors.toList());
        
        try {
            // 批量查询ACLs（一次数据库查询获取所有数据）
            Map<ObjectIdentity, Acl> acls = aclService.readAclsById(oids, sids);
            
            // 检查每个文档的权限
            return docIds.stream()
                    .collect(Collectors.toMap(
                        id -> id,
                        id -> {
                            ObjectIdentity oid = new ObjectIdentityImpl(Document.class, id);
                            if (acls.containsKey(oid)) {
                                try {
                                    return acls.get(oid).isGranted(
                                            Collections.singletonList(permission), sids, false);
                                } catch (NotFoundException | UnloadedSidException e) {
                                    return false;
                                }
                            }
                            return false;
                        }
                    ));
        } catch (Exception e) {
            // 处理异常，如记录未找到等
            return docIds.stream()
                    .collect(Collectors.toMap(id -> id, id -> false));
        }
    }
    
    /**
     * 批量过滤用户有权访问的文档
     * @param documents 要过滤的文档列表  
     * @param permission 所需权限
     * @param authentication 用户认证信息
     * @return 用户有权访问的文档列表
     */
    public List<Document> filterAccessible(List<Document> documents, 
                                         Permission permission,
                                         Authentication authentication) {
        if (documents.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 提取所有文档ID
        List<Long> docIds = documents.stream()
                .map(Document::getId)
                .collect(Collectors.toList());
        
        // 批量检查权限
        Map<Long, Boolean> permissionMap = batchCheckPermission(
                docIds, permission, authentication);
        
        // 过滤出有权限的文档
        return documents.stream()
                .filter(doc -> Boolean.TRUE.equals(permissionMap.get(doc.getId())))
                .collect(Collectors.toList());
    }
    
    /**
     * 与分页结合的权限过滤示例
     */
    public Page<Document> getAccessibleDocuments(Pageable pageable, Authentication authentication) {
        // 1. 先获取当前页面的所有记录（无权限过滤）
        Page<Document> allDocuments = documentRepository.findAll(pageable);
        
        // 2. 应用批量权限过滤
        List<Document> accessibleContent = filterAccessible(
                allDocuments.getContent(), 
                BasePermission.READ,
                authentication);
        
        // 3. 创建新的分页结果
        return new PageImpl<>(
                accessibleContent,
                pageable,
                allDocuments.getTotalElements() // 注：这里总数可能不准确，生产中应优化
        );
    }
    
    /**
     * 从Authentication获取Sid列表
     */
    private List<Sid> getSids(Authentication authentication) {
        List<Sid> sids = new ArrayList<>();
        
        // 添加用户SID
        sids.add(new PrincipalSid(authentication.getName()));
        
        // 添加角色SID
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            sids.add(new GrantedAuthoritySid(authority.getAuthority()));
        }
        
        return sids;
    }
}
```

3. **数据库优化**
   - 确保已创建所有必要的索引（查看2.2节中的索引示例）
   - 考虑定期清理不再需要的ACL记录

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
@Transactional  // 确保权限操作的原子性
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
    
    // 如果是在文件夹内创建的文档，设置父对象关系
    if (document.getParentId() != null) {
        try {
            // 获取父对象的ACL
            ObjectIdentity parentOid = new ObjectIdentityImpl(Document.class, document.getParentId());
            MutableAcl parentAcl = (MutableAcl) aclService.readAclById(parentOid);
            
            // 设置父子关系
            acl.setParent(parentAcl);
            
            // 启用权限继承
            acl.setEntriesInheriting(true);
        } catch (NotFoundException nfe) {
            // 父对象不存在或没有ACL记录，忽略继承
        }
    }
    
    aclService.updateAcl(acl);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

3. **权限变更审计**
   - 实现自定义AuditLogger以记录权限变更
   - 保存权限变更的操作者、时间和内容

```java
/**
 * 自定义审计日志记录器，记录权限变更
 */
public class DatabaseAuditLogger implements AuditLogger {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void logIfNeeded(boolean granted, AccessControlEntry ace) {
        // 只有在配置了审计的情况下才记录
        if ((granted && ace.isAuditSuccess()) || (!granted && ace.isAuditFailure())) {
            String principal = SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            
            String sidName = ace.getSid().toString();
            String objectIdentity = ace.getAcl().getObjectIdentity().toString();
            String permission = ace.getPermission().toString();
            
            jdbcTemplate.update(
                "INSERT INTO acl_audit_log (timestamp, principal, sid, object_identity, " +
                "permission, granted) VALUES (?, ?, ?, ?, ?, ?)",
                new Timestamp(System.currentTimeMillis()),
                principal,
                sidName,
                objectIdentity,
                permission,
                granted
            );
        }
    }
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

## 9. Spring Boot 3.x 兼容性指南

随着Spring Boot 3.x的发布，Spring Security ACL配置需要进行一些调整以保持兼容性。以下是针对Spring Boot 3.x环境的配置更新：

### 9.1 依赖更新

```xml
<!-- Spring Security ACL - Spring Boot 3.x版本 -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-acl</artifactId>
</dependency>

<!-- Spring Boot 3.x使用的缓存支持 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
</dependency>
<!-- Spring Boot 3.x推荐使用Caffeine替代EhCache -->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

### 9.2 更新的缓存配置

Spring Boot 3.x中，推荐使用Caffeine替代EhCache作为缓存提供者。以下是使用Caffeine的ACL缓存配置：

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
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
    public SpringCacheBasedAclCache aclCache() {
        return new SpringCacheBasedAclCache(
                cacheManager().getCache("aclCache"),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy());
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("aclCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10000));
        return cacheManager;
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

### 9.3 注解变更

Spring Boot 3.x中，一些安全注解也发生了变化：

- `@EnableGlobalMethodSecurity` 已被弃用，应使用 `@EnableMethodSecurity`
- 方法安全表达式处理器配置方式略有变化

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig {

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            PermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
            new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}
```

## 10. 自定义权限处理详解

### 10.1 自定义权限类实现

以下是一个完整的自定义权限类实现，包括权限名称映射和组合权限支持：

```java
public class CustomPermission extends BasePermission {
    // 自定义权限常量
    public static final Permission VIEW_METADATA = new CustomPermission(1 << 5, "VIEW_METADATA"); // 32
    public static final Permission VIEW_CONFIDENTIAL = new CustomPermission(1 << 6, "VIEW_CONFIDENTIAL"); // 64
    public static final Permission EXPORT = new CustomPermission(1 << 7, "EXPORT"); // 128
    public static final Permission SHARE = new CustomPermission(1 << 8, "SHARE"); // 256
    
    // 组合权限示例
    public static final Permission VIEW_ALL = new CustomPermission(
            VIEW_METADATA.getMask() | VIEW_CONFIDENTIAL.getMask(), "VIEW_ALL"); // 96
    public static final Permission FULL_CONTROL = new CustomPermission(
            READ.getMask() | WRITE.getMask() | CREATE.getMask() | 
            DELETE.getMask() | ADMINISTRATION.getMask(), "FULL_CONTROL"); // 31
    
    // 权限名称到掩码的映射
    private static final Map<String, Integer> PERMISSION_MAP = new HashMap<>();
    
    static {
        // 注册标准权限
        PERMISSION_MAP.put("READ", READ.getMask());
        PERMISSION_MAP.put("WRITE", WRITE.getMask());
        PERMISSION_MAP.put("CREATE", CREATE.getMask());
        PERMISSION_MAP.put("DELETE", DELETE.getMask());
        PERMISSION_MAP.put("ADMINISTRATION", ADMINISTRATION.getMask());
        
        // 注册自定义权限
        PERMISSION_MAP.put("VIEW_METADATA", VIEW_METADATA.getMask());
        PERMISSION_MAP.put("VIEW_CONFIDENTIAL", VIEW_CONFIDENTIAL.getMask());
        PERMISSION_MAP.put("EXPORT", EXPORT.getMask());
        PERMISSION_MAP.put("SHARE", SHARE.getMask());
        
        // 注册组合权限
        PERMISSION_MAP.put("VIEW_ALL", VIEW_ALL.getMask());
        PERMISSION_MAP.put("FULL_CONTROL", FULL_CONTROL.getMask());
    }
    
    protected CustomPermission(int mask) {
        super(mask);
    }
    
    protected CustomPermission(int mask, String code) {
        super(mask, code);
    }
    
    /**
     * 通过权限名称获取权限掩码
     */
    public static int getMaskByName(String permissionName) {
        Integer mask = PERMISSION_MAP.get(permissionName.toUpperCase());
        if (mask == null) {
            throw new IllegalArgumentException("Unknown permission: " + permissionName);
        }
        return mask;
    }
    
    /**
     * 检查是否包含特定权限
     */
    public static boolean containsPermission(int combinedMask, int permissionToCheck) {
        return (combinedMask & permissionToCheck) == permissionToCheck;
    }
}
```

### 10.2 自定义权限工厂

为了支持在表达式中使用自定义权限名称，需要实现自定义权限工厂：

```java
public class CustomPermissionFactory extends DefaultPermissionFactory {
    
    public CustomPermissionFactory() {
        super();
        // 注册自定义权限
        registerPublicPermissions(CustomPermission.class);
    }
    
    @Override
    public Permission buildFromMask(int mask) {
        // 处理自定义权限掩码
        if (mask == CustomPermission.VIEW_METADATA.getMask()) {
            return CustomPermission.VIEW_METADATA;
        } else if (mask == CustomPermission.VIEW_CONFIDENTIAL.getMask()) {
            return CustomPermission.VIEW_CONFIDENTIAL;
        } else if (mask == CustomPermission.EXPORT.getMask()) {
            return CustomPermission.EXPORT;
        } else if (mask == CustomPermission.SHARE.getMask()) {
            return CustomPermission.SHARE;
        } else if (mask == CustomPermission.VIEW_ALL.getMask()) {
            return CustomPermission.VIEW_ALL;
        } else if (mask == CustomPermission.FULL_CONTROL.getMask()) {
            return CustomPermission.FULL_CONTROL;
        }
        
        // 对于未识别的掩码，尝试使用基类处理
        return super.buildFromMask(mask);
    }
    
    @Override
    public Permission buildFromName(String name) {
        // 处理自定义权限名称
        try {
            int mask = CustomPermission.getMaskByName(name);
            return buildFromMask(mask);
        } catch (IllegalArgumentException e) {
            // 对于未识别的名称，尝试使用基类处理
            return super.buildFromName(name);
        }
    }
}
```

### 10.3 配置自定义权限工厂

在ACL配置中注册自定义权限工厂：

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class AclConfig {
    // ... 其他配置 ...
    
    @Bean
    public PermissionFactory permissionFactory() {
        return new CustomPermissionFactory();
    }
    
    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
            new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
        // 设置自定义权限工厂
        permissionEvaluator.setPermissionFactory(permissionFactory());
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}
```

### 10.4 使用组合权限的实际示例

以下是如何在实践中使用组合权限的示例：

```java
@Service
public class DocumentService {
    
    @Autowired
    private MutableAclService aclService;
    
    /**
     * 为用户授予完全控制权限
     */
    @Transactional
    public void grantFullControl(Document document, String username) {
        ObjectIdentity oid = new ObjectIdentityImpl(Document.class, document.getId());
        Sid sid = new PrincipalSid(username);
        
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);
        }
        
        // 使用组合权限
        acl.insertAce(acl.getEntries().size(), CustomPermission.FULL_CONTROL, sid, true);
        aclService.updateAcl(acl);
    }
    
    /**
     * 检查用户是否拥有特定权限
     */
    public boolean checkPermission(Document document, String username, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(Document.class, document.getId());
        Sid sid = new PrincipalSid(username);
        
        try {
            // 获取ACL
            Acl acl = aclService.readAclById(oid);
            
            // 获取用户的所有ACE
            List<AccessControlEntry> entries = acl.getEntries();
            
            for (AccessControlEntry entry : entries) {
                if (entry.getSid().equals(sid) && entry.isGranting()) {
                    // 检查权限掩码是否包含所需权限
                    int entryMask = entry.getPermission().getMask();
                    int requiredMask = permission.getMask();
                    
                    if (CustomPermission.containsPermission(entryMask, requiredMask)) {
                        return true;
                    }
                }
            }
            
            return false;
        } catch (NotFoundException e) {
            return false;
        }
    }
    
    /**
     * 使用自定义权限的方法示例
     */
    @PreAuthorize("hasPermission(#document, 'VIEW_CONFIDENTIAL')")
    public String getConfidentialContent(Document document) {
        return document.getConfidentialContent();
    }
    
    /**
     * 使用组合权限的方法示例
     */
    @PreAuthorize("hasPermission(#document, 'FULL_CONTROL')")
    public void updateAndShare(Document document, String newContent) {
        document.setContent(newContent);
        // 执行分享操作...
    }
}
```

### 10.5 位掩码权限的正确处理

为了正确处理位掩码权限，特别是在检查组合权限时，可以扩展`AclImpl`类来改进权限检查逻辑：

```java
public class CustomAclImpl extends AclImpl {
    
    public CustomAclImpl(ObjectIdentity objectIdentity, 
                        Serializable id, 
                        AclAuthorizationStrategy aclAuthorizationStrategy,
                        AuditLogger auditLogger,
                        Sid owner,
                        List<Sid> loadedSids,
                        boolean entriesInheriting) {
        super(objectIdentity, id, aclAuthorizationStrategy, auditLogger, 
              owner, loadedSids, entriesInheriting);
    }
    
    @Override
    protected boolean isGranted(AccessControlEntry ace, Permission permission) {
        if (ace.isGranting()) {
            // 使用位运算检查权限，而不是简单的相等性检查
            int aceMask = ace.getPermission().getMask();
            int requestedMask = permission.getMask();
            
            // 检查ACE的掩码是否包含请求的所有权限位
            return (aceMask & requestedMask) == requestedMask;
        }
        
        return false;
    }
}
```

要使用这个自定义实现，需要创建一个自定义的`LookupStrategy`：

```java
public class CustomLookupStrategy extends BasicLookupStrategy {
    
    public CustomLookupStrategy(DataSource dataSource, 
                               AclCache aclCache,
                               AclAuthorizationStrategy aclAuthorizationStrategy,
                               AuditLogger auditLogger) {
        super(dataSource, aclCache, aclAuthorizationStrategy, auditLogger);
    }
    
    @Override
    protected Acl convertToAcl(AclImpl acl) {
        // 创建自定义ACL实现
        return new CustomAclImpl(
                acl.getObjectIdentity(),
                acl.getId(),
                acl.getAclAuthorizationStrategy(),
                acl.getAuditLogger(),
                acl.getOwner(),
                acl.getLoadedSids(),
                acl.isEntriesInheriting()
        );
    }
}
```

最后，在配置中使用自定义的`LookupStrategy`：

```java
@Bean
public LookupStrategy lookupStrategy() {
    return new CustomLookupStrategy(
            dataSource,
            aclCache(),
            aclAuthorizationStrategy(),
            new ConsoleAuditLogger()
    );
}
```

通过这些改进，Spring Security ACL将能够正确处理复杂的位掩码权限组合，使权限系统更加灵活和强大。

## 11. 高级场景与性能挑战

随着应用规模的增长，ACL实现可能面临一些性能和可扩展性挑战。本节探讨如何应对这些挑战。

### 11.1 大规模数据场景的性能优化

在大规模数据场景下，常规的ACL实现可能面临性能瓶颈，主要来自以下几个方面：

#### 11.1.1 分页查询优化

传统的`@PostFilter`注解在分页场景中存在明显缺陷：

```java
// 问题示例：使用@PostFilter进行分页时的性能问题
@PostFilter("hasPermission(filterObject, 'READ')")
public Page<Document> findAll(Pageable pageable) {
    return documentRepository.findAll(pageable);
}
```

这种方式存在的问题：
1. 先获取指定页的全部数据，再在内存中过滤，导致实际返回数量小于请求页大小
2. 总记录数不准确，分页计算错误
3. 大数据量时性能下降明显

**改进方案：两阶段分页**

```java
/**
 * 大规模数据的分页优化方案
 */
@Service
public class OptimizedAclPagingService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private MutableAclService aclService;

    /**
     * 高效的ACL分页查询实现
     */
    public Page<Document> findAccessibleDocuments(Pageable originalPageable, Authentication authentication) {
        // 1. 先获取用户有权限访问的所有ID（只查询ID以提高性能）
        List<Long> accessibleIds = findAllAccessibleIds(authentication);
        
        // 如果没有可访问的文档，返回空结果
        if (accessibleIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), originalPageable, 0);
        }
        
        // 2. 计算实际总数（这是准确的总数）
        long totalElements = accessibleIds.size();
        
        // 3. 计算要跳过的元素数量和获取的元素数量
        int pageSize = originalPageable.getPageSize();
        int pageNumber = originalPageable.getPageNumber();
        int skip = pageNumber * pageSize;
        
        // 如果跳过的数量超过了总数，返回空结果
        if (skip >= totalElements) {
            return new PageImpl<>(Collections.emptyList(), originalPageable, totalElements);
        }
        
        // 4. 获取当前页的ID子集
        List<Long> pageIds = accessibleIds.stream()
                .skip(skip)
                .limit(pageSize)
                .collect(Collectors.toList());
        
        // 5. 使用ID列表查询完整对象
        List<Document> pageContent = documentRepository.findAllById(pageIds);
        
        // 6. 按照ID列表的顺序排序结果（保持排序一致性）
        Map<Long, Document> documentMap = pageContent.stream()
                .collect(Collectors.toMap(Document::getId, Function.identity()));
        
        List<Document> orderedContent = pageIds.stream()
                .map(documentMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        // 7. 返回正确的分页结果
        return new PageImpl<>(orderedContent, originalPageable, totalElements);
    }
    
    /**
     * 查找用户有权访问的所有文档ID
     * 注：对于超大数据量，这个方法还可以进一步优化，如使用游标或分批处理
     */
    private List<Long> findAllAccessibleIds(Authentication authentication) {
        // 获取当前用户的所有SID
        List<Sid> sids = getSids(authentication);
        Permission permission = BasePermission.READ;
        
        // 只查询ID以提高性能
        List<Long> allIds = documentRepository.findAllIds();
        
        // 使用批处理检查权限，每批100个ID
        List<Long> accessibleIds = new ArrayList<>();
        for (int i = 0; i < allIds.size(); i += 100) {
            int end = Math.min(i + 100, allIds.size());
            List<Long> batchIds = allIds.subList(i, end);
            
            // 构建ObjectIdentity批量查询
            List<ObjectIdentity> oids = batchIds.stream()
                    .map(id -> new ObjectIdentityImpl(Document.class, id))
                    .collect(Collectors.toList());
            
            try {
                // 批量查询ACL
                Map<ObjectIdentity, Acl> acls = aclService.readAclsById(oids, sids);
                
                // 检查每个ID的权限
                for (Long id : batchIds) {
                    ObjectIdentity oid = new ObjectIdentityImpl(Document.class, id);
                    if (acls.containsKey(oid)) {
                        try {
                            if (acls.get(oid).isGranted(Collections.singletonList(permission), sids, false)) {
                                accessibleIds.add(id);
                            }
                        } catch (NotFoundException | UnloadedSidException e) {
                            // 忽略不可访问的
                        }
                    }
                }
            } catch (Exception e) {
                // 处理异常
            }
        }
        
        return accessibleIds;
    }
    
    /**
     * 从Authentication获取Sid列表
     */
    private List<Sid> getSids(Authentication authentication) {
        // ... 同前面的实现 ...
    }
}
```

#### 11.1.2 数据库索引与查询优化

对于大规模ACL数据，除了之前提到的基本索引外，还可以考虑以下优化：

```sql
-- 组合索引优化常见查询模式
CREATE INDEX idx_acl_entry_oid_sid ON acl_entry (acl_object_identity, sid);

-- 如果经常按权限类型查询，可以添加包含mask的索引
CREATE INDEX idx_acl_entry_oid_mask ON acl_entry (acl_object_identity, mask);

-- 对于PostgreSQL等支持部分索引的数据库，可以创建只针对granted=true的索引
CREATE INDEX idx_acl_entry_granted ON acl_entry (acl_object_identity, sid, mask) 
WHERE granting = true;
```

#### 11.1.3 缓存策略优化

针对大规模数据，可以采用多级缓存策略：

```java
@Configuration
public class AdvancedAclCacheConfig {
    
    /**
     * 针对大数据量的高级缓存配置
     */
    @Bean
    public CacheManager aclCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("aclCache", "aclPermissionsCache");
        
        // 主ACL缓存配置 - 较长的过期时间
        Caffeine<Object, Object> aclCacheBuilder = Caffeine.newBuilder()
                .maximumSize(50000)
                .expireAfterWrite(60, TimeUnit.MINUTES) 
                .recordStats();
        
        // 权限结果缓存 - 较短的过期时间，用于缓存hasPermission结果
        Caffeine<Object, Object> permissionsCacheBuilder = Caffeine.newBuilder()
                .maximumSize(100000)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats();
        
        // 注册不同的缓存配置
        cacheManager.setCaffeine(aclCacheBuilder);
        cacheManager.registerCustomCache("aclPermissionsCache", permissionsCacheBuilder.build());
        
        return cacheManager;
    }
    
    /**
     * 自定义的权限结果缓存服务
     */
    @Bean
    public PermissionCachingService permissionCachingService(CacheManager cacheManager) {
        return new PermissionCachingServiceImpl(cacheManager.getCache("aclPermissionsCache"));
    }
}

/**
 * 权限结果缓存服务，用于缓存hasPermission的计算结果
 */
class PermissionCachingServiceImpl implements PermissionCachingService {
    
    private final Cache permissionsCache;
    
    public PermissionCachingServiceImpl(Cache permissionsCache) {
        this.permissionsCache = permissionsCache;
    }
    
    /**
     * 缓存权限检查结果
     */
    public boolean hasPermission(Authentication authentication, 
                              ObjectIdentity oid,
                              Permission permission) {
        String cacheKey = buildKey(authentication, oid, permission);
        
        // 尝试从缓存获取结果
        Boolean cachedResult = permissionsCache.get(cacheKey, Boolean.class);
        if (cachedResult != null) {
            return cachedResult;
        }
        
        // 缓存未命中，执行实际权限检查
        boolean result = checkPermissionFromAcl(authentication, oid, permission);
        
        // 缓存结果
        permissionsCache.put(cacheKey, result);
        
        return result;
    }
    
    /**
     * 构建缓存键
     */
    private String buildKey(Authentication authentication, 
                         ObjectIdentity oid,
                         Permission permission) {
        return authentication.getName() + ":" + 
               oid.getType() + ":" + 
               oid.getIdentifier() + ":" + 
               permission.getMask();
    }
    
    /**
     * 实际的权限检查逻辑
     */
    private boolean checkPermissionFromAcl(Authentication authentication, 
                                      ObjectIdentity oid,
                                      Permission permission) {
        // 实际权限检查逻辑...
        // 此处省略，通常会使用AclService和标准的ACL权限检查
        return false; // 需要替换为实际实现
    }
    
    /**
     * 当ACL变更时清除相关缓存
     */
    public void clearPermissionCache(ObjectIdentity oid) {
        // 实际实现可能更复杂，需要清除与特定对象相关的所有缓存项
        // 这里只是示意，实际可能需要使用正则表达式匹配或其他机制
    }
}
```

### 11.2 分布式环境下的缓存一致性

在分布式应用环境中，ACL缓存一致性是一个关键挑战。当在一个节点上更新了ACL权限后，其他节点的缓存可能仍然保留旧数据，导致权限检查不一致。

#### 11.2.1 问题与挑战

分布式环境中的ACL缓存面临以下挑战：

1. **缓存同步延迟**: 权限更新后，其他节点可能在缓存过期前继续使用旧权限
2. **缓存状态不一致**: 不同节点可能有不同的缓存状态，导致同一用户在不同请求中获得不同的权限结果
3. **缓存失效策略**: 需要在性能和一致性之间找到平衡

#### 11.2.2 解决方案

##### 方案一：短期缓存策略（简单实现）

对于不要求严格实时一致性的应用，可以采用较短的缓存过期时间：

```java
@Bean
public CacheManager aclCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("aclCache");
    
    // 设置较短的过期时间，牺牲一些性能换取更好的一致性
    cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES) // 较短的过期时间
            .expireAfterAccess(2, TimeUnit.MINUTES) // 较短的闲置过期时间
    );
    
    return cacheManager;
}
```

这种方案简单易实现，适用于以下场景：
- 小型到中型规模应用
- 权限变更不频繁
- 可以接受几分钟的权限更新延迟

##### 方案二：基于消息的缓存失效（推荐方案）

对于要求更好一致性的应用，可以使用消息系统（如Redis、Kafka、RabbitMQ）实现主动缓存失效：

```java
@Configuration
@EnableCaching
public class DistributedAclCacheConfig {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Bean
    public AclCache aclCache() {
        // 创建基于Spring Cache的ACL缓存
        SpringCacheBasedAclCache cache = new SpringCacheBasedAclCache(
                cacheManager().getCache("aclCache"),
                new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger()),
                new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN")));
        
        // 注册缓存失效监听器
        return new NotifyingAclCache(
                cache, 
                new RedisCacheInvalidationService(redisTemplate));
    }
    
    // 其他配置...
}

/**
 * 基于Redis的缓存失效服务
 */
class RedisCacheInvalidationService implements CacheInvalidationService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final String CHANNEL = "acl:cache:invalidation";
    
    @Autowired
    public RedisCacheInvalidationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        
        // 订阅缓存失效消息
        subscribeToInvalidationEvents();
    }
    
    /**
     * 发布缓存失效消息
     */
    @Override
    public void invalidate(ObjectIdentity oid) {
        CacheInvalidationMessage message = new CacheInvalidationMessage(oid);
        redisTemplate.convertAndSend(CHANNEL, message);
    }
    
    /**
     * 订阅缓存失效消息
     */
    private void subscribeToInvalidationEvents() {
        redisTemplate.getConnectionFactory().getConnection().subscribe(
            (message, pattern) -> {
                // 反序列化消息
                CacheInvalidationMessage invalidationMsg = (CacheInvalidationMessage)
                    redisTemplate.getValueSerializer().deserialize(message.getBody());
                
                // 清除本地缓存
                clearLocalCache(invalidationMsg.getObjectIdentity());
            },
            CHANNEL.getBytes()
        );
    }
    
    /**
     * 清除本地缓存
     */
    private void clearLocalCache(ObjectIdentity oid) {
        // 从Spring缓存管理器中清除指定的缓存项
        // 实际实现取决于使用的缓存管理器
    }
}

/**
 * 缓存失效消息
 */
class CacheInvalidationMessage implements Serializable {
    private ObjectIdentity objectIdentity;
    
    public CacheInvalidationMessage(ObjectIdentity objectIdentity) {
        this.objectIdentity = objectIdentity;
    }
    
    public ObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }
}

/**
 * 支持通知的ACL缓存装饰器
 */
class NotifyingAclCache implements AclCache {
    
    private final AclCache delegate;
    private final CacheInvalidationService invalidationService;
    
    public NotifyingAclCache(AclCache delegate, CacheInvalidationService invalidationService) {
        this.delegate = delegate;
        this.invalidationService = invalidationService;
    }
    
    @Override
    public void evictFromCache(ObjectIdentity objectIdentity) {
        // 1. 先从本地缓存清除
        delegate.evictFromCache(objectIdentity);
        
        // 2. 通知其他节点也清除缓存
        invalidationService.invalidate(objectIdentity);
    }
    
    @Override
    public void evictFromCache(Serializable pk) {
        delegate.evictFromCache(pk);
    }
    
    @Override
    public MutableAcl getFromCache(ObjectIdentity objectIdentity) {
        return delegate.getFromCache(objectIdentity);
    }
    
    @Override
    public MutableAcl getFromCache(Serializable pk) {
        return delegate.getFromCache(pk);
    }
    
    @Override
    public void putInCache(MutableAcl acl) {
        delegate.putInCache(acl);
    }
}

/**
 * 缓存失效服务接口
 */
interface CacheInvalidationService {
    void invalidate(ObjectIdentity objectIdentity);
}
```

这种方案适用于以下场景：
- 中大型分布式应用
- 权限变更需要较快生效
- 存在多个应用节点共享权限数据

##### 方案三：共享缓存策略

另一种方案是完全使用分布式缓存系统（如Redis）作为ACL缓存：

```java
@Configuration
public class SharedAclCacheConfig {
    
    @Bean
    public AclCache aclCache(RedisConnectionFactory redisConnectionFactory) {
        // 创建基于Redis的缓存管理器
        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair
                                .fromSerializer(new JdkSerializationRedisSerializer())))
                .build();
        
        // 创建Redis缓存的ACL实现
        return new SpringCacheBasedAclCache(
                cacheManager.getCache("aclCache"),
                new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger()),
                new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
```

这种方案的特点：
- 所有节点共享一个缓存源，天然保持一致性
- 不需要额外的失效机制
- 缓存操作可能增加网络延迟
- 适合对一致性要求高的场景

#### 11.2.3 最佳实践建议

根据应用特性选择合适的缓存策略：

1. **低频权限变更场景**：使用简单的短期缓存策略，设置合理的过期时间
2. **中等频率变更场景**：使用消息机制的缓存失效策略，平衡性能和一致性
3. **高频变更场景**：考虑共享缓存或完全禁用缓存，优先保证一致性

无论选择哪种策略，都应该实现权限变更的审计日志，以便在出现问题时进行追踪：

```java
@Service
public class AclAuditService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 记录ACL权限变更
     */
    @Transactional
    public void logAclChange(ObjectIdentity oid, 
                           Authentication authentication,
                           String operation,
                           Permission permission,
                           Sid sid,
                           boolean granting) {
        jdbcTemplate.update(
            "INSERT INTO acl_audit_log (timestamp, principal, object_type, " +
            "object_id, operation, permission, sid, granting) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            new Timestamp(System.currentTimeMillis()),
            authentication.getName(),
            oid.getType(),
            oid.getIdentifier().toString(),
            operation,
            permission.toString(),
            sid.toString(),
            granting
        );
    }
}
```

### 11.3 性能监控与优化建议

无论采用哪种策略，监控ACL性能并及时优化都是关键：

1. **缓存命中率监控**:
```java
@Component
public class AclCacheMetrics {
    
    @Autowired
    private CacheManager cacheManager;
    
    @Scheduled(fixedRate = 60000) // 每分钟记录一次
    public void logCacheMetrics() {
        Cache aclCache = cacheManager.getCache("aclCache");
        if (aclCache instanceof CaffeineCache) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = 
                ((CaffeineCache) aclCache).getNativeCache();
            
            CacheStats stats = nativeCache.stats();
            log.info("ACL Cache Stats - Hit Rate: {}%, Hit Count: {}, Miss Count: {}", 
                    stats.hitRate() * 100, 
                    stats.hitCount(), 
                    stats.missCount());
        }
    }
}
```

2. **数据库查询监控**:
   - 监控ACL相关查询的执行时间和频率
   - 识别热点查询模式并优化索引

3. **权限检查耗时监控**:
```java
// 使用AOP监控hasPermission方法的性能
@Aspect
@Component
public class PermissionEvaluationMonitor {
    
    private static final Logger log = LoggerFactory.getLogger(PermissionEvaluationMonitor.class);
    
    @Around("execution(* org.springframework.security.acls.AclPermissionEvaluator.hasPermission(..))")
    public Object monitorPermissionEvaluation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // 记录耗时超过阈值的权限检查
            if (duration > 50) { // 50ms阈值，可调整
                Object[] args = joinPoint.getArgs();
                log.warn("Slow permission check: {}ms, Args: {}", duration, Arrays.toString(args));
            }
        }
    }
}
```

通过定期分析这些监控数据，可以及时发现性能瓶颈并采取相应优化措施。
