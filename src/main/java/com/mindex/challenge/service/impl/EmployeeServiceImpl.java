package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import jdk.internal.org.jline.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);
        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Finding employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee.getDirectReports() != null) {
            List <Employee> fullyPopulatedEmployeeList = new ArrayList<>();
            for (Employee directReportEmployee : employee.getDirectReports()) {
                // TODO : Is there a better way to  fill in this data instead of querying for every direct report?
                fullyPopulatedEmployeeList.add(read(directReportEmployee.getEmployeeId()));
            }
            employee.setDirectReports(fullyPopulatedEmployeeList);
        }
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);
        return employeeRepository.save(employee);
    }
}
