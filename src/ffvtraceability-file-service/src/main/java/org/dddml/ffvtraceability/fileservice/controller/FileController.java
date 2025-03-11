package org.dddml.ffvtraceability.fileservice.controller;

import org.dddml.ffvtraceability.fileservice.config.StorageSecurityProperties;
import org.dddml.ffvtraceability.fileservice.domain.FileInfo;
import org.dddml.ffvtraceability.fileservice.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private static final String ANONYMOUS_USER = "anonymous";
    private final FileService fileService;
    private final int minRemainingTime;
    private final StorageSecurityProperties securityProperties;

    public FileController(FileService fileService,
                          @Value("${storage.url.min-remaining:300}") int minRemainingTime,
                          StorageSecurityProperties securityProperties) {
        this.fileService = fileService;
        this.minRemainingTime = minRemainingTime;
        this.securityProperties = securityProperties;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
            Authentication authentication) {
        String userId = ANONYMOUS_USER;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        FileInfo fileInfo = fileService.uploadFile(file, userId, isPublic);
        return ResponseEntity.ok(fileInfo);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileInfo> getFile(
            @PathVariable String fileId,
            Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        FileInfo fileInfo = fileService.getFileWithOptionalUser(fileId, userId);
        return ResponseEntity.ok(fileInfo);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable String fileId,
            Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        fileService.deleteFile(fileId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FileInfo>> listFiles(Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<FileInfo> files = fileService.listFiles(userId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileId,
            Authentication authentication) {
        String userId = authentication != null ?
                ((UserDetails) authentication.getPrincipal()).getUsername() : null;

        FileInfo fileInfo = fileService.getFileWithOptionalUser(fileId, userId);

        byte[] data = fileService.downloadFile(fileInfo);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileInfo.getOriginalFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{fileId}/media")
    public ResponseEntity<?> serveMedia(
            @PathVariable String fileId,
            Authentication authentication) {
        try {
            String userId = authentication != null ?
                    ((UserDetails) authentication.getPrincipal()).getUsername() : null;

            FileInfo fileInfo = fileService.getFileWithOptionalUser(fileId, userId);

            if (!fileInfo.getContentType().startsWith("image/") &&
                    !fileInfo.getContentType().startsWith("video/") &&
                    !fileInfo.getContentType().startsWith("audio/")) {
                return ResponseEntity.badRequest().body("Not a media file");
            }

            Instant now = Instant.now();
            if (fileInfo.getUrl() != null &&
                    fileInfo.getUrlExpireTime() != null &&
                    !now.isAfter(fileInfo.getUrlExpireTime().minusSeconds(minRemainingTime))) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(fileInfo.getUrl()))
                        .build();
            }

            String url = fileService.getFileUrl(fileInfo);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();

        } catch (Exception e) {
            log.error("Failed to serve media file", e);
            return ResponseEntity.notFound().build();
        }
    }
} 