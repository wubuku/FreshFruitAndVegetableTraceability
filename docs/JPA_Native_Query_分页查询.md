# JPA Native Query 分页查询


一个示例，使用 JPA 的 Native Query 进行分页查询，并使用 Jackson 进行 DTO 转换。

## 1. DTO 类定义

```java
package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RawItemDTO {
    private String productId;
    private String productName;
    private String description;
    private BigDecimal quantityIncluded;
    private LocalDateTime createdAt;
    private String status;
    private String supplierId;
}
```

## 2. 查询参数封装

```java
package com.example.dto;

import lombok.Data;

@Data
public class RawItemQuery {
    private String status;
    private String supplierId;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
}
```

## 3. 基于 Jackson 的 DTO 转换器

```java
package com.example.repository.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.hibernate.transform.ResultTransformer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonResultTransformer<T> implements ResultTransformer {
    private final Class<T> targetClass;
    private final ObjectMapper objectMapper;

    public JacksonResultTransformer(Class<T> targetClass) {
        this.targetClass = targetClass;
        this.objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // 可以根据需要配置 ObjectMapper，例如：
        // objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < aliases.length; i++) {
            map.put(aliases[i], tuple[i]);
        }
        return objectMapper.convertValue(map, targetClass);
    }

    @Override
    public List transformList(List list) {
        return list;
    }
}
```

## 4. Repository 实现

```java
package com.example.repository;

import com.example.dto.RawItemDTO;
import com.example.dto.RawItemQuery;
import com.example.repository.transformer.JacksonResultTransformer;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class RawItemRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public Page<RawItemDTO> findRawItems(RawItemQuery query, Pageable pageable) {
        // 构建基础SQL
        StringBuilder baseSQL = new StringBuilder(
            "SELECT " +
            "   r.product_id, " +
            "   r.product_name, " +
            "   r.description, " +
            "   r.quantity_included, " +
            "   r.created_at, " +
            "   r.status, " +
            "   r.supplier_id " +
            "FROM raw_items r " +
            "WHERE 1=1 "
        );
        
        Map<String, Object> params = new HashMap<>();
        
        // 添加查询条件
        if (query.getStatus() != null) {
            baseSQL.append("AND r.status = :status ");
            params.put("status", query.getStatus());
        }
        if (query.getSupplierId() != null) {
            baseSQL.append("AND r.supplier_id = :supplierId ");
            params.put("supplierId", query.getSupplierId());
        }
        if (query.getCreatedDateFrom() != null) {
            baseSQL.append("AND r.created_at >= :createdDateFrom ");
            params.put("createdDateFrom", query.getCreatedDateFrom());
        }
        if (query.getCreatedDateTo() != null) {
            baseSQL.append("AND r.created_at <= :createdDateTo ");
            params.put("createdDateTo", query.getCreatedDateTo());
        }
        
        // 执行count查询
        String countSQL = "SELECT COUNT(1) FROM (" + baseSQL + ") t";
        Query countQuery = entityManager.createNativeQuery(countSQL);
        params.forEach(countQuery::setParameter);
        long total = ((Number) countQuery.getSingleResult()).longValue();
        
        if (total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        
        // 添加排序
        StringBuilder querySQL = new StringBuilder(baseSQL);
        if (pageable.getSort().isSorted()) {
            querySQL.append("ORDER BY ");
            Iterator<Sort.Order> orderIterator = pageable.getSort().iterator();
            while (orderIterator.hasNext()) {
                Sort.Order order = orderIterator.next();
                querySQL.append(order.getProperty())
                       .append(" ")
                       .append(order.getDirection());
                if (orderIterator.hasNext()) {
                    querySQL.append(", ");
                }
            }
        }
        
        // 添加分页
        querySQL.append(" LIMIT :pageSize OFFSET :offset");
        
        // 创建查询并设置转换器
        Query query = entityManager.createNativeQuery(querySQL.toString());
        query.unwrap(NativeQuery.class)
             .setResultTransformer(new JacksonResultTransformer<>(RawItemDTO.class));
        
        // 设置参数
        params.forEach(query::setParameter);
        query.setParameter("pageSize", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());
        
        @SuppressWarnings("unchecked")
        List<RawItemDTO> content = query.getResultList();
        
        return new PageImpl<>(content, pageable, total);
    }
}
```

## 5. Service 层使用示例

```java
package com.example.service;

import com.example.dto.RawItemDTO;
import com.example.dto.RawItemQuery;
import com.example.repository.RawItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RawItemService {
    @Autowired
    private RawItemRepository rawItemRepository;
    
    @Transactional(readOnly = true)
    public Page<RawItemDTO> findRawItems(RawItemQuery query, Pageable pageable) {
        return rawItemRepository.findRawItems(query, pageable);
    }
}
```

## 主要优点

1. **使用 Jackson 进行灵活的类型转换**
   - 利用 Jackson 强大的类型转换能力
   - 支持复杂的嵌套对象转换
   - 可以通过配置 ObjectMapper 来自定义转换规则

2. **SQL 查询优化**
   - 分离 count 查询和数据查询
   - 动态构建查询条件
   - 支持灵活的排序

3. **代码组织清晰**
   - DTO 类职责单一
   - 查询参数封装
   - 转换逻辑独立

4. **类型安全**
   - 使用泛型确保类型安全
   - 参数绑定防止 SQL 注入

5. **性能考虑**
   - 当总数为 0 时快速返回
   - 使用参数绑定而不是字符串拼接
   - 可以方便地添加缓存层

## PostgreSQL 分页查询的特别说明

PostgreSQL 完全支持标准的 `LIMIT ... OFFSET ...` 语法，这种语法在中小规模数据下表现良好。但在处理大数据集时，需要注意以下几点：

### 1. 大偏移量问题

当 OFFSET 值很大时，PostgreSQL 需要扫描并丢弃 OFFSET 之前的所有行，可能导致性能问题。针对这种情况，有几种优化方案：

```sql
-- 方案1：使用 KEYSET 分页（推荐）
SELECT * FROM raw_items
WHERE (created_at, id) < (?, ?) -- 上一页的最后一条记录的时间戳和ID
ORDER BY created_at DESC, id DESC
LIMIT ?

-- 方案2：使用子查询优化
SELECT *
FROM raw_items r
WHERE r.id IN (
    SELECT i.id
    FROM raw_items i
    ORDER BY i.created_at DESC
    LIMIT ? OFFSET ?
)
```

### 2. 排序优化

如果使用 `ORDER BY`，确保相关列有适当的索引：

```sql
-- 为支持排序的列创建索引
CREATE INDEX idx_raw_items_created_at ON raw_items(created_at DESC);
-- 对于多列排序
CREATE INDEX idx_raw_items_created_at_id ON raw_items(created_at DESC, id DESC);
```

### 3. Keyset 分页实现

```java
package com.example.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public class RawItemRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public Page<RawItemDTO> findRawItemsWithKeyset(
            RawItemQuery query, 
            LocalDateTime lastCreatedAt,
            Long lastId,
            int pageSize) {
        
        StringBuilder baseSQL = new StringBuilder(
            "SELECT r.* FROM raw_items r WHERE 1=1 "
        );
        
        Map<String, Object> params = new HashMap<>();
        
        // 添加查询条件
        if (lastCreatedAt != null && lastId != null) {
            baseSQL.append("AND (r.created_at, r.id) < (:lastCreatedAt, :lastId) ");
            params.put("lastCreatedAt", lastCreatedAt);
            params.put("lastId", lastId);
        }
        
        // ... 其他查询条件 ...
        
        // 添加排序和限制
        baseSQL.append("ORDER BY r.created_at DESC, r.id DESC LIMIT :pageSize");
        
        Query query = entityManager.createNativeQuery(baseSQL.toString());
        query.unwrap(NativeQuery.class)
             .setResultTransformer(new JacksonResultTransformer<>(RawItemDTO.class));
        
        params.forEach(query::setParameter);
        query.setParameter("pageSize", pageSize + 1); // 多查一条用于判断是否有下一页
        
        @SuppressWarnings("unchecked")
        List<RawItemDTO> content = query.getResultList();
        
        boolean hasNext = content.size() > pageSize;
        if (hasNext) {
            content = content.subList(0, pageSize);
        }
        
        // 注意：使用 Keyset 分页时，通常不返回总记录数
        return new KeysetPageImpl<>(content, hasNext);
    }
}

// 自定义的 Keyset 分页实现
public class KeysetPageImpl<T> implements Page<T> {
    private final List<T> content;
    private final boolean hasNext;
    
    public KeysetPageImpl(List<T> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
    
    @Override
    public List<T> getContent() {
        return content;
    }
    
    @Override
    public boolean hasNext() {
        return hasNext;
    }
    
    // ... 其他必要的方法实现 ...
}
```

### 4. 性能监控

```java
@Service
public class RawItemService {
    private static final Logger log = LoggerFactory.getLogger(RawItemService.class);
    
    @Autowired
    private RawItemRepository repository;
    
    @Transactional(readOnly = true)
    public Page<RawItemDTO> findRawItems(RawItemQuery query, Pageable pageable) {
        // 添加性能日志
        long startTime = System.currentTimeMillis();
        try {
            return repository.findRawItems(query, pageable);
        } finally {
            long endTime = System.currentTimeMillis();
            if (endTime - startTime > 1000) { // 超过1秒的查询
                log.warn("Slow query detected: {} ms", endTime - startTime);
            }
        }
    }
}
```

### 5. 使用建议

1. **场景选择**
   - 小数据集（如后台管理系统）：继续使用 `LIMIT/OFFSET`
   - 大数据集或高性能要求：考虑使用 Keyset 分页
   - 总是为排序字段创建适当的索引
   - 考虑使用物化视图或缓存来优化频繁访问的数据

2. **子查询优化说明**
   子查询优化的优势主要体现在：
   - **减少数据传输**：子查询只返回主键或少量字段
   - **索引利用**：子查询可以更好地利用索引
   - **避免排序开销**：可以减少大数据集的排序开销
   - **减少锁定和争用**：只处理需要的行
   - **提高缓存命中率**：减少数据扫描范围

   但需注意，子查询优化的效果取决于具体查询和数据库执行计划，建议通过实际测试验证效果。


## 另一个示例，使用注解

Repository 类：

```java
package org.dddml.aptosflexswap.repository;

import org.dddml.aptosflexswap.domain.dto.TokenPairProjection;
import org.dddml.aptosflexswap.domain.fungibleassetpair.AbstractFungibleAssetPairState.SimpleFungibleAssetPairState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TokenPairRepository extends JpaRepository<SimpleFungibleAssetPairState, String> {

    String BASE_TOKEN_PAIRS_QUERY = """
            SELECT id as id,
                off_chain_version as offChainVersion,
                x_reserve_value as x_ReserveValue,
                y_reserve_value as y_ReserveValue,
                total_liquidity as totalLiquidity,
                liquidity_burned as liquidityBurned,
                fee_numerator as feeNumerator,
                fee_denominator as feeDenominator,
                k_last as k_Last,
                fee_to as feeTo,
                version as version,
                x_token_type as x_TokenType,
                y_token_type as y_TokenType,
                created_at as createdAt,
                updated_at as updatedAt,
                created_by as createdBy,
                updated_by as updatedBy,
                active as active,
                deleted as deleted,
                pair_type as pairType
            FROM (
                SELECT cp.id,
                    cp.off_chain_version,
                    cp.x_reserve_value,
                    cp.y_reserve_value,
                    cp.total_liquidity,
                    cp.liquidity_burned,
                    cp.fee_numerator,
                    cp.fee_denominator,
                    cp.k_last,
                    cp.fee_to,
                    cp.version,
                    cp.x_token_type,
                    cp.y_token_type,
                    cp.created_at,
                    cp.updated_at,
                    cp.created_by,
                    cp.updated_by,
                    cp.active,
                    cp.deleted,
                    'COIN_PAIR' as pair_type
                FROM coin_pair cp
                UNION ALL
                SELECT facp.id,
                    facp.off_chain_version,
                    afs.balance as x_reserve_value,
                    facp.y_reserve_value,
                    facp.total_liquidity,
                    facp.liquidity_burned,
                    facp.fee_numerator,
                    facp.fee_denominator,
                    facp.k_last,
                    facp.fee_to,
                    facp.version,
                    afs.metadata as x_token_type,
                    facp.y_token_type,
                    facp.created_at,
                    facp.updated_at,
                    facp.created_by,
                    facp.updated_by,
                    facp.active,
                    facp.deleted,
                    'FUNGIBLE_ASSET_COIN_PAIR' as pair_type
                FROM fungible_asset_coin_pair facp
                    LEFT JOIN aptos_fungible_store afs ON facp.x_reserve = afs.object_address
                UNION ALL
                SELECT fap.id,
                    fap.off_chain_version,
                    afs_x.balance as x_reserve_value,
                    afs_y.balance as y_reserve_value,
                    fap.total_liquidity,
                    fap.liquidity_burned,
                    fap.fee_numerator,
                    fap.fee_denominator,
                    fap.k_last,
                    fap.fee_to,
                    fap.version,
                    afs_x.metadata as x_token_type,
                    afs_y.metadata as y_token_type,
                    fap.created_at,
                    fap.updated_at,
                    fap.created_by,
                    fap.updated_by,
                    fap.active,
                    fap.deleted,
                    'FUNGIBLE_ASSET_PAIR' as pair_type
                FROM fungible_asset_pair fap
                    LEFT JOIN aptos_fungible_store afs_x ON fap.x_reserve = afs_x.object_address
                    LEFT JOIN aptos_fungible_store afs_y ON fap.y_reserve = afs_y.object_address
            ) all_pairs""";

    String COUNT_QUERY = """
            SELECT COUNT(1) FROM (
            """ + BASE_TOKEN_PAIRS_QUERY + """
            ) t""";

    @Query(value = BASE_TOKEN_PAIRS_QUERY + 
           "\nORDER BY id" +
           "\nLIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}",
           countQuery = COUNT_QUERY,
           nativeQuery = true)
    Page<TokenPairProjection> findAllTokenPairs(Pageable pageable);

    @Query(value = 
            "SELECT * FROM (" + BASE_TOKEN_PAIRS_QUERY + ") tp " +
            "WHERE pair_type = :pairType " +
            "ORDER BY id " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}",
            countQuery = "SELECT COUNT(1) FROM (" + BASE_TOKEN_PAIRS_QUERY + ") tp WHERE pair_type = :pairType",
            nativeQuery = true)
    Page<TokenPairProjection> findByPairType(@Param("pairType") String pairType, Pageable pageable);

    @Query(value = 
            "SELECT * FROM (" + BASE_TOKEN_PAIRS_QUERY + ") tp " +
            "WHERE x_token_type = :tokenType OR y_token_type = :tokenType " +
            "ORDER BY id " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}",
            countQuery = "SELECT COUNT(1) FROM (" + BASE_TOKEN_PAIRS_QUERY + ") tp " +
                        "WHERE x_token_type = :tokenType OR y_token_type = :tokenType",
            nativeQuery = true)
    Page<TokenPairProjection> findByTokenType(@Param("tokenType") String tokenType, Pageable pageable);

    @Query(value = 
            "SELECT DISTINCT tp.*, " +
            "    CASE WHEN ftp.token_pair_id IS NOT NULL THEN 0 ELSE 1 END as sort_featured, " +
            "    ftp.priority_order as sort_priority " +
            "FROM (" + BASE_TOKEN_PAIRS_QUERY + ") tp " +
            "LEFT JOIN featured_token_pair ftp ON tp.id = ftp.token_pair_id " +
            "WHERE (:featureType IS NULL " +
            "      OR (ftp.token_pair_id IS NOT NULL " +
            "          AND ftp.feature_type = :featureType)) " +
            "ORDER BY sort_featured, " +
            "    CASE WHEN sort_priority IS NULL THEN 1 ELSE 0 END, " +
            "    sort_priority, " +
            "    tp.id " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}",
            countQuery = 
            "SELECT COUNT(DISTINCT tp.id) " +
            "FROM (" + BASE_TOKEN_PAIRS_QUERY + ") tp " +
            "LEFT JOIN featured_token_pair ftp ON tp.id = ftp.token_pair_id " +
            "WHERE (:featureType IS NULL " +
            "      OR (ftp.token_pair_id IS NOT NULL " +
            "          AND ftp.feature_type = :featureType))",
            nativeQuery = true)
    Page<TokenPairProjection> findAllFeaturedTokenPairs(@Param("featureType") String featureType, Pageable pageable);
}
```

上面的代码中的 `@Query` 注解，使用了 `countQuery` 属性，来指定 count 查询的 SQL 语句；
之前的 `RawItemRepository` 示例中，通过注入 `EntityManager`，使用字符串拼接的方式来构建查询语句，共享 `baseSQL` 语句，拼接字符串的方式灵活度灰更大一些。


RestController 代码：

```java
package org.dddml.aptosflexswap.restful.resource;

import org.dddml.aptosflexswap.domain.dto.TokenPairProjection;
import org.dddml.aptosflexswap.repository.TokenPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "TokenPairs", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class TokenPairResource {
    @Autowired
    private TokenPairRepository tokenPairRepository;

    @GetMapping
    public Page<TokenPairProjection> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return tokenPairRepository.findAllTokenPairs(PageRequest.of(page, size));
    }

    @GetMapping(path = "featured")
    public Page<TokenPairProjection> getFeatured(
            @RequestParam(required = false) String featureType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return tokenPairRepository.findAllFeaturedTokenPairs(featureType, PageRequest.of(page, size));
    }
}
```