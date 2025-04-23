package org.dddml.ffvtraceability.fileservice.service.impl;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;
import org.dddml.ffvtraceability.fileservice.config.TosConfigProperties;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.dddml.ffvtraceability.fileservice.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "tos")
public class TosStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(TosStorageService.class);

    private final TOSV2 tosClient;
    private final TosConfigProperties tosConfigProperties;

    public TosStorageService(TOSV2 tosClient, TosConfigProperties tosConfigProperties) {
        this.tosClient = tosClient;
        this.tosConfigProperties = tosConfigProperties;
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String bucket = path.startsWith("public/") ? tosConfigProperties.getPublicBucket() : tosConfigProperties.getPrivateBucket();
            String key = path.startsWith("public/") ? path.substring("public/".length()) : path;

            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());

            PutObjectInput input = new PutObjectInput()
                    .setBucket(bucket)
                    .setKey(key)
                    //.setContentDisposition("attachment")
                    .setContentLength(file.getSize())
                    .setContent(file.getInputStream());
            //.setMeta(metadata);

            tosClient.putObject(input);
            return path;
        } catch (IOException | TosClientException | TosServerException e) {
            log.error("Failed to upload file to TOS", e);
            throw new StorageException("Could not store file", e);
        }
    }

    @Override
    public String generateUrl(String path, int expiryMinutes) {
        try {
            String bucket = tosConfigProperties.getPrivateBucket();

            PreSignedURLInput input = new PreSignedURLInput()
                    .setBucket(bucket)
                    .setKey(path)
                    .setExpires(expiryMinutes * 60L);

            PreSignedURLOutput output = tosClient.preSignedURL(input);
            return output.getSignedUrl();
        } catch (Exception e) {
            log.error("Failed to generate URL", e);
            throw new StorageException("Could not generate URL", e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            String bucket = path.startsWith("public/") ? tosConfigProperties.getPublicBucket() : tosConfigProperties.getPrivateBucket();
            String key = path.startsWith("public/") ? path.substring("public/".length()) : path;

            DeleteObjectInput input = new DeleteObjectInput()
                    .setBucket(bucket)
                    .setKey(key);

            tosClient.deleteObject(input);
        } catch (Exception e) {
            log.error("Failed to delete file", e);
            throw new StorageException("Could not delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            String bucket = path.startsWith("public/") ? tosConfigProperties.getPublicBucket() : tosConfigProperties.getPrivateBucket();
            String key = path.startsWith("public/") ? path.substring("public/".length()) : path;

            GetObjectV2Input input = new GetObjectV2Input()
                    .setBucket(bucket)
                    .setKey(key);

            GetObjectV2Output output = tosClient.getObject(input);

            try (InputStream inputStream = output.getContent();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            log.error("Failed to download file", e);
            throw new StorageException("Could not download file", e);
        }
    }

    @Override
    public String getPublicUrl(String path) {
        if (!path.startsWith("public/")) {
            throw new StorageException("Not a public file");
        }

        String key = path.substring("public/".length());
        String publicEndpoint = tosConfigProperties.getPublicEndpoint();
        return String.format("%s/%s", publicEndpoint.endsWith("/") ? publicEndpoint.substring(0, publicEndpoint.length() - 1) : publicEndpoint, key);
    }

    @Override
    public String makePublic(String path) {
        try {
            // Source information
            String sourceBucket = tosConfigProperties.getPrivateBucket();
            String sourceKey = path;

            // Target information
            String targetBucket = tosConfigProperties.getPublicBucket();
            String targetKey = path;
            String publicPath = "public/" + path;

            // Copy from private to public bucket
            CopyObjectV2Input copyInput = new CopyObjectV2Input()
                    .setBucket(targetBucket)
                    .setKey(targetKey)
                    .setSrcBucket(sourceBucket)
                    .setSrcKey(sourceKey);

            tosClient.copyObject(copyInput);

            // Delete from private bucket
            DeleteObjectInput deleteInput = new DeleteObjectInput()
                    .setBucket(sourceBucket)
                    .setKey(sourceKey);

            tosClient.deleteObject(deleteInput);

            return publicPath;
        } catch (Exception e) {
            log.error("Failed to make file public", e);
            throw new StorageException("Could not make file public", e);
        }
    }
} 