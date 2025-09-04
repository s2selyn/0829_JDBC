package com.kh.statement.model.vo;

import java.util.Objects;

public class Employee {
	
	// 사원, 사원명, 급여, 부서명, 직급명
	// 사번 입력만 받을거니까 필드 필요없음
	// 퇴사여부, 퇴사일은 업데이트만 할거임 필드 필요없음
	private String empId;
	private String empName;
	private int salary;
	private String deptTitle;
	private String jobName;
	
	// 생성자
	public Employee() {}

	public Employee(String empId, String empName, int salary, String deptTitle, String jobName) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.salary = salary;
		this.deptTitle = deptTitle;
		this.jobName = jobName;
	}

	// 메소드(getter만, toString, hashCode, equals)
	public String getEmpId() {
		return empId;
	}

	public String getEmpName() {
		return empName;
	}

	public int getSalary() {
		return salary;
	}

	public String getDeptTitle() {
		return deptTitle;
	}

	public String getJobName() {
		return jobName;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", salary=" + salary + ", deptTitle=" + deptTitle
				+ ", jobName=" + jobName + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(deptTitle, empId, empName, jobName, salary);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(deptTitle, other.deptTitle) && Objects.equals(empId, other.empId)
				&& Objects.equals(empName, other.empName) && Objects.equals(jobName, other.jobName)
				&& salary == other.salary;
	}
	
}
