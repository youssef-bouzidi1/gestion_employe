package com.hrms.employee.service;

import com.hrms.employee.model.Employee;
import com.hrms.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
    
    @Transactional
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployee(id);
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());
        return employeeRepository.save(employee);
    }
    
    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}