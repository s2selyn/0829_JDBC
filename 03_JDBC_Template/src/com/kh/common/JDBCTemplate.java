package com.kh.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTemplate {
	/*
	 * JDBC과정 중 반복적으로 쓰이는 구문들을 가각의 메소드로 정의해둘 클래스
	 * 중복된 코드들을 메소드로 분리하여 '재사용' --> 재사용과 관련된 것 있지 않았나요? static 키워드!
	 * 이 클래스의 모든 메소드는 전부 static으로 선언
	 * DAO에서 작업할때도 객체 생성 안하고 사용할 친구들은 forName, getConnection은 전부 스태틱으로 만들어져있었으니까 우리도 JDBC관련 메소드를 전부 스태틱으로 선언해서 사용해보자
	 * DAO에서 각 메소드가 SQL을 실행하는 부분을 제외하고 나머지 우리가 뺄 만한 부분들은 0부터 8중에?
	 * 커넥션 객체 생성하는 부분, 드라이버 등록, 예외처리(try, if, null체크)..
	 * 굳이 이렇게까지 하지는 않았지만 사실은 트랜잭션 처리에서도 꼭 해줘야하는데 여기서 해주면 안됨, 밖에서 문제가 생길수도 있고 하니까 무조건 할 수 있도록 finally에서 해줘야함, 근데 DAO에서 이작업을 하려고 하면 얘도 커밋을 하다가 예외가 발생할수있어서 try-catch로 묶어야하는데 얘도 널일수 있으니까 얘도 널체크 하면서 진행해야하는 코드였음..! 꼼꼼하게 해줘야하는 작업이었따!
	 * 이런것들도 명시적으로 트랜잭션 처리를 한다고 하면 if, try-catch 했어야하니까 얘도 따로 분리해주는게 좋겠다
	 * 아무튼 중복되는 코드들, 실제로 SQL실행해서 결과받고 이런건 DAO에서 해야하고 나머지 드라이버, 커넥션, 트랜잭션, 자원반납등의 중복되는 코드들은 여기로 빼자
	 * 
	 */
	
	// 1번부터 따로 빼자, 뭐해야함? 드라이버 등록
	// JDBC Driver를 등록하는 메소드
	// 프로그램 실행 중 단 한 번만 실행되면 됨
	public static void resistDriver() {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 딸깍 예외처리
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	// 커넥션 객체 반환받는 친구 구현하기, 얘도 스태틱으로 선언
	// 호출하면 커넥션 객체 줄거니까 반환형은 Connection, 메소드명은 getConnection(우리 썼던거, 좋은 예제였다)
	// DB의 연결정보를 가지고 있는 Connection객체를 생성해서 반환해주는 메소드
	public static Connection getConnection() {
		
		try {
			
			Connection conn = DriverManager.getConnection("jdbc:oracl:thin:@115.90.212.20:10000:XE"
														, "CJ18"
														, "CJ181234"); // 딸깍 예외처리
			
// 9:30 커밋 우리가 직접 한다고 하고
			conn.setAutoCommit(false);
			
			return conn; // 예외 발생하면 catch가 잡아가서 여기서 커넥션 객체 반환 못함
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null; // 정상적으로 못만들었다면 null값 돌려줌
		
	}
	
	// 트랜잭션 처리 메소드(COMMIT / ROLLBACK)
	
	// 커밋하려면 뭐가 필요함? 커넥션!
	// Connection객체를 이용해서 commit 시켜주는 메소드
	// 밖에서 부를거니까 퍼블릭, 객체 안만들거니까 스태틱 메소드, 딱히 반환해줄것은 없을듯
	public static void commit(Connection conn) { // 우리가 여기서 그냥 커밋할수는 없음, 커넥션이있어야함, 메소드 부를때 커넥션을 받아서 쓰고싶음 --> 매개변수 자리에 커넥션 타입
		
		try {
			
			if(conn != null) { // 널인지는 직접 체크해줘야하니까 널체크는 따로 해줌
				conn.commit();
			}
			
		} catch(SQLException e) { // 커밋하다가 SQLExecption 발생할 수 있음
			e.printStackTrace();
		}
		
	}
	
	// 커밋이랑 짝궁 롤백, 똑같이 만들면 되겠지, 메소드 식별자랑 메소드 호출정도 바뀔듯
	// Connection 객체를 이용해서 rollback시켜주는 메소드
	public static void rollback(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.rollback();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// DAO에서 많이 힘들었던 finally블록에서의 예외처리, 벅찼음
	// JDBC용 객체를 반납해주는 메소드 (각 객체별로) 따로 여기서 처리하자, 그거 할때마다 try-catch 했어야했음
	// DAO에서는 try-catch 안쓰고 메소드 호출만으로 해결할 수 있게 해주자
	// Connection 객체를 전달받아서 반납해주는 메소드
	// 밖에서 불러야하니까 접근제한자는 public, static으로 할거고 반환형은 void, 메소드명은 자원반납이니까 close
	// rollback이랑 다 똑같은데 호출하는 메소드만 close로 다름
	public static void close(Connection conn) {
		
		try {
			
			if(conn != null) {
				conn.close();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// Statement객체를 전달받아서 반납해주는 메소드
	// 똑같은 이름 됨! 오버로딩 할 수 있어서 --> 매개변수 타입이 다르기 때문에 메소드명 동일해도 됨, 정적 바인딩이 일어나기 때문에
	// PreparedStatement는 Statement의 자식클래스니까 메소드를 호출하면 부모타입으로 들어와서 똑같이 반납할 수 있음
	// => 다형성을 적용하여 PreparedStatement객체도 Statement타입으로 받을 수 있음(따로 만들 필요 없음)
	public static void close(Statement stmt) {
		
		try {
			
			if(stmt != null) {
				stmt.close();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 삼총사의 마지막 닫아줘야지 ResultSet
	// ResultSet 객체를 전달받아서 반납해주는 메소드
	public static void close(ResultSet rset) {
		
		try {
			
			if(rset != null) {
				rset.close();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// SQL문 실행하고 결과 받아오는 부분 제외하고 나머지 중복 코드들은 이 클래스를 만들어서 분리했음!
	// DAO에 중복코드 대격변 예상됨^_^
	
}
