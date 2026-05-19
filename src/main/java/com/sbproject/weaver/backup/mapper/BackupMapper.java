package com.sbproject.weaver.backup.mapper;

import com.sbproject.weaver.backup.dto.BackupDto;
import com.sbproject.weaver.backup.entity.BackupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackupMapper {
    @Mapping(source = "file.id", target = "fileId")
    BackupDto toBackupDto(BackupEntity backupEntity);
}
