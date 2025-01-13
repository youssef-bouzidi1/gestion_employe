package com.hrms.batch.client;

import com.hrms.batch.model.SalaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "salary-service", url = "http://localhost:8082")  // Replace with actual salary service URL
public interface SalaryClient {

    @PostMapping("/api/salaries")
    void saveSalary(SalaryDTO salaryDTO);  // Save the SalaryDTO
}
