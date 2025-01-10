package com.hrms.absence.service;

import com.hrms.absence.model.Leave;         // Import the Leave class (LeaveStatus is accessed from here)
import com.hrms.absence.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveService {
    private final LeaveRepository leaveRepository;

    @Transactional
    public Leave requestLeave(Leave leave) {
        leave.setStatus(Leave.LeaveStatus.PENDING);  // Use Leave.LeaveStatus here
        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave approveLeave(Long leaveId, Long approverId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leave.setStatus(Leave.LeaveStatus.APPROVED);  // Use Leave.LeaveStatus here
        leave.setApproverId(approverId);
        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave rejectLeave(Long leaveId, Long approverId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leave.setStatus(Leave.LeaveStatus.REJECTED);  // Use Leave.LeaveStatus here
        leave.setApproverId(approverId);
        return leaveRepository.save(leave);
    }
}
