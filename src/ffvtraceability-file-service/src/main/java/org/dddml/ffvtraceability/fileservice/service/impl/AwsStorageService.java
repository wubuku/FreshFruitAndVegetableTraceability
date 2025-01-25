package org.dddml.ffvtraceability.fileservice.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.dddml.ffvtraceability.fileservice.config.AwsConfig;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.dddml.ffvtraceability.fileservice.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "aws")
public class AwsStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(AwsStorageService.class);

    private final AmazonS3 s3Client;
    private final String privateBucket;
    private final String publicBucket;

    public AwsStorageService(AmazonS3 s3Client, AwsConfig config) {
        this.s3Client = s3Client;
        this.privateBucket = config.getPrivateBucket();
        this.publicBucket = config.getPublicBucket();
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String bucket = path.startsWith("public/") ? publicBucket : privateBucket;
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
    public String getPublicUrl(String path) {
        if (!path.startsWith("public/")) {
            throw new StorageException("Not a public file");
        }
        String key = path.substring("public/".length());
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                publicBucket, s3Client.getRegionName(), key);
    }

    @Override
    public String generateUrl(String path, int expiryMinutes) {
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(privateBucket, path)
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
        try {
            s3Client.deleteObject(privateBucket, path);
        } catch (Exception e) {
            throw new StorageException("Could not delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            S3Object object = s3Client.getObject(privateBucket, path);
            return object.getObjectContent().readAllBytes();
        } catch (Exception e) {
            throw new StorageException("Could not download file", e);
        }
    }

    @Override
    public String makePublic(String path) {
        try {
            // 从私有桶复制到公开桶
            CopyObjectRequest copyRequest = new CopyObjectRequest(
                    privateBucket, path,
                    publicBucket, path);
            s3Client.copyObject(copyRequest);

            // 删除私有桶中的文件
            s3Client.deleteObject(privateBucket, path);

            // 返回带 public/ 前缀的路径
            return "public/" + path;
        } catch (Exception e) {
            throw new StorageException("Could not make file public", e);
        }
    }
} 