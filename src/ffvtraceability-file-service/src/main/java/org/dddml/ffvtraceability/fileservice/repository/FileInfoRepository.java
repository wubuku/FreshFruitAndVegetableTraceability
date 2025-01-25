package org.dddml.ffvtraceability.fileservice.repository;

import org.dddml.ffvtraceability.fileservice.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {
    Optional<FileInfo> findByIdAndUserId(String id, String userId);

    List<FileInfo> findByUserId(String userId);
} 