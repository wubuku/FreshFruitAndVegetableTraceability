# 事件总线系统：Guava EventBus 与 Spring Events 详解

## 初始配置要求

### Guava EventBus 配置
Guava EventBus 需要显式注册监听器，不会自动扫描和注册。有两种注册方式：

1. **单个监听器注册**
   ```java
   EventBus eventBus = new EventBus();
   eventBus.register(new UserEventListener());
   ```

2. **批量注册**
   ```java
   public class EventBusConfig {
       private final EventBus eventBus = new EventBus();
       
       public void registerListeners(List<Object> listeners) {
           listeners.forEach(eventBus.register);
       }
   }
   ```

### Spring Events 配置
Spring Events 系统是 Spring Framework 的一部分，基本配置很简单：

1. **基础配置**
   ```java
   @Configuration
   @ComponentScan  // 确保能扫描到事件监听器
   public class EventConfig {
   }
   ```

2. **异步支持配置**
   ```java
   @Configuration
   @EnableAsync
   public class AsyncEventConfig {
       @Bean
       public AsyncEventMulticaster applicationEventMulticaster() {
           SimpleAsyncEventMulticaster eventMulticaster = 
               new SimpleAsyncEventMulticaster();
           eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
           return eventMulticaster;
       }
   }
   ```

## 事件继承支持对比

### Guava EventBus 的继承支持

1. **基本继承**
   ```java
   // 基础事件
   public class BaseEvent {
       private final String commonField;
       
       public BaseEvent(String commonField) {
           this.commonField = commonField;
       }
   }
   
   // 子事件
   public class UserCreatedEvent extends BaseEvent {
       private final String username;
       
       public UserCreatedEvent(String commonField, String username) {
           super(commonField);
           this.username = username;
       }
   }
   
   // 监听器可以处理父类事件
   public class EventListener {
       @Subscribe
       public void handleBaseEvent(BaseEvent event) {
           // 处理所有继承自 BaseEvent 的事件
       }
       
       @Subscribe
       public void handleUserCreated(UserCreatedEvent event) {
           // 只处理 UserCreatedEvent
       }
   }
   ```

### Spring Events 的继承支持

Spring Events 同样支持事件继承，而且提供了更灵活的配置：

1. **基本继承**
   ```java
   // 基础事件
   public abstract class BaseEvent extends ApplicationEvent {
       private final String commonField;
       
       public BaseEvent(Object source, String commonField) {
           super(source);
           this.commonField = commonField;
       }
   }
   
   // 子事件
   public class UserCreatedEvent extends BaseEvent {
       private final String username;
       
       public UserCreatedEvent(Object source, String commonField, String username) {
           super(source, commonField);
           this.username = username;
       }
   }
   ```

2. **继承事件的监听**
   ```java
   @Component
   public class EventListener {
       @EventListener
       public void handleBaseEvent(BaseEvent event) {
           // 处理所有继承自 BaseEvent 的事件
       }
       
       @EventListener
       public void handleUserCreated(UserCreatedEvent event) {
           // 只处理 UserCreatedEvent
       }
   }
   ```

3. **条件继承处理**
   ```java
   @Component
   public class ConditionalEventListener {
       @EventListener(condition = "#event instanceof T(com.example.UserCreatedEvent)")
       public void handleSpecificEvent(BaseEvent event) {
           // 只处理特定类型的事件
       }
   }
   ```


## 自动配置与手动配置详解

### Guava EventBus 的配置模式

1. **手动配置模式（推荐）**
   ```java
   public class EventBusManager {
       private final EventBus eventBus;
       
       public EventBusManager() {
           this.eventBus = new EventBus();
       }
       
       public void register(Object listener) {
           eventBus.register(listener);
       }
       
       public void unregister(Object listener) {
           eventBus.unregister(listener);
       }
       
       public void post(Object event) {
           eventBus.post(event);
       }
   }
   ```

2. **Spring 集成配置（可选）**
   ```java
   @Configuration
   public class EventBusConfig {
       @Bean
       public EventBus eventBus() {
           return new EventBus();
       }
       
       @Bean
       public EventBusManager eventBusManager(EventBus eventBus) {
           return new EventBusManager(eventBus);
       }
       
       // 自动注册所有标记了特定注解的监听器
       @PostConstruct
       public void registerListeners(ApplicationContext context) {
           context.getBeansWithAnnotation(EventBusListener.class)
                 .values()
                 .forEach(eventBus()::register);
       }
   }
   ```

### Spring Events 的配置模式

1. **自动配置（默认）**
   - Spring Boot 项目会自动配置事件机制
   - 无需额外配置即可使用 `@EventListener`
   - 组件扫描会自动发现监听器

2. **手动配置（特殊需求）**
   ```java
   @Configuration
   public class EventConfig {
       @Bean
       public ApplicationEventMulticaster applicationEventMulticaster() {
           SimpleApplicationEventMulticaster multicaster = 
               new SimpleApplicationEventMulticaster();
           // 自定义配置
           return multicaster;
       }
   }
   ```

## 重要注意事项

### Guava EventBus 注意事项

1. **注册管理**
   - 必须显式注册监听器
   - 需要管理监听器的生命周期
   - 注意内存泄漏问题

2. **线程安全**
   ```java
   // 异步事件总线需要明确指定线程池
   ExecutorService executor = Executors.newFixedThreadPool(4);
   AsyncEventBus asyncEventBus = new AsyncEventBus(executor);
   
   // 记得在应用关闭时关闭线程池
   Runtime.getRuntime().addShutdownHook(new Thread(() -> {
       executor.shutdown();
       try {
           executor.awaitTermination(5, TimeUnit.SECONDS);
       } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
       }
   }));
   ```

### Spring Events 注意事项

1. **自动配置**
   - 默认情况下无需特殊配置
   - `@EventListener` 注解会被自动处理
   - 需要确保组件扫描能够覆盖到监听器类

2. **事务集成**
   ```java
   @Component
   public class TransactionalEventListener {
       @TransactionalEventListener(
           phase = TransactionPhase.AFTER_COMMIT,
           fallbackExecution = true
       )
       public void handleEvent(CustomEvent event) {
           // 事务提交后执行
       }
   }
   ```

## 实际应用示例

### Guava EventBus 完整示例

```java
// 自定义注解（可选）
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusListener {}

// 事件监听器
@EventBusListener
public class UserEventListener {
    @Subscribe
    public void handleUserCreated(UserCreatedEvent event) {
        System.out.println("User created: " + event.getUsername());
    }
}

// 配置类
@Configuration
public class EventBusConfiguration {
    private final EventBus eventBus;
    private final ApplicationContext applicationContext;
    
    public EventBusConfiguration(ApplicationContext applicationContext) {
        this.eventBus = new EventBus();
        this.applicationContext = applicationContext;
        registerListeners();
    }
    
    private void registerListeners() {
        applicationContext.getBeansWithAnnotation(EventBusListener.class)
                        .values()
                        .forEach(eventBus::register);
    }
    
    @Bean
    public EventBus eventBus() {
        return eventBus;
    }
}
```

### Spring Events 完整示例

```java
@Configuration
@EnableAsync
public class EventConfiguration {
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleAsyncEventMulticaster eventMulticaster = 
            new SimpleAsyncEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }
}

@Component
public class UserEventListener {
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        System.out.println("User created: " + event.getUsername());
    }
    
    @EventListener
    @Async
    public void handleUserCreatedAsync(UserCreatedEvent event) {
        System.out.println("Async: User created: " + event.getUsername());
    }
}
```

## 选择建议

1. **选择 Guava EventBus 的场景**
   - 需要在非 Spring 环境中使用事件机制
   - 项目规模较小，不需要复杂的事务支持
   - 需要更好的性能和更少的内存占用
   - 需要完全控制事件的注册和分发过程

2. **选择 Spring Events 的场景**
   - 已经在使用 Spring Framework
   - 需要与 Spring 事务管理集成
   - 需要异步事件处理和事务支持
   - 项目规模较大，需要企业级特性

## Spring Events 的事务处理支持

### 1. 事务事件监听器

Spring 提供了专门的 `@TransactionalEventListener` 注解，支持在不同的事务阶段处理事件：

```java
@Component
public class OrderEventHandler {
    
    // 在事务提交后处理
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedAfterCommit(OrderCreatedEvent event) {
        // 发送确认邮件等操作
    }
    
    // 在事务完成后处理（无论提交还是回滚）
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleOrderCreatedAfterCompletion(OrderCreatedEvent event) {
        // 清理临时资源等操作
    }
    
    // 在事务提交前处理
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreatedBeforeCommit(OrderCreatedEvent event) {
        // 预处理操作
    }
    
    // 在事务回滚后处理
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleOrderCreatedAfterRollback(OrderCreatedEvent event) {
        // 处理失败情况
    }
}
```

### 2. 事务事件监听器的高级特性

1. **fallbackExecution 支持**
```java
@Component
public class PaymentEventHandler {
    
    // 当没有事务时也会执行
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT,
        fallbackExecution = true
    )
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        // 处理支付成功事件
    }
}
```

2. **条件执行**
```java
@Component
public class OrderEventHandler {
    
    // 只处理特定条件的事件
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT,
        condition = "#event.amount > 1000"
    )
    public void handleLargeOrder(OrderCreatedEvent event) {
        // 处理大额订单
    }
}
```

### 3. 事务事件发布示例

```java
@Service
@Transactional
public class OrderService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder(Order order) {
        // 保存订单
        orderRepository.save(order);
        
        // 发布事件（会在事务提交后被处理）
        eventPublisher.publishEvent(new OrderCreatedEvent(this, order));
    }
}
```

### 4. 事务事件处理的最佳实践

1. **选择合适的事务阶段**
   - AFTER_COMMIT：适用于需要确保事务成功的后续操作
   - BEFORE_COMMIT：适用于需要在事务提交前进行验证的场景
   - AFTER_COMPLETION：适用于无论事务结果如何都需要执行的清理操作
   - AFTER_ROLLBACK：适用于需要记录或处理失败情况的场景

2. **错误处理**
```java
@Component
public class OrderEventHandler {
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            // 处理事件
            sendOrderConfirmation(event.getOrder());
        } catch (Exception e) {
            // 错误处理（记录日志、重试等）
            errorHandler.handle(e);
        }
    }
}
```

3. **异步处理**
```java
@Component
public class OrderEventHandler {
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCreatedAsync(OrderCreatedEvent event) {
        // 异步处理订单事件
        // 注意：异步方法不应该抛出检查异常
    }
}
```

4. **避免长时间运行的操作**
```java
@Component
public class OrderEventHandler {
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        // 不要在这里执行耗时操作
        // 而是将任务提交到专门的执行器
        taskExecutor.submit(() -> {
            processLongRunningTask(event);
        });
    }
}
```

这些事务支持特性使得 Spring Events 特别适合在需要事务保证的企业级应用中使用，例如：
- 订单处理系统
- 支付系统
- 库存管理系统
- 任何需要事务完整性的业务场景

通过合理使用这些特性，可以构建出可靠的、松耦合的事件驱动系统。

## Spring Events 的执行模式

### 1. 默认同步执行

默认情况下，Spring Events 是同步执行的，这意味着：

```java
@Service
public class OrderService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder(Order order) {
        // 1. 保存订单
        orderRepository.save(order);
        
        // 2. 发布事件 - 这里会同步执行所有监听器
        eventPublisher.publishEvent(new OrderCreatedEvent(this, order));
        
        // 3. 只有当所有监听器都执行完成后，才会执行这里
        logger.info("Order created");
    }
}

@Component
public class OrderEventListener {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // 如果这里抛出异常，会导致 OrderService.createOrder 方法失败
        throw new RuntimeException("处理失败");
    }
}
```

### 2. 异常处理策略

1. **在监听器中处理异常**
```java
@Component
public class OrderEventListener {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            processOrder(event.getOrder());
        } catch (Exception e) {
            logger.error("Error processing order", e);
        }
    }
}
```

2. **使用全局 ErrorHandler**
```java
@Configuration
public class EventConfig {
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = 
            new SimpleApplicationEventMulticaster();
        
        eventMulticaster.setErrorHandler(throwable -> {
            logger.error("Error in event listener", throwable);
        });
        
        return eventMulticaster;
    }
}
```

### 3. **异步事件的错误处理**

```java
@Configuration
@EnableAsync
public class AsyncEventConfig {  // 异步配置
    @Bean
    public AsyncEventMulticaster applicationEventMulticaster() {
        SimpleAsyncEventMulticaster eventMulticaster = 
            new SimpleAsyncEventMulticaster();
        
        // 配置异步执行器
        eventMulticaster.setTaskExecutor(taskExecutor());
        
        // 配置异步错误处理
        eventMulticaster.setErrorHandler(throwable -> {
            logger.error("Error in async event listener", throwable);
        });
        
        return eventMulticaster;
    }
    
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(25);
        executor.initialize();
        return executor;
    }
}
```

### 重要说明

1. **同步事件的错误处理**
   - `SimpleApplicationEventMulticaster` 用于处理同步事件
   - 设置的 ErrorHandler 会捕获所有同步监听器抛出的异常
   - 可以防止异常传播到事件发布者

2. **异步事件的错误处理**
   - `SimpleAsyncEventMulticaster` 用于处理异步事件
   - 需要配置专门的任务执行器
   - 异步错误处理器处理异步执行中的异常

3. **两种配置的区别**
   ```java
   // 同步事件配置
   @Configuration
   public class EventConfig {
       @Bean
       public ApplicationEventMulticaster applicationEventMulticaster() {
           return new SimpleApplicationEventMulticaster();  // 同步多播器
       }
   }
   
   // 异步事件配置
   @Configuration
   @EnableAsync
   public class AsyncEventConfig {
       @Bean
       public AsyncEventMulticaster applicationEventMulticaster() {
           return new SimpleAsyncEventMulticaster();  // 异步多播器
       }
   }
   ```

4. **使用建议**
   - 如果只使用同步事件，使用 `SimpleApplicationEventMulticaster`
   - 如果需要异步支持，使用 `SimpleAsyncEventMulticaster`
   - 不要同时配置两种多播器，会造成冲突

