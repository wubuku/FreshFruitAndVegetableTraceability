# Spring Boot 文件管理服务实现教程

## 前言

本教程将指导你在 Spring Boot 项目中实现一个灵活的文件管理服务。这个服务最初使用 MinIO 作为存储方案，同时预留了切换到 AWS S3 或 Google Cloud Storage (GCS) 的扩展能力。

适合人群：
- 了解 Spring Boot 基础知识
- 有一定的 Java 开发经验
- 想要实现文件上传下载功能的开发者

## 第一部分：开发环境准备

### 1. 基础环境要求
- JDK 8 或更高版本
- Maven 3.6+ 或 Gradle 7.0+
- IDE（推荐使用 IntelliJ IDEA）
- Docker（用于运行 MinIO）
- Postman（用于测试 API）

### 2. 项目创建

1. 使用 Spring Initializr (https://start.spring.io/) 创建项目：
   - Project: Maven
   - Language: Java
   - Spring Boot: 2.7.x
   - Project Metadata:
     - Group: com.example
     - Artifact: file-service
     - Name: file-service
     - Description: File Management Service
     - Package name: com.example.fileservice
   - Dependencies:
     - Spring Web
     - Spring Security
     - Spring Data JPA
     - Lombok
     - H2 Database

2. 下载并解压项目，使用 IDE 打开

### 3. MinIO 环境搭建

1. 拉取 MinIO Docker 镜像：
```bash
docker pull minio/minio
```

2. 创建 MinIO 数据目录：
```bash
mkdir -p ~/minio/data
```

3. 启动 MinIO 服务：
```bash
docker run -d \
  --name minio \
  -p 9002:9000 \
  -p 9001:9001 \
  -e "MINIO_ROOT_USER=admin" \
  -e "MINIO_ROOT_PASSWORD=password123" \
  -v ~/minio/data:/data \
  minio/minio server /data --console-address ":9001"
```

删除容器 minio 
```bash
docker rm -f minio
```

4. 验证 MinIO 是否正常运行：
   - 访问 http://localhost:9001
   - 使用以下凭据登录：
     - Username: admin
     - Password: password123

5. 配置 Bucket 和访问策略：
    1. 登录 MinIO Console (http://localhost:9001)
    2. 点击左侧菜单的 "Buckets"
    3. 点击 "Create Bucket"，创建名为 "my-bucket" 的 bucket
    4. 在 Buckets 列表中点击 "my-bucket"
    5. 点击 "Access Policy" 的编辑图标
    6. 在 "Add Policy" 下拉列表中选择 "Custom"
    7. 在编辑器中输入以下策略：
       ```json
       {
         "Version": "2012-10-17",
         "Statement": [
           {
             "Effect": "Allow",
             "Principal": {
               "AWS": ["*"]
             },
             "Action": ["s3:GetObject"],
             "Resource": ["arn:aws:s3:::my-bucket/public/*"]
           }
         ]
       }
       ```
    6. 点击 "Set" 按钮保存策略。
        这个配置将允许任何人访问 my-bucket 中 public/ 目录下的文件，
        与应用程序中的公开访问机制相匹配。

6. 或者可以使用预设选项来配置 bucket 的匿名访问策略：
    1. 在 "Anonymous" 页签中，点击 "Add Access Rule" 按钮
    2. 在 "Prefix" 栏中输入 "public/"
    3. 在 "Access" 下拉列表中选择 "readonly"
    4.  点击 "Save" 按钮保存匿名访问策略。

7. 获取 Access Keys

可以在控制台左边栏的 "Access Keys" 页签中创建 Access Key。


### 4. 项目依赖配置

1. 在 pom.xml 中添加必要的依赖：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <!-- 项目基本信息 -->
    <groupId>com.example</groupId>
    <artifactId>file-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>file-service</name>
    <description>File Management Service</description>
    
    <!-- 父项目依赖 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.x</version>
    </parent>
    
    <dependencies>
        <!-- Spring Boot 基础依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- MinIO 依赖 -->
        <dependency>
            <groupId>io.minio</groupId>
            <artifactId>minio</artifactId>
            <version>8.5.2</version>
        </dependency>
        
        <!-- Apache Commons IO -->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.11.0</version>
        </dependency>
        
        <!-- AWS S3 依赖（后续使用） -->
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-s3</artifactId>
            <version>1.12.261</version>
        </dependency>
        
        <!-- GCS 依赖（后续使用） -->
        <dependency>
            <groupId>com.google.cloud</groupId>
            <artifactId>google-cloud-storage</artifactId>
            <version>2.22.0</version>
        </dependency>
        
        <!-- 测试依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

2. 在 application.yml 中添加基础配置：

```yaml
server:
  port: 8080

spring:
  application:
    name: file-service
  
  # 数据库配置
  datasource:
    url: jdbc:h2:mem:filedb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  # JPA 配置
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        jdbc.time_zone: UTC
        format_sql: true
  
  # H2 控制台配置
  h2:
    console:
      enabled: true
      path: /h2-console

# 文件上传配置
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
      location: ${java.io.tmpdir}  # 临时文件目录

# 存储服务配置
storage:
  # 当前使用的存储服务类型：minio, aws, gcs
  type: minio
  
  # MinIO 配置
  minio:
    endpoint: http://localhost:9000
    access-key: admin
    secret-key: password123
    bucket: my-bucket
    # bucket-policy: |
    #   {
    #     "Version": "2012-10-17",
    #     "Statement": [
    #       {
    #         "Effect": "Allow",
    #         "Principal": {
    #           "AWS": ["*"]
    #         },
    #         "Action": [
    #           "s3:GetObject",
    #           "s3:GetObjectVersion"
    #         ],
    #         "Resource": ["arn:aws:s3:::my-bucket/public/*"],
    #         "Sid": "PublicRead"
    #       }
    #     ]
    #   }
  
  # AWS S3 配置
  aws:
    access-key: your-aws-access-key
    secret-key: your-aws-secret-key
    region: your-aws-region
    private-bucket: flex-api-private
    public-bucket: flex-api-public
  
  # GCS 配置
  gcs:
    project-id: your-project-id
    private-bucket: flex-api-private
    public-bucket: flex-api-public
```

### 5. 验证开发环境

1. 启动 Spring Boot 应用，确保没有报错
2. 访问 http://localhost:8080/h2-console 确认 H2 数据库可访问
3. 访问 http://localhost:9001 确认 MinIO 控制台可访问

## 第二部分：基础代码实现

### 1. 数据库表结构
```sql
CREATE TABLE file_info (
    id VARCHAR(36) PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    storage_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    size BIGINT,
    user_id VARCHAR(100) NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    url VARCHAR(1000),
    url_expire_time TIMESTAMP,
    is_public BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_user_id ON file_info(user_id);
```

### 2. 实体类定义
```java
import javax.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "file_info")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String originalFilename;  // 原始文件名
    private String storageFilename;   // 存储文件名
    private String contentType;       // 文件类型
    private Long size;               // 文件大小
    private String userId;           // 上传用户ID
    
    @CreationTimestamp
    private Instant uploadTime; // 上传时间(UTC)
    
    private String url;              // 访问URL
    private Instant urlExpireTime; // URL过期时间(UTC)

    private boolean isPublic;  // 是否公开访问

    // 构造函数
    public FileInfo() {}

    public FileInfo(String originalFilename, String storageFilename, String contentType, 
                   Long size, String userId, String url, Instant urlExpireTime, boolean isPublic) {
        this.originalFilename = originalFilename;
        this.storageFilename = storageFilename;
        this.contentType = contentType;
        this.size = size;
        this.userId = userId;
        this.url = url;
        this.urlExpireTime = urlExpireTime;
        this.isPublic = isPublic;
    }

    // Getters
    public String getId() { return id; }
    public String getOriginalFilename() { return originalFilename; }
    public String getStorageFilename() { return storageFilename; }
    public String getContentType() { return contentType; }
    public Long getSize() { return size; }
    public String getUserId() { return userId; }
    public Instant getUploadTime() { return uploadTime; }
    public String getUrl() { return url; }
    public Instant getUrlExpireTime() { return urlExpireTime; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public void setStorageFilename(String storageFilename) { this.storageFilename = storageFilename; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setSize(Long size) { this.size = size; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUploadTime(Instant uploadTime) { this.uploadTime = uploadTime; }
    public void setUrl(String url) { this.url = url; }
    public void setUrlExpireTime(Instant urlExpireTime) { this.urlExpireTime = urlExpireTime; }

    // Builder 模式实现
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String originalFilename;
        private String storageFilename;
        private String contentType;
        private Long size;
        private String userId;
        private String url;
        private Instant urlExpireTime;
        private boolean isPublic;

        public Builder originalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public Builder storageFilename(String storageFilename) {
            this.storageFilename = storageFilename;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder size(Long size) {
            this.size = size;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder urlExpireTime(Instant urlExpireTime) {
            this.urlExpireTime = urlExpireTime;
            return this;
        }

        public Builder isPublic(boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public FileInfo build() {
            return new FileInfo(originalFilename, storageFilename, contentType, 
                              size, userId, url, urlExpireTime, isPublic);
        }
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + id + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", storageFilename='" + storageFilename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", userId='" + userId + '\'' +
                ", uploadTime=" + uploadTime +
                ", url='" + url + '\'' +
                ", urlExpireTime=" + urlExpireTime +
                '}';
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
```

### 3. 异常类定义
```java
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 4. 存储服务接口
```java
public interface StorageService {
    /**
     * 上传文件
     * @param file 文件对象
     * @param path 存储路径
     * @return 文件访问路径
     */
    String uploadFile(MultipartFile file, String path);
    
    /**
     * 生成文件访问URL
     * @param path 文件路径
     * @return 访问URL
     */
    String generateUrl(String path, int expiryMinutes);
    
    /**
     * 删除文件
     * @param path 文件路径
     */
    void deleteFile(String path);
    
    /**
     * 下载文件
     * @param path 文件路径
     * @return 文件字节流
     */
    byte[] downloadFile(String path);
    
    /**
     * 生成公开访问的URL（适用于图片等媒体文件）
     * @param path 文件路径
     * @return 公开访问URL
     */
    String getPublicUrl(String path);
    
    /**
     * 将文件移动到公开访问目录
     * @param path 原始路径
     * @return 新的公开访问路径
     * @throws StorageException 如果操作失败
     */
    String makePublic(String path);
}
```

## 第三部分：存储服务接口和实现

### 1. 存储服务接口
```java
public interface StorageService {
    /**
     * 上传文件
     * @param file 文件对象
     * @param path 存储路径
     * @return 文件访问路径
     */
    String uploadFile(MultipartFile file, String path);
    
    /**
     * 生成文件访问URL
     * @param path 文件路径
     * @return 访问URL
     */
    String generateUrl(String path, int expiryMinutes);
    
    /**
     * 删除文件
     * @param path 文件路径
     */
    void deleteFile(String path);
    
    /**
     * 下载文件
     * @param path 文件路径
     * @return 文件字节流
     */
    byte[] downloadFile(String path);
    
    /**
     * 生成公开访问的URL（适用于图片等媒体文件）
     * @param path 文件路径
     * @return 公开访问URL
     */
    String getPublicUrl(String path);
    
    /**
     * 将文件移动到公开访问目录
     * @param path 原始路径
     * @return 新的公开访问路径
     * @throws StorageException 如果操作失败
     */
    String makePublic(String path);
}
```

### 2. MinIO实现

1. **MinIO 配置类 (MinioConfig.java)**
```java
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "storage.minio")
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    
    // Getters
    public String getEndpoint() { return endpoint; }
    public String getAccessKey() { return accessKey; }
    public String getSecretKey() { return secretKey; }
    public String getBucket() { return bucket; }
    
    // Setters
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
    
    @PostConstruct
    public void init() {
        try {
            MinioClient minioClient = minioClient();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
                
                // // 设置bucket policy
                // minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                //         .bucket(bucket)
                //         .config(bucketPolicy)
                //         .build());
            }
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
```

2. **MinIO 存储服务实现 (MinioStorageService.java)**
```java
@Service("minioStorageService")
@ConditionalOnProperty(name = "storage.type", havingValue = "minio", matchIfMissing = true)
public class MinioStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(MinioStorageService.class);
    
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    
    public MinioStorageService(MinioClient minioClient, MinioConfig minioConfig) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
    }
    
    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return path;
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO", e);
            throw new StorageException("Could not store file", e);
        }
    }
    
    @Override
    public String generateUrl(String path, int expiryMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(path)
                    .method(Method.GET)
                    .expiry(expiryMinutes, TimeUnit.MINUTES)
                    .build());
        } catch (Exception e) {
            log.error("Failed to generate URL", e);
            throw new StorageException("Could not generate URL", e);
        }
    }
    
    @Override
    public void deleteFile(String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(path)
                    .build());
        } catch (Exception e) {
            log.error("Failed to delete file", e);
            throw new StorageException("Could not delete file", e);
        }
    }
    
    @Override
    public byte[] downloadFile(String path) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(path)
                    .build());
            return IOUtils.toByteArray(response);
        } catch (Exception e) {
            log.error("Failed to download file", e);
            throw new StorageException("Could not download file", e);
        }
    }
    
    @Override
    public String getPublicUrl(String path) {
        return String.format("%s/%s/%s", minioConfig.getEndpoint(), 
                           minioConfig.getBucket(), path);
    }
    
    @Override
    public String makePublic(String path) {
        try {
            // 正确: 将文件移动到public目录下，该目录已通过bucket policy配置为公开访问
            String publicPath = "public/" + FilenameUtils.getName(path);
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(publicPath)
                    .source(CopySource.builder()
                        .bucket(minioConfig.getBucket())
                        .object(path)
                        .build())
                    .build()
            );
            
            // 删除原文件
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(path)
                    .build()
            );
            
            return publicPath;
        } catch (Exception e) {
            throw new StorageException("Could not make file public", e);
        }
    }
}
```

### 3. AWS S3 实现

1. **S3 配置类**
```java
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "storage.aws")
@Data
public class S3Config {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    
    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "aws")
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(region)
                .build();
    }
}
```

2. **S3 存储服务实现**
```java
@Service("awsStorageService")
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);
    
    private final AmazonS3 s3Client;
    private final S3Config s3Config;
    
    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String bucket = path.startsWith("public/") ? s3Config.getBucket() : s3Config.getPrivateBucket();
            String key = path.startsWith("public/") ? 
                    path.substring("public/".length()) : path;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest request = new PutObjectRequest(bucket, key,
                    new ByteArrayInputStream(file.getBytes()), metadata);

            s3Client.putObject(request);
            return path;
        } catch (IOException e) {
            throw new StorageException("Could not store file to S3", e);
        }
    }
    
    @Override
    public String generateUrl(String path, int expiryMinutes) {
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(s3Config.getPrivateBucket(), path)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(Date.from(Instant.now().plusSeconds(expiryMinutes * 60L)));

            URL url = s3Client.generatePresignedUrl(request);
            return url.toString();
        } catch (Exception e) {
            throw new StorageException("Could not generate URL", e);
        }
    }
    
    @Override
    public void deleteFile(String path) {
        s3Client.deleteObject(s3Config.getPrivateBucket(), path);
    }
    
    @Override
    public byte[] downloadFile(String path) {
        try {
            S3Object object = s3Client.getObject(s3Config.getPrivateBucket(), path);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (Exception e) {
            throw new StorageException("Failed to download file from AWS S3", e);
        }
    }
    
    @Override
    public String getPublicUrl(String path) {
        if (!path.startsWith("public/")) {
            throw new StorageException("Not a public file");
        }
        String key = path.substring("public/".length());
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3Config.getBucket(), s3Config.getRegion(), key);
    }
    
    @Override
    public String makePublic(String path) {
        try {
            // 设置对象的ACL为公开读取
            s3Client.setObjectAcl(s3Config.getPrivateBucket(), path, CannedAccessControlList.PublicRead);
            
            return path;
        } catch (Exception e) {
            throw new StorageException("Could not make file public", e);
        }
    }
}
```

### 4. GCS 实现

1. **GCS 配置类**
```java
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "storage.gcs")
@Data
public class GcsConfig {
    private String projectId;
    private String credentialsPath;
    private String bucket;
    
    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "gcs")
    public Storage googleCloudStorage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new ClassPathResource(credentialsPath).getInputStream());
        
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
```

2. **GCS 存储服务实现**
```java
@Service("gcsStorageService")
@RequiredArgsConstructor
public class GcsStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(GcsStorageService.class);
    
    private final Storage storage;
    private final GcsConfig gcsConfig;
    
    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(gcsConfig.getBucket(), path)
                    .setContentType(file.getContentType())
                    .build();
            
            storage.create(blobInfo, file.getBytes());
            return path;
        } catch (Exception e) {
            throw new StorageException("Failed to upload file to GCS", e);
        }
    }
    
    @Override
    public String generateUrl(String path, int expiryMinutes) {
        BlobInfo blobInfo = BlobInfo.newBuilder(gcsConfig.getBucket(), path).build();
        
        return storage.signUrl(blobInfo, expiryMinutes, TimeUnit.MINUTES).toString();
    }
    
    @Override
    public void deleteFile(String path) {
        storage.delete(gcsConfig.getBucket(), path);
    }
    
    @Override
    public byte[] downloadFile(String path) {
        try {
            Blob blob = storage.get(gcsConfig.getBucket(), path);
            return blob.getContent();
        } catch (Exception e) {
            throw new StorageException("Failed to download file from GCS", e);
        }
    }
    
    @Override
    public String getPublicUrl(String path) {
        return String.format("https://storage.googleapis.com/%s/%s",
                gcsConfig.getBucket(), path);
    }
    
    @Override
    public String makePublic(String path) {
        try {
            // 设置对象的ACL为公开读取
            BlobId blobId = BlobId.of(gcsConfig.getBucket(), path);
            Blob blob = storage.get(blobId);
            blob.toBuilder().setAcl(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))).build().update();
            
            return path;
        } catch (Exception e) {
            throw new StorageException("Could not make file public", e);
        }
    }
}
```

## 第四部分：业务逻辑实现

### 1. 文件服务
```java
@Service
@Transactional
public class FileService {
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    
    private final StorageService storageService;
    private final FileInfoRepository fileInfoRepository;
    private final int defaultUrlExpiry;
    private final int minRemainingTime;
    
    public FileService(StorageService storageService, 
                      FileInfoRepository fileInfoRepository,
                      @Value("${storage.url.default-expiry:1800}") int defaultUrlExpiry,
                      @Value("${storage.url.min-remaining:300}") int minRemainingTime) {
        this.storageService = storageService;
        this.fileInfoRepository = fileInfoRepository;
        this.defaultUrlExpiry = defaultUrlExpiry;
        this.minRemainingTime = minRemainingTime;
    }
    
    /**
     * 获取文件信息，支持公开文件的匿名访问
     */
    public FileInfo getFileWithOptionalUser(String fileId, String userId) {
        // 先尝试查找指定用户的文件
        if (userId != null) {
            Optional<FileInfo> userFile = fileInfoRepository.findByIdAndUserId(fileId, userId);
            if (userFile.isPresent()) {
                return refreshFileUrl(userFile.get());
            }
        }

        // 如果找不到指定用户的文件，查找是否是公开文件
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        // 如果不是公开文件且用户ID为空，拒绝访问
        if (!fileInfo.isPublic() && userId == null) {
            throw new AccessDeniedException("File is not public");
        }

        return refreshFileUrl(fileInfo);
    }

    @Transactional
    private FileInfo refreshFileUrl(FileInfo fileInfo) {
        if (fileInfo.isPublic()) {
            fileInfo.setUrl(storageService.getPublicUrl(fileInfo.getStorageFilename()));
            fileInfo.setUrlExpireTime(null);
            return fileInfoRepository.save(fileInfo);
        }

        if (fileInfo.getUrlExpireTime() == null || 
            fileInfo.getUrlExpireTime().isBefore(Instant.now().plusSeconds(minRemainingTime))) {
            String url = storageService.generateUrl(fileInfo.getStorageFilename(), defaultUrlExpiry);
            fileInfo.setUrl(url);
            fileInfo.setUrlExpireTime(Instant.now().plusSeconds(defaultUrlExpiry));
            return fileInfoRepository.save(fileInfo);
        }
        return fileInfo;
    }

    /**
     * 上传文件
     */
    public FileInfo uploadFile(MultipartFile file, String userId, boolean isPublic) {
        try {
            // 生成存储路径
            String path = isPublic ? "public/" + generateStoragePath(userId, file) 
                                  : generateStoragePath(userId, file);
            
            // 上传文件
            storageService.uploadFile(file, path);
            
            // 保存文件信息
            FileInfo fileInfo = FileInfo.builder()
                    .originalFilename(file.getOriginalFilename())
                    .storageFilename(path)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .userId(userId)
                    .isPublic(isPublic)
                    .build();
            
            // 根据文件类型生成不同的URL
            if (isPublic) {
                String url = storageService.getPublicUrl(path);
                fileInfo.setUrl(url);
                fileInfo.setUrlExpireTime(null);  // 公开文件URL不过期
            } else {
                String url = storageService.generateUrl(path, defaultUrlExpiry);
                fileInfo.setUrl(url);
                fileInfo.setUrlExpireTime(Instant.now().plusSeconds(defaultUrlExpiry));
            }
            
            return fileInfoRepository.save(fileInfo);
        } catch (Exception e) {
            log.error("Upload failed", e);
            throw new StorageException("Could not upload file", e);
        }
    }
    
    /**
     * 更新文件信息
     */
    public FileInfo updateFileInfo(FileInfo fileInfo) {
        return fileInfoRepository.save(fileInfo);
    }
    
    public void deleteFile(String fileId, String userId) {
        FileInfo fileInfo = getFileWithOptionalUser(fileId, userId);
        
        // 只有文件所有者可以删除文件
        if (!fileInfo.getUserId().equals(userId)) {
            throw new AccessDeniedException("Only file owner can delete the file");
        }
        
        storageService.deleteFile(fileInfo.getStorageFilename());
        fileInfoRepository.delete(fileInfo);
    }

    /**
     * 获取用户的文件列表
     */
    public List<FileInfo> listFiles(String userId) {
        return fileInfoRepository.findByUserId(userId);
    }

    private String generateStoragePath(String userId, MultipartFile file) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return String.format("%s/%s_%s.%s", userId, timestamp, randomStr, extension);
    }

    /**
     * 获取文件访问URL
     */
    public String getFileUrl(FileInfo fileInfo) {
        if (fileInfo.isPublic()) {
            return storageService.getPublicUrl(fileInfo.getStorageFilename());
        }
        return storageService.generateUrl(fileInfo.getStorageFilename(), defaultUrlExpiry);
    }

    /**
     * 下载文件
     * @param fileInfo 文件信息
     * @return 文件字节数组
     */
    public byte[] downloadFile(FileInfo fileInfo) {
        try {
            return storageService.downloadFile(fileInfo.getStorageFilename());
        } catch (Exception e) {
            log.error("Failed to download file: {}", fileInfo.getStorageFilename(), e);
            throw new StorageException("Could not download file", e);
        }
    }
}
```

### 2. 控制器实现
```java
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;
    private final int minRemainingTime;
    
    public FileController(FileService fileService,
                         @Value("${storage.url.min-remaining:300}") int minRemainingTime) {
        this.fileService = fileService;
        this.minRemainingTime = minRemainingTime;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<FileInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
            Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        FileInfo fileInfo = fileService.uploadFile(file, userId, isPublic);
        return ResponseEntity.ok(fileInfo);
    }
    
    @GetMapping("/{fileId}")
    public ResponseEntity<FileInfo> getFile(
            @PathVariable String fileId,
            Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        FileInfo fileInfo = fileService.getFileWithOptionalUser(fileId, userId);
        return ResponseEntity.ok(fileInfo);
    }
    
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable String fileId,
            Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        fileService.deleteFile(fileId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<FileInfo>> listFiles(Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<FileInfo> files = fileService.listFiles(userId);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileId,
            Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        FileInfo fileInfo = fileService.getFileWithOptionalUser(fileId, userId);
        
        byte[] data = fileService.downloadFile(fileInfo);  // 通过FileService下载
        ByteArrayResource resource = new ByteArrayResource(data);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + fileInfo.getOriginalFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping("/{fileId}/media")
    public ResponseEntity<?> serveMedia(
            @PathVariable String fileId,
            Authentication authentication) {
        try {
            String userId = authentication != null ? 
                ((UserDetails) authentication.getPrincipal()).getUsername() : null;
            
            FileInfo fileInfo = fileService.getFileWithOptionalUser(fileId, userId);
            
            // 检查是否是媒体文件
            if (!fileInfo.getContentType().startsWith("image/") && 
                !fileInfo.getContentType().startsWith("video/") &&
                !fileInfo.getContentType().startsWith("audio/")) {
                return ResponseEntity.badRequest().body("Not a media file");
            }

            // 检查是否已有有效的临时URL
            Instant now = Instant.now();
            if (fileInfo.getUrl() != null && 
                fileInfo.getUrlExpireTime() != null && 
                !now.isAfter(fileInfo.getUrlExpireTime().minusSeconds(minRemainingTime))) {
                // 如果现有URL还有超过5分钟有效期,直接重定向到该URL
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(fileInfo.getUrl()))
                        .build();
            }

            // 生成新的临时URL(30分钟有效)
            String url = fileService.getFileUrl(fileInfo);
            
            // 重定向到临时URL
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();
            
        } catch (Exception e) {
            log.error("Failed to serve media file", e);
            return ResponseEntity.notFound().build();
        }
    }
}
```

## 第五部分：性能优化和最佳实践

### 1. 性能优化

1. **大文件处理**
```java
@PostMapping("/upload/chunk")
public ResponseEntity<String> uploadChunk(
        @RequestParam("file") MultipartFile chunk,
        @RequestParam("uploadId") String uploadId,
        @RequestParam("chunkNumber") int chunkNumber) {
    // 分片上传实现
}
```

2. **异步处理**
```java
@Async
public CompletableFuture<FileInfo> processFileAsync(FileInfo fileInfo) {
    // 异步处理逻辑
}
```

3. **缓存配置**
```java
@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("fileUrls");
    }
}
```

### 2. 安全最佳实践

1. **文件类型验证**
```java
private void validateFileType(MultipartFile file) {
    String contentType = file.getContentType();
    if (!allowedContentTypes.contains(contentType)) {
        throw new StorageException("File type not allowed");
    }
}
```

2. **文件大小限制**
```java
private void validateFileSize(MultipartFile file) {
    if (file.getSize() > maxFileSize) {
        throw new StorageException("File size exceeds limit");
    }
}
```

3. **访问控制**
```java
@PreAuthorize("hasRole('USER')")
public FileInfo getFile(String fileId, String userId) {
    // 实现代码
}
```

### 3. 运维最佳实践

1. **健康检查**
```java
@Component
public class StorageHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        // 实现健康检查逻辑
    }
}
```

2. **备份策略**
```java
@Scheduled(cron = "0 0 2 * * *")
public void backupFileMetadata() {
    // 实现备份逻辑
}
```

3. **监控指标**
```java
@Component
public class StorageMetrics {
    private final MeterRegistry registry;
    
    public void recordUpload(long size) {
        registry.counter("file.uploads").increment();
        registry.gauge("file.size", size);
    }
}
```

## 第六部分：安全配置

### 1. Security 配置类 (SecurityConfig.java)
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .ignoringAntMatchers("/api/files/**")
            .and()
            .authorizeRequests()
                .antMatchers("/api/files/**").authenticated()
                .antMatchers("/api/files/*/media").permitAll()
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 第七部分：配置类

### 1. StorageConfig
```java
@Configuration
public class StorageConfig {
    @Value("${storage.type}")
    private String storageType;

    @Bean
    @Primary
    public StorageService storageService(
            @Qualifier("minioStorageService") StorageService minioService,
            @Qualifier("awsStorageService") StorageService awsService,
            @Qualifier("gcsStorageService") StorageService gcsService) {
        switch (storageType) {
            case "aws": return awsService;
            case "gcs": return gcsService;
            default: return minioService;
        }
    }
}
```

## 第八部分：API 测试示例

以下是使用 curl 命令测试文件服务 API 的示例。请根据实际部署的服务地址和端口调整 URL。

### 1. 文件上传
```bash
# 上传私有文件
curl -X POST \
  -H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
  -F "file=@/path/to/your/file.jpg" \
  -F "isPublic=false" \
  http://localhost:8080/api/files/upload

# 上传公开文件
curl -X POST \
  -H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
  -F "file=@/path/to/your/file.jpg" \
  -F "isPublic=true" \
  http://localhost:8080/api/files/upload
```

### 2. 获取文件信息
```bash
# 获取指定文件的元数据信息
curl -X GET \
  -H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
  http://localhost:8080/api/files/{fileId}
```

### 3. 下载文件
```bash
# 下载文件
curl -X GET \
  -H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
  -O \
  http://localhost:8080/api/files/{fileId}/download

# 访问媒体文件（支持公开访问）
curl -X GET \
  http://localhost:8080/api/files/{fileId}/media
```

### 4. 删除文件
```bash
# 删除指定文件
curl -X DELETE \
  -H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
  http://localhost:8080/api/files/{fileId}
```

### 5. 获取文件列表
```bash
# 获取当前用户的所有文件
curl -X GET \
  -H "Authorization: Basic dXNlcjpwYXNzd29yZA==" \
  http://localhost:8080/api/files
```

注意事项：
1. 将 `dXNlcjpwYXNzd29yZA==` 替换为实际的 Base64 编码的认证信息（格式：`username:password`）
2. 将 `{fileId}` 替换为实际的文件 ID
3. 将 `/path/to/your/file.jpg` 替换为要上传的实际文件路径
4. 根据实际部署情况调整主机地址和端口号

测试响应示例：
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "originalFilename": "example.jpg",
  "contentType": "image/jpeg",
  "size": 12345,
  "userId": "user123",
  "uploadTime": "2024-03-19T10:30:00Z",
  "url": "http://localhost:9000/my-bucket/public/example.jpg",
  "urlExpireTime": null,
  "isPublic": true
}
```

### Google Cloud Storage 配置

我们使用两个存储桶来分别存储公开和私有文件：
- 公开文件存储在公开存储桶中
- 私有文件存储在私有存储桶中

#### 1. 配置文件设置
```yaml
storage:
  type: gcs  # 启用 GCS 存储服务
  gcs:
    project-id: your-project-id     # GCP 项目 ID
    private-bucket: flex-api-private # 私有文件存储桶
    public-bucket: flex-api-public  # 公开文件存储桶
```

#### 2. 存储桶创建和配置

1. 创建存储桶：
```bash
# 创建私有存储桶
gsutil mb gs://flex-api-private

# 创建公开存储桶
gsutil mb gs://flex-api-public
```

2. 设置公开存储桶策略：
```bash
# 允许公开访问
gsutil iam ch allUsers:objectViewer gs://flex-api-public
```

#### 3. 认证配置

本服务使用 Application Default Credentials (ADC) 进行认证：

1. 本地开发环境：
```bash
# 使用 gcloud CLI 登录，会自动配置本地凭证
gcloud auth application-default login
```

2. Google Cloud 环境（如 GKE）：
   - 默认使用节点的服务账号
   - 或配置 Workload Identity

3. 其他环境：
```bash
# 设置凭证环境变量
export GOOGLE_APPLICATION_CREDENTIALS="/path/to/service-account.json"
```

#### 4. 文件访问机制

1. 公开文件：
   - 存储在 public 存储桶中
   - 可以通过固定 URL 直接访问
   - 格式：`https://storage.googleapis.com/flex-api-public/filename`

2. 私有文件：
   - 存储在 private 存储桶中
   - 通过签名 URL 访问
   - URL 包含临时访问令牌

#### 5. 所需权限
- Storage Object Viewer (`roles/storage.objectViewer`)
- Storage Object Creator (`roles/storage.objectCreator`)
- Storage Object Admin (`roles/storage.objectAdmin`)

注意：
1. 两个存储桶完全分离，便于权限管理
2. 公开存储桶中的所有文件都可以被公开访问
3. 私有存储桶中的文件需要认证才能访问
4. 定期审查公开存储桶中的文件

### AWS S3 配置

我们使用两个存储桶来分别存储公开和私有文件：
- 公开文件存储在公开存储桶中
- 私有文件存储在私有存储桶中

#### 1. 配置文件设置
```yaml
storage:
  type: aws
  aws:
    access-key: your-aws-access-key
    secret-key: your-aws-secret-key
    region: your-aws-region
    private-bucket: flex-api-private
    public-bucket: flex-api-public
```

#### 2. 存储桶创建和配置

1. 创建存储桶：
   - 在 AWS Console 创建两个 S3 存储桶
   - 或使用 AWS CLI：
```bash
# 创建私有存储桶
aws s3 mb s3://flex-api-private

# 创建公开存储桶
aws s3 mb s3://flex-api-public
```

2. 配置公开存储桶策略：
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicReadGetObject",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::flex-api-public/*"
        }
    ]
}
```

#### 3. 文件访问机制

1. 公开文件：
   - 存储在 public 存储桶中
   - 可以通过固定 URL 直接访问
   - 格式：`https://{bucket}.s3.{region}.amazonaws.com/{filename}`

2. 私有文件：
   - 存储在 private 存储桶中
   - 通过预签名 URL 访问
   - URL 包含临时访问令牌

注意：
1. 确保正确配置 CORS 策略
2. 定期轮换 AWS 访问密钥
3. 使用 IAM 角色而不是访问密钥（在 AWS 环境中）
4. 定期审查公开存储桶中的文件
