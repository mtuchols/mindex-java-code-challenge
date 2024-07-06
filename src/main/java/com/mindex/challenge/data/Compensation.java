package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Compensation {

	private Employee employee;
	private BigDecimal salary;
	private String effectiveDate;
	
	public Compensation(Employee employee, BigDecimal salary, String effectiveDate ) {
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
	}

	public void setEmployee (Employee employee) {
		this.employee = employee;
	}
	
	public void setSalary (BigDecimal salary) {
		this.salary = salary;
	}
	
	public void setEffectiveDate (String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Employee getEmployee () {
		return employee;
	}

	public BigDecimal getSalary () {
		return salary;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}
}
