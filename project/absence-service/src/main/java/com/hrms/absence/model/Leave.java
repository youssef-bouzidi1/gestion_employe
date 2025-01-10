package com.hrms.absence.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType type;
    private LeaveStatus status;  // This field uses the LeaveStatus enum
    private String reason;

    @Column(name = "approver_id")
    private Long approverId;

    // Enum for leave types
    public enum LeaveType {
        VACATION, SICK, PERSONAL, MATERNITY, PATERNITY
    }

    // Make sure this enum is public
    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED
    }
}
