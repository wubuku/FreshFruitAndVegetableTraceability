package org.dddml.ffvtraceability.fileservice.service;

import org.apache.commons.io.FilenameUtils;
import org.dddml.ffvtraceability.fileservice.domain.FileInfo;
import org.dddml.ffvtraceability.fileservice.exception.FileNotFoundException;
import org.dddml.ffvtraceability.fileservice.exception.StorageException;
import org.dddml.ffvtraceability.fileservice.repository.FileInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public FileInfo getFileWithOptionalUser(String fileId, String userId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found: " + fileId));

        // 如果文件是公开的,允许任何人访问
        if (fileInfo.isPublic()) {
            return fileInfo;
        }

        // 非公开文件需要验证用户权限
        if (userId == null || !fileInfo.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied to file: " + fileId);
        }

        return fileInfo;
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

    public FileInfo uploadFile(MultipartFile file, String userId, boolean isPublic) {
        try {
            String path = isPublic ? "public/" + generateStoragePath(userId, file)
                    : generateStoragePath(userId, file);

            storageService.uploadFile(file, path);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setOriginalFilename(file.getOriginalFilename());
            fileInfo.setStorageFilename(path);
            fileInfo.setContentType(file.getContentType());
            fileInfo.setSize(file.getSize());
            fileInfo.setUserId(userId);
            fileInfo.setPublic(isPublic);

            if (isPublic) {
                String url = storageService.getPublicUrl(path);
                fileInfo.setUrl(url);
                fileInfo.setUrlExpireTime(null);
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

    public FileInfo updateFileInfo(FileInfo fileInfo) {
        return fileInfoRepository.save(fileInfo);
    }

    public void deleteFile(String fileId, String userId) {
        FileInfo fileInfo = getFileWithOptionalUser(fileId, userId);

        if (!fileInfo.getUserId().equals(userId)) {
            throw new AccessDeniedException("Only file owner can delete the file");
        }

        storageService.deleteFile(fileInfo.getStorageFilename());
        fileInfoRepository.delete(fileInfo);
    }

    public List<FileInfo> listFiles(String userId) {
        return fileInfoRepository.findByUserId(userId);
    }

    private String generateStoragePath(String userId, MultipartFile file) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return String.format("%s/%s_%s.%s", userId, timestamp, randomStr, extension);
    }

    public String getFileUrl(FileInfo fileInfo) {
        if (fileInfo.isPublic()) {
            return storageService.getPublicUrl(fileInfo.getStorageFilename());
        }
        return storageService.generateUrl(fileInfo.getStorageFilename(), defaultUrlExpiry);
    }

    public byte[] downloadFile(FileInfo fileInfo) {
        try {
            return storageService.downloadFile(fileInfo.getStorageFilename());
        } catch (Exception e) {
            log.error("Failed to download file: {}", fileInfo.getStorageFilename(), e);
            throw new StorageException("Could not download file", e);
        }
    }
} 