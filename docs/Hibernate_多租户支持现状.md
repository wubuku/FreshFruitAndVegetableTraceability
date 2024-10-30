# Hibernate 多租户支持现状

## 概述

Hibernate 声称支持三种多租户策略：
- SEPARATE_DATABASE：每个租户独立数据库
- SEPARATE_SCHEMA：租户共享数据库但使用独立 schema
- DISCRIMINATOR：租户共享数据库和 schema，通过鉴别列（Discriminator，也可翻译为鉴别器）区分


前两种方案（SEPARATE_DATABASE 和 SEPARATE_SCHEMA）其实并没有太本质上的差异。它们都依赖数据库层面提供的隔离机制，无非是隔离的层次不同。

### SEPARATE_DATABASE vs SEPARATE_SCHEMA 对比

#### 共同点

1. **物理隔离**：都提供了租户数据的物理隔离
2. **安全性**：都能提供较高的数据安全保障
3. **灵活性**：都支持租户级别的架构定制
4. **备份恢复**：都支持租户级别的独立备份和恢复

#### 差异对比

| 特性 | SEPARATE_DATABASE | SEPARATE_SCHEMA |
|------|------------------|-----------------|
| 隔离级别 | 最高 | 高 |
| 资源利用 | 较低 | 中等 |
| 运维成本 | 较高 | 中等 |
| 扩展性 | 最佳 | 良好 |
| 成本 | 较高 | 中等 |

#### SEPARATE_DATABASE 优势
1. **完全隔离**：租户间数据完全独立，安全性最高
2. **性能可控**：每个租户有独立的数据库资源（比如，为每个租户安装一个独立的数据库服务器，独占 CPU、内存、磁盘等资源）
3. **维护便利**：可以独立进行数据库维护、升级
4. **灾难恢复**：租户级别的备份恢复最为简单

#### SEPARATE_DATABASE 劣势
1. **资源消耗**：每个租户需要独立的数据库实例
2. **成本较高**：需要更多的硬件资源和许可证
3. **运维复杂**：需要管理多个数据库实例
4. **资源浪费**：小租户也需要完整的数据库资源

#### SEPARATE_SCHEMA 优势
1. **资源共享**：共享数据库实例，降低资源消耗
2. **成本适中**：减少数据库许可证需求
3. **维护便捷**：数据库级别的维护工作统一处理
4. **隔离充分**：提供足够的数据隔离级别

#### SEPARATE_SCHEMA 劣势
1. **性能干扰**：租户间可能存在资源争用
2. **扩展限制**：单个数据库实例的容量限制
3. **备份复杂**：需要额外工作来实现租户级别的备份
4. **故障影响**：数据库实例故障会影响所有租户


### DISCRIMINATOR 策略分析

相比前两种基于“物理隔离”的方案，DISCRIMINATOR 策略采用逻辑隔离的方式，具有其独特的优势和局限性。

#### 优势

1. **资源利用效率最高**
   - 所有租户共享数据库资源
   - 显著降低基础设施成本
   - 非常适合大量小型租户的场景

2. **运维简单**
   - 单一数据库实例管理
   - 架构变更一次性完成
   - 备份恢复流程统一

3. **弹性伸缩**
   - 新租户创建几乎零成本
   - 租户数量扩展不受限于数据库实例
   - 资源动态分配更灵活

4. **功能共享**
   - 缓存机制可跨租户共享
   - 数据库连接池更高效利用
   - 查询优化器可以更好工作

5. **适合微服务**
   - 部署简单，容器化友好
   - 服务实例可以处理任意租户
   - 有利于实现无状态设计

#### 局限性

1. **数据隔离较弱**
   - 依赖应用层面保证隔离
   - 潜在的数据泄露风险
   - 需要更严格的访问控制

2. **性能考量**
   - 查询需要额外的租户过滤条件
   - 大租户可能影响其他租户
   - 索引效率可能受影响

3. **定制化受限**
   - 难以支持租户级别的架构定制
   - 升级必须所有租户同步
   - 难以满足特殊租户需求

4. **数据管理复杂**
   - 租户数据清理较为困难
   - 特定租户数据迁移复杂
   - 审计和合规要求可能相对更难满足，特别是不同租户属于不同的法律主体时

然而，尽管 DISCRIMINATOR 策略在配置选项中存在，但实际上 Hibernate 并未完整实现这个功能。
这个问题已经存在多年，至今没有得到真正的解决。


## 相关 Issue

以下是 Hibernate JIRA 上的相关问题报告：

- [HHH-6054](https://hibernate.atlassian.net/browse/HHH-6054) - Discriminator based multitenancy implementation
- [HHH-7249](https://hibernate.atlassian.net/browse/HHH-7249) - Support for discriminator-based multi-tenancy
- [HHH-11890](https://hibernate.atlassian.net/browse/HHH-11890) - Multi-tenancy DISCRIMINATOR not working


在我们看来 Hibernate 的开发团队对该策略的支持并不积极，并且可能会持续下去。
从技术实现的角度来看，这个策略的复杂度确实比前两者要高很多。


## 现有替代方案

由于缺乏原生支持，业界普遍采用一些替代方案来实现类似的功能。

下面提到的方法一般需要结合使用才能实现相对完整的多租户支持：

### 1. 使用 Hibernate Filter

```java
@FilterDef(name = "tenantFilter")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Entity {
    private String id;
    private String tenantId;
    // ...
}

// 使用时需要启用过滤器
session.enableFilter("tenantFilter")
       .setParameter("tenantId", currentTenant);
```

### 2. 使用 Hibernate Interceptor

```java
public class TenantInterceptor extends EmptyInterceptor {
    @Override
    public String onPrepareStatement(String sql) {
        // 在 SQL 中添加租户条件
        return sql + " AND tenant_id = " + getCurrentTenant();
    }
}
```

### 3. 使用 @Where 注解

```java
@Entity
@Where(clause = "tenant_id = '${tenantId}'")
public class Entity {
    // ...
}
```

## 替代方案的局限性

1. **性能影响**
   - 所有查询都需要附加额外的过滤条件
   - 可能影响数据库查询优化器的效果

2. **维护复杂性**
   - 需要确保所有查询都正确应用了租户过滤
   - 原生 SQL 查询需要特别处理
   - 数据迁移和备份恢复较为复杂

3. **安全风险**
   - 如果忘记应用过滤器可能导致数据泄露
   - 需要额外的应用层面安全控制

## 结论

1. Hibernate 目前并未真正实现 DISCRIMINATOR 策略的原生支持
2. 现有的多租户支持主要集中在 DATABASE 和 SCHEMA 两种策略
3. 需要通过 Filter、Interceptor 等机制来变通实现 DISCRIMINATOR 策略
4. 这些替代方案虽然可用，但都离比较完美的解决方案有相当大的距离，各有其局限性


## 建议

1. 如果项目对多租户的数据隔离有较高要求，且可以承受多个数据库带来的额外维护成本，建议：
   - 使用 Hibernate 的 DATABASE 或 SCHEMA 策略
   - 考虑其他专门的多租户解决方案
   
2. 当然，特定项目选择基于鉴别器的多租户方案往往也有其充足的理由。如果基于 Hibernate 来实现，这时往往考虑需要：
   - 建立严格的开发规范
   - 实现完善的测试覆盖
   - 考虑封装统一的多租户访问层


