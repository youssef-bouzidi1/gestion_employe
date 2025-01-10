package com.hrms.department.controller;

import com.hrms.department.model.Department;
import com.hrms.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
    
    @GetMapping("/{id}")
    public Department getDepartment(@PathVariable Long id) {
        return departmentService.getDepartment(id);
    }
    
    @GetMapping("/{id}/subdepartments")
    public List<Department> getSubDepartments(@PathVariable Long id) {
        return departmentService.getSubDepartments(id);
    }
    
    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }
    
    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }
}