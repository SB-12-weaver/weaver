package com.sbproject.weaver.department.service;

import com.sbproject.weaver.common.dto.CursorPageResponse;
import com.sbproject.weaver.department.dto.DepartmentDto;
import com.sbproject.weaver.department.dto.DepartmentSearchRequest;

import java.util.UUID;

public interface DepartmentService {
    CursorPageResponse<DepartmentDto> findSlice(UUID cursor, int size, DepartmentSearchRequest search);
    DepartmentDto findById(UUID id);
}
