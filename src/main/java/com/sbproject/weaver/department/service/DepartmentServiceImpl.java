package com.sbproject.weaver.department.service;

import com.sbproject.weaver.department.dto.CreateRequest;
import com.sbproject.weaver.department.dto.DepartmentDto;
import com.sbproject.weaver.department.entity.DepartmentEntity;
import com.sbproject.weaver.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;


  @Override
  public DepartmentDto create(CreateRequest request) {
    DepartmentEntity department = request.toEntity();

    // name 중복 체크
    if(departmentRepository.existsByName(department.getName())) {
      throw new IllegalArgumentException("Department name already exists");
    }

    // 비어있는지 확인
    if(department.getName() == null || department.getName().isEmpty()){
      throw new IllegalArgumentException("Department name cannot be empty");
    }
    if(department.getDescription() == null || department.getDescription().isEmpty()){
      throw new IllegalArgumentException("Department description cannot be empty");
    }
    if (department.getEstablishedDate() == null) {
      throw new IllegalArgumentException("Department foundedDate cannot be empty");
    }

    DepartmentEntity savedDepartment = departmentRepository.save(department);

    return DepartmentDto.fromEntity(savedDepartment);
  }
}
