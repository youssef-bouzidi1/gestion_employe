package com.hrms.department.repository;

import com.hrms.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByParentDepartmentId(Long parentId);
}