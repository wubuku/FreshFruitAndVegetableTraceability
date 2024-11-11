# Java 编译时模板引擎对比分析

本文主要探讨 Java 生态中类似 .NET T4 的模板引擎解决方案。重点关注能够在编译时生成代码，并具有高性能运行时特性的工具。

## 主要选项

### 1. JavaPoet

主要用于生成 Java 源代码的 API：

```java
MethodSpec main = MethodSpec.methodBuilder("main")
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .returns(void.class)
    .addParameter(String[].class, "args")
    .addStatement("$T.out.println($S)", System.class, "Hello, World!")
    .build();

TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    .addMethod(main)
    .build();
```

### 2. StringTemplate

支持预编译为 Java 类的模板引擎：

```java
@CompiledST
public class MyTemplate extends StringTemplate {
    public static void main(String[] args) {
        ST hello = new ST("Hello, <name>");
        hello.add("name", "World");
        System.out.println(hello.render());
    }
}
```

### 3. Apache Velocity

可以通过 Maven 插件在编译时生成代码：

```java
public class CompiledTemplate extends VelocityTemplate {
    public String merge(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello, ").append(context.get("name"));
        return sb.toString();
    }
}
```

## JTE (Java Template Engine) 详细分析

JTE 是最接近 T4 特性的解决方案。

### 基本特性

1. **编译时转换**
```java
// 模板文件 example.jte
@param String name
<div>Hello ${name}!</div>

// 编译后生成的Java类
public final class ExampleTemplate {
    public static void render(String name, TemplateOutput output) {
        output.writeContent("<div>Hello ");
        output.writeContent(name);
        output.writeContent("!</div>");
    }
}
```

2. **类型安全**
```java
// 编译时类型检查
@param User user  // 如果User类不存在，编译失败
${user.name}     // 如果name属性不存在，编译失败
```

### 构建集成

Maven 配置示例：

```xml
<plugin>
    <groupId>gg.jte</groupId>
    <artifactId>jte-maven-plugin</artifactId>
    <version>${jte.version}</version>
    <configuration>
        <sourceDirectory>${project.basedir}/src/main/jte</sourceDirectory>
        <targetDirectory>${project.build.directory}/generated-sources/jte</targetDirectory>
        <contentType>Html</contentType>
    </configuration>
</plugin>
```

### 社区情况

1. **基本信息**
- GitHub Stars：约 1.3k（截至2024年3月）
- 首次发布：2020年
- 最新版本：3.1.9（2024年2月）

2. **活跃度**
- 主要维护者：casid (Andreas Hager)
- 贡献者：约20人
- 更新频率：平均每1-2个月

3. **对比其他模板引擎**
- Thymeleaf：约 4.7k stars
- FreeMarker：约 5.8k stars
- Velocity：Apache 项目，使用广泛

## 与 T4 的对比

### 优势
1. 编译时生成 Java 代码
2. 运行时高性能（直接使用 StringBuilder）
3. 类型安全
4. 支持热重载（开发模式）

### 劣势
1. 社区规模相对较小
2. 功能范围主要针对 HTML/文本生成
3. 可能缺少一些 T4 的高级特性
4. 工具支持不如 T4 完善

## 结论

虽然 Java 生态中没有完全对应 T4 的解决方案，但 JTE 提供了类似的核心功能：
1. 编译时代码生成
2. 运行时高性能
3. 类型安全

对于需要在 Java 项目中实现类似 T4 功能的场景，JTE 是一个值得考虑的选择，特别是当主要需求是高性能的模板渲染时。但需要注意其社区规模相对较小的问题。


---

# Compile-time vs. Runtime Template Engines: A Java Perspective

## 1. Compile-time Template Engines（编译时模板引擎）

在编译时将模板转换为可执行代码的模板引擎。

示例代码：

```java
// 1. 在编译时，模板被转换为 Java 代码
@CompileTime
public class CompiledTemplate {
    public static String render(String name) {
        return "Hello " + name;  // 直接的字符串拼接
    }
}

// 2. 运行时直接执行生成的代码
String result = CompiledTemplate.render("World");
```

## 2. Runtime Template Engines（运行时模板引擎）

在运行时解释和执行模板的引擎。

示例代码：

```java
// 1. 在运行时，需要先加载和解析模板
Template template = engine.loadTemplate("hello.tpl");  // 模板: Hello ${name}

// 2. 然后解释执行模板
Map<String, Object> context = new HashMap<>();
context.put("name", "World");
String result = template.render(context);  // 运行时解析和替换变量
```

## 主要区别

### 1. 处理时机
- **Compile-time**：编译期处理，生成代码
- **Runtime**：运行期处理，解释执行

### 2. 性能特点
- **Compile-time**：更快（无需运行时解析）
- **Runtime**：较慢（需要运行时解析）

### 3. 灵活性
- **Compile-time**：较低（模板修改需要重新编译）
- **Runtime**：较高（可以动态修改模板）

### 4. 典型代表
- **Compile-time**：JTE、T4
- **Runtime**：Freemarker、Velocity、Thymeleaf

### 5. 使用场景
- **Compile-time**：代码生成、高性能要求场景
- **Runtime**：网页模板、动态内容生成

## 选择建议

1. 如果需要**高性能**且模板相对稳定，选择 Compile-time Template Engines
2. 如果需要**动态修改**模板或更高的灵活性，选择 Runtime Template Engines
3. 在**微服务**或**高并发**场景下，Compile-time Template Engines 可能是更好的选择
4. 对于**内容管理系统**或需要**频繁修改模板**的场景，Runtime Template Engines 更合适


---

# Compile-time Template Engines in Java: A Comparative Analysis

This article explores template engine solutions in the Java ecosystem that are similar to .NET's T4, focusing on tools that can generate code at compile-time and offer high-performance runtime characteristics.

## Main Options

### 1. JavaPoet

An API primarily used for Java source code generation:

```java
MethodSpec main = MethodSpec.methodBuilder("main")
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .returns(void.class)
    .addParameter(String[].class, "args")
    .addStatement("$T.out.println($S)", System.class, "Hello, World!")
    .build();

TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    .addMethod(main)
    .build();
```

### 2. StringTemplate

A template engine that supports precompilation to Java classes:

```java
@CompiledST
public class MyTemplate extends StringTemplate {
    public static void main(String[] args) {
        ST hello = new ST("Hello, <name>");
        hello.add("name", "World");
        System.out.println(hello.render());
    }
}
```

### 3. Apache Velocity

Supports code generation at compile-time through Maven plugins:

```java
public class CompiledTemplate extends VelocityTemplate {
    public String merge(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello, ").append(context.get("name"));
        return sb.toString();
    }
}
```

## JTE (Java Template Engine) Detailed Analysis

JTE is the solution that most closely matches T4's features.

### Core Features

1. **Compile-time Transformation**
```java
// Template file example.jte
@param String name
<div>Hello ${name}!</div>

// Generated Java class after compilation
public final class ExampleTemplate {
    public static void render(String name, TemplateOutput output) {
        output.writeContent("<div>Hello ");
        output.writeContent(name);
        output.writeContent("!</div>");
    }
}
```

2. **Type Safety**
```java
// Compile-time type checking
@param User user  // Compilation fails if User class doesn't exist
${user.name}     // Compilation fails if name property doesn't exist
```

### Build Integration

Maven configuration example:

```xml
<plugin>
    <groupId>gg.jte</groupId>
    <artifactId>jte-maven-plugin</artifactId>
    <version>${jte.version}</version>
    <configuration>
        <sourceDirectory>${project.basedir}/src/main/jte</sourceDirectory>
        <targetDirectory>${project.build.directory}/generated-sources/jte</targetDirectory>
        <contentType>Html</contentType>
    </configuration>
</plugin>
```

### Community Status

1. **Basic Information**
- GitHub Stars: ~1.3k (as of March 2024)
- Initial Release: 2020
- Latest Version: 3.1.9 (February 2024)

2. **Activity**
- Main Maintainer: casid (Andreas Hager)
- Contributors: ~20 people
- Update Frequency: Every 1-2 months on average

3. **Comparison with Other Template Engines**
- Thymeleaf: ~4.7k stars
- FreeMarker: ~5.8k stars
- Velocity: Apache project, widely used

## Comparison with T4

### Advantages
1. Compile-time Java code generation
2. High runtime performance (direct StringBuilder usage)
3. Type safety
4. Hot reload support (development mode)

### Disadvantages
1. Relatively small community
2. Feature set primarily focused on HTML/text generation
3. May lack some of T4's advanced features
4. Less comprehensive tooling support compared to T4

## Conclusion

While the Java ecosystem doesn't have a direct equivalent to T4, JTE provides similar core functionality:
1. Compile-time code generation
2. High runtime performance
3. Type safety

For scenarios requiring T4-like functionality in Java projects, JTE is a viable option, especially when high-performance template rendering is a primary requirement. However, its relatively small community size should be taken into consideration.


