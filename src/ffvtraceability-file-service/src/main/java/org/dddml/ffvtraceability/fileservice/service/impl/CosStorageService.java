package org.dddml.ffvtraceability.fileservice.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import org.dddml.ffvtraceability.fileservice.config.CosConfigProperties;
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
import java.net.URL;
import java.util.Date;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "cos")
public class CosStorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(CosStorageService.class);

    private final COSClient cosClient;
    private final CosConfigProperties cosConfigProperties;

    public CosStorageService(COSClient cosClient, CosConfigProperties cosConfigProperties) {
        this.cosClient = cosClient;
        this.cosConfigProperties = cosConfigProperties;
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String bucket = path.startsWith("public/") ? cosConfigProperties.getPublicBucket() : cosConfigProperties.getPrivateBucket();
            String key = path.startsWith("public/") ? path.substring("public/".length()) : path;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest request = new PutObjectRequest(
                    bucket, key, file.getInputStream(), metadata);

            cosClient.putObject(request);
            return path;
        } catch (IOException e) {
            log.error("Failed to upload file to COS", e);
            throw new StorageException("Could not store file", e);
        }
    }

    @Override
    public String generateUrl(String path, int expiryMinutes) {
        try {
            String bucket = cosConfigProperties.getPrivateBucket();
            Date expirationDate = new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000L);
            
            URL url = cosClient.generatePresignedUrl(
                    bucket, path, expirationDate);
            
            return url.toString();
        } catch (Exception e) {
            log.error("Failed to generate URL", e);
            throw new StorageException("Could not generate URL", e);
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            String bucket = path.startsWith("public/") ? cosConfigProperties.getPublicBucket() : cosConfigProperties.getPrivateBucket();
            String key = path.startsWith("public/") ? path.substring("public/".length()) : path;
            
            cosClient.deleteObject(bucket, key);
        } catch (Exception e) {
            log.error("Failed to delete file", e);
            throw new StorageException("Could not delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String path) {
        try {
            String bucket = path.startsWith("public/") ? cosConfigProperties.getPublicBucket() : cosConfigProperties.getPrivateBucket();
            String key = path.startsWith("public/") ? path.substring("public/".length()) : path;
            
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            
            try (InputStream inputStream = cosObject.getObjectContent();
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
        String publicEndpoint = cosConfigProperties.getPublicEndpoint();
        return String.format("%s/%s", publicEndpoint.endsWith("/") ? publicEndpoint.substring(0, publicEndpoint.length() - 1) : publicEndpoint, key);
    }

    @Override
    public String makePublic(String path) {
        try {
            String sourceKey = path;
            String targetKey = path;
            String publicPath = "public/" + path;
            
            // Copy from private to public bucket
            cosClient.copyObject(
                    cosConfigProperties.getPrivateBucket(), sourceKey,
                    cosConfigProperties.getPublicBucket(), targetKey
            );
            
            // Delete from private bucket
            cosClient.deleteObject(cosConfigProperties.getPrivateBucket(), sourceKey);
            
            return publicPath;
        } catch (Exception e) {
            log.error("Failed to make file public", e);
            throw new StorageException("Could not make file public", e);
        }
    }
} 