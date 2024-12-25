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

> 提示：分组接口本质上就是一个空的标记接口（Marker Interface）。

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


> Payload 的用途举例：
> 1. **错误级别分类**：区分验证失败的严重程度
> 2. **业务分类**：标记不同类型的业务验证
> 3. **自定义处理逻辑**：根据 Payload 类型执行不同的处理流程
> 4. **文档和元数据**：为验证约束提供额外的描述信息


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




## 附：正则表达式的反向否定预查的解析

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

