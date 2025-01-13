package com.hrms.batch.controller;

import com.hrms.batch.model.EmployeeDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hrms.batch.client.EmployeeClient;

import java.util.Collections;
import java.util.List;

@RestController
public class SalaryBatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job salaryBatchJob;

    @Autowired
    private EmployeeClient employeeClient;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = employeeClient.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // Endpoint to trigger the batch job manually
    @PostMapping("/start-salary-batch")
    public ResponseEntity<?> startBatch() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(salaryBatchJob, jobParameters);

            String response = String.format(
                    "Job Status: %s\nStart Time: %s\nEnd Time: %s\n" +
                            "Read Count: %d\nWrite Count: %d\nSkip Count: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStartTime(),
                    jobExecution.getEndTime(),
                    jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getReadCount).sum(),
                    jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getWriteCount).sum(),
                    jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getSkipCount).sum()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting the batch job: " + e.getMessage() +
                            "\nCause: " + (e.getCause() != null ? e.getCause().getMessage() : "No cause"));
        }
    }
}