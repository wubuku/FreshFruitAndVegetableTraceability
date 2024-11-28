# Spring Events 与 Spring Integration

## 以一个业务场景为例讨论 Spring Events 的使用

### 业务场景

在一个 Spring 应用中，我们可能会遇到这样的需求场景：
当一个业务操作发生时，需要在数据库中标记某些数据待处理，同时后续还需要对这些数据进行进一步处理。例如：

1. **数据标记需求**
   - 在订单创建时，需要在数据库表中标记该订单需要后续处理
   - 这个标记操作必须在原始事务中完成，确保数据一致性
   - 不希望修改现有的订单创建逻辑，避免代码耦合

2. **异步处理需求**
   - 对已标记数据的处理可能比较耗时（如发送通知、更新相关统计等）
   - 这些处理不应该阻塞主业务流程
   - 需要在标记完成后异步执行这些操作

3. **代码复用需求**
   - 类似的标记-处理模式在多个业务场景中都会用到
   - 希望通过泛型机制来复用处理逻辑
   - 不同的处理器可以灵活匹配不同类型的事件

### 技术需求

基于上述业务场景，我们需要一个事件系统满足以下技术要求：

1. **混合处理模式**
   - 数据标记操作需要同步处理（在同一事务中）
   - 后续处理操作需要异步处理（避免阻塞）

2. **类型安全**
   - 事件定义支持泛型，可以传递不同类型的业务对象
   - 事件包含固定的基础信息（如事件ID、时间戳）
   - 支持在发布事件时提供额外的参数

3. **松耦合设计**
   - 业务代码只需要发布事件，不需要关心处理逻辑
   - 处理逻辑可以独立演进，不影响业务代码
   - 支持灵活添加新的事件处理器

### 解决方案

#### 1. 基础事件类设计

```java:events/BaseEvent.java
public class BaseEvent<T> {
    private final String eventId;
    private final LocalDateTime timestamp;
    private final T payload;
    
    public BaseEvent(T payload) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.payload = payload;
    }
    
    // getters...
}
```

#### 2. 具体事件类型

```java:events/DatabaseMarkerEvent.java
// 同步事件：用于数据库标记
public class DatabaseMarkerEvent<T> extends BaseEvent<T> {
    private final String tableName;
    private final String markType;
    
    public DatabaseMarkerEvent(T payload, String tableName, String markType) {
        super(payload);
        this.tableName = tableName;
        this.markType = markType;
    }
    
    // getters...
}

// 异步事件：用于耗时处理
public class ProcessingEvent<T> extends BaseEvent<T> {
    private final String processingType;
    
    public ProcessingEvent(T payload, String processingType) {
        super(payload);
        this.processingType = processingType;
    }
    
    // getters...
}
```

#### 3. 事件配置

```java:config/EventConfig.java
@Configuration
@EnableAsync
public class EventConfig {
    
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        
        eventMulticaster.setErrorHandler(throwable -> {
            log.error("Error in event processing", throwable);
        });
        
        return eventMulticaster;
    }
    
    @Bean
    public Executor asyncEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("AsyncEvent-");
        executor.initialize();
        return executor;
    }
}
```

#### 4. 事件监听器

```java:listeners/EventListeners.java
@Component
@Transactional
public class DatabaseMarkerListener {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @EventListener
    public void handleDatabaseMarker(DatabaseMarkerEvent<?> event) {
        // 在同一事务中标记数据
        String sql = String.format(
            "UPDATE %s SET needs_processing = true, mark_type = ? WHERE id = ?",
            event.getTableName()
        );
        
        jdbcTemplate.update(sql, event.getMarkType(), event.getPayload().getId());
        
        // 发布异步处理事件
        eventPublisher.publishEvent(new ProcessingEvent<>(
            event.getPayload(),
            "PROCESS_" + event.getMarkType()
        ));
    }
}

@Component
public class AsyncProcessingListener {
    
    @EventListener
    @Async("asyncEventExecutor")
    public void handleProcessing(ProcessingEvent<Order> event) {
        // 处理订单相关的异步操作
        processOrder(event.getPayload());
    }
    
    @EventListener
    @Async("asyncEventExecutor")
    public void handleProcessing(ProcessingEvent<UserData> event) {
        // 处理用户相关的异步操作
        processUser(event.getPayload());
    }
    
    private void processOrder(Order order) {
        // 耗时的订单处理逻辑
    }
    
    private void processUser(UserData user) {
        // 耗时的用户处理逻辑
    }
}
```

#### 5. 业务服务集成

```java:services/OrderService.java
@Service
@Transactional
public class OrderService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder(Order order) {
        // 保存订单的原有业务逻辑
        orderRepository.save(order);
        
        // 发布事件来标记数据需要处理
        eventPublisher.publishEvent(new DatabaseMarkerEvent<>(
            order,
            "orders",
            "NEW_ORDER"
        ));
    }
}
```

### 方案特点

1. **事务一致性保证**
   - 数据库标记操作在原始事务中执行
   - 确保数据一致性

2. **类型安全**
   - 通过泛型支持不同类型的载荷
   - 编译时类型检查

3. **松耦合设计**
   - 业务代码与数据标记/处理逻辑解耦
   - 通过事件机制实现解耦

4. **灵活性**
   - 支持为不同类型数据添加专门的处理器
   - 处理器可以根据事件类型和泛型参数进行匹配

5. **错误处理**
   - 统一的错误处理机制
   - 异步操作的错误不影响主流程

### 使用示例

```java
// 处理订单
Order order = new Order();
eventPublisher.publishEvent(new DatabaseMarkerEvent<>(
    order,
    "orders",
    "NEW_ORDER"
));

// 处理用户
UserData user = new UserData();
eventPublisher.publishEvent(new DatabaseMarkerEvent<>(
    user,
    "users",
    "NEW_USER"
));
```

### 工作流程

1. 业务方法调用触发事件发布
2. 同步监听器在当前事务中处理数据库标记
3. 标记完成后发布异步处理事件
4. 异步监听器在独立线程中处理耗时操作

这个设计方案成功地实现了原始需求，提供了一个类型安全、松耦合且灵活的事件处理系统，同时保证了事务一致性和系统性能。


## Spring Events 异步处理的失败处理方案


下面讨论一个可能的解决方案，需要注意的是，这个方案引入了一些假设，比如处理逻辑的幂等性是可以实现的。


### 设计思路

1. **状态驱动设计**
   - 使用明确的状态枚举而非布尔值
   - 空状态表示无需处理
   - 状态转换反映处理进度

2. **幂等性保证**
   - 处理逻辑保证幂等
   - 支持重复处理
   - 状态不一致时仍然安全

3. **可见性优先**
   - 清晰的状态流转
   - 便于问题排查
   - 支持处理过程追踪

### 具体实现

#### 1. 处理状态定义

```java:enums/ProcessingStatus.java
public enum ProcessingStatus {
    PENDING("待处理"),
    PROCESSING("处理中"),
    COMPLETED("已处理"),
    FAILED("处理失败");
    
    private final String description;
    
    ProcessingStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
```

#### 2. 业务表结构示例

```sql
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    /* 其他业务字段 */
    processing_status VARCHAR(20),
    processing_type VARCHAR(50),
    last_processing_time TIMESTAMP,
    processing_message TEXT,
    INDEX idx_processing (processing_status, processing_type)
);
```

#### 3. 事件监听器实现

```java:listeners/AsyncProcessingListener.java
@Component
@Slf4j
public class AsyncProcessingListener {
    
    @Autowired
    private OrderProcessor orderProcessor;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @EventListener
    @Async("asyncEventExecutor")
    public void handleProcessing(ProcessingEvent<Order> event) {
        Order order = event.getPayload();
        
        try {
            // 更新为处理中状态
            updateProcessingStatus(order.getId(), 
                ProcessingStatus.PROCESSING, 
                "开始处理订单");
            
            // 执行处理
            orderProcessor.process(order);
            
            // 处理成功，更新状态
            updateProcessingStatus(order.getId(), 
                ProcessingStatus.COMPLETED, 
                "订单处理成功");
            
        } catch (Exception e) {
            log.error("Failed to process order {}: {}", 
                order.getId(), e.getMessage());
            
            try {
                // 尝试更新失败状态，如果更新失败也没关系
                // 状态保持为 PROCESSING，后台任务会处理
                updateProcessingStatus(order.getId(), 
                    ProcessingStatus.FAILED, 
                    "处理失败：" + e.getMessage());
            } catch (Exception ex) {
                log.error("Failed to update status for order {}", 
                    order.getId(), ex);
            }
        }
    }
    
    private void updateProcessingStatus(Long orderId, 
                                      ProcessingStatus status, 
                                      String message) {
        jdbcTemplate.update("""
            UPDATE orders 
            SET processing_status = ?,
                processing_message = ?,
                last_processing_time = NOW()
            WHERE id = ?
            """, status.name(), message, orderId);
    }
}
```

#### 4. 后台处理任务

```java:scheduler/OrderProcessingScheduler.java
@Component
@Slf4j
public class OrderProcessingScheduler {
    
    @Autowired
    private OrderProcessor orderProcessor;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Scheduled(fixedDelay = 300000) // 每5分钟执行一次
    public void processUnprocessedOrders() {
        List<Order> ordersToProcess = findOrdersToProcess();
        
        for (Order order : ordersToProcess) {
            try {
                // 更新为处理中状态
                updateProcessingStatus(order.getId(), 
                    ProcessingStatus.PROCESSING, 
                    "后台任务开始处理");
                
                // 执行处理
                orderProcessor.process(order);
                
                // 更新为完成状态
                updateProcessingStatus(order.getId(), 
                    ProcessingStatus.COMPLETED, 
                    "后台任务处理成功");
                
            } catch (Exception e) {
                log.error("Failed to process order {} in scheduler: {}", 
                    order.getId(), e.getMessage());
                
                try {
                    updateProcessingStatus(order.getId(), 
                        ProcessingStatus.FAILED, 
                        "后台任务处理失败：" + e.getMessage());
                } catch (Exception ex) {
                    log.error("Failed to update status for order {}", 
                        order.getId(), ex);
                }
            }
        }
    }
    
    private List<Order> findOrdersToProcess() {
        return jdbcTemplate.query("""
            SELECT * FROM orders 
            WHERE processing_status IN ('PENDING', 'PROCESSING', 'FAILED')
            AND processing_type = 'NEW_ORDER'
            ORDER BY id ASC 
            LIMIT 100
            """, orderRowMapper);
    }
}
```

#### 5. 业务服务集成

```java:services/OrderService.java
@Service
@Transactional
public class OrderService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder(Order order) {
        // 保存订单，初始状态为 PENDING
        order.setProcessingStatus(ProcessingStatus.PENDING);
        order.setProcessingType("NEW_ORDER");
        orderRepository.save(order);
        
        // 发布处理事件
        eventPublisher.publishEvent(new ProcessingEvent<>(order));
    }
}
```

### 方案优势

1. **状态清晰**
   - 明确的状态定义
   - 处理过程可追踪
   - 支持失败原因记录

2. **处理可靠**
   - 基于幂等性的重试机制
   - 状态不一致时仍然安全
   - 后台任务兜底保证

3. **易于维护**
   - 状态流转直观
   - 问题排查方便
   - 支持监控和告警

### 最佳实践

1. **状态设计**
   - 状态定义要清晰
   - 记录状态变更原因
   - 添加时间戳便于追踪

2. **处理策略**
   - 保证处理逻辑幂等
   - 合理设置重试间隔
   - 考虑并发处理能力

3. **监控建议**
   - 监控各状态记录数量
   - 关注 FAILED 状态积压
   - 设置状态停留时间告警

这个改进后的方案通过明确的状态定义，提供了更好的可见性和可维护性，同时保持了处理的可靠性和简单性。状态的流转更加清晰，也为后续的问题排查和系统监控提供了便利。



## Spring Events 与企业集成模式

### 企业集成模式概述

企业集成模式（Enterprise Integration Patterns，EIP）是一系列用于解决企业应用集成问题的设计模式，由 Gregor Hohpe 和 Bobby Woolf 在其著作《Enterprise Integration Patterns》中系统化地提出。这些模式主要解决以下问题：

1. **消息路由**：如何将消息发送到正确的目的地
2. **消息转换**：如何处理不同系统间的消息格式转换
3. **消息分割与聚合**：如何处理大消息的拆分和小消息的组合
4. **消息过滤**：如何过滤掉不需要的消息
5. **消息通道**：如何在系统间传递消息


### Spring Integration 简介

Spring Integration 是 Spring 生态系统中实现企业集成模式的框架，它提供了：

1. **声明式适配器**：用于连接各种外部系统
2. **消息通道抽象**：支持点对点和发布订阅模式
3. **消息路由能力**：基于内容、头部等信息进行路由
4. **消息转换器**：处理不同格式间的转换
5. **端点（Endpoint）**：处理消息的入口和出口

### Spring Events 与 Spring Integration 的对比

| 特性 | Spring Events | Spring Integration |
|------|--------------|-------------------|
| 范围 | 应用内部事件处理 | 跨系统消息集成 |
| 复杂度 | 相对简单 | 较为复杂 |
| 使用场景 | 单应用内的解耦 | 企业级系统集成 |
| 配置方式 | 注解为主 | DSL/XML/注解 |
| 扩展性 | 中等 | 很强 |


### 结合使用的优势

1. **增强的消息路由能力**
   - 可以基于事件内容进行复杂路由
   - 支持动态路由策略

2. **更强大的转换能力**
   - 事件可以无缝转换为各种消息格式
   - 支持复杂的转换链

3. **外部系统集成**
   - 可以将内部事件发送到外部系统
   - 支持多种协议和格式

4. **可靠性提升**
   - 利用 Spring Integration 的错误处理
   - 支持消息持久化

### 实践建议

1. **渐进式采用**
   - 先使用 Spring Events 满足基本需求
   - 在需要外部集成时引入 Spring Integration

2. **职责分离**
   - Spring Events 处理应用内事件
   - Spring Integration 处理系统间集成

3. **性能考虑**
   - 评估消息转换的开销
   - 合理配置缓冲区和线程池

4. **监控和管理**
   - 使用 Spring Integration 的监控特性
   - 实现消息追踪和审计

这种结合使用的方式既保持了 Spring Events 的简单性，又获得了 Spring Integration 的强大集成能力，特别适合那些需要处理复杂集成场景的应用。


## 使用 Spring Integration 与外部系统集成

### 场景需求

假设我们的系统需要：

1. **订单处理通知**
   - 当订单创建后，需要通知多个外部系统
   - 不同的外部系统可能需要不同格式的数据
   - 有些通知通过 Kafka 发送，有些通过 HTTP 接口

2. **灵活的路由策略**
   - VIP 客户的订单需要优先处理
   - 不同类型的订单发送到不同的处理队列
   - 某些特殊订单需要特殊处理流程

### 具体实现

#### 1. 事件网关配置
这个配置创建了一个统一的事件处理入口，类似一个"消息总线"。

```java:config/EventGatewayConfig.java
@Configuration
public class EventGatewayConfig {
    
    @Bean
    public MessageChannel eventChannel() {
        // 创建一个直接通道，用于事件传递
        return new DirectChannel();
    }
    
    @Bean
    @ServiceActivator(inputChannel = "eventChannel")
    public MessageHandler eventHandler() {
        return message -> {
            // 这里可以添加通用的处理逻辑
            // 比如日志记录、监控指标收集等
            BaseEvent<?> event = (BaseEvent<?>) message.getPayload();
            log.info("Received event: {}", event.getEventId());
        };
    }
    
    // 提供一个更简单的接口来发布事件
    @MessagingGateway
    public interface EventGateway {
        @Gateway(requestChannel = "eventChannel")
        void publishEvent(BaseEvent<?> event);
    }
}
```

#### 2. 事件格式转换
不同的外部系统可能需要不同格式的数据，这个组件负责转换。

```java:integration/EventMessageTransformer.java
@Component
public class EventMessageTransformer {
    
    @Transformer(inputChannel = "eventChannel", outputChannel = "messageChannel")
    public Message<?> transformEvent(Message<BaseEvent<?>> message) {
        BaseEvent<?> event = message.getPayload();
        
        // 转换为通用消息格式
        // 例如：订单事件转换为包含必要字段的消息
        return MessageBuilder
            .withPayload(event.getPayload())
            .setHeader("eventId", event.getEventId())
            .setHeader("timestamp", event.getTimestamp())
            // 可以添加更多的业务相关头信息
            .setHeader("orderType", getOrderType(event))
            .setHeader("customerLevel", getCustomerLevel(event))
            .build();
    }
}
```

#### 3. 事件路由配置
根据不同的业务规则将事件路由到不同的处理通道。

```java:integration/EventRouter.java
@Component
public class EventRouter {
    
    @Router(inputChannel = "eventChannel")
    public String routeEvent(BaseEvent<?> event) {
        // 根据事件类型和业务规则决定路由目标
        if (event instanceof DatabaseMarkerEvent) {
            // 数据库标记事件走数据库处理通道
            return "databaseChannel";
        } else if (event instanceof ProcessingEvent) {
            // 根据订单类型决定处理通道
            ProcessingEvent<?> processingEvent = (ProcessingEvent<?>) event;
            if (isVipOrder(processingEvent)) {
                return "vipProcessingChannel";
            }
            return "normalProcessingChannel";
        }
        return "defaultChannel";
    }
    
    private boolean isVipOrder(ProcessingEvent<?> event) {
        // 判断是否是 VIP 订单的业务逻辑
        return false;
    }
}
```

#### 4. 外部系统集成
配置与外部系统的集成，例如发送消息到 Kafka。

```java:integration/ExternalSystemIntegration.java
@Configuration
public class ExternalSystemIntegration {
    
    @Bean
    @ServiceActivator(inputChannel = "processingChannel")
    public MessageHandler kafkaHandler() {
        // 配置 Kafka 消息处理器
        KafkaProducerMessageHandler<String, String> handler = 
            new KafkaProducerMessageHandler<>(kafkaTemplate());
        
        // 设置目标 topic
        handler.setTopicExpression(new LiteralExpression("orders-topic"));
        
        return handler;
    }
    
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        // Kafka 生产者配置
        return new KafkaTemplate<>(producerFactory());
    }
}
```

### 使用示例

```java:services/OrderService.java
@Service
public class OrderService {
    
    @Autowired
    private EventGateway eventGateway;
    
    public void processOrder(Order order) {
        // 发布订单处理事件
        eventGateway.publishEvent(new ProcessingEvent<>(order));
    }
}
```

### 实际应用场景

1. **订单处理流程**
   ```
   订单创建 -> 事件发布 -> 格式转换 -> 路由判断 ->
   ├── VIP订单 -> Kafka快速处理队列
   ├── 普通订单 -> 标准处理队列
   └── 特殊订单 -> 人工审核队列
   ```

2. **多系统通知**
   ```
   订单状态变更 -> 事件发布 ->
   ├── 转换为短信格式 -> 短信服务
   ├── 转换为邮件格式 -> 邮件服务
   └── 转换为APP推送格式 -> 推送服务
   ```

### 方案优势

1. **解耦合**
   - 业务代码只需要发布事件
   - 不需要关心具体的处理流程
   - 便于添加新的处理逻辑

2. **灵活性**
   - 可以动态改变处理流程
   - 支持复杂的路由规则
   - 便于处理特殊情况

3. **可维护性**
   - 各个组件职责清晰
   - 便于监控和排查问题
   - 支持灵活的配置调整



## Spring Events 与 Spring Integration 的结合使用


### 从 Spring Events 到 Spring Integration

在之前的讨论中，我们使用 Spring Events 实现了应用内的事件处理：

```java:services/OrderService.java
@Service
public class OrderService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder(Order order) {
        orderRepository.save(order);
        // 发布事件来标记数据需要处理
        eventPublisher.publishEvent(new DatabaseMarkerEvent<>(
            order,
            "orders",
            "NEW_ORDER"
        ));
    }
}
```

这种方式很好地解决了应用内的事件处理需求。但是，当我们需要：
1. 将事件发送到外部系统（如 Kafka、RabbitMQ）
2. 对不同类型的事件进行复杂路由
3. 进行消息格式转换

这时，我们可以考虑将 Spring Events 与 Spring Integration 结合使用。

### 结合方式示例

#### 1. 通过事件监听器桥接

```java:integration/EventBridgeListener.java
@Component
public class EventBridgeListener {
    
    @Autowired
    private MessageChannel integrationChannel;
    
    @EventListener
    public void bridgeToIntegration(DatabaseMarkerEvent<?> event) {
        // 将 Spring Event 转换为 Spring Integration Message
        Message<?> message = MessageBuilder
            .withPayload(event)
            .setHeader("eventType", "DATABASE_MARKER")
            .build();
            
        // 发送到 Spring Integration 通道
        integrationChannel.send(message);
    }
}
```

这种方式的优点是：
- 保持原有的 Spring Events 代码不变
- 通过监听器实现与 Integration 的桥接
- 可以选择性地桥接特定类型的事件

#### 2. 在需要外部集成时使用 Spring Integration

比如，当我们需要将某些特定事件发送到 Kafka 时：

```java:integration/KafkaIntegrationConfig.java
@Configuration
public class KafkaIntegrationConfig {
    
    @Bean
    @ServiceActivator(inputChannel = "integrationChannel")
    public MessageHandler kafkaHandler() {
        // 仅处理需要发送到 Kafka 的事件
        return message -> {
            if (shouldSendToKafka(message)) {
                // 发送到 Kafka 的逻辑
            }
        };
    }
    
    private boolean shouldSendToKafka(Message<?> message) {
        return message.getPayload() instanceof DatabaseMarkerEvent;
    }
}
```

### 最佳实践建议

1. **保持简单**
   - 对于纯应用内的事件处理，继续使用 Spring Events
   - 只在确实需要外部集成时引入 Spring Integration

2. **清晰的边界**
   - Spring Events 处理应用内的事件流转
   - Spring Integration 处理与外部系统的集成

3. **渐进式采用**
   - 先用 Spring Events 实现核心功能
   - 需要外部集成时，通过监听器桥接到 Integration

## 附录：类、注解与依赖说明

### Maven 依赖

```xml
<!-- Spring Events 相关（包含在 spring-context 中）-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
</dependency>
<!-- Spring Events 相关依赖说明 -->
<!-- 
在 Spring Boot 项目中，通常不需要显式添加 spring-context 依赖，
因为常用的 starter（如 spring-boot-starter-web）已经包含了这个依赖
-->

<!-- Spring Integration 核心 -->
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-core</artifactId>
    <version>${spring-integration.version}</version>
</dependency>

<!-- Spring Integration Kafka（如果需要 Kafka 集成）-->
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-kafka</artifactId>
    <version>${spring-integration.version}</version>
</dependency>
```

### 关键类和注解清单

#### 1. Spring Events 相关

**核心接口/类** (org.springframework.context.event)
- `ApplicationEventPublisher` (org.springframework.context)
- `ApplicationEventMulticaster` (org.springframework.context.event)
- `SimpleApplicationEventMulticaster` (org.springframework.context.event)

**注解**
- `@EventListener` (org.springframework.context.event.EventListener)
- `@Async` (org.springframework.scheduling.annotation.Async)
- `@EnableAsync` (org.springframework.scheduling.annotation.EnableAsync)
- `@Scheduled` (org.springframework.scheduling.annotation.Scheduled)

#### 2. Spring Integration 相关

**注解**
- `@ServiceActivator` (org.springframework.integration.annotation)
- `@MessagingGateway` (org.springframework.integration.annotation)
- `@Gateway` (org.springframework.integration.annotation)
- `@Transformer` (org.springframework.integration.annotation)
- `@Router` (org.springframework.integration.annotation)

**核心类**
- `MessageChannel` (org.springframework.messaging)
- `DirectChannel` (org.springframework.integration.channel)
- `MessageHandler` (org.springframework.messaging)
- `MessageBuilder` (org.springframework.integration.support)
- `KafkaProducerMessageHandler` (org.springframework.integration.kafka.outbound)

#### 3. 自定义类（文章中提到的）

**事件类**
- `BaseEvent<T>` (events)
- `DatabaseMarkerEvent<T>` (events)
- `ProcessingEvent<T>` (events)

**监听器**
- `DatabaseMarkerListener` (listeners)
- `AsyncProcessingListener` (listeners)

**配置类**
- `EventConfig` (config)
- `EventGatewayConfig` (config)
- `KafkaIntegrationConfig` (config)

**服务类**
- `OrderService` (services)
- `ProcessingTaskService` (services)
- `OrderProcessor` (services)

**集成相关**
- `EventBridgeListener` (integration)
- `EventMessageTransformer` (integration)
- `EventRouter` (integration)
- `ExternalSystemIntegration` (integration)

**枚举**
- `ProcessingStatus` (enums)

### 包结构建议

```
com.example.project/
├── config/
├── events/
├── listeners/
├── services/
├── integration/
├── enums/
└── model/
```

### 使用注意事项

1. Spring Events 的核心功能已包含在 spring-context 中
2. 使用 Spring Integration 需要额外引入相关依赖
3. 如果需要特定的集成（如 Kafka），需要引入对应的集成模块
4. 建议按功能模块组织包结构，保持代码的清晰和可维护性
