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

            storage.create(blobInfo, file.getBytes());
            return path;
        } catch (Exception e) {
            throw new StorageException("Could not store file to GCS", e);
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
    public String generateUrl(String path, int expiryMinutes) {
        // 私有文件生成带签名的临时 URL
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(privateBucket, path)).build();
            return storage.signUrl(blobInfo, expiryMinutes, TimeUnit.MINUTES,
                            Storage.SignUrlOption.withV4Signature())
                    .toString();
        } catch (Exception e) {
            throw new StorageException("Could not generate URL", e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            BlobId blobId = BlobId.of(privateBucket, path);
            storage.delete(blobId);
        } catch (Exception e) {
            throw new StorageException("Could not delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            Blob blob = storage.get(BlobId.of(privateBucket, path));
            if (blob == null) {
                throw new StorageException("File not found: " + path);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.downloadTo(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new StorageException("Could not download file", e);
        }
    }

    @Override
    public String makePublic(String path) {
        try {
            // 构造新的公开路径
            String filename = path.substring(path.lastIndexOf('/') + 1);
            String publicPath = "public/" + filename;

            // 复制到 public 目录
            BlobId sourceBlobId = BlobId.of(privateBucket, path);
            BlobId targetBlobId = BlobId.of(publicBucket, publicPath);

            Storage.CopyRequest copyRequest = Storage.CopyRequest.newBuilder()
                    .setSource(sourceBlobId)
                    .setTarget(targetBlobId)
                    .build();

            storage.copy(copyRequest);

            // 删除原文件
            storage.delete(sourceBlobId);

            return publicPath;
        } catch (Exception e) {
            throw new StorageException("Could not make file public", e);
        }
    }
} 