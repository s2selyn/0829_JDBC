package com.kh.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTemplate {
	
	// 중복되는 작업 빼야함
	// 드라이버 등록, 커넥션 만들기, 예외처리, 클로즈? + 트랜잭션(커밋, 롤백)
	// 메소드 오버로드도 해야함!
	
	// 스태틱으로 선언해야함
	
	// 드라이버등록
	public static void resistDriver() {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	// 커넥션 만들기
	// 여기 흐름이 이해안됨
	// 객체를 만들어서 리턴, 객체를 못만들면 널, 캐치블럭 들어가면?
	public static Connection getConnection() {
		
		try {
			
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@115:90:212:20:10000:XE", "CJ18", "CJ181234");
			return conn;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	// 조건이 있나? 아.. 커밋하려면 커넥션 있어야함
	public static void commit(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.commit();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 그럼 롤백도 커넥션이 있어야겠지
	public static void rollback(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 자원반납+예외처리 세개 해야함 : conn, stmt(pstmt는 자식이니까 안함), rset
	public static void close(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(Statement stmt) {
		
		try {
			
			if(stmt != null) {
				stmt.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(ResultSet rset) {
		
		try {
			
			if(rset != null) {
				rset.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
