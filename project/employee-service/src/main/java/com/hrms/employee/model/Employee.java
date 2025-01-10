package com.hrms.employee.model;

import com.hrms.employee.model.Department;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    
    private LocalDate joiningDate;
    private String position;
    private Double salary;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}

enum ContractType {
    CDI, CDD, FREELANCE
}