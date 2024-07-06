package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

	private String reportingStructureURL;
    private String employeeUrl;

	@Autowired
	ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureURL = "http://localhost:" + port + "/reportingstructure/{id}";
    	employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testRead() {
        List<Employee> directReportsList = new ArrayList<Employee>();
        Employee employeeWithReports = new Employee ("JB", "Bickerstaff", "Front Office", "Head Coach", null);
        Employee report1 =  new Employee ("Donovan", "Mitchell", "Team", "Shooting Guard", null);
        Employee report2 =  new Employee ("Mike", "Tucholski", "Arena Crew", "Food Vendor", null);

        Employee createdReporter1 = restTemplate.postForEntity(employeeUrl, report1, Employee.class).getBody();
        Employee createdReporter2 = restTemplate.postForEntity(employeeUrl, report2, Employee.class).getBody();

        directReportsList.add(createdReporter1);
        directReportsList.add(createdReporter2);
        employeeWithReports.setDirectReports(directReportsList);
        ReportingStructure testRS = new ReportingStructure(employeeWithReports, 2);

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, employeeWithReports, Employee.class).getBody();
        ReportingStructure readRS = restTemplate.getForEntity(reportingStructureURL, ReportingStructure.class,  createdEmployee.getEmployeeId()).getBody();

        assertReportingStructureEquivalence(testRS, readRS);
    }

    @Test
    /**
     * This test verifies the recursive (tree branch structure) functionality of the getReportsForEmployee() method
     */
    public void testMultipleLevelsOfReports () {
        List<Employee> directReportsList = new ArrayList<Employee>();
        Employee employee = new Employee ("JB", "Bickerstaff", "Front Office", "Head Coach", null);
        Employee report1 =  new Employee ("Donovan", "Mitchell", "Team", "Shooting Guard", null);
        Employee report2 =  new Employee ("Mike", "Tucholski", "Arena Crew", "Food Vendor", null);

        Employee createdReport2 = restTemplate.postForEntity(employeeUrl, report2, Employee.class).getBody();

        //create the 'second tree branch' of the hierarchy of reports
        List<Employee> listWithSingleReport = new ArrayList<Employee>();
        listWithSingleReport.add(createdReport2);
        report1.setDirectReports(listWithSingleReport);
        Employee createdReport1 = restTemplate.postForEntity(employeeUrl, report1, Employee.class).getBody();

        //create base of branch, which has another branch extending from it
        List<Employee> listForEmployee = new ArrayList<Employee>();
        listForEmployee.add(createdReport1);
        employee.setDirectReports(listForEmployee);

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();
        ReportingStructure reportingStructureRead = restTemplate.getForEntity(reportingStructureURL, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();

        assertEquals(reportingStructureRead.getNumberOfReports(), 2);
    }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertNotNull(actual);
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }

    // use the method from EmployeeServiceImplTest to verify equivalence between the 2 employees inside assertReportingStructureEquivalence()
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
