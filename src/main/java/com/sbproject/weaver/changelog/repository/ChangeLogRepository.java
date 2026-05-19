package com.sbproject.weaver.changelog.repository;

import com.sbproject.weaver.changelog.dto.ChangeLogSearchRequest;
import com.sbproject.weaver.changelog.entity.ChangeLogType;
import com.sbproject.weaver.changelog.entity.EmployeeChangeLog;
import org.springframework.data.domain.Slice;

public interface ChangeLogRepository {
    Slice<EmployeeChangeLog> search(String cursor, int size, ChangeLogSearchRequest search, ChangeLogType type);

    Long count(ChangeLogSearchRequest search, ChangeLogType type);
}
