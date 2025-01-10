package com.hrms.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SalaryCalculationJob {
    
    @Bean
    public Job calculateMonthlySalaries(
            JobRepository jobRepository,
            Step calculateSalariesStep) {
        return new JobBuilder("calculateMonthlySalaries", jobRepository)
            .start(calculateSalariesStep)
            .build();
    }
    
    @Bean
    public Step calculateSalariesStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<Employee> reader,
            ItemProcessor<Employee, Salary> processor,
            ItemWriter<Salary> writer) {
        return new StepBuilder("calculateSalariesStep", jobRepository)
            .<Employee, Salary>chunk(10, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }
}