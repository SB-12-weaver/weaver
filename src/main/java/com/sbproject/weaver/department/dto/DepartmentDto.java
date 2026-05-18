package com.sbproject.weaver.department.dto;

import com.sbproject.weaver.department.entity.DepartmentEntity;
import java.time.LocalDate;
import java.util.UUID;


public record DepartmentDto(
    UUID id,
    String name,
    String description,
    LocalDate foundedDate
) {

  public static DepartmentDto fromEntity(
      DepartmentEntity department
  ) {

    return new DepartmentDto(
        department.getId(),
        department.getName(),
        department.getDescription(),
        department.getFoundedDate()
    );
  }

}
