package com.hrms.batch.client;

import com.hrms.batch.model.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service", url = "http://localhost:8081")  // Replace with the actual URL of the Employee Microservice
public interface EmployeeClient {

    // Fetch a single employee by ID from the employee service
    @GetMapping("/api/employees/{id}")
    EmployeeDTO getEmployeeById(@PathVariable("id") Long id);

    // Fetch all employees from the employee service
    @GetMapping("/api/employees")
    List<EmployeeDTO> getAllEmployees();
}
