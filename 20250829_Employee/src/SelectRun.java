import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectRun {

	public static void main(String[] args) {
		
		// 변수 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// sql 준비
		String sql = """
						SELECT
						       *
						  FROM
						       EMPLOYEE
						 ORDER
						    BY
						       EMP_ID
					 """;
		
		try {
			
			// 드라이버등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE", "CJ18", "CJ181234");
			
			// 편집기만들기
			stmt = conn.createStatement();
			
			// sql 보내고 결과받기
			rset = stmt.executeQuery(sql);
			
			// 받은거 출력하기
			/*
			 * EMP_ID int
			 * EMP_NAME
			 * EMP_NO
			 * EMAIL
			 * PHONE
			 * DEPT_CODE
			 * JOB_CODE
			 * SAL_LEVEL
			 * SALARY int
			 * BONUS double
			 * MANAGER_ID
			 * HIRE_DATE date
			 * ENT_DATE
			 * ENT_YN
			 * 
			 */
			while(rset.next()) {
				
				// 자료형 변수명 = rset.getXXX(컬럼명);
				int empId = rset.getInt("EMP_ID");
				String empName = rset.getString("EMP_NAME");
				String empNo = rset.getString("EMP_NO");
				String email = rset.getString("EMAIL");
				String phone = rset.getString("PHONE");
				String deptCode = rset.getString("DEPT_CODE");
				String jobCode = rset.getString("JOB_CODE");
				String salLevel = rset.getString("SAL_LEVEL");
				int salary = rset.getInt("SALARY");
				double bonus = rset.getDouble("BONUS");
				String managerId = rset.getString("MANAGER_ID");
				Date hireDate = rset.getDate("HIRE_DATE");
				System.out.println("사원번호 : " + empId
								 + ", 직원명 : " + empName
								 + ", 주민등록번호 : " + empNo
								 + ", 이메일 : " + email
								 + ", 전화번호 : " + phone
								 + ", 부서코드 : " + deptCode
								 + ", 직급코드 : " + jobCode
								 + ", 급여등급 : " + salLevel
								 + ", 급여 : " + salary
								 + ", 보너스율 : " + bonus
								 + ", 관리자사번 : " + managerId
								 + ", 입사일 : " + hireDate
								  );
				
			}
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 자원 반납하기 --> rset, stmt, conn 순서로
			try {
				
				if(rset != null && !rset.isClosed()) {
					rset.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
