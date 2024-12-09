# Spring MVC 文件上传

## 1. 基本文件上传

Spring MVC 支持同时接收普通表单字段和文件上传。基本控制器示例：

```java
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    @PostMapping("/import-csv")
    public void importFromCsv(
        @RequestParam String x1, 
        @RequestParam String x2, 
        @RequestParam("file") MultipartFile file
    ) {
        // 处理文件
    }
}
```

测试命令：
```bash
curl -X POST http://localhost:8080/BffRawItems/import-csv \
  -F "x1=value1" \
  -F "x2=value2" \
  -F "file=@/path/to/your/file.csv"
```

## 2. 自定义参数解析

### 2.1 创建自定义解析器

通过实现 `HandlerMethodArgumentResolver` 接口，可以直接将上传的 CSV 文件转换为 `CSVParser` 对象：

```java
import java.io.InputStreamReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class CsvParserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CSVParser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) webRequest.getNativeRequest();
        
        // 从 @RequestParam 注解中获取参数名
        RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
        String paramName = requestParam.value().isEmpty() ? parameter.getParameterName() : requestParam.value();
        
        MultipartFile file = multipartRequest.getFile(paramName);
        
        if (file == null) {
            throw new IllegalArgumentException("No file uploaded for parameter: " + paramName);
        }

        return new CSVParser(
            new InputStreamReader(file.getInputStream()), 
            CSVFormat.DEFAULT.withHeader().withTrim()
        );
    }
}
```

### 2.2 注册解析器

```java
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CsvParserArgumentResolver());
    }
}
```

### 2.3 使用自定义解析器

```java
import java.io.IOException;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsvImportController {
    @PostMapping("/import-csv")
    public void importFromCsv(
        @RequestParam String x1, 
        @RequestParam String x2, 
        @RequestParam("file") CSVParser parser
    ) {
        try (parser) {  // 使用 try-with-resources 自动关闭
            for (CSVRecord record : parser) {
                // 处理每一行数据
                String column1 = record.get("header1");
                String column2 = record.get("header2");
                // ...
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }
}
```

## 3. 重要注意事项

1. **资源释放**：务必确保 CSVParser 被正确关闭，推荐使用 try-with-resources 语句。

2. **参数名称**：使用 `@RequestParam` 注解的 value 属性指定的参数名，避免硬编码。

3. **错误处理**：添加适当的错误处理逻辑，包括：
   - 文件不存在
   - 文件格式错误
   - 解析错误

4. **文件编码**：注意指定正确的字符编码，通常使用 UTF-8：
```java
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;

new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
```

## 4. 测试

使用 curl 进行测试时，可以添加 `-v` 参数查看详细信息：

```bash
curl -v -X POST http://localhost:8080/BffRawItems/import-csv \
  -F "x1=value1" \
  -F "x2=value2" \
  -F "file=@/path/to/your/file.csv"
```

如果接口需要认证，添加认证头：

```bash
curl -X POST http://localhost:8080/BffRawItems/import-csv \
  -H "Authorization: Bearer your-token-here" \
  -F "x1=value1" \
  -F "x2=value2" \
  -F "file=@/path/to/your/file.csv"
```

