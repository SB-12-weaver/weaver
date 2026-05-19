package com.sbproject.weaver.department.controller;

import com.sbproject.weaver.common.dto.CursorPageResponse;
import com.sbproject.weaver.department.dto.DepartmentDto;
import com.sbproject.weaver.department.dto.DepartmentSearchRequest;
import com.sbproject.weaver.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<CursorPageResponse<DepartmentDto>> findAll(
            @RequestParam(required = false) UUID cursor,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute DepartmentSearchRequest search
            ) {
        CursorPageResponse<DepartmentDto> response = departmentService.findSlice(cursor, size, search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable UUID id) {
        DepartmentDto response = departmentService.findById(id);
        return ResponseEntity.ok(response);
    }
}
