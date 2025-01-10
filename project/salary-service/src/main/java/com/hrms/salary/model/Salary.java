package com.hrms.salary.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "salaries")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employee_id")
    private Long employeeId;
    private LocalDate paymentDate;
    private Double baseSalary;
    private Double overtimePay;
    private Double bonuses;
    private Double deductions;
    private Double netSalary;
    
    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL)
    private List<SalaryComponent> components;
}

@Data
@Entity
@Table(name = "salary_components")
class SalaryComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double amount;
    private ComponentType type;
    
    @ManyToOne
    @JoinColumn(name = "salary_id")
    private Salary salary;
}

enum ComponentType {
    BONUS, DEDUCTION, OVERTIME
}