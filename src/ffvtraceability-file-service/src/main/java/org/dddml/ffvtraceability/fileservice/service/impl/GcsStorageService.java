package org.dddml.ffvtraceability.fileservice.service.impl;

import com.google.cloud.storage.*;
import org.dddml.ffvtraceability.fileservice.config.GcsConfig;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.dddml.ffvtraceability.fileservice.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "gcs")
public class GcsStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(GcsStorageService.class);

    private final Storage storage;
    private final String bucket;

    public GcsStorageService(Storage storage, GcsConfig config) {
        this.storage = storage;
        this.bucket = config.getBucket();
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            BlobId blobId = BlobId.of(bucket, path);
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
    public String generateUrl(String path, int expiryMinutes) {
        try {
            BlobId blobId = BlobId.of(bucket, path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            URL url = storage.signUrl(blobInfo,
                    expiryMinutes, TimeUnit.MINUTES,
                    Storage.SignUrlOption.withV4Signature());

            return url.toString();
        } catch (Exception e) {
            throw new StorageException("Could not generate URL", e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            BlobId blobId = BlobId.of(bucket, path);
            storage.delete(blobId);
        } catch (Exception e) {
            throw new StorageException("Could not delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            Blob blob = storage.get(BlobId.of(bucket, path));
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
    public String getPublicUrl(String path) {
        return String.format("https://storage.googleapis.com/%s/%s", bucket, path);
    }

    @Override
    public String makePublic(String path) {
        try {
            BlobId blobId = BlobId.of(bucket, path);
            Blob blob = storage.get(blobId);
            if (blob == null) {
                throw new StorageException("File not found: " + path);
            }

            Acl.Entity entity = Acl.User.ofAllUsers();
            Acl.Role role = Acl.Role.READER;
            blob.createAcl(Acl.of(entity, role));

            return getPublicUrl(path);
        } catch (Exception e) {
            throw new StorageException("Could not make file public", e);
        }
    }
} 