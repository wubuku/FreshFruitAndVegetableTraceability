package org.dddml.ffvtraceability.fileservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(FileExceptionHandler.class);

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<String> handleStorageException(StorageException e) {
        log.error("Storage error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException e) {
        log.warn("File not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Access denied", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
} 