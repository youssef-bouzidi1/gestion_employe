package com.hrms.absence.repository;

import com.hrms.absence.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    // Custom queries can be added here if needed
}
