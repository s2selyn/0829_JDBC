import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class InsertRun {

	public static void main(String[] args) {
		
		// 0) 변수
		Connection conn = null;
		Statement stmt = null;
		int result = 0;
		
		Scanner sc = new Scanner(System.in);
		
		// Employee 테이블에 머머 넣어야함?
		/*
		 * EMP_ID
		 * EMP_NAME
		 * EMP_NO
		 * EMAIL
		 * PHONE
		 * DEPT_CODE
		 * JOB_CODE
		 * SAL_LEVEL
		 * SALARY
		 * BONUS
		 * MANAGER_ID
		 * HIRE_DATE 이건 SYSDATE쓰고
		 * ENT_DATE 이건 받지말고
		 * ENT_YN 이것도 받지말고
		 * 
		 */
		
		System.out.print("사원번호 입력 > ");
		int empId = sc.nextInt();
		sc.nextLine();
		
		System.out.print("직원명 입력 > ");
		String empName = sc.nextLine();
		
		System.out.print("주민등록번호 입력 > ");
		String empNo = sc.nextLine();
		
		System.out.print("이메일 입력 > ");
		String email = sc.nextLine();
		
		System.out.print("전화번호 입력 > ");
		String phone = sc.nextLine();
		
		System.out.print("부서코드 입력 > ");
		String deptCode = sc.nextLine();
		
		System.out.print("직급코드 입력 > ");
		String jobCode = sc.nextLine();
		
		System.out.print("급여등급 입력 > ");
		String salLevel = sc.nextLine();
		
		System.out.print("급여 입력 > ");
		int salary = sc.nextInt();
		sc.nextLine();
		
		System.out.print("보너스율 입력 > ");
		double bonus = sc.nextDouble();
		sc.nextLine();
		
		System.out.print("관리자사번 입력 > ");
		String managerId = sc.nextLine();
		
		// 입력받은걸로 보낼 SQL문
		String sql = "INSERT "
					 + "INTO "
					 	   + "EMPLOYEE "
				   + "VALUES "
					 	   + "("
					  + "'" + empId + "'"
					+ ", '" + empName + "'"
					+ ", '" + empNo + "'"
					+ ", '" + email + "'"
					+ ", '" + phone + "'"
					+ ", '" + deptCode + "'"
					+ ", '" + jobCode + "'"
					+ ", '" + salLevel + "'"
					+ ", " + salary
					+ ", " + bonus
					+ ", '" + managerId
					+ "', SYSDATE, null, null)";
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE", "CJ18", "CJ181234");
			
			conn.setAutoCommit(false);
			
			stmt = conn.createStatement();
			
			result = stmt.executeUpdate(sql);
			
			if(result > 0) {
				conn.commit();
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
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
