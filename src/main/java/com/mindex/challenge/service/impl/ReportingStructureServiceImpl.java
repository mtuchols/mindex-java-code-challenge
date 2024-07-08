package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	@Override
	public ReportingStructure read(String employeeId) {
        LOG.debug("Creating ReportingStructure for employeeId [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
		if (employee.getDirectReports() != null) {
			List <Employee> fullyPopulatedEmployeeList = new ArrayList<>();
			for (Employee directReportEmployee : employee.getDirectReports()) {
				// TODO : Is there a better way to  fill in this data instead of querying for every direct report?
				fullyPopulatedEmployeeList.add(employeeService.read(directReportEmployee.getEmployeeId()));
			}
			employee.setDirectReports(fullyPopulatedEmployeeList);
		}
        if (employee == null) {
            throw new RuntimeException("Error while attempting to find employee with employeeId: " + employeeId);
        }

        int reports = getReportsForEmployee(employee);
        LOG.debug("reports amount[{}]", reports);

        return new ReportingStructure(employee, reports);
	}

	/**
	 * Sums up all current subordinates for an employee, with their subordinates being added recursively
	 *  @param employee who's subordinates should be summed
	 *  @return the total subordinates for a given employee
	 */
	private int getReportsForEmployee (Employee employee){
        int reports = 0;
		if (employee == null) {
			LOG.info("Employee is null, returning 0 for safety.");
			return 0;
		} else if (employee.getDirectReports() == null) {
			LOG.info("DirectReports is null, returning 0 for safety.");
			return 0;
		}

        for (Employee subordinate : employee.getDirectReports() ) {
        	subordinate = employeeRepository.findByEmployeeId(subordinate.getEmployeeId());
        	if (subordinate == null) {
				LOG.debug("Subordinate was not found. If the employee was expected to be found, check to make sure employee id is not null.");
				break;
        	} else {
                reports += 1; // add direct report
                reports += getReportsForEmployee(subordinate); // add the direct report's subordinates recursively
        	}
        }
        return reports;
	}
}
