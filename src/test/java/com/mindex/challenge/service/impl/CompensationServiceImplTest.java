package com.mindex.challenge.service.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
	
    private String employeeUrl;
    private String compensationUrl;
    private String compensationGetUrl;
	
    @LocalServerPort
    private int port;
	    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationGetUrl = "http://localhost:" + port + "/compensation/{employeeId}";
    }
    
    @Test
    public void testCreateRead() {
    	Employee employeeTest = new Employee ("JB", "Bickerstaff", "Front Office", "Head Coach", null);
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, employeeTest, Employee.class).getBody();
        
        Compensation compensationTest = new Compensation(createdEmployee, new BigDecimal(500000.00), "2021-01-01");
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensationTest, Compensation.class).getBody();

        //test POST
        assertNotNull(createdCompensation.getEmployee());
        assertEmployeeEquivalence(compensationTest.getEmployee(), createdCompensation.getEmployee());

        String createdCompensationEmployeeId = createdCompensation.getEmployee().getEmployeeId();
        Compensation readCompensation = restTemplate.getForEntity(compensationGetUrl, Compensation.class, createdCompensationEmployeeId).getBody();
        
        //test GET
        assertNotNull(readCompensation);
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }
    
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

    // use the method from EmployeeServiceImplTest to verify equivalence between the 2 employees inside testCreateRead and assertCompensationEquivalence(
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
