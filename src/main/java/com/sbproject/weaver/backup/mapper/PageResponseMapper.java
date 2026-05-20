package com.sbproject.weaver.backup.mapper;

import com.sbproject.weaver.backup.dto.BackupDto;
import com.sbproject.weaver.common.dto.CursorPageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

    CursorPageResponse<BackupDto> toCursorPage(
            Page<BackupDto> page,
            String nextCursor,
            String nextIdAfter
    );
}