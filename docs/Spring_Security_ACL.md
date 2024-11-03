# Spring Security ACL

> 注意：以下内容基于与 AI 对话的结果整理而成，细节上没有经过验证，仅供参考。

## 1. ACL 基础概念

ACL (Access Control List，访问控制列表) 是一种细粒度的权限控制机制，它允许您为单个域对象（比如具体的文档、记录或资源）定义权限。
这与基于角色的访问控制（RBAC）不同，RBAC 主要关注的是用户对整个类型资源的访问权限。

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

## 2. 实现配置

### 2.1 添加依赖

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-acl</artifactId>
</dependency>
```

### 2.2 配置 ACL 服务

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcMutableAclService aclService() {
        return new JdbcMutableAclService(
            dataSource, 
            lookupStrategy(), 
            aclCache()
        );
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(
            dataSource,
            aclCache(),
            aclAuthorizationStrategy(),
            new ConsoleAuditLogger()
        );
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
    }

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(
            ehCacheFactoryBean().getObject(),
            permissionGrantingStrategy(),
            aclAuthorizationStrategy()
        );
    }
}
```

### 2.3 域对象示例

```java
@Entity
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String content;
    // getters and setters
}
```

### 2.4 服务层使用 ACL 注解

```java
@Service
public class DocumentService {
    @PreAuthorize("hasPermission(#id, 'example.Document', 'READ')")
    public Document getDocument(Long id) {
        return documentRepository.findById(id);
    }

    @PreAuthorize("hasPermission(#document, 'WRITE')")
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }
}
```

## 3. ACL 内部机制

### 3.1 对象标识符处理

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

## 4. 数据库配置

### 4.1 默认表结构

```sql
CREATE TABLE acl_object_identity (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    object_id_class BIGINT NOT NULL,  -- 对象类型ID
    object_id_identity BIGINT NOT NULL,  -- 对象实例ID
    -- 其他字段...
);
```

### 4.2 自定义表结构

```sql
CREATE TABLE acl_object_identity (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    object_id_class BIGINT NOT NULL,
    object_id_identity VARCHAR(255) NOT NULL,  -- 修改为支持其他类型
    -- 其他字段...
);
```

## 5. 最佳实践

1. **使用标准 ID 类型**
   - 推荐使用 `Long` 作为 ID 类型
   - 避免不必要的复杂性

2. **自定义类型注意事项**
   - 确保实现 `Serializable` 接口
   - 提供合适的 `equals()` 和 `hashCode()` 方法
   - 考虑数据库存储效率

3. **处理新建和更新场景**
```java
@PreAuthorize("hasPermission(#document, 'WRITE') or " +
              "(#document.id == null and hasRole('ROLE_CREATOR'))")
public Document saveDocument(Document document) {
    return documentRepository.save(document);
}
```

## 6. ACL 的优势与注意事项

### 6.1 优势

1. **细粒度控制**
   - 可以控制到具体对象级别
   - 比如：用户A可以读取文档1，但不能读取文档2

2. **灵活的权限分配**
   - 可以动态分配和撤销权限
   - 支持权限继承

3. **适合复杂业务场景**
   - 特别适合多租户系统
   - 适合需要对象级别权限控制的应用

### 6.2 注意事项

1. **性能考虑**
   - ACL 查询可能会影响性能
   - 建议使用缓存机制

2. **复杂性**
   - 相比简单的 RBAC，ACL 配置更复杂
   - 需要额外的数据库表来存储 ACL 信息

3. **维护成本**
   - 需要管理大量的 ACL 条目
   - 需要定期清理无用的 ACL 记录
