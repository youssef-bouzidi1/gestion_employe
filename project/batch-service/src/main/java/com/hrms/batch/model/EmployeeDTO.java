package com.hrms.batch.model;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Double salary;  // Assuming this field exists in your EmployeeDTO

    // Any other fields you might need
}
