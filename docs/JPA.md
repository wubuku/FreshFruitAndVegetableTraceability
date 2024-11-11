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
