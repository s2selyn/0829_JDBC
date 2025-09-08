package com.kh.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	// JDBC 절차와 관련된 중복된 일들 여기서 다 하고싶음
	// 메소드 스태틱!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	// 드라이버 등록 --> 메인메소드에서 한번만 하면 될건데 이것도?
	// 만들어놓기는 여기 만들고 호출만 메인에서 함
	
	// 프로젝트에 드라이버 등록해야함 --> 했음
	
	// 드라이버 등록 메소드 --> 만들어놓은거 메인메소드에서 호출하기 ㄱ
	public static void resistDriver() {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	// 커넥션 객체 생성
	public static Connection getConnection() {
		
		Properties prop = new Properties();
		
		try {
			
			// 커넥션 객체 반환해야함, 인자로 뭐받음? URL, ID, PW
			// 잉 아님 메소드가 받는게 아님 메소드 안에서 외부파일이랑 연결하고 읽어오는거임
			// 외부 파일 properties에서 읽어올건데, 이걸 위해서 따로 클래스 만들어서 파일 만들고(출력)
			// 만든거에서 URL, ID, PW 넣어서 수정하고
			// 수정한 파일 다시 읽어와서(입력)
			prop.load(new FileInputStream("resources/driver.properties"));
			
			// 안에 들어있는거 까서 여기 커넥션 호출할 때 집어넣음
			Connection conn = DriverManager.getConnection(prop.getProperty("URL")
														, prop.getProperty("USERNAME")
														, prop.getProperty("PASSWORD"));
			
			return conn;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	// pstmt는 DAO가 할거임
	
	// 트랜잭션 처리(commit, rollback) --> 커넥션 객체로 합니다
	public static void commit(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.commit();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void rollback(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 자원반납 + 예외처리(conn, stmt, rset)
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
