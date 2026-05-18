package com.sbproject.weaver.department.dto;

import com.sbproject.weaver.department.entity.DepartmentEntity;
import java.time.LocalDate;
import java.util.UUID;

public record CreateRequest(
    UUID id,
    String name,
    String description,
    LocalDate foundedDate
) {
  public DepartmentEntity toEntity() {

    DepartmentEntity department = new DepartmentEntity();

    department.setName(name);
    department.setDescription(description);
    department.setFoundedDate(foundedDate);

    return department;
  }

}
