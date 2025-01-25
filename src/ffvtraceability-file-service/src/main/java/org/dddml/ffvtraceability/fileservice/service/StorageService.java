package org.dddml.ffvtraceability.fileservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(MultipartFile file, String path);

    String generateUrl(String path, int expiryMinutes);

    void deleteFile(String path);

    byte[] downloadFile(String path);

    String getPublicUrl(String path);

    String makePublic(String path);
}