# JPA (Java Persistence API)

## JPA 查询

### JPA 实体 JOIN 查询及返回类型

#### 1. 基础实体定义

首先定义两个实体类：

```java
@Entity
public class EntityA {
    @Id
    private Long id;
    private String name;
    // 其他字段...
    
    // 构造函数、getter、setter
}

@Entity
public class EntityB {
    @Id
    private Long id;
    private String name;
    @Column(name = "a_id")
    private Long aId;
    // 其他字段...
    
    // 构造函数、getter、setter
}
```

#### 2. 使用 Criteria API 进行 JOIN 查询

##### 2.1 基本查询实现

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<Tuple> query = cb.createTupleQuery();
Root<EntityA> entityA = query.from(EntityA.class);

// JOIN EntityB（不依赖预定义的关系）
Join<EntityA, EntityB> entityB = entityA.join(EntityB.class, JoinType.LEFT);

// 设置 JOIN 条件
entityB.on(cb.equal(entityA.get("id"), entityB.get("aId")));

// 选择整个实体
query.multiselect(
    entityA.alias("entityA"),
    entityB.alias("entityB")
);

List<Tuple> results = entityManager.createQuery(query).getResultList();
```

#### 3. 查询结果转换为前端友好的格式

##### 3.1 使用 Apache Commons Lang3 的 Pair

```java
import org.apache.commons.lang3.tuple.Pair;

@GetMapping("/entity-pairs")
public List<Pair<EntityA, EntityB>> getEntityPairs() {
    // ... 上面的查询代码 ...
    
    return results.stream()
        .map(tuple -> Pair.of(
            tuple.get("entityA", EntityA.class),
            tuple.get("entityB", EntityB.class)
        ))
        .collect(Collectors.toList());
}
```

##### 3.2 使用 Vavr 的 Tuple2

```java
import io.vavr.Tuple2;

@GetMapping("/entity-pairs")
public List<Tuple2<EntityA, EntityB>> getEntityPairs() {
    // ... 上面的查询代码 ...
    
    return results.stream()
        .map(tuple -> new Tuple2<>(
            tuple.get("entityA", EntityA.class),
            tuple.get("entityB", EntityB.class)
        ))
        .collect(Collectors.toList());
}
```

##### 3.3 使用自定义 Pair 类

```java
public class Pair<T1, T2> {
    private final T1 first;
    private final T2 second;
    
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    
    // getters
    public T1 getFirst() { return first; }
    public T2 getSecond() { return second; }
}

@GetMapping("/entity-pairs")
public List<Pair<EntityA, EntityB>> getEntityPairs() {
    // ... 上面的查询代码 ...
    
    return results.stream()
        .map(tuple -> new Pair<>(
            tuple.get("entityA", EntityA.class),
            tuple.get("entityB", EntityB.class)
        ))
        .collect(Collectors.toList());
}
```

#### 4. 依赖配置

##### 4.1 Apache Commons Lang3

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.12.0</version>
</dependency>
```

#### 4.2 Vavr

```xml
<dependency>
    <groupId>io.vavr</groupId>
    <artifactId>vavr</artifactId>
    <version>0.10.4</version>
</dependency>
```

#### 5. 注意事项

1. 实体类需要有合适的构造函数
2. 如果实体间有双向关联，需要处理 JSON 序列化的循环引用问题
3. 建议使用成熟的第三方库（如 Apache Commons Lang3 或 Vavr）而不是自定义 Pair 类
4. 在处理 LEFT JOIN 的结果时，注意处理可能的 null 值

### Spring Data JPA 投影（Projections）

#### 1. 投影的两种方式

##### 1.1 基于接口的投影（Interface-based Projections）
- 更简洁，Spring 自动实现接口
- 只能作为数据容器，不能包含业务逻辑
- 示例：
```java
public interface TokenPairMetadataDto {
    String getTokenPairId();
    String getPairType();
    // ... 其他 getter 方法
}
```

##### 1.2 基于类的投影（Class-based Projections）
- 可以包含业务逻辑
- 必须提供匹配的构造函数
- 不支持原生 SQL 查询
- 示例：
```java
public class TokenPairMetadataDto {
    private String tokenPairId;
    private String pairType;

    // 构造函数参数名必须匹配查询结果的别名
    public TokenPairMetadataDto(String tokenPairId, String pairType) {
        this.tokenPairId = tokenPairId;
        this.pairType = pairType;
    }
    
    // getters, setters 和其他业务方法
}
```

#### 2. 构造函数参数名称匹配

##### 2.1 使用编译器参数
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <parameters>true</parameters>
    </configuration>
</plugin>
```

##### 2.2 使用 @ConstructorProperties 注解
```java
import java.beans.ConstructorProperties;

public class TokenPairMetadataDto {
    @ConstructorProperties({"tokenPairId", "pairType"})
    public TokenPairMetadataDto(String tokenPairId, String pairType) {
        this.tokenPairId = tokenPairId;
        this.pairType = pairType;
    }
}
```

#### 3. 查询示例

##### 3.1 使用 JPQL
```java
@Query("SELECT tp.id as tokenPairId, " +
       "tp.pairType as pairType " +
       "FROM TokenPairCache tp " +
       "WHERE tp.id = :tokenPairId")
TokenPairMetadataDto findById(@Param("tokenPairId") String tokenPairId);
```

##### 3.2 使用构造函数表达式
```java
@Query("SELECT new com.example.TokenPairMetadataDto(tp.id, tp.pairType) " +
       "FROM TokenPairCache tp")
List<TokenPairMetadataDto> findAll();
```

#### 4. 重要注意事项

1. Class-based Projections 必须提供匹配的构造函数
2. 构造函数参数名必须与查询结果别名匹配
3. 参数名称匹配可通过两种方式实现：
   - 编译器参数 `-parameters`
   - `@ConstructorProperties` 注解
4. 不支持原生 SQL 查询
5. 使用 JPQL 时需要使用构造函数表达式

#### 5. 参考文档

- [Spring Data JPA - Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html)
- [Interface-based Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html#projections.interfaces)
- [Class-based Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html#projections.dtos)

### Spring Data JPA 原生查询（Native Query）投影

#### 1. 接口投影（Interface-based Projections）

##### 1.1 完全支持原生 SQL
- ✅ 可以直接使用 `nativeQuery = true`
- 属性名必须与 SQL 查询结果的别名匹配

##### 1.2 示例代码
```java
public interface TokenPairMetadataDto {
    String getTokenPairId();
    String getPairType();
}

@Repository
public interface TokenPairRepository extends JpaRepository<TokenPair, String> {
    @Query(
        value = "SELECT " +
                "tp.id as tokenPairId, " +     // 别名必须匹配接口属性名
                "tp.pair_type as pairType " +  // 下划线命名映射到驼峰命名
                "FROM token_pair tp",
        nativeQuery = true
    )
    List<TokenPairMetadataDto> findAllProjections();
}
```

#### 2. 类投影（Class-based Projections）

##### 2.1 原生 SQL 支持情况
- ❌ 不支持原生 SQL 查询
- 官方文档明确指出："Class-based projections do not work with native queries AT ALL"

##### 2.2 替代方案
如果必须使用原生 SQL 并映射到类，可以使用 @SqlResultSetMapping：
```java
@Entity
@SqlResultSetMapping(
    name = "TokenPairMapping",
    classes = @ConstructorResult(
        targetClass = TokenPairMetadataDto.class,
        columns = {
            @ColumnResult(name = "tokenPairId"),
            @ColumnResult(name = "pairType")
        }
    )
)
public class TokenPair {
    // entity definition
}

@Repository
public interface TokenPairRepository {
    @Query(
        value = "SELECT id as tokenPairId, pair_type as pairType FROM token_pair",
        nativeQuery = true,
        resultSetMapping = "TokenPairMapping"
    )
    List<TokenPairMetadataDto> findAllWithMapping();
}
```

#### 3. 最佳实践建议

##### 3.1 使用原生 SQL 时
- 优先选择接口投影方式
- 接口方法名要符合 JavaBean 规范（get 前缀）
- SQL 别名要与接口属性名精确匹配

##### 3.2 命名约定
- SQL 中的下划线命名（pair_type）要通过别名映射到驼峰命名（pairType）
- 别名区分大小写，必须精确匹配

##### 3.3 避免的做法
- 不要尝试将原生 SQL 查询直接映射到类投影
- 如果必须使用类投影，考虑改用 JPQL 而不是原生 SQL

#### 4. 参考文档
- [Spring Data JPA - Native Queries](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.native-queries)
- [Spring Data JPA - Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html)


