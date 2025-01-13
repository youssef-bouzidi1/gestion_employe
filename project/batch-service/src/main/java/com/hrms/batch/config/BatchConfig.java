package com.hrms.batch.config;

import com.hrms.batch.client.EmployeeClient;
import com.hrms.batch.client.SalaryClient;
import com.hrms.batch.model.EmployeeDTO;
import com.hrms.batch.model.SalaryDTO;
import com.hrms.batch.reader.EmployeeIdReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private EmployeeClient employeeClient;

    @Autowired
    private SalaryClient salaryClient;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public ItemReader<Long> employeeIdReader() {
        logger.info("Initializing Employee ID Reader");
        List<Long> employeeIds = fetchEmployeeIds();
        logger.info("Employee IDs fetched for batch: {}", employeeIds);
        return new EmployeeIdReader(employeeIds);
    }

    private List<Long> fetchEmployeeIds() {
        logger.info("Fetching employee IDs from EmployeeClient");
        try {
            List<EmployeeDTO> employees = employeeClient.getAllEmployees();
            List<Long> employeeIds = employees.stream().map(EmployeeDTO::getId).toList();
            logger.info("Successfully fetched {} employee IDs", employeeIds.size());
            return employeeIds;
        } catch (Exception e) {
            logger.error("Error fetching employee IDs from EmployeeClient", e);
            return Collections.emptyList(); // Return empty list to prevent job failure, handle appropriately
        }
    }

    @Bean
    public ItemProcessor<Long, SalaryDTO> salaryProcessor() {
        logger.info("Initializing Salary Processor");
        return employeeId -> {
            logger.info("Processing salary for employee ID: {}", employeeId);
            EmployeeDTO employee = null;
            try {
                employee = employeeClient.getEmployeeById(employeeId);
                logger.debug("Retrieved employee data: {}", employee);
            } catch (Exception e) {
                logger.error("Error fetching employee data for ID: {}", employeeId, e);
                return null; // Skip processing this employee
            }

            if (employee == null) {
                logger.warn("Employee not found for ID: {}", employeeId);
                return null;
            }

            SalaryDTO salaryDTO = new SalaryDTO();
            salaryDTO.setEmployeeId(employee.getId());
            salaryDTO.setPaymentDate(LocalDate.now());
            salaryDTO.setBaseSalary(employee.getSalary());
            salaryDTO.setOvertimePay(0.0);
            salaryDTO.setBonuses(0.0);
            salaryDTO.setDeductions(0.0);
            salaryDTO.setNetSalary(salaryDTO.getBaseSalary() + salaryDTO.getOvertimePay()
                    + salaryDTO.getBonuses() - salaryDTO.getDeductions());

            logger.info("Processed salary for employee: {}", employeeId);
            logger.debug("Salary details: {}", salaryDTO);
            return salaryDTO;
        };
    }

    @Bean
    public ItemWriter<SalaryDTO> salaryWriter() {
        logger.info("Initializing Salary Writer");
        String filePath = "output/salaries.csv";

        // Create directory if it doesn't exist
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                logger.info("Created output directory: {}", outputDir.getAbsolutePath());
            } else {
                logger.error("Failed to create output directory: {}", outputDir.getAbsolutePath());
            }
        }

        return new FlatFileItemWriterBuilder<SalaryDTO>()
                .name("salaryWriter")
                .resource(new FileSystemResource(filePath))
                .append(false) // Overwrite the file on each run
                .delimited()
                .delimiter(",")
                .names(new String[]{"employeeId", "paymentDate", "baseSalary", "overtimePay", "bonuses", "deductions", "netSalary"})
                .headerCallback(writer -> writer.write("Employee ID,Payment Date,Base Salary,Overtime Pay,Bonuses,Deductions,Net Salary"))
                .build();
    }

    @Bean
    public Step salaryProcessingStep() {
        logger.info("Initializing Salary Processing Step");
        return new StepBuilder("salaryProcessingStep", jobRepository)
                .<Long, SalaryDTO>chunk(10, transactionManager)
                .reader(employeeIdReader())
                .processor(salaryProcessor())
                .writer(salaryWriter())
                .build();
    }

    @Bean
    public Job salaryBatchJob() {
        logger.info("Initializing Salary Batch Job");
        return new JobBuilder("salaryBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(salaryProcessingStep())
                .build();
    }
}