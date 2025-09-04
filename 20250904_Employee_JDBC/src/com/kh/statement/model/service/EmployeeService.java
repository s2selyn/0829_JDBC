package com.kh.statement.model.service;

import java.sql.Connection;
import java.util.List;

import com.kh.common.JDBCTemplate;
import com.kh.statement.model.dao.EmployeeDAO;
import com.kh.statement.model.vo.Employee;

public class EmployeeService {
	
	// 얘는 컨트롤러랑 DAO 중간다리
	
	// DAO가 DB랑 SQL 보내고 결과받는거만 남기고 여기서 JDBC 관련 처리해줘야함
	// 템플릿 만들어서 관리하고싶음, 만들어놓고 와따
	// 그러니까 얘는 DAO랑 와따가따할때 커넥션 인자로 줘야함
	// 커넥션도 매번 만들지 말고 DAO 생성자에 넣으면 알아서 ? 아니지...
	// 서비스 생성자에 넣으면? 아 어디넣는거더라
	// 서비스 생성자에 넣는게 맞음, 근데 왜 여기 넣었는지 기억이 안남
	// 왜인지 찾아보고 왔음, 얘가 만들어질 때 자동으로 커넥션 생성하게 해주고싶은거였음
	// 그러려면 생성자에 넣으면 됨, 커넥션이 서비스 생성자 호출하면서 메소드 호출할것임
	// 필드에 넣어놨으니까 만들어짐? 아님 그냥 메소드 호출하려고 바로 생성해서 부름
	// 컨트롤러에 필드를 안두고 서비스 객체 생성(생성자 호출) 하면서 ~ 생각 정리하다가 엿듣게 된것같은데 다른사람이 비슷한거 질문한듯
	// 커넥션은 매번 새로 만들었다가 닫아야함, 수업시간에 한 대로 ㄱ
	
	// 커넥션 필드로 두고요, 생성자 안에 넣는겁니다
	private Connection conn = null;
	
	public EmployeeService() {
		this.conn = JDBCTemplate.getConnection();
	}
	
	// 컨트롤러가 불렀으니까 DAO한테 받은거 돌려줘야함
	// 전체조회 메소드
	public List<Employee> findAll() {
		
		// 다른 인자 넘겨줄 필요 없고 커넥션만 넘겨주면 다긁어옴
		List<Employee> employees = new EmployeeDAO().findAll(conn);
		// 왜 DAO에서 리스트가 오는지 생각안남, 걔가 받은 결과는 rset일텐데 가공해서주나? ㅇㅇ 그래야함
		// 진짜 그래야함? ㅠㅠㅠㅠ
		
		// DAO에서 돌아오면 리스트 받아옴, 이걸로 뭐 해야할 일이 있나?
		// 실행해봤는데 잘됨 아직은 딱히 없는듯
		
		// 일단 커넥션은 여기서 닫아줘야함
		JDBCTemplate.close(conn);
		
		return employees;
		
	}
	
	// 부서 키워드로 검색하는 메소드
	public List<Employee> findByDept(String deptKeyword) {
		
		// DAO한테 가야하고, 얘는 커넥션 넘겨주면서 키워드도 같이 넘겨줌
		List<Employee> employees = new EmployeeDAO().findByDept(conn, deptKeyword);
		// 돌아오는 rset은 DAO가 가공해줄것임
		
		// 커넥션 닫아주고
		JDBCTemplate.close(conn);
		
		// 반환시키기
		return employees;
		
	}
	
	// 직급명으로 검색
	public List<Employee> findByJob(String jobKeyword) {
		
		// DAO한테 가야하고, 커넥션 넘겨주면서 키워드 넘겨주기
		List<Employee> employees = new EmployeeDAO().findByJob(conn, jobKeyword);
		// 돌아오는 것은 DAO가 rset을 가공해서 리스트에 담은것
		
		// 커넥션 닫기
		JDBCTemplate.close(conn);
		
		// 결과 반환
		return employees;
		
	}

}
