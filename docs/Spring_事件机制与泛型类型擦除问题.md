# Spring 事件机制与泛型类型擦除问题

## 问题描述

在使用 Spring 事件机制时，可能会遇到泛型类型的监听器无法接收到事件的问题。具体表现为：

```java
// 事件定义
public class OnChainStateRetrieved<T> {
    private final T state;
    // ...
}

// 监听器
@EventListener
public void handleOnChainStateRetrieved(OnChainStateRetrieved<FungibleAssetCoinPairState> event) {
    // 这个方法没有被调用
}
```

## 问题分析

### 1. Java 泛型类型擦除

Java 的泛型在运行时会被擦除，这意味着：
```java
OnChainStateRetrieved<FungibleAssetCoinPairState> event
// 在运行时实际变成
OnChainStateRetrieved event
```

### 2. Spring 事件类型匹配

Spring 事件机制需要在运行时匹配事件类型，但由于类型擦除，无法准确匹配泛型参数类型。这就是为什么使用通配符反而可以工作：

```java
@EventListener
public void handleOnChainStateRetrieved(OnChainStateRetrieved<?> event) {
    // 这个方法可以被调用
}
```

## 解决方案

### 1. 实现 ResolvableTypeProvider

通过实现 `ResolvableTypeProvider` 接口，我们可以在运行时提供准确的类型信息。有几种实现方式：

#### 方案一：显式指定类型（推荐）
```java
public class OnChainStateRetrieved<T> implements ResolvableTypeProvider {
    private final T state;
    private final Class<T> stateType;  // 明确指定要使用的类型

    public OnChainStateRetrieved(T state, Class<T> stateType) {
        this.state = state;
        this.stateType = stateType;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
            getClass(),
            ResolvableType.forClass(stateType)
        );
    }
}

// 使用方式
applicationEventPublisher.publishEvent(
    new OnChainStateRetrieved<>(state, FungibleAssetCoinPairState.class)
);
```

优点：
- 类型安全
- 明确的类型意图
- 避免运行时的类型推断错误

缺点：
- 需要额外传递类型参数

#### 方案二：自动推断接口（不推荐）
```java
@Override
public ResolvableType getResolvableType() {
    Class<?>[] interfaces = state.getClass().getInterfaces();
    Class<?> typeToUse = interfaces.length > 0 ? interfaces[0] : state.getClass();
    return ResolvableType.forClassWithGenerics(
        getClass(),
        ResolvableType.forClass(typeToUse)
    );
}
```

这种方式存在以下问题：
1. 如果类实现了多个接口，取第一个接口可能不是我们想要的
2. 接口的顺序是不确定的，依赖它是不安全的
3. 可能导致意外的类型匹配

#### 方案三：使用具体的事件类型
```java
// 为特定类型创建专门的事件类
public class FungibleAssetCoinPairStateRetrievedEvent {
    private final FungibleAssetCoinPairState state;

    public FungibleAssetCoinPairStateRetrievedEvent(FungibleAssetCoinPairState state) {
        this.state = state;
    }

    public FungibleAssetCoinPairState getState() {
        return state;
    }
}

// 监听器
@EventListener
public void handleStateRetrieved(FungibleAssetCoinPairStateRetrievedEvent event) {
    // 处理事件
}
```

优点：
- 类型安全
- 代码清晰
- 避免泛型带来的复杂性
- 符合单一职责原则

### 2. 选择建议

1. 如果事件类型是固定的，优先使用方案三（具体的事件类型）
2. 如果确实需要泛型，使用方案一（显式指定类型）
3. 避免使用方案二（自动推断接口），因为它可能导致不可预期的行为

### 3. 注意事项

1. 在使用泛型事件时：
   - 明确指定类型参数
   - 避免依赖运行时类型推断
   - 保持类型安全

2. 事件设计原则：
   - 优先考虑具体的事件类型
   - 让事件类表达明确的业务含义
   - 避免过度抽象

## 可能会遇到的其他问题

### 1. 在开发环境中运行时类找不到

遇到错误：
```
java.lang.Error: Unresolved compilation problem: 
    OnChainStateRetrieved cannot be resolved to a type
```

原因：
- Spring AOP 代理在运行时找不到类
- 编译缓存不一致

解决方案：
1. 确保类的可见性（public）
2. 正确的包路径
3. clean 后重新编译

### 2. 内部类和接口的类型问题

当事件携带的状态是内部类实现时：
```java
// 实际类型
class AbstractFungibleAssetCoinPairState$SimpleFungibleAssetCoinPairState
// 期望类型
interface FungibleAssetCoinPairState
```

解决方案是在 `getResolvableType()` 中使用接口类型而不是具体实现类型。

## 最佳实践

1. 事件类设计：
   - 使用具体的事件类而不是泛型（如果可能）
   - 如果必须使用泛型，实现 `ResolvableTypeProvider`
   - 将事件类放在专门的包中（如 `.events` 或 `.event`）

2. 监听器设计：
   - 明确指定要监听的类型
   - 避免使用通配符（除非确实需要）

3. 开发流程：
   - 修改类结构后执行 clean
   - 确保正确的包导入
   - 保持良好的类型设计

## 参考资料

- [Spring Events Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-events)
- [Java Generics Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
- [Spring ResolvableType](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/ResolvableType.html)

