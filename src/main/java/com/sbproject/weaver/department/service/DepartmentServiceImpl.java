package com.sbproject.weaver.department.service;

import com.sbproject.weaver.common.dto.CursorPageResponse;
import com.sbproject.weaver.department.dto.DepartmentDto;
import com.sbproject.weaver.department.dto.DepartmentSearchRequest;
import com.sbproject.weaver.department.repository.DepartmentQueryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentQueryRepositoryImpl departmentRepository;

    @Transactional(readOnly = true)
    public CursorPageResponse<DepartmentDto> findSlice(UUID cursor, int size, DepartmentSearchRequest search){
        return departmentRepository.searchSlice(cursor, size, search);
    }

    @Transactional(readOnly = true)
    public DepartmentDto findById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 부서를 찾을 수 없습니다"));
    }
}
