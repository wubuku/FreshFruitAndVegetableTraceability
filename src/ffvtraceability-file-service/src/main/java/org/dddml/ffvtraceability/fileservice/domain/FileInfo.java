package org.dddml.ffvtraceability.fileservice.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "file_info")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String originalFilename;
    private String storageFilename;
    private String contentType;
    private Long size;
    private String userId;

    @Column(updatable = false)
    private Instant uploadTime;

    private String url;
    private Instant urlExpireTime;
    private boolean isPublic;

    // Default constructor
    public FileInfo() {
        this.uploadTime = Instant.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStorageFilename() {
        return storageFilename;
    }

    public void setStorageFilename(String storageFilename) {
        this.storageFilename = storageFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getUrlExpireTime() {
        return urlExpireTime;
    }

    public void setUrlExpireTime(Instant urlExpireTime) {
        this.urlExpireTime = urlExpireTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
} 