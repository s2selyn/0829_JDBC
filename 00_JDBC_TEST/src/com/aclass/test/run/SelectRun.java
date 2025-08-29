package com.aclass.test.run;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectRun {

	public static void main(String[] args) {
		
		// 현재 작업중인 DB서버에서 각자 자신이 가지고있는 TB_STUDENT테이블의 모든 행 조회 Console에 출력
		// 조회해서 출력할거니까, SQL문은 SELECT문 써야지, SELECT 보내면 결과가 ResultSet으로 옴, JDBC에도 ResultSet이라는 인터페이스로 구현되어있음
		// SELECT문
		// ResultSet(조회된 행들의 집합)
		// => ResultSet타입의 데이터를 받아서 뽑기
		
		// 필요한 변수들 먼저 선언하자, 나중에 자원반납해야하는데 try블럭 안에서 선언하면 그안에서밖에 사용못하니까
		// 0) 필요한 변수들 먼저 세팅
		// DB에서 작업하려면 접속을 해야함
		// JDBC 접속 담당 친구는 Connection
		Connection conn = null;
		
		// 접속한 다음에는 디비버에서와 마찬가지로 편집기 열고 써서 실행해야함, 편집기 역할은 Statement
		Statement stmt = null;
		
		// 이번에 보낼 쿼리문은 SQL 중에서 SELECT, 반환결과는 ResultSet으로 오는데 자바에서 이걸 받으려면 ResultSet 타입을 써야함
		ResultSet rset = null;
		
// 14:10 SELECT문 쓸때랑 준비물이 다름
		
		// 0단계 끝!
		
		// 추가적으로 준비
		// 요청할 SQL문도 미리 완성형태로 준비해두기, 같이 만들어봅시다 --> 디비버에서 먼저 쓰러감
		// 그냥 막쓸수없음, 이쁘게쓰고싶음, 한줄로 쓸거 아니잖아, 근데 소소하게 신경쓸게 너무많음, 굉장히 힘들다, 이럴때 뭘 쓰면 좋음? 자바에서 배운거
		// 두개나 배웠는데 뭐있음?
		// 1. 텍스트 파일로 빼서 텍스트 파일에서 입력받아서 쓰기
		// 2. 텍스트 블록(""" 이렇게 쓰는거 """)
		String sql = """
						SELECT
						       STUDENT_ID
						     , STUDENT_NAME
						     , ENROLL_DATE
						  FROM
						       TB_STUDENT
						 ORDER
						    BY
						       ENROLL_DATE DESC
					 """;
		// 여기까지 하면 준비 끝
		
		// 본격적으로 JDBC 절차 시작해보자
		// 1. JDBC Driver등록
		try {
			
			// Class라는 클래스의 forName 메소드 호출
			// 자바의 리플렉션이라는 기능을 이용하는 것
			// 리플렉션을 통한 드라이버 클래스 로딩
			// OracleDriver 그냥 import 해서 쓰면 안되나? 왜 이렇게 해야함?
			// 실행시점에 필요한 데이터베이스 드라이버를 동적으로 로드
			// 코드의 변경 없이 데이터베이스를 연결할 수 있도록
			// DB바꿀때마다 코드바꿔야되면 귀찮음
			Class.forName("oracle.jdbc.driver.OracleDriver");
// 14:30 이걸 깜빡하고 안넣어놨을 수 있으니 ClassNotFoundException 발생할 수 있으니 이라인을 예외처리 해줘야함
			
			// ojdbc가 있기 때문에 오라클 서버에 연결(접속)할수있음
			// 이건 JDBC에서 Connection이라는 객체가 담당함
			// 2. Connection 객체 생성
			// DriverManager 클래스의 getConnection 메소드를 호출해서 생성함
			// DB서버에 연결하고 싶으면 필요한 정보가 있음(네개)
			// 여기서 커넥션 만들어낼때도 마찬가지, getConnection의 인자로 싹다 전달해줘야함
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE"
											 , "CJ18"
											 , "CJ181234");
			// jdbc:oracle:thin: 이건 고정, 어떤 DBMS, 버전 등에 따라 달라짐
			// @ 적고 ip주소
			// : 적은 다음 포트번호
			// : 적은 다음에 ORCL이냐 XE냐 두개가 있으니 이거 적어줌
			// 사용자 이름과 비밀번호가 있어야함
			// 이 안에서 문제가 발생할 여지가 많이 있음, URL, IP, PORT, 서버문제(컴터꺼져있거나), 계정명, 비밀번호
			// 컴파일러가 보기에도 뭔가 100% 예외가 발생할 수 있는데? 그냥은 넘어갈 수 없는데? 하고 알려줌 --> 예외처리(catch 블록 추가)
			// 이 정보로 연결하면 커넥션 객체가 반환되므로 conn = 추가해서 받아줌
			
			System.out.println("DB서버 접속 성공!");
			
			// 실질적으로 SQL문 실행해서 결과 받아주는
			// SQL 편집기의 역할을 하는 다재다능한 Statement
			// 3. Statement 객체 생성
			// 이건 커넥션 객체가지고 만듦, 이 접속정보를 가지고 열어야하니까
			stmt = conn.createStatement();
			// 이걸 만들면 statement 객체의 주소값을 반환해줌, 이거 받아야하니까 Statement타입의 변수로 받아야함, 이건 0번에서 선언해놨음
			// 여기까지 하면 편집기 연거랑 똑같음
			
			// 편집기 열었으니까 머함? SQL문은 아까만들어놨음
			// 4. SQL문을 실행 후 결과 받기
			// Statement는 다재다능해서 실행하면서 결과도 한번에 받아옴
			// 자바니까 방법이 뭐밖에 없음? 메소드호출하는거지, 객체 참조하고 execute
// 14:47 권장하지 않는게 있음!!!!
			// 실행할 SQL문이 SELECT문인경우
			// stmt.executeQuery(sql) : ResultSet
			rset = stmt.executeQuery(sql);
			// excuteUpdate는 결과가 정수로 와서 SELECT문의 경우에는 못씀
			// 결과가 돌아오고 반환타입이 ResultSet이니까 변수로 받아주는 부분 작성(위에 선언해놨음)
			// 이 결과츨 출력해줄건데 아마도 ..
			// System.out.println(rset); // oracle.jdbc.driver.ForwardOnlyResultSet@6ffab045 --> ResultSet 객체의 주소
			// 이걸 보고싶은건 아닌데용, 실제로 우리가 출력하고 싶은 데이터는 총 12개의 값
			// ResultSet, 행을 찍고 싶은게 아니라 각 셀에 들어있는 값을 하나하나 출력하고 싶은것, 총 출력해야하는 값은 12개
			// 문제는? 지금은 12개지만, 12개가 아닐수도 있음, 데이터의 양에 따라서 다르지만 전체 출력이니까 가지고 있는 만큼 다 해줘야함
			// 데이터가 몇개가 있을지는 모르겠지만 존재하는 데이터는 전부 출력해줄것임
			
			/*
			// ResultSet이 오면 이걸 행단위로 접근할 수 있음, ResultSet에 커서라는 개념이 있음
			// 처음에 커서는 위에 달라붙어있음
			
			// ResultSet 반환됨
			// 커서를 조작
			// ResultSet의 메소드를 호출해서 함
			rset.next(); // 처음에 맨위에 붙어있던게 이동해서 첫번째 행을 가리키게 됨
			// 한 행에서 세개의 값을 뽑아야함(세개의 컬럼에서 각각 하나하나씩 뽑아서)
			// 값을 갖고싶으면? get, ResultSet의 get에는 자료형이 붙어있음
			// STUDENT_ID를 뽑으면 자바에서는 정수로 다뤄야함, get을 쓰고 내가 쓰고싶은 자료형을 써줌
			// 어떤 컬럼에서 뽑을건지 메소드의 인자값으로 컬럼명을 적어둠(정수 자료형을 쓰는 컬럼이 여러개일수있으니까)
			int studentId = rset.getInt("STUDENT_ID");
			// get 해서 int형으로 가져올거니까 변수로 받아서 쓸거면 int형 변수로 받아야함
			
			// 두번째 셀에 들어있는 값을 다루려면 문자열이 필요함
			// String으로 뽑고싶으니까 getString, String이 여러개 있을 수 있으니 컬럼명을 적어줘야함, 메소드의 인자로 전달
			String studentName = rset.getString("STUDENT_NAME");
			// 자료형과 변수를 선언해서 받아줌
			
			// 세번째 셀에 들어있는건 오라클의 DATE타입
			// 얘를 자바에서는 어떤 타입으로 다뤄야되나 고민해보면 애매함
			// tochar로 받아서 포매팅해서 쓰는 방법이 있지만 안써본 방법 써보자
			// 똑같이 get 할건데 날짜 타입이니까 getDate
			Date enrollDate = rset.getDate("ENROLL_DATE"); // import하라고 하는데 뭘로 해야함? java.sql.Date
			
			System.out.println("번호 : " + studentId + ", 이름 : " + studentName + ", 등록일 : " + enrollDate);
			// 이렇게 해서 한 행 조회한것
			// 첫번째 행에 대한 모든 것들을 get 해서 출력해줬음
			
			// 그 다음 행을 출력하고 싶으면? 커서를 또 한 칸 내려야함(다음행으로), 그러고 또 반복해야겠지
			// 변수명은 첫번째만 선언하고 나머지는 대입해서 돌리면 되겠지
			rset.next();
			studentId = rset.getInt("STUDENT_ID");
			studentName = rset.getString("STUDENT_NAME");
			enrollDate = rset.getDate("ENROLL_DATE");
			System.out.println("번호 : " + studentId + ", 이름 : " + studentName + ", 등록일 : " + enrollDate);
			
			rset.next();
			studentId = rset.getInt("STUDENT_ID");
			studentName = rset.getString("STUDENT_NAME");
			enrollDate = rset.getDate("ENROLL_DATE");
			System.out.println("번호 : " + studentId + ", 이름 : " + studentName + ", 등록일 : " + enrollDate);
			*/
			// 지금은 이렇게 쓸 수 있지만 나중에 없는 행이 나오면? 없는디? 너 벌써 다 뽑았는디? 없는디? 예외발생
			// 몇번이나 반복해야하는지 특정지을 수 없음
			// 조회된 행 개수만큼 출력을 해줄거란말이지 --> 반복문 필요
			// 반복횟수를 정확히 모름 --> while
			while(rset.next()) { // next 메소드는 반환타입이 boolean, true/false로 온다
				// rset.next() --> 커서를 조작했을 때 행이 존재한다면 true / 행이 존재하지 않는다 false
				
				int studentId = rset.getInt("STUDENT_ID");
				String studentName = rset.getString("STUDENT_NAME");
				Date enrollDate = rset.getDate("ENROLL_DATE");
				System.out.println("번호 : " + studentId + ", 이름 : " + studentName + ", 등록일 : " + enrollDate);
				
			}
			
		} catch(ClassNotFoundException e) {
			
			System.out.println("ojdbc추가했나요?");
			System.out.println("oracle.jdbc.driver.OracleDriver 오타없나요?");
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			System.out.println("jdbc:oracle:thin:@115.90.212.20:10000:XE 오타없나요?");
			System.out.println("사용자 계정명 / 비밀번호가 올바른가요?");
			System.out.println("SQL문 잘썼나요??");
			// SQLSyntaxErrorException이 SQLException의 자식 클래스라서 여기서 잡힘
			
		} finally { // 자원반납 해야돼, 항상 생성의 역순으로!
			
			try {
				
				if(rset != null && !rset.isClosed()) {
					
					// ResultSet부터 반납
					rset.close(); // 반납할 때 문제가 생길 수 있으니 이것에 대한 예외처리
					// 처음에 statement 반환받는 변수가 없었다면 NullPointerException 일어남
					// statement로 result를 만들어야하는데 이것도 빈것이됨.. 그래서 와장창 줄줄이 null
					// null이 아닐 때 닫을 수 있도록 작업해줘야함
					// 서버측에서 이걸 닫을수도 있음, 먼저 닫혀있는지 안닫혀있는지 확인 --> isClosed --> 안 닫힌 상태일때만 얘를 반납하게 해줘야함
					
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
		
		// 다들 Closeable 구현하고 있어서 try resource 구문으로 해결할 수 있는데 지금 정석대로 해봤음
		// 귀찮을수록 더 해봐야함
		
		// 오늘의 중요한 내용 : Statement 사용 시 JDBC 사용 절차
		// 다음주에 여러가지 할텐데, 코드 쓰는건 계속 바뀜, 새로운걸 계속 배울거니까
		// 오늘 했던 JDBC로 어쩌구 했던 절차는 절대 안바뀜, 꼭 해야하는것들은 바뀔 수 없음
		// 무슨 기술을 배우고 뭘 쓰든 JDBC는 이렇게 돌아야함
		// 내가 안썼는데 이게 되면 다른 클래스에서 내가 할 일을 대신 해준것
		
		// 0) JDBC용 변수 선언 및 SQL준비(INSERT, SELECT 상관없이)
		// DML(INSERT)의 경우에는 Connection, Statement, 결과값을 받을 int를 선언했고
		// SELECT의 경우에는 Connection, Statement, 결과값을 받아올 ResultSet을 선언했음
		
		// 1) 실행할 SQL문(완성된형태) 준비(지금은 완성된형태밖에 못씀)
// 나중에 Statement로 어쩌구 해야하는데????
		
		// 2) JDBC Driver 등록(Oracle에 접속하기 위해서 ojdbc를 썼음)
		
		// 등록했으면 getConnection 메소드를 호출할 수 있음
		// 3) Connection 객체 생성(URL, username, password)
		// 세개의 정보로 만듦
		
// 커넥션 객체에 담겨있는 정보로 뭘 해올 Statement 객체를 만들어??
		// 4) Statement 객체 생성
		// 실제로 SQL문을 수행하고 결과를 받아올 수 있음
		
		// 여기서 분기가 나뉨
		// 호출해야 하는 메소드가 다름(DML / SELECT에 따라서)
		// DML이면 executeUpdate() 메소드 호출 --> 처리된 행의 수가 정수값으로 반환 --> 반환된 정수값을 확인해서 트랜잭션 처리를 해야함
		// 5) executeUpdate()
		// 6) 정수값 확인 후 트랜잭션 처리
		
		// SELECT면 executeQuery() 메소드 호출 --> 반환 결과를 ResultSet 타입의 결과로 반환 --> 결과가 있는지 없는지(행의 존재유무) 확인하고 데이터를 가공
		// 5) executeQuery()
		// 6) 행의 존재 유무 확인 후 데이터를 가공
		
		// 5, 6을 서로 다 끝냈다면 다시 똑같아짐
		// 왜? 자원반납 해야하니까!
		// 7) 자원반납(생성의 역순으로)
		
		// 맨날 이 작업을 다른 방법으로 시도해서 코드는 계속 바뀌지만 절차는 절대 안바뀜
		// 내가 DB에 뭐 해야한다, 작업을 할거다 하면 무슨 절차, 다음에 뭐해야하지 이거 생각해야함
// 드라이버 만들어야(?) 커넥션 만들고, 커넥션 만들어야 스테이트먼트, 스테이트먼트 만들어야...
		// 앞에꺼 안하면 다음거 못함, 순서 안지키면 마지막까지 도달못함
		// 반드시 무조건 이 순서대로 돌아야함
		// JDBC는 이게 다고 다른 객체가 추가될건데 이렇게 하면 시시해서 다음주 월요일부터는 기존에 작업하던 MVC 구조에 얘를 덧붙일거임
		// MVC는 MVC대로, 실제 데이터 저장은 이걸로 덧붙일것
		
	}

}
