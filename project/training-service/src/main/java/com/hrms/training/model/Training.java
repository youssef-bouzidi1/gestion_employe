package com.hrms.training.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private TrainingStatus status;
    
    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private List<TrainingParticipant> participants;
}

@Data
@Entity
@Table(name = "training_participants")
class TrainingParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employee_id")
    private Long employeeId;
    
    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;
    
    private ParticipationStatus status;
    private LocalDate completionDate;
}

enum TrainingStatus {
    PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
}

enum ParticipationStatus {
    ENROLLED, IN_PROGRESS, COMPLETED, DROPPED
}