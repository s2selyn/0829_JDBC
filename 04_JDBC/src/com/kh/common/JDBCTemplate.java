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

	public static void resistDriver() {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection() {
		
		// driver.properties에서 작성한 내용을 사용해보자
		Properties prop = new Properties(); // Properties 객체 생성
		
		try {
		
			// load 메소드 호출하면서 인풋 스트림 넣어서 외부에 있는 것을 입력받을것, 만들어둔 파일에서 입력받을것임
			prop.load(new FileInputStream("resources/driver.properties")); // 예외처리 추가
			// 작성해둔 키밸류를 properties 객체로 받아옴
			
			// value를 얻으려면 key가 있어야함, getProperty 해서 key값을 전달함
			// String keyA = prop.getProperty("A");
			// System.out.println("A 키값의 Value : " + keyA);
			// 키를 넣으니까 밸류를 준다, 키값으로 URL을 넣으면 우리가 넣어둔 밸류가 나옴
			// System.out.println(prop.getProperty("URL"));
			// getProperty 반환타입이 String, 우리가 필요한것도 String이니까 대체 가능
			Connection conn = DriverManager.getConnection(prop.getProperty("URL")
														, prop.getProperty("USERNAME")
														, prop.getProperty("PASSWORD"));
// 10:28 원래는 코드상에 하드코딩 되어있던 문자열을 외부에서 파일로 가져와서??
			// 동작은 똑같이 돈다, 차이없음
			// 값을 읽어오는 시점이 바뀌었음, 서비스의 생성자를 호출할 때마다 데이터를 읽어옴
			// 원래라면 프로그램 시작했을 때 클래스 파일에 적혀있는 값을 읽음(리터럴값이 바이트코드에 찍혀있음) --> 얘(값 세개)를 메모리에 올려서 작업함
			// 바꾸고 나면 바이트 코드상에 이 값이 없음, 키값밖에 없음
			// 여기 들어가는 값은 파일인풋스트림하는 시점에 생김, 이 시점은 멤버서비스 생성자 호출할때가 그 시점
			// 잘 돌다가 DB서버 IP가 바뀌었다면 원래라면 코드 수정하고 다시 컴파일해서 메모리에 올려야함
			// 이제 바꾸고 나면 다 껐다켜야할것을 서비스 읽어오는 시점에 다시 읽어오는거라서 프로그램을 안 껐다켜도 동작을 함
			// 사용자입장에서도 생각, 프로그램 잘 이용하다가 교체해야해서 서버 껐다켠다고 하면 불편함, 이런거 바뀔때마다 나가라고 하면 불편함
			// 하드코딩해놓은 내용을 동적으로 빼놓으면 프로그램을 유연하게 동작시킬 수 있다
			// 얘만 동적으로 뺄 건 아님
			
			// 동적으로 빼고 싶은 친구들이 DAO에 수두룩빽빽하게 많음 --> SQL문들도 다 문자열값, 문자열 형태로 여기에 넣기만 하면 끝인데
			// 정렬기준 바꾸는건 하루이틀일이아니고(엄청 자주 일어나겠지)
			// DB접속 정보처럼 빼러감 --> DAO ㄱ
			
			conn.setAutoCommit(false);
			
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
	
	public static void commit(Connection conn) { // 우리가 여기서 그냥 커밋할수는 없음, 커넥션이있어야함, 메소드 부를때 커넥션을 받아서 쓰고싶음 --> 매개변수 자리에 커넥션 타입
		
		try {
			
			if(conn != null) {
				conn.commit();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void rollback(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.rollback();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.close();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(Statement stmt) {
		
		try {
			
			if(stmt != null) {
				stmt.close();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(ResultSet rset) {
		
		try {
			
			if(rset != null) {
				rset.close();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
