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


### JPA 投影查询：如何优雅地处理只读 DTO

#### 问题背景
在实际开发中，我们经常遇到这样的情况：需要使用一个无法修改的 DTO 类（可能是其他团队维护的接口）来接收查询结果。这个 DTO：
- 有完整的 getter/setter 方法
- 是典型的数据结构类型
- 无法修改其构造方法

传统的 Class-based Projection 方式会导致冗长的查询语句：
```java
@Query("SELECT new com.example.TokenPairMetadataDto(tp.id, tp.pairType) FROM TokenPairCache tp")
```

#### 解决方案

##### Interface-based Projection + 对象映射（推荐方案）

###### 基本实现
```java
// 1. 定义投影接口
public interface FooProjection {
    String getField1();
    String getField2();
    // ... 其他与 FooDto 一致的 getter 方法
}

// 2. Repository 实现
@Repository
public interface FooRepository extends JpaRepository<Foo, Long> {
    @Query(value = "SELECT f.field1 as field1, f.field2 as field2 FROM foo f", 
           nativeQuery = true)
    List<FooProjection> findAllProjections();
}
```

###### 对象映射方案比较

**A. 使用 MapStruct（推荐）**
```java
@Mapper(componentModel = "spring")
public interface FooMapper {
    FooDto projectionToDto(FooProjection projection);
}

// 在 Repository 中使用
default List<FooDto> findAllDtos() {
    return findAllProjections().stream()
        .map(mapper::projectionToDto)
        .collect(Collectors.toList());
}
```

**B. 使用 Spring BeanUtils**
```java
default List<FooDto> findAllDtos() {
    return findAllProjections().stream()
        .map(proj -> {
            FooDto dto = new FooDto();
            BeanUtils.copyProperties(proj, dto);
            return dto;
        })
        .collect(Collectors.toList());
}
```

**C. 使用 ModelMapper**
```java
default List<FooDto> findAllDtos() {
    ModelMapper modelMapper = new ModelMapper();
    return findAllProjections().stream()
        .map(proj -> modelMapper.map(proj, FooDto.class))
        .collect(Collectors.toList());
}
```

###### 映射工具对比

| 特性       | MapStruct | Spring BeanUtils | ModelMapper  |
| ---------- | --------- | ---------------- | ------------ |
| 性能       | 最佳      | 中等             | 较低         |
| 类型检查   | 编译时    | 运行时           | 运行时       |
| 配置复杂度 | 低        | 最低             | 高           |
| 错误检测   | 编译时    | 运行时静默失败   | 运行时可配置 |
| 额外依赖   | 需要      | 不需要           | 需要         |


##### 使用 ResultTransformer（如果使用 Hibernate）

```java
@Repository
public interface FooRepository extends JpaRepository<Foo, Long> {
    @Query(value = "SELECT f.field1 as field1, f.field2 as field2 FROM foo f", 
           nativeQuery = true)
    @org.hibernate.annotations.ResultTransformer(
        org.hibernate.transform.AliasToBeanResultTransformer.class)
    List<FooDto> findAllDtos();
}
```

##### 使用 JdbcTemplate 和 BeanPropertyRowMapper

```java
@Repository
public class FooRepositoryImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<FooDto> findAllDtos() {
        String sql = "SELECT field1, field2 FROM foo";
        return jdbcTemplate.query(sql, 
            new BeanPropertyRowMapper<>(FooDto.class));
    }
}
```

##### MapStruct 最佳实践

###### 严格配置
```java
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface FooMapper {
    @Mapping(target = "fullName", source = "name")
    FooDto projectionToDto(FooProjection projection);
}
```

###### Maven 依赖
```xml
<properties>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
</properties>

<dependencies>
    <!-- MapStruct 核心库 -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <annotationProcessorPaths>
                    <!-- MapStruct 注解处理器 -->
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

###### 方案优势

1. **类型安全**
   - MapStruct 提供编译时检查
   - 属性变更时立即发现问题
   - IDE 重构支持

2. **代码简洁**
   - 避免手写映射代码
   - 查询语句清晰
   - 维护成本低

3. **性能优秀**
   - MapStruct 生成接近手写的代码
   - 零反射调用
   - 运行时开销最小

###### 注意事项

1. 选择 MapStruct 的原因：
   - 编译时类型检查最安全
   - 性能最优
   - 错误提示清晰
   - 支持复杂映射场景

2. 避免使用：
   - 手动编写映射代码
   - 运行时反射映射
   - 不安全的属性拷贝

3. 重构建议：
   - 使用 MapStruct 的严格配置
   - 明确声明所有映射关系
   - 保持接口和 DTO 的属性命名一致


### 附录：Vavr 简介

[Vavr](https://www.vavr.io/) (原名 Javaslang) 是一个函数式编程库，为 Java 提供了不可变数据类型和函数式编程特性。

#### 1. 不可变集合

##### 简介
提供 `List`, `Set`, `Map` 等不可变集合类型，所有修改操作都返回新的集合实例，原集合保持不变。

##### 代码示例
```java
import io.vavr.collection.*;

// List 示例
List<Integer> list1 = List.of(1, 2, 3);
List<Integer> list2 = list1.append(4);  // list1 不变，返回新列表
List<Integer> list3 = list1.prepend(0); // [0,1,2,3]

// Map 示例
Map<String, Integer> map1 = HashMap.of(
    "a", 1,
    "b", 2
);
Map<String, Integer> map2 = map1.put("c", 3); // 返回新 Map

// Set 示例
Set<String> set1 = HashSet.of("a", "b", "c");
Set<String> set2 = set1.add("d");  // 返回新 Set
```

#### 2. 元组（Tuple）

##### 简介
提供最多8个元素的元组类型，支持模式匹配和转换操作。

##### 基础用法
```java
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;

// 基本使用
Tuple2<String, Integer> person = Tuple.of("Alex", 30);
String name = person._1;    // "Alex"
Integer age = person._2;    // 30
```

##### 高级操作
```java
// 元组转换
Tuple2<Integer, String> swapped = person.map(
    (name, age) -> Tuple.of(age, name)
);

// 三元组示例
Tuple3<String, Integer, Boolean> data = Tuple.of("test", 123, true);
Tuple3<String, Integer, Boolean> mapped = data.map(
    (s, i, b) -> Tuple.of(s.toUpperCase(), i * 2, !b)
);
```

#### 3. 函数式接口

##### 简介
支持函数组合、柯里化和部分应用，提供丰富的函数转换操作。

##### 函数组合
```java
import io.vavr.Function1;
import io.vavr.Function2;

Function1<Integer, Integer> plusOne = a -> a + 1;
Function1<Integer, Integer> multiplyByTwo = a -> a * 2;
Function1<Integer, Integer> composed = plusOne.andThen(multiplyByTwo);
Integer result = composed.apply(5);  // (5 + 1) * 2 = 12
```

##### 柯里化与记忆化
```java
// 柯里化
Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
Function1<Integer, Integer> add5 = sum.curried().apply(5);
Integer sumResult = add5.apply(3);  // 5 + 3 = 8

// 记忆化（缓存函数结果）
Function1<Integer, Integer> factorial = Function1
    .of(n -> n <= 1 ? 1 : n * factorial.apply(n - 1))
    .memoized();
```

#### 4. Option 类型

##### 简介
提供更安全的空值处理和丰富的函数式转换方法。

##### 基础操作
```java
import io.vavr.control.Option;

Option<String> maybeName = Option.of(null);
String result1 = maybeName.getOrElse("unknown");

Option<String> name = Option.of("Alex");
Option<String> greeting = name
    .map(n -> "Hello, " + n)
    .filter(g -> g.length() > 0);
```

##### 高级用法
```java
// 模式匹配
String result2 = name.match(
    Case($(Some($)), value -> "Got: " + value),
    Case($(None()), () -> "Nothing here")
);

// 与 Stream 结合
List<Option<String>> options = List.of(
    Option.of("a"),
    Option.none(),
    Option.of("c")
);
List<String> validValues = options
    .filter(Option::isDefined)
    .map(Option::get);
```

#### 5. Either 类型

##### 简介
用于错误处理和条件分支，可以携带成功或失败的详细信息。

##### 基础用法
```java
import io.vavr.control.Either;

Either<String, Integer> divide(Integer a, Integer b) {
    if (b == 0) {
        return Either.left("Division by zero");
    }
    return Either.right(a / b);
}
```

##### 链式操作
```java
Either<String, Integer> result = divide(10, 2)
    .map(r -> r * 2)
    .mapLeft(error -> "Error: " + error);

String output = result.match(
    left -> "Failed: " + left,
    right -> "Success: " + right
);
```

##### 实际应用示例
```java
public Either<String, User> findUser(String id) {
    try {
        User user = userRepository.findById(id);
        return user != null 
            ? Either.right(user)
            : Either.left("User not found");
    } catch (Exception e) {
        return Either.left("Error: " + e.getMessage());
    }
}
```

#### 使用建议

##### 性能考虑
- 不可变集合的修改操作会创建新实例，注意大量数据时的性能影响
- 适当使用 `memoized()` 可以提高重复计算的性能

##### 与 Java 标准库互操作
- Vavr 提供了与 Java 集合框架的转换方法
- 可以通过 `toJavaXXX()` 和 `ofAll()` 方法进行转换

##### Spring 框架集成
- Spring 5+ 原生支持 Vavr 的 `Option` 类型
- 可以在 Controller 方法返回值中直接使用 Vavr 类型

#### 参考资料
- [Vavr 官方文档](https://docs.vavr.io/)
- [Vavr API 文档](https://www.javadoc.io/doc/io.vavr/vavr/latest/index.html)
- [Vavr GitHub](https://github.com/vavr-io/vavr)

