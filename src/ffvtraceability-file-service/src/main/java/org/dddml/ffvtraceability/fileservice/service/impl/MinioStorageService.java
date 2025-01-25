package org.dddml.ffvtraceability.fileservice.service.impl;

import io.minio.*;
import io.minio.http.Method;
import org.dddml.ffvtraceability.fileservice.config.MinioConfig;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.dddml.ffvtraceability.fileservice.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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

            try (InputStream is = response;
                 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                return os.toByteArray();
            }
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
            String filename = path.substring(path.lastIndexOf('/') + 1);
            String publicPath = "public/" + filename;

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