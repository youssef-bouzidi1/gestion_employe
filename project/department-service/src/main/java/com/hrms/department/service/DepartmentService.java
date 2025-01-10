package com.hrms.department.service;

import com.hrms.department.model.Department;
import com.hrms.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found"));
    }
    
    public List<Department> getSubDepartments(Long parentId) {
        return departmentRepository.findByParentDepartmentId(parentId);
    }
    
    @Transactional
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
    
    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = getDepartment(id);
        department.setName(departmentDetails.getName());
        department.setDescription(departmentDetails.getDescription());
        department.setManager(departmentDetails.getManager());
        department.setParentDepartment(departmentDetails.getParentDepartment());
        return departmentRepository.save(department);
    }
    
    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}