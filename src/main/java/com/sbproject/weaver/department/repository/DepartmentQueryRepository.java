package com.sbproject.weaver.department.repository;

import com.sbproject.weaver.common.dto.CursorPageResponse;
import com.sbproject.weaver.department.dto.DepartmentDto;
import com.sbproject.weaver.department.dto.DepartmentSearchRequest;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentQueryRepository {
    Optional<DepartmentDto> findById(UUID id);
    CursorPageResponse<DepartmentDto> searchSlice(UUID cursor, int size, DepartmentSearchRequest search);
    long countSearch(DepartmentSearchRequest search);
}
