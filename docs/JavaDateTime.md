# Java 时间类型处理（序列化、反序列化）

因为项目的需要，大多数日期-时间类型可能都有必要使用 `OffsetDateTime` 类型。

本文主要介绍 Java 中时间类型（特别是 OffsetDateTime）的序列化、反序列化方法，以及在 Jackson 中的处理方式。

## 一、原生 Java 时间类型处理

### 1. OffsetDateTime 标准格式转换

`OffsetDateTime` 默认使用 ISO-8601 格式进行字符串转换：

```java
// 转为 ISO-8601 格式字符串
OffsetDateTime dateTime = OffsetDateTime.now();
String isoString = dateTime.toString();  // 如：2024-03-14T15:30:00+08:00

// 从 ISO-8601 字符串解析
OffsetDateTime parsed = OffsetDateTime.parse("2024-03-14T15:30:00+08:00");
```

### 2. 自定义格式转换

使用 `DateTimeFormatter` 可以实现自定义格式的转换：

```java
// 使用 DateTimeFormatter
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX");

// 格式化
String formatted = dateTime.format(formatter);

// 解析
OffsetDateTime custom = OffsetDateTime.parse(formatted, formatter);

// 使用预定义的格式器
String basic = dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
String date = dateTime.format(DateTimeFormatter.ISO_DATE);
String time = dateTime.format(DateTimeFormatter.ISO_TIME);
```

### 3. 时区转换

可以在保持时间点不变的情况下转换时区偏移：

```java
// 转换到 UTC
OffsetDateTime utc = dateTime.withOffsetSameInstant(ZoneOffset.UTC);

// 转换到特定偏移
OffsetDateTime plus8 = dateTime.withOffsetSameInstant(ZoneOffset.ofHours(8));
```

### 4. 与其他时间类型转换

```java
// 转为 Instant
Instant instant = dateTime.toInstant();

// 转为 ZonedDateTime
ZonedDateTime zoned = dateTime.toZonedDateTime();

// 从 Instant 创建
OffsetDateTime fromInstant = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
```

## 二、Jackson 时间类型处理

### 1. 默认序列化行为

最新版本的 Jackson (2.x) 对常见 Java 时间类型的默认序列化结果：

```java
// 假设时区为 UTC+8，时间为 2024-03-14 15:30:00
{
    "localDate": "2024-03-14",                          // LocalDate
    "localTime": "15:30:00",                            // LocalTime
    "localDateTime": "2024-03-14T15:30:00",            // LocalDateTime
    "offsetDateTime": "2024-03-14T15:30:00+08:00",     // OffsetDateTime
    "zonedDateTime": "2024-03-14T15:30:00+08:00[Asia/Shanghai]", // ZonedDateTime
    "instant": "2024-03-14T07:30:00Z"                  // Instant (总是 UTC)
}
```

### 2. Jackson 配置

```java
ObjectMapper mapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())      // 1
    .setDateFormat(new StdDateFormat())        // 2
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // 3
```

#### 1. `registerModule(new JavaTimeModule())`
- **作用**：注册 Java 8 日期时间类型的序列化/反序列化处理器
- **必要性**：必须配置，否则无法正确处理 Java 8 时间类型
- **影响**：
  - 添加对 `LocalDate`, `LocalDateTime`, `Instant` 等类型的支持
  - 不配置会抛出异常：`InvalidDefinitionException: Java 8 date/time type not supported`

#### 2. `setDateFormat(new StdDateFormat())`
- **作用**：设置日期格式化的标准
- **默认行为**：如果不设置，默认也是 `StdDateFormat`
- **影响**：
  - 主要影响 `java.util.Date` 和 `java.util.Calendar` 的格式化
  - 对 Java 8 时间类型影响较小，因为它们有自己的序列化器

#### 3. `disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)`
- **作用**：禁用将日期写为时间戳的特性
- **默认行为**：默认是启用的（即默认将日期写为时间戳）
- **影响**：
  ```java
  // 启用时（默认）
  {
    "instant": 1647248400000,            // 时间戳形式
    "localDate": [2024, 3, 14],          // 数组形式
    "localDateTime": [2024, 3, 14, 15, 30, 0],
    "offsetDateTime": [2024, 3, 14, 15, 30, 0, 28800]
  }

  // 禁用后
  {
    "instant": "2024-03-14T07:30:00Z",
    "localDate": "2024-03-14",
    "localDateTime": "2024-03-14T15:30:00",
    "offsetDateTime": "2024-03-14T15:30:00+08:00"
  }
  ```

#### 其他常用配置选项

```java
ObjectMapper mapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    // 设置时区
    .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
    // 日期格式使用 ISO-8601
    .setDateFormat(new StdDateFormat().withColonInTimeZone(true))
    // 更多序列化特性配置
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
    // 更多反序列化特性配置
    .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
```

#### 使用建议
1. **基本配置**：至少需要注册 `JavaTimeModule`
2. **时间戳 vs ISO 格式**：
   - API 交互建议使用 ISO 格式（禁用 WRITE_DATES_AS_TIMESTAMPS）
   - 内部存储可以考虑使用时间戳格式
3. **时区处理**：
   - 如果需要统一时区处理，设置 ObjectMapper 的默认时区
   - 特定字段的时区处理优先使用 `@JsonFormat`


### 3. 使用注解自定义序列化

```java
public class TimeDTO {
    // 使用自定义格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime customFormat;

    // 指定时区
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime withTimezone;

    // 使用时间戳
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant asTimestamp;
}
```

## 三、最佳实践建议

### 1. API 交互
- 推荐使用 ISO-8601 格式（默认的 toString() 输出）
- 使用 `OffsetDateTime` 或 `Instant` 以保留时区信息
- 如果不需要时区信息，使用 `LocalDateTime`

### 2. 格式化
- 需要特定格式时，使用 DateTimeFormatter
- 在 Jackson 中优先使用 `@JsonFormat` 注解配置

### 3. 时区处理
- 存储时建议统一使用 UTC
- 显示时再转换为所需时区
- 注意 `Instant` 始终序列化为 UTC
- `OffsetDateTime` 保留原始时区偏移
- `ZonedDateTime` 保留完整时区信息

### 4. Jackson 相关
- 2.6.0 之前需要显式添加 `jackson-datatype-jsr310` 依赖
- 2.6.0 之后已包含在 `jackson-modules-java8` 中
- 建议显式配置 ObjectMapper 以确保行为一致性
