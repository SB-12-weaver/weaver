package com.sbproject.weaver.employee.dto;

import com.sbproject.weaver.employee.entity.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class EmployeeBackupRow {

    private UUID id;
    private String employeeNumber;
    private String name;
    private String email;
    private UUID departmentId;
    private String position;
    private LocalDate hireDate;
    private EmployeeStatus status;
}