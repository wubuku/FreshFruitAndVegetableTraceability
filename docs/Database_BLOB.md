# 数据库中的大型二进制数据存储方案

## MySQL 的 BLOB 类型
MySQL 提供了一系列 BLOB 类型用于存储二进制数据：

- LONGBLOB: 最大可存储 4GB (2^32 - 1 字节)
- TINYBLOB: 最大 255 字节
- BLOB: 最大 65KB
- MEDIUMBLOB: 最大 16MB

```sql
CREATE TABLE documents (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    content LONGBLOB
);
```

## PostgreSQL 的二进制数据存储

PostgreSQL 不直接支持 BLOB 类型，而是提供两种方案：

### 1. BYTEA 类型
- 直接存储在表中的二进制数据
- 理论最大支持 1GB
- 适合较小的二进制数据

#### BYTEA 和 TOAST 存储机制
1. **基础说明**
- TOAST（The Oversized-Attribute Storage Technique）是 PostgreSQL 处理大数据的机制
- TOAST chunk size 在编译时确定，默认是 1996 字节
- 当数据超过页面大小的 1/4 时会触发 TOAST 存储

1. **TOAST 存储策略**
- BYTEA 的默认存储策略是 `EXTENDED`
- 四种存储策略选项：
  - PLAIN：尽量保持原始形式，不压缩，不 TOAST
  - EXTENDED：先尝试压缩，如果还是太大再 TOAST（默认）
  - EXTERNAL：直接 TOAST，不压缩
  - MAIN：尽量保持在主表中，压缩但不 TOAST
- 不同数据类型有不同的默认策略：
  - TEXT, BYTEA: EXTENDED
  - VARCHAR, TEXT[]等: EXTENDED
  - 大多数数值类型: PLAIN

```sql
-- 创建表时指定存储策略（可选）
CREATE TABLE documents (
    id serial PRIMARY KEY,
    name varchar(100),
    content bytea STORAGE EXTERNAL
);

-- 查看当前表的存储策略
SELECT attname, attstorage
FROM pg_attribute
WHERE attrelid = 'documents'::regclass
  AND attnum > 0;

-- 修改已有表的存储策略
ALTER TABLE documents 
ALTER COLUMN content SET STORAGE EXTERNAL;
```

3. **TOAST 配置和查看**
```sql
-- 查看数据页大小（编译时确定）
SHOW block_size;

-- 查看 TOAST 表的存储参数
SELECT relname, reloptions 
FROM pg_class 
WHERE relkind = 't';  -- 't' 表示 TOAST 表
```

4. **存储限制**
- 实际存储限制取决于：
  - 可用磁盘空间
  - 系统内存
  - 操作系统文件大小限制


### 2. Large Object (LO)
- 存储在特殊的系统表（pg_largeobject）中
- 最大支持 4TB
- 通过 OID（Object Identifier）引用
- 必须在事务中操作

```sql
-- 创建表存储文件信息
CREATE TABLE files (
    id serial PRIMARY KEY,
    filename text,
    file_oid oid
);

-- Large Object 操作示例
-- 创建
SELECT lo_create(-1);  -- 返回一个 OID

-- 写入数据
SELECT lo_open(oid, INV_WRITE);
SELECT lo_write(fd, data);
SELECT lo_close(fd);

-- 读取数据
SELECT lo_open(oid, INV_READ);
SELECT lo_read(fd, len);
SELECT lo_close(fd);

-- 删除
SELECT lo_unlink(oid);

-- 导入导出示例
INSERT INTO files (filename, file_oid) 
VALUES ('example.pdf', lo_import('/path/to/file.pdf'));

SELECT lo_export(file_oid, '/path/to/output.pdf') 
FROM files 
WHERE filename = 'example.pdf';
```

## 其他数据库的对应类型
- Oracle: BLOB
- SQL Server: VARBINARY(max)
- SQLite: BLOB

## 使用建议

1. **跨数据库兼容性**
   - 如果需要跨数据库兼容，建议使用标准的 BLOB 类型
   - PostgreSQL 项目使用 BYTEA 或 Large Object

2. **PostgreSQL 中的选择**
   - 数据 < 1MB：优先使用 BYTEA
   - 数据 > 1MB：考虑使用 Large Object
   - 需要流式处理：使用 Large Object
   - 简单操作：使用 BYTEA

3. **BYTEA 使用注意事项**
   - 了解 TOAST 存储机制的工作原理
   - 合理选择 STORAGE 策略
   - 监控 TOAST 表的使用情况
   - 考虑性能影响，过大的 BYTEA 可能影响查询效率
   - 对于超大二进制数据，建议使用 Large Object 而不是 BYTEA

4. **Large Object 注意事项**
   - 操作必须在事务中进行
   - 需要特别注意清理机制，避免产生孤立对象
   - 考虑使用 `lo_manage` 触发器自动管理
   - 建议在应用层保存 OID 与业务数据的关联

5. **通用建议**
   - 对于非常大的二进制数据，考虑使用文件系统存储，数据库只存储文件路径
   - 根据实际业务需求和性能要求选择合适的存储方案

# JPA/Hibernate 与 PostgreSQL BYTEA 映射指南

## 基本映射方式

### 1. 使用 byte[] 类型（推荐）

```java
@Entity
public class Document {
    @Id
    private Long id;
    
    private String name;
    
    @Lob  // Optional but recommended
    @Column(columnDefinition = "bytea")
    private byte[] content;
    
    // getters and setters
}
```

### 2. 使用 java.sql.Blob 类型

```java
@Entity
public class Document {
    @Id
    private Long id;
    
    private String name;
    
    @Lob
    @Column(columnDefinition = "bytea")
    private Blob content;
    
    // getters and setters
}
```

## Hibernate XML 映射配置

如果使用 Hibernate 映射文件（.hbm.xml），配置方式如下：

```xml
<hibernate-mapping>
    <class name="com.example.Document" table="documents">
        <id name="id" type="long">
            <column name="id"/>
            <generator class="native"/>
        </id>
        
        <property name="name" type="string">
            <column name="name"/>
        </property>
        
        <!-- Using byte[] -->
        <property name="content" type="binary">
            <column name="content" sql-type="bytea"/>
        </property>
        
        <!-- Or using Blob -->
        <!--
        <property name="content" type="blob">
            <column name="content" sql-type="bytea"/>
        </property>
        -->
    </class>
</hibernate-mapping>
```

## 最佳实践

### 数据大小选择
- 小型二进制数据（<1MB）：推荐使用 `byte[]`
- 大型二进制数据：推荐使用 `Blob`（支持流式处理）

### 性能考虑
- `byte[]` 会将整个数据加载到内存中
- `Blob` 支持延迟加载和流式处理，更适合大文件

## 使用示例

### 使用 byte[] 存储

```java
@Repository
public class DocumentRepository {
    @Autowired
    private EntityManager em;
    
    public void saveDocument(String name, byte[] content) {
        Document doc = new Document();
        doc.setName(name);
        doc.setContent(content);
        em.persist(doc);
    }
    
    public byte[] getContent(Long id) {
        Document doc = em.find(Document.class, id);
        return doc.getContent();
    }
}
```

### 使用 Blob 存储

```java
@Repository
public class DocumentRepository {
    @Autowired
    private EntityManager em;
    
    public void saveDocument(String name, InputStream content) throws SQLException {
        Document doc = new Document();
        doc.setName(name);
        
        Blob blob = Hibernate.getLobCreator(em).createBlob(content, content.available());
        doc.setContent(blob);
        em.persist(doc);
    }
    
    public InputStream getContent(Long id) throws SQLException {
        Document doc = em.find(Document.class, id);
        return doc.getContent().getBinaryStream();
    }
}
```

## 配置说明

### 应用配置
在 application.properties/yaml 中添加必要的配置：

```properties
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.datasource.hikari.max-allowed-packet=10485760  # For large files
```

## 注意事项

1. **内存管理**
   - 处理大文件时要注意内存使用
   - 考虑使用分页或流式处理来处理大量二进制数据

2. **资源管理**
   - 在事务中正确关闭流资源
   - 使用 try-with-resources 处理流

3. **存储选择**
   - 对于超大文件（>10MB），建议考虑文件系统存储方案
   - 数据库中只存储文件元数据和路径信息

4. **性能优化**
   - 合理设置连接池参数
   - 考虑使用批处理进行批量操作
   - 适当配置事务隔离级别
