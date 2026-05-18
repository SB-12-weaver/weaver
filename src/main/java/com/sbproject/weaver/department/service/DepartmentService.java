package com.sbproject.weaver.department.service;

import com.sbproject.weaver.department.dto.CreateRequest;
import com.sbproject.weaver.department.dto.DepartmentDto;

public interface DepartmentService {
  DepartmentDto create(CreateRequest request);
}
