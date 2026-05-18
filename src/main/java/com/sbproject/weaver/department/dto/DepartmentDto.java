package com.sbproject.weaver.department.dto;

import com.sbproject.weaver.department.entity.DepartmentEntity;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepartmentDto{
    private final UUID id;
    private final String name;
    private final String description;
    private final LocalDate establishedDate;

  public static DepartmentDto fromEntity(DepartmentEntity department) {
    return new DepartmentDto(
        department.getId(),
        department.getName(),
        department.getDescription(),
        department.getEstablishedDate()
    );
  }

}
