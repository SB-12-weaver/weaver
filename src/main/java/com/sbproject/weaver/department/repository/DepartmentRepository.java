package com.sbproject.weaver.department.repository;

import com.sbproject.weaver.department.entity.DepartmentEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {
  Boolean existsByName(String name);
}
