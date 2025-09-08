package com.kh.statement.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kh.common.JDBCTemplate;
import com.kh.statement.model.vo.Employee;

public class EmployeeDAO {
	
	public List<Employee> findAll(SqlSession session) {
		
		return session.selectList("employeeMapper.findAll");
		
	}
	
	// 서비스가 보내준 커넥션이랑 부서 키워드 받아서 DB갔다와야함
	public List<Employee> findByDept(Connection conn, String deptKeyword) {
		
		// 0 변수 세팅~
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// prop 이용해서 받아와야함, SQL문 빼놓는 작업 ㄱ
		String sql = prop.getProperty("findByDept");
		List<Employee> employees = new ArrayList();
		
		try {
			
			// pstmt 바인딩? ㅇㅇ
			// 아니야 객체생성 먼저 해서 미리 만든 구멍난 SQL 전달하고
			// 그 다음에 바인딩 하는거임
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, deptKeyword);
			
			// 바인딩 끝났으면 실행, 인자 전달 XXXXXXXXXXXXXXXX
			rset = pstmt.executeQuery();
			// 결과는 rset으로 돌아옴
			
			// rset 까서 employee객체에 담고
			// rset에 들었는지는 isEmpty로 확인? ㅋㅋ ㄴㄴ 커서 내려야함
			
			// 헉 if 말고 있는동안 계속 반복해야함, while로 반복시킴
			while(rset.next()) {
				
				// 들어오면 행이 있다는 뜻
				Employee employee = new Employee(rset.getString("EMP_ID")
											   , rset.getString("EMP_NAME")
											   , rset.getInt("SALARY")
											   , rset.getString("DEPT_TITLE")
											   , rset.getString("JOB_NAME"));
				
				// 여러개면 객체가 여러개일거니까 그걸 리스트로 만들어서
				employees.add(employee);
				
				// 리스트 하나를 리턴... 어디서? 여기서? 아니넹 ㅎ
				// 여기서 리턴하면 한번만 반복하고 리턴해버리지않을까요 고객님
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 자원반납은 약속입니다
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return employees;
		
	}
	
	// 직급명으로 탐색, DB 갔다오기, 매개변수 두개있음
	public List<Employee> findByJob(Connection conn, String jobKeyword) {
		
		// 해야 할 일
		
		// 변수 세팅
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 반환할 결과 담을 객체 미리 준비하는거 그만까먹어라~~~~~~~~~~~
		List<Employee> employees = new ArrayList();
		
		// sql 준비 --> prop 이용해서 가져옴
		String sql = prop.getProperty("findByJob");
		
		try {
			
			// pstmt 객체생성 이것도 커넥션 객체로 하는거란다... 그만까먹자
			pstmt = conn.prepareStatement(sql);
			
			// pstmt 객체 이용해서 SQL문 바인딩
			pstmt.setString(1, jobKeyword);
			
			// SQL 실행
			rset = pstmt.executeQuery();
			// 실행한 결과 받기
			
			// 가공
			// rset이 비어있거나 뭐가 있거나.. 아 이거 아니고요 커서 움직이라고 커서엇ㅇ서ㅏㅣㅇ너ㅣ사머이;섬ㄴ;ㅅ이ㅏㅓㅁㅇ
			while(rset.next()) {
				
				Employee employee = new Employee(rset.getString("EMP_ID")
											   , rset.getString("EMP_NAME")
											   , rset.getInt("SALARY")
											   , rset.getString("DEPT_TITLE")
											   , rset.getString("JOB_NAME"));
				
				employees.add(employee);
				
			}
			
			// 반환도 제발 밖에서 하지 않을래?
			// 만약에 여기서 반환하면 어케됨? 마지막에 return null 써야하는 귀찮음이 생김
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return employees;
		
	}
	
	// 사번으로 찾기 메소드
	// 서비스에서 커넥션과 키워드를 준다, 매개변수로 받아야함, 나중에 조회된 결과 돌려줘야함
	public Employee findById(Connection conn, int idKeyword) {
		
		// 필요한 변수 세팅 : pstmt, rset, employee
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Employee employee = null;
		
		// SQL 준비하는거 까먹음 ㅎ
		String sql = prop.getProperty("findById");
		
		try {
			
			// 커넥션으로 pstmt 생성
			pstmt = conn.prepareStatement(sql);
			
			// SQL 실행하고 결과 받기
			rset = pstmt.executeQuery();
			
			// 받아온 결과 가공
			while(rset.next()) {
			
			employee = new Employee(rset.getString("EMP_ID")
								  , rset.getString("EMP_NAME")
								  , rset.getInt("SALARY")
								  , rset.getString("DEPT_TITLE")
								  , rset.getString("JOB_NAME"));
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 반환
		return employee;
		
	}

}
