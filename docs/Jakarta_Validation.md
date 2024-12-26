# Jakarta Validation

## 1. 基本概念

### 1.1 验证注解和验证器

一个完整的验证约束包含两部分：验证注解和验证器实现。

```java
// 定义验证约束
@Target({ElementType.FIELD})           // 注解可以用在字段上
@Retention(RetentionPolicy.RUNTIME)    // 运行时保留注解
@Constraint(validatedBy = MyValidator.class)  // 指定验证器实现
public @interface MyConstraint {
    String message() default "...";    // 默认错误消息
    Class<?>[] groups() default {};    // 分组验证
    Class<? extends Payload>[] payload() default {};  // 额外信息载体
}

// 验证器实现
public class MyValidator implements ConstraintValidator<MyConstraint, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 验证逻辑
        return true;
    }
}
```

## 2. 内置验证注解

Jakarta Validation 提供了许多常用的内置验证注解：

### 2.1 空值相关
```java
@NotNull              // 不能为 null
@NotEmpty            // 不能为 null 且不能为空（字符串长度>0，集合大小>0）
@NotBlank            // 不能为 null 且去除前后空格后长度>0
```

### 2.2 数值相关
```java
@Min(0)              // 最小值
@Max(100)            // 最大值
@Positive           // 必须为正数 (>0)
@PositiveOrZero    // 必须为非负数 (>=0)
@Negative          // 必须为负数 (<0)
@NegativeOrZero   // 必须为非正数 (<=0)
@DecimalMin("0.1") // 最小值（字符串形式，可以是小数）
@DecimalMax("9.9") // 最大值（字符串形式，可以是小数）
```

### 2.3 字符串相关
```java
@Size(min = 2, max = 30)  // 字符串长度范围
@Pattern(regexp = "^[A-Z0-9]+$")  // 正则表达式匹配
@Email                    // 邮箱格式
```

### 2.4 时间相关
```java
@Past               // 必须是过去的日期
@PastOrPresent     // 必须是过去或现在的日期
@Future            // 必须是将来的日期
@FutureOrPresent  // 必须是将来或现在的日期
```

## 3. 分组验证

分组验证允许根据不同场景使用不同的验证规则。
分组接口本质上是一个空的标记接口（Marker Interface）。

### 3.1 定义分组
```java
// 分组接口（标记接口）
public interface Create {}       // 标记创建场景
public interface Update {}       // 标记更新场景
public interface Delete {}       // 标记删除场景
```

### 3.2 使用分组
```java
public class BffLotDto {
    @NotNull(groups = Update.class)  // 只在更新时需要 ID
    private String lotId;

    @NotNull(groups = {Create.class, Update.class})  // 创建和更新时都需要
    @Gs1Batch(groups = {Create.class, Update.class})
    private String gs1Batch;
}

@RestController
public class BffLotController {
    @PostMapping
    public String createLot(@Validated(Create.class) @RequestBody BffLotDto lot) {
        // 只验证 Create 组的约束
    }

    @PutMapping
    public void updateLot(@Validated(Update.class) @RequestBody BffLotDto lot) {
        // 只验证 Update 组的约束
    }
}
```

## 4. Payload 机制

Payload 在 Jakarta Validation 中本质上是一个标记接口（Marker Interface）体系：

```java
// Jakarta Validation 中的基础 Payload 接口定义
package jakarta.validation.payload;

public interface Payload {
    // 空接口，仅用作标记
}
```

Payload 机制的特点：
1. **类型安全**：只能使用 Payload 的子类型
2. **轻量级**：不需要实例化，只使用类引用
3. **可扩展**：可以根据需要定义新的 Payload 类型
4. **编译时检查**：错误的 Payload 类型会在编译时被发现

在验证注解中，payload 属性总是以 Class 数组的形式定义：
```java
@NotNull(
    message = "Field cannot be null",
    payload = {Severity.Error.class, BusinessValidation.Critical.class}
)
private String field;
```


Payload 的用途举例：
1. **错误级别分类**：区分验证失败的严重程度
2. **业务分类**：标记不同类型的业务验证
3. **自定义处理逻辑**：根据 Payload 类型执行不同的处理流程
4. **文档和元数据**：为验证约束提供额外的描述信息


### 4.1 定义 Payload
```java
// 定义不同严重级别的 Payload
public class Severity {
    public interface Info extends Payload {}     // 信息级别
    public interface Warning extends Payload {}  // 警告级别
    public interface Error extends Payload {}    // 错误级别
}

// 定义业务相关的 Payload
public class ValidationGroup {
    public interface Critical extends Payload {}    // 关键验证
    public interface Performance extends Payload {} // 性能相关
    public interface Security extends Payload {}    // 安全相关
}
```

### 4.2 使用 Payload
```java
public class BffLotDto {
    @NotNull(
        message = "GS1 batch is required",
        payload = {Severity.Error.class, ValidationGroup.Critical.class}
    )
    @Gs1Batch(
        message = "Invalid GS1 batch format",
        payload = {Severity.Warning.class}
    )
    private String gs1Batch;
}
```

### 4.3 处理 Payload
```java
@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            Set<Class<? extends Payload>> payloads = violation.getConstraintDescriptor()
                                                            .getPayload();
            
            if (payloads.contains(Severity.Error.class)) {
                return ResponseEntity.status(400).body("Error: " + violation.getMessage());
            } else if (payloads.contains(Severity.Warning.class)) {
                return ResponseEntity.status(200).body("Warning: " + violation.getMessage());
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
```


## 5. 实际应用示例

### 5.1 完整的验证示例
```java
public class BffLotDto {
    @NotNull(message = "Lot ID cannot be null")
    @Size(min = 5, max = 50)
    private String lotId;

    @NotNull
    @Gs1Batch  // 自定义验证注解
    private String gs1Batch;

    @PositiveOrZero
    private BigDecimal quantity;

    @FutureOrPresent
    private OffsetDateTime expirationDate;
}
```

## 6. 最佳实践

1. 使用合适的内置验证注解
2. 为复杂的验证逻辑创建自定义注解
3. 使用分组验证处理不同场景
4. 提供清晰的错误消息
5. 避免在验证器中包含业务逻辑
6. 合理使用 Payload 增强验证的可维护性
7. 保持验证逻辑的单一职责
8. 适当使用验证组合




## 7. @Valid vs 直接验证注解

### 7.1 @Valid 注解的使用场景

对于复杂对象（特别是 @RequestBody），必须使用 @Valid 注解才能触发验证：

```java
@PostMapping
public String createLot(
    @Valid @RequestBody BffLotDto lot  // @Valid 是必需的
) {
    // ...
}
```

### 7.2 简单参数的验证

对于简单参数（如 @RequestParam、@PathVariable、@ModelAttribute），验证注解可以直接使用，不需要 @Valid：

```java
@GetMapping
public Page<BffLotDto> getLots(
    @Min(0) @RequestParam(value = "page", defaultValue = "0") Integer page,
    @Min(1) @Max(100) @RequestParam(value = "size", defaultValue = "20") Integer size
) {
    // ...
}

@GetMapping("{lotId}")
public BffLotDto getLot(
    @NotBlank @Size(max=50) @PathVariable("lotId") String lotId
) {
    // ...
}

@PostMapping("/form")
public void handleForm(
    @NotNull @ModelAttribute FormData formData  // 对于 @ModelAttribute，也不需要 @Valid
) {
    // ...
}
```

### 7.3 区别说明

- @Valid：用于触发复杂对象的递归验证
- 直接验证注解：用于简单参数的验证，由 Spring MVC 的参数解析器直接处理




## 8. 接口方法的验证约束

验证注解不仅可以用在类的字段上，还可以用在接口的 getter 方法上。这种方式在定义 DTO 接口时特别有用。

### 8.1 验证注解的 @Target 设置

当验证注解需要支持方法级别的验证时，需要在 `@Target` 中包含 `ElementType.METHOD`：

```java
@Target({ElementType.FIELD, ElementType.METHOD})  // 同时支持字段和方法
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Gs1BatchValidator.class)
@Documented
public @interface Gs1Batch {
    String message() default "Invalid GS1 batch format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

需要注意的是：
- `ElementType.METHOD` 使注解可以用在任何方法上
- 但在 Bean Validation 中，主要用于以下场景：
  1. 验证接口或类中的 getter 方法返回值
  2. 验证方法的参数（通过 `@Valid` 或其他约束注解）
  3. 验证方法的执行结果（通过 `@ValidateOnExecution`）
- 虽然技术上可以放在任何方法上，但验证框架主要关注这些特定场景
- 这样的设计遵循了 Bean Validation 规范，使验证行为更可预测

### 8.2 在接口上声明验证约束

```java
public interface BffLotInfo {
    @NotNull
    @Size(min = 5, max = 50)
    String getLotId();
    
    @NotNull
    @Gs1Batch
    String getGs1Batch();
}
```

### 8.3 验证的触发方式

1. 通过 @Valid 注解验证实现类：
```java
@PostMapping
public void process(@Valid @RequestBody BffLotInfoImpl lotInfo) {
    // 验证会生效
}
```

2. 直接验证接口类型：
```java
@PostMapping
public void process(@Valid @RequestBody BffLotInfo lotInfo) {
    // 验证会生效
}
```

3. 使用 Validator 手动验证：
```java
@Autowired
private Validator validator;

public void validateLot(BffLotInfo lotInfo) {
    Set<ConstraintViolation<BffLotInfo>> violations = validator.validate(lotInfo);
    if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
    }
}
```

### 8.4 优点

1. 可以在接口层面定义验证规则
2. 实现类自动继承这些验证约束
3. 支持多个实现类共享相同的验证规则
4. 有助于代码复用和维护

### 8.5 方法级验证的注意事项

#### 8.5.1 验证注解的继承行为

当接口中的方法被实现类重写时，验证注解会被继承：

```java
public interface BffLotInfo {
    @NotNull
    String getLotId();
}

public class BffLotInfoImpl implements BffLotInfo {
    @Override  // 这里会继承 @NotNull 约束
    public String getLotId() {
        return lotId;
    }
}
```

#### 8.5.2 常见陷阱

1. 重写方法时的验证约束：
```java
public interface BffLotInfo {
    @Size(max = 50)
    String getLotId();
}

public class BffLotInfoImpl implements BffLotInfo {
    @Size(max = 100)  // 错误：不应该在重写方法上重新定义约束
    @Override
    public String getLotId() {
        return lotId;
    }
}
```

2. 私有方法的验证：
```java
public class BffLotService {
    @NotNull  // 无效：私有方法上的验证注解不会被处理
    private String validateLotId(String lotId) {
        return lotId;
    }
}
```

#### 8.5.3 最佳实践

1. 保持验证约束在接口层面的一致性：
   - 在接口中定义基本的验证约束
   - 避免在实现类中重新定义或覆盖这些约束
   - 如需添加额外验证，考虑使用组合而不是继承

2. 明确验证的作用范围：
   - 接口方法验证主要用于契约定义
   - 实现类的字段验证用于内部状态校验
   - 避免在同一属性的不同层面重复定义相同的验证规则

3. 合理使用方法级验证：
```java
@Validated
@Service
public class BffLotServiceImpl implements BffLotService {
    @Autowired
    private Validator validator;

    // 基本验证
    @Override
    public BffLotInfo getLot(@NotBlank String lotId) {
        // ...
    }

    // 扩展验证
    public BffLotInfo getLotWithBusinessValidation(String lotId) {
        // 1. 基本验证
        if (lotId == null || lotId.trim().isEmpty()) {
            throw new ValidationException("Lot ID cannot be blank");
        }
        
        // 2. 获取数据
        BffLotInfo lot = getLot(lotId);
        
        // 3. 额外的业务验证
        validateBusinessRules(lot);
        
        return lot;
    }
}
```

4. 文档化验证规则：
   - 在接口文档中清晰说明验证约束
   - 使用明确的错误消息
   - 考虑使用自定义验证组进行场景区分


## 附一：正则表达式的反向否定预查的解析

### 需求
找出文本中前面没有双引号的 "StatusId" 字符串。

### 解决方案
使用带有反向否定预查的正则表达式：
```
(?<!")StatusId
```

### 表达式解析

#### 1. 基本结构 `(?< )`
- 这是反向预查（lookbehind）的基本语法结构
- 用于检查当前位置之前的内容
- 对应的正向预查（lookahead）语法是 `(?= )`，用于检查当前位置之后的内容

#### 2. 否定符号 `!`
- 放在预查结构中表示"不匹配"
- 如果没有 `!` 则是肯定预查，表示"必须匹配"

#### 3. 匹配字符 `"`
- 这里是要检查的具体字符（双引号）

#### 常见预查类型
- `(?=...)` - 正向肯定预查
- `(?!...)` - 正向否定预查
- `(?<=...)` - 反向肯定预查
- `(?<!...)` - 反向否定预查

#### 示例
```
text"StatusId    // 不匹配，因为 StatusId 前面有双引号
textStatusId     // 匹配，因为 StatusId 前面没有双引号
```

### 在 VS Code 中使用

在 VS Code 的搜索框中使用此正则表达式时，需要勾选"使用正则表达式"选项（通常显示为 `.*` 图标）。


## 附二：Java 泛型中的通配符

在 Java 泛型中，问号 `?` 表示通配符（Wildcard），是泛型编程中的一个重要概念。

### 1. 无界通配符
```java
List<?> list;  // 可以接受任何类型的List
```
- 表示可以是任何类型
- 常用于只读操作
- 不能向其中添加元素（除了 null）

### 2. 上界通配符
```java
List<? extends Number> numbers;  // 可以接受 Number 或其子类的List
```
- 使用 `extends` 关键字
- 表示类型必须是指定类或其子类
- 常用于从集合中读取元素

### 3. 下界通配符
```java
List<? super Integer> integers;  // 可以接受 Integer 或其父类的List
```
- 使用 `super` 关键字
- 表示类型必须是指定类或其父类
- 常用于向集合中写入元素

### 4. PECS 原则
Producer Extends, Consumer Super 原则的实际应用：

```java
// 生产者 - 使用 extends（读取）
public void readNumbers(List<? extends Number> numbers) {
    Number n = numbers.get(0);  // 安全，因为一定是 Number 或其子类
    // numbers.add(1);  // 编译错误，不能添加元素
}

// 消费者 - 使用 super（写入）
public void addIntegers(List<? super Integer> integers) {
    integers.add(1);  // 安全，因为容器一定可以接受 Integer
    // Integer n = integers.get(0);  // 编译错误，不能保证类型
}
```

这个原则帮助我们：
- 使用 `extends` 来从泛型中读取数据
- 使用 `super` 来向泛型中写入数据
- 提高代码的类型安全性
- 使泛型代码更加灵活

