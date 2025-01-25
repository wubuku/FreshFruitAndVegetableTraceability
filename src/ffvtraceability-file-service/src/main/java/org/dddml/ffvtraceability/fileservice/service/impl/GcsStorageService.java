package org.dddml.ffvtraceability.fileservice.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.dddml.ffvtraceability.fileservice.config.GcsConfig;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.dddml.ffvtraceability.fileservice.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "gcs")
public class GcsStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(GcsStorageService.class);

    private final Storage storage;
    private final String privateBucket;
    private final String publicBucket;

    public GcsStorageService(Storage storage, GcsConfig config) {
        this.storage = storage;
        this.privateBucket = config.getPrivateBucket();
        this.publicBucket = config.getPublicBucket();
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String bucket = path.startsWith("public/") ? publicBucket : privateBucket;
            String objectName = path.startsWith("public/") ?
                    path.substring("public/".length()) : path;

            BlobId blobId = BlobId.of(bucket, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // 上传文件
            storage.create(blobInfo, file.getBytes());
            return path;
        } catch (Exception e) {
            log.error("Failed to upload file to GCS", e);
            throw new StorageException("Could not store file to GCS", e);
        }
    }

    @Override
    public String generateUrl(String path, int expiryMinutes) {
        try {
            String bucket = path.startsWith("public/") ? publicBucket : privateBucket;
            String objectName = path.startsWith("public/") ?
                    path.substring("public/".length()) : path;

            log.info("Generating URL for bucket: {}, object: {}", bucket, objectName);
            
            if (path.startsWith("public/")) {
                // 公开文件直接返回公开URL
                return String.format("https://storage.googleapis.com/%s/%s",
                        publicBucket, objectName);
            }

            // 私有文件
            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName)).build();
            
            // 使用服务账号凭证生成签名URL
            return storage.signUrl(
                    blobInfo,
                    expiryMinutes,
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.withV4Signature()
            ).toString();
        } catch (Exception e) {
            log.error("Failed to generate URL. Error: {}", e.getMessage(), e);
            throw new StorageException("Could not generate URL", e);
        }
    }

    @Override
    public String getPublicUrl(String path) {
        if (!path.startsWith("public/")) {
            throw new StorageException("Not a public file");
        }
        String objectName = path.substring("public/".length());
        return String.format("https://storage.googleapis.com/%s/%s",
                publicBucket, objectName);
    }

    @Override
    public void deleteFile(String path) {
        try {
            String bucket = path.startsWith("public/") ? publicBucket : privateBucket;
            String objectName = path.startsWith("public/") ?
                    path.substring("public/".length()) : path;

            BlobId blobId = BlobId.of(bucket, objectName);
            boolean deleted = storage.delete(blobId);
            
            if (!deleted) {
                log.warn("File not found when attempting to delete: {}", path);
            }
        } catch (Exception e) {
            log.error("Failed to delete file", e);
            throw new StorageException("Could not delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            String bucket = path.startsWith("public/") ? publicBucket : privateBucket;
            String objectName = path.startsWith("public/") ?
                    path.substring("public/".length()) : path;

            Blob blob = storage.get(BlobId.of(bucket, objectName));
            if (blob == null) {
                throw new StorageException("File not found: " + path);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.downloadTo(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to download file", e);
            throw new StorageException("Could not download file", e);
        }
    }

    @Override
    public String makePublic(String path) {
        try {
            // 源文件信息
            String objectName = path;
            BlobId sourceBlobId = BlobId.of(privateBucket, objectName);
            Blob sourceBlob = storage.get(sourceBlobId);
            
            if (sourceBlob == null) {
                throw new StorageException("Source file not found: " + path);
            }

            // 复制到公开存储桶
            BlobId targetBlobId = BlobId.of(publicBucket, objectName);
            BlobInfo targetBlobInfo = BlobInfo.newBuilder(targetBlobId)
                    .setContentType(sourceBlob.getContentType())
                    .build();

            // 执行复制
            storage.copy(Storage.CopyRequest.newBuilder()
                    .setSource(sourceBlobId)
                    .setTarget(targetBlobInfo)
                    .build());

            // 删除源文件
            storage.delete(sourceBlobId);

            // 返回新的公开路径
            return "public/" + objectName;
        } catch (Exception e) {
            log.error("Failed to make file public", e);
            throw new StorageException("Could not make file public", e);
        }
    }
} 