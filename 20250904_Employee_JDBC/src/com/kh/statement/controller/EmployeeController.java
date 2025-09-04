package com.kh.statement.controller;

import java.util.List;

import com.kh.statement.model.service.EmployeeService;
import com.kh.statement.model.vo.Employee;

public class EmployeeController {
	
	// 뷰가 메소드로 날 부를것임, 나는 또 서비스한테 할거 떠넘길것
	// 여기서 할 일은 흐름제어
	
	// findAll 메소드
	// 서비스한테 갔다오면 리스트 반환받을거임 --> 그거 돌려줘야함
	// ResultSet은 DAO가 DB한테 받겠지, 그거 까서 서비스가 나한테는 리스트로 가공해서 줄거임
	// 까는건 DAO가 까고, 서비스는 이미 DAO한테서 까서 가공된 리스트를 받음, 그 리스트를 컨트롤러한테 줄거임
	public List<Employee> findAll() {
		
		List<Employee> employees = new EmployeeService().findAll();
		// 여기도 서비스 갔다오면 리스트가 돌아옴
		// 해야 할 일은 모르겠고 일단 부른곳으로 들려보내서 돌아감
		return employees;
		
	}
	
	// findByDept 메소드
	// 뷰가 불러서 왔음, 부서 키워드 받아야함
	// 나중에 서비스에서 받은거 돌려줘야함, 여러개가 올수도 있음 --> 리스트로 올것임(비어있을수도)
	public List<Employee> findByDept(String deptKeyword) {
		
		// 얘도 보낼 때 키워드 보내줘야해
		List<Employee> employees = new EmployeeService().findByDept(deptKeyword);
		// 결과는 리스트로 돌아올것이고
		
		// 돌아온 리스트를 나를 부른 뷰에게 다시 돌려줘야함
		return employees;
		
	}
	
	// 직급명으로 찾기, 매개변수 있음, 리스트로 돌려줄거임
	public List<Employee> findByJob(String jobKeyword) {
		
		// 키워드 보내줘야하고
		List<Employee> employees = new EmployeeService().findByJob(jobKeyword);
		// 결과는 리스트로 오고
		
		// 리스트를 뷰에게 돌려줌
		return employees;
	}

}
