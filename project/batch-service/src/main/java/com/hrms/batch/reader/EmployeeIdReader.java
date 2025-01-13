package com.hrms.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;
import java.util.Iterator;

public class EmployeeIdReader implements ItemReader<Long> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeIdReader.class);

    private final Iterator<Long> employeeIdIterator;

    public EmployeeIdReader(List<Long> employeeIds) {
        this.employeeIdIterator = employeeIds.iterator();
        logger.info("EmployeeIdReader initialized with {} employee IDs", employeeIds.size());
    }

    @Override
    public Long read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (employeeIdIterator.hasNext()) {
            Long employeeId = employeeIdIterator.next();
            logger.debug("Reading employee ID: {}", employeeId);
            return employeeId;
        } else {
            logger.info("No more employee IDs to read.");
            return null;
        }
    }
}