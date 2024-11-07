# Spring Boot 应用集成 ELK


## 1. 架构概览

```
Spring Boot App                      ELK Stack
+---------------+                    +---------------+
|               |                    |               |
| Application   |-- log files ------>| Filebeat      |
|   - logback   |      或            |     ↓         |
|   - log4j2    |--- TCP/UDP ------->| Logstash      |
|               |                    |     ↓         |
|               |                    | Elasticsearch |
|               |<------------------ |     ↓         |
|               |    查询/统计        | Kibana        |
+---------------+                    +---------------+
```


## 2. Spring Boot 应用配置

### 2.1 添加依赖

```xml
<!-- pom.xml -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

### 2.2 配置 Logback

```xml
<!-- logback-spring.xml -->
<configuration>
    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- Logstash 输出 -->
    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>localhost:5000</destination>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <!-- 自定义字段 -->
            <customFields>{"application":"your-app-name"}</customFields>
            
            <!-- JSON 编码配置 -->
            <includeMdcKeyName>true</includeMdcKeyName>
            <timestampPattern>yyyy-MM-dd'T'HH:mm:ss.SSS</timestampPattern>
            <includeRawMessage>false</includeRawMessage>
            <includeLevelValue>true</includeLevelValue>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="LOGSTASH" />
    </root>
</configuration>
```

## 3. 日志服务实现

### 3.1 结构化日志记录方式

#### 3.1.1 使用 StructuredArguments（推荐）

```java
import static net.logstash.logback.argument.StructuredArguments.entries;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
public class OperationLogService {
    
    public void logOperation(OperationType type, String operator, Object detail) {
        // StructuredArguments 会被 logstash-logback-encoder 识别并特殊处理
        log.info("Operation performed {}",
            entries(
                kv("operationType", type.name()),
                kv("operator", operator),
                kv("timestamp", LocalDateTime.now()),
                kv("detail", detail)  // detail 可以是任何类型的对象
            )
        );
    }
}
```

输出示例：
```json
{
    "message": "Operation performed",
    "operationType": "UPDATE",
    "operator": "admin",
    "timestamp": "2024-03-21T14:30:00.000",
    "detail": {
        "userId": "123",
        "changes": {
            "role": {
                "from": "USER",
                "to": "ADMIN"
            }
        }
    }
}
```

#### 3.1.2 使用 MDC 方式

```java
@Slf4j
@Service
public class OperationLogService {
    
    public void logOperation(OperationType type, String operator, Object detail) {
        try {
            // MDC 是线程级别的上下文存储
            MDC.put("operationType", type.name());
            MDC.put("operator", operator);
            MDC.put("timestamp", LocalDateTime.now().toString());
            MDC.put("detail", detail.toString());  // 注意：会调用 toString()
            
            log.info("Operation performed");
        } finally {
            MDC.clear();  // 必须清理 MDC
        }
    }
}
```

### 3.2 使用示例

#### 3.2.1 基本使用

```java
// 1. 简单对象
Map<String, String> detail = Map.of(
    "userId", "123",
    "action", "password_reset"
);
operationLogService.logOperation(OperationType.UPDATE, "admin", detail);

// 2. 嵌套对象
Map<String, Object> detail = new HashMap<>();
detail.put("userId", "123");
detail.put("changes", Map.of(
    "role", Map.of(
        "from", "USER",
        "to", "ADMIN"
    )
));
operationLogService.logOperation(OperationType.UPDATE, "admin", detail);

// 3. 动态 JSON
ObjectNode detail = JsonNodeFactory.instance.objectNode()
    .put("userId", "123")
    .put("timestamp", System.currentTimeMillis());
detail.putObject("changes")
    .put("oldRole", "USER")
    .put("newRole", "ADMIN");
operationLogService.logOperation(OperationType.UPDATE, "admin", detail);
```

#### 3.2.2 在服务中使用

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final OperationLogService operationLogService;
    
    public void updateUser(UserDTO userDTO) {
        User oldUser = userRepository.findById(userDTO.getId())
            .orElseThrow(() -> new UserNotFoundException(userDTO.getId()));
            
        // 记录变更
        Map<String, Object> detail = new HashMap<>();
        detail.put("userId", userDTO.getId());
        detail.put("changes", Map.of(
            "name", Map.of(
                "from", oldUser.getName(),
                "to", userDTO.getName()
            ),
            "role", Map.of(
                "from", oldUser.getRole(),
                "to", userDTO.getRole()
            )
        ));
        
        // 更新用户
        updateUserInDB(userDTO);
        
        // 记录操作日志
        operationLogService.logOperation(
            OperationType.UPDATE,
            SecurityContextHolder.getContext().getAuthentication().getName(),
            detail
        );
    }
}
```

## 4. ELK 配置

### 4.1 Logstash 配置

```conf
# logstash.conf
input {
  tcp {
    port => 5000
    codec => json_lines
  }
}

filter {
  # 时间戳处理
  date {
    match => [ "timestamp", "yyyy-MM-dd'T'HH:mm:ss.SSS" ]
    target => "@timestamp"
  }
  
  # 如果 detail 是字符串，尝试解析为 JSON
  if [detail] =~ ^\{ {
    json {
      source => "detail"
      target => "parsed_detail"
    }
  }
}

output {
  elasticsearch {
    hosts => ["localhost:9200"]
    index => "operation-logs-%{+YYYY.MM.dd}"
  }
  # 调试时可以启用标准输出
  # stdout { codec => rubydebug }
}
```

### 4.2 Elasticsearch 索引模板

```json
PUT _template/operation-logs
{
  "index_patterns": ["operation-logs-*"],
  "mappings": {
    "dynamic": true,  // 允许动态字段
    "properties": {
      "@timestamp": { "type": "date" },
      "operationType": { "type": "keyword" },
      "operator": { "type": "keyword" },
      "detail": {
        "type": "object",
        "dynamic": true  // detail 对象内部也允许动态字段
      }
    }
  }
}
```

### 4.3 索引生命周期管理

```json
PUT _ilm/policy/logs_policy
{
  "policy": {
    "phases": {
      "hot": {
        "actions": {
          "rollover": {
            "max_size": "50GB",
            "max_age": "7d"
          }
        }
      },
      "warm": {
        "min_age": "30d",
        "actions": {
          "forcemerge": {
            "max_num_segments": 1
          },
          "shrink": {
            "number_of_shards": 1
          }
        }
      },
      "delete": {
        "min_age": "90d",
        "actions": {
          "delete": {}
        }
      }
    }
  }
}
```

## 5. 日志查询

### 5.1 Java 查询客户端

```java
@Service
@RequiredArgsConstructor
public class LogQueryService {
    private final RestHighLevelClient elasticsearchClient;
    
    public List<LogEntry> queryLogs(LogQueryDTO queryDTO) {
        SearchRequest searchRequest = new SearchRequest("operation-logs-*");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        
        // 构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        // 1. 基本字段查询
        if (queryDTO.getOperationType() != null) {
            boolQuery.must(QueryBuilders.termQuery("operationType", queryDTO.getOperationType()));
        }
        if (queryDTO.getOperator() != null) {
            boolQuery.must(QueryBuilders.termQuery("operator", queryDTO.getOperator()));
        }
        
        // 2. 时间范围查询
        if (queryDTO.getStartTime() != null && queryDTO.getEndTime() != null) {
            boolQuery.must(QueryBuilders.rangeQuery("@timestamp")
                .from(queryDTO.getStartTime())
                .to(queryDTO.getEndTime()));
        }
        
        // 3. detail 内部字段查询
        if (queryDTO.getUserId() != null) {
            boolQuery.must(QueryBuilders.termQuery("detail.userId", queryDTO.getUserId()));
        }
        
        // 4. 嵌套字段查询
        if (queryDTO.getNewRole() != null) {
            boolQuery.must(QueryBuilders.termQuery("detail.changes.role.to", queryDTO.getNewRole()));
        }
        
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);
        
        try {
            SearchResponse response = elasticsearchClient.search(
                searchRequest, RequestOptions.DEFAULT);
            return convertToLogEntries(response);
        } catch (IOException e) {
            throw new RuntimeException("Failed to query logs", e);
        }
    }
    
    private List<LogEntry> convertToLogEntries(SearchResponse response) {
        return Arrays.stream(response.getHits().getHits())
            .map(hit -> {
                Map<String, Object> source = hit.getSourceAsMap();
                return LogEntry.builder()
                    .timestamp((String) source.get("@timestamp"))
                    .operationType((String) source.get("operationType"))
                    .operator((String) source.get("operator"))
                    .detail(source.get("detail"))
                    .build();
            })
            .collect(Collectors.toList());
    }
}
```

### 5.2 REST API 查询示例

```json
GET operation-logs-*/_search
{
  "query": {
    "bool": {
      "must": [
        { "term": { "operationType": "UPDATE" } },
        { "term": { "detail.userId": "123" } },
        { "term": { "detail.changes.role.to": "ADMIN" } },
        {
          "range": {
            "@timestamp": {
              "gte": "now-7d",
              "lte": "now"
            }
          }
        }
      ]
    }
  },
  "sort": [
    { "@timestamp": "desc" }
  ]
}
```

## 6. Docker 环境

```yaml
# docker-compose.yml
version: '3'
services:
  elasticsearch:
    image: elasticsearch:7.17.10
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
    volumes:
      - es_data:/usr/share/elasticsearch/data
      
  logstash:
    image: logstash:7.17.10
    volumes:
      - ./logstash.conf:/usr/share/logstash/pipeline/logstash.conf
    ports:
      - "5000:5000"
    depends_on:
      - elasticsearch
      
  kibana:
    image: kibana:7.17.10
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch

volumes:
  es_data:
```

## 7. 最佳实践

1. **日志记录**
   - 使用 StructuredArguments 而不是 MDC
   - 保持日志结构的一致性
   - 避免记录敏感信息
   - 合理使用日志级别

2. **性能优化**
   - 使用索引生命周期管理
   - 定期清理旧数据
   - 合理设置分片数
   - 监控索引大小

3. **运维建议**
   - 定期备份重要索引
   - 监控 ELK 集群状态
   - 设置告警机制
   - 做好容量规划

4. **安全建议**
   - 启用 X-Pack 安全功能
   - 配置访问控制
   - 加密传输层
   - 定期审计日志


