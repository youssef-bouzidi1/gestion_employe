package com.hrms.department.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.hrms.department.model.Employee;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;
    
    @OneToMany(mappedBy = "parentDepartment")
    private List<Department> subDepartments;
    
    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
}