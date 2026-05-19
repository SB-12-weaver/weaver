package com.sbproject.weaver.backup.repository;

import com.sbproject.weaver.backup.dto.BackupDto;
import com.sbproject.weaver.backup.entity.BackupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface BackupRepositoryCustom {

    Page<BackupDto> findBackups(
            String worker,
            BackupStatus status,
            Instant from,
            Instant to,
            String cursor,
            String direction,
            String sortField,
            Pageable pageable
    );
}