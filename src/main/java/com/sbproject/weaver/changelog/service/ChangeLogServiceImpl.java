package com.sbproject.weaver.changelog.service;

import com.sbproject.weaver.changelog.dto.ChangeLogDto;
import com.sbproject.weaver.changelog.dto.ChangeLogResponse;
import com.sbproject.weaver.changelog.dto.ChangeLogSearchRequest;
import com.sbproject.weaver.changelog.entity.ChangeLogType;
import com.sbproject.weaver.changelog.entity.EmployeeChangeLog;
import com.sbproject.weaver.changelog.mapper.ChangeLogMapper;
import com.sbproject.weaver.changelog.repository.ChangeLogRepository;
import com.sbproject.weaver.common.dto.CursorPageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

    private final ChangeLogRepository changeLogRepository;
    private final ChangeLogMapper changeLogMapper;

    @Transactional
    @Override
    public CursorPageResponse<ChangeLogDto> search(String cursor, int size, ChangeLogSearchRequest request) {
        ChangeLogSearchRequest req = request.withDefaults();

        ChangeLogType type = (req.getType() == null || req.getType().isBlank() || req.getType().equals("ALL"))
                ? null
                : ChangeLogType.valueOf(req.getType());

        Slice<EmployeeChangeLog> slice = changeLogRepository.search(cursor, size, req, type);

        List<ChangeLogDto> content = slice.getContent().stream()
                .map(changeLogMapper::roResponse)
                .toList();
        String nextCursor = null;

        if (slice.hasNext()) {
            nextCursor = content.get(content.size() - 1).getId().toString();
        }

        Long totalElements = changeLogRepository.count(req, type);

        return CursorPageResponse.<ChangeLogDto>builder()
                .content(content)
                .nextCursor(nextCursor)
                .nextIdAfter(null)
                .size(size)
                .totalElements(totalElements)
                .hasNext(slice.hasNext())
                .build();
    }
}
