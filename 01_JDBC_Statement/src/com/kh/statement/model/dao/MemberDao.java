package com.kh.statement.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

public class MemberDao {
	
	// JDBC 어마어마하게 중요
	// 실제로 프로젝트나 회사에서 개발을 하면 직접 쓸 일은 없겠지만 앞으로 배우고 사용하게 될 기술이 전부 JDBC기반이라서 내부적으로 이 코드가 동작할것
	// 실제로 문제가 발생했을 때 여러가지 기술을 사용해서 코드를 작성했을 때 꼭 문제가 발생함(코드는 문제가 발생할수밖에없음)
	// 문제가 발생했다면 문제를 해결해야함, 문제 해결 할 때 JDBC가 어떻게 도는지 동작 원리를 모르면 뭐가 문젠지 알수가 없어서 해결을 못함, 동작원리를 이해해야함, 직접 사용되는 기술은 아닌데 전부 JDBC로 구현되어있음
	// 좀 오래된 회사에 유지보수를 가게되면 실제로 만나게 될 수도 있음, 직접 JDBC코드를 만져야되는 경우도 생김
	// 극한의 성능이 필요한 경우에 JDBC 상위 기술을 쓰면 이게 JDBC로 돌아가는 변환 과정이 필요함, 그 연산을 빼서 성능을 끝판왕으로 돌리겠다면 억지로 JDBC를 쓰는 경우가 있음(신입이나 초보한테는 안시키겠지)
	// 아무튼 그런 경우에 JDBC가 기반 기술이 되는 경우가 있음(성능을 정말 극한으로 뽑는다면)
	// 그러므로 JDBC는 현시대의 모든 웹개발의 기반, DB작업의 기반기술이기 때문에 개념적 중요도가 어마어마하게 굉장히 높음, 특정 회사에는 이거로 먹고사는 경우도 있음
	
	/*
	 * JDBC용 인터페이스
	 * 
	 * - Connection : 데이터베이스와의 세션(연결된 상태)을 나타내는 인터페이스
	 * 
	 * 데이터베이스와의 연결은 사실 굉장히 복잡한 과정이 일어남, 통신 네트워킹 하려면 프로토콜이 있어야하고, 그걸 어떻게 정할지, 인증, 인가, 보안설정 어떻게 할지 등, 접속정보 등 이런것들이 많이 필요해서 복잡한 기술
	 * 이런것들을 전부 DBMS제조사에서 구현해서 제공해줌, 실제 자바 개발자들은 어떻게 구체적으로 구현되어있는지 몰라도 커넥션이라는 인터페이스를 공부해서 DB접속하고 연결하고 이런것들을 사용할 수 있음
	 * 우리는 주로 이런 용도로 사용↓
	 * -> 데이터베이스와의 통신 채널 연결
	 * -> 트랜잭션 관리(트랜잭션을 수동으로 관리하게 된다면)
	 * -> SQL문을 실행을 위한 Statement객체 생성(연결한 뒤에 SQL을 실행해야하므로)
	 * 
	 * 주의사항) (지금은 이정도 나중에 새로운거 배우면 조금 추가될 예정)
	 * 메모리 누수를 방지하기 위해 항상 close()(자원반납)
	 * 트랜잭션을 수동으로 관리한다면 DML수행 이후에는 반드시 commit/rollback
	 * 
	 * - Statement : SQL문을 실행하고 결과를 받아오기 위한 인터페이스
	 * 1. Statement : 정적 SQL문(완성된 SQL문) 실행
	 * 2. PreparedStatement : 파라미터화된 SQL문(미완성된 SQL문) 실행
	 * 3. CallableStatement : 저장 프로시저 호출
	 * 
	 * - ResultSet : SELECT문 실행결과를 담는 테이블형태의 데이터 셋
	 * -> 커서(cursor)라는 개념을 이용해서 데이터(행)에 접근
	 * -> 다양한 데이터타입 변환 메소드를 제공
	 * 
	 * (DAO에서 일어나야 하는) JDBC 처리 순서
	 * 
	 * 1) JDBC Driver 등록 : DBMS제조사에서 제공하는 클래스를 리플렉션을 이용해 등록
	 * JDBC용 인터페이스 --> 결국 자바에서 순수 인터페이스, 아무 의미 없음, 약속만 정해놓음, 실제로 이것을 구현한것은 각 DBMS 제조사들이 만든다
	 * --> 제조사들이 제공해주는 클래스를 가져와서 등록하는 과정이 필요
	 * 
	 * 2) Connection 객체 생성 : 접속하고자 하는 DB정보를 전달하면서 Connection 객체 반환
	 * DB와의 연결정보를 담고 있는 친구, 위에서 등록한 드라이버를 가지고 드라이버를 사용할 수 있는 드라이버매니저 클래스의 메소드 호출하면서 어떤 DB서버에 접속할건지,
	 * DB는 접속할 때 항상 사용자 정보가 필요, 어떤 사용자 이름을 쓸건지, 사용자 비밀번호가 뭔지 전달해줘야함, 
	 * 
	 * 3) Statement 객체 생성 : Connection 객체를 이용해서 생성
	 * 실제 SQL문을 수행할 수 있음
	 * 
	 * 4) SQL문을 전달하면서 실행 : Statement 객체를 이용해서 SQL문을 실행
	 *  > SELECT - executeQuery() 호출
	 *  > DML    - executeUpdate() 호출
	 *  
	 * 5) 결과받기
	 *  > SELECT -> ResultSet(조회된 데이터들이 테이블형태로 담김)객체로 받기
	 *  > DML -> int(처리된 행 수)로 받기
	 * 두 메소드 모두 반환값이 있음, 반환 타입은 각각 다름
	 *  
	 * 6)
	 * SELECT문은 데이터가 엄청 많이 오는데 뷰까지 돌아갈 때 리턴을 한개밖에 못함
	 *  > SELECT -> ResultSet에 담겨있는 데이터를 하나하나씩 뽑아서 VO객체로 담기
	 *  > DML -> 트랜잭션을 수동으로 처리한다면 commit / rollback
	 *  
	 * JDBC 객체 할일 끝!
	 * 7) 자원반납 -> close() -> 생성의 역순으로
	 * 
	 * 8) 결과값 반환
	 *  > SELECT -> 6에서 가공한 형태를 반환
	 *  > DML -> int(처리된 행 수)
	 * 
	 * 하는 방법, 도구 바뀔 수 있음, 절차는 바뀔 수 없음
	 * 
	 */
	
	// 컨트롤러에서 멤버의 주소(매개변수)를 넘겨줌, 받아서 써야함, Member같이 생김
	public int save(Member member) {
		
		// 나중에 자원반납 finally블록에서 해줄거임
		// 0) 필요한 변수를 먼저 선언 및 null값으로 초기화
		Connection conn = null; // 접속할 DB서버와의 연결정보를 담는 객체
		Statement stmt = null; // SQL문 실행 후 결과를 받기 위한 객체
		// import
		
		// 생각! 이번에 실행할 SQL문 INSERT임, 결과가 어떻게 오는가? --> Updated Rows, 행 카운트 해서 옴, 이걸 나중에 돌려주러 가야함, 받을 정수(int) 받을 변수도 하나 선언
		int result = 0; // DML 수행 후 결과를 받기 위한 변수
		
		// 그다음 생각 --> Statement는 SQL문 어떻게 써야하지?
		// SQL문 (정적인 형태)
		/*
		 * INSERT
		 *   INTO
		 *        MEMBER
		 * VALUES
		 *        (
		 *        SEQ_USERNO.NEXTVAL
		 *      , '사용자가 입력한 아이디 값'
		 *      , '사용자가 입력한 비밀번호 값'
		 *      , '사용자가 입력한 이름 값'
		 *      , '사용자가 입력한 이메일 값'
		 *      , SYSDATE
		 *        )
		 * 
		 * 이런 모양의 SQL문을 만들어야함, 말도안되게 어렵진않음!
		 * SELECT는 무궁무진하게 나올 수 있는데 INSERT는 맨날 이렇게 쓰면 끝, 끝날때까지 이수준
		 * 한번만 제대로 해놓으면됨(INSERT UPDATE... DML 삼총사), 이대로 쭉 가는거니까
		 * 
		 */
		String sql = "INSERT "
					 + "INTO "
					      + "MEMBER "
			       + "VALUES "
			       		  + "("
			       		  + "SEQ_USERNO.NEXTVAL"
			       	   + ", '" + member.getUserId() + "'"
			       	   + ", '" + member.getUserPwd() + "'"
		       		   + ", '" + member.getUserName() + "'"
       				   + ", '" + member.getEmail() + "'"
       				   + ", SYSDATE"
       				     + ")";
		// 사용자가 입력한 아이디값은 멤버의 필드에 있음, 주소를 가져왔는데 그냥 멤버의 필드에 접근할 수 없음
		// 멤버의 필드값을 가져오려면 getter 써야함, 멤버의 아이디에 있으니까 getUserId
		// 문자 데이터 앞뒤로 따옴표 달기
		// System.out.println(sql); Statement를 사용할때는 문자열이니까 출력문으로 찍어볼 수 있음, INSERT 문을 작성할 줄 알아야겠지만 미리 확인해볼 수 있다는 장점이 있음
		// 메인 메소드에서 실행해보고 실행될 수 있는 인서트문인지 확인 --> 이걸 완성된 형태, 정적인 형태라고 함
		// 주의) SQL문을 문법적으로 올바르게 작성하지 못했다면
		// SQLSyntaxErrorException이 발생함
		
		try {
			
			// 1) JDBC Driver 등록
			// 메소드 호출해서 등록, 패키지 경로부터 클래스명까지 문자열로 전달(우리는 고정)
			Class.forName("oracle.jdbc.driver.OracleDriver"); // CE발생 --> try-catch
			// 1. 오타가 날 경우
			// 2. 프로젝트에 라이브러리를 추가 하지않아서 진짜로 클래스를 못찾는 경우
			// -> ClassNotFoundException 발생
			
			// 라이브러리 추가함
			
			// 2) Connection 객체 생성 (DB와 연결 -> URL, 사용자이름, 비밀번호)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE"
											 , "CJ18"
											 , "CJ181234");
			// 메소드 호출하면 커넥션을 돌려받음
			// URL(uniform resource location)은 내가 어떤 DBMS를 쓰느냐에 따라 다 다름, 한 여덟개?, 내가 지금 자원을 얻고자하는 연결하고자 하는 위치
			// DB 접속할 때 항상 사용자명이 있어야함
			// 요런 느낌 new Connection("jdbc:oracle머시기", "CJ18", "CJ181234") (객체 생성으로 생각 --> 생성하고 끝나면 안됨, 계속 쓰려고 만들었음, 변수에 담아줌, 커넥션 타입의 변수 만들어뒀음, 변수에 담아둬야 메소드 호출할 수 있음)
			// 1. URL을 잘못 적었을 수 있음
			// 2. 사용자 계정명을 잘못 적었을 수 있음
			// 3. 비밀번호를 잘못 적었을 수 있음
			// 4. 서버가 안 켜져(열려)있을 수 있음
			// 5. 접속 권한을 부여받지 못했을 수 있음
			// 밖에 나가는거임, 인터넷 세상에 있는 다른 호스트로 내가 연결을 요청하는것, 별의 별 문제가 다 일어날 수 있음 --> 뭉뚱그려서 SQLException
			// SQLExcecption이 발생, 왜일어났는지는 12345 읽어보면서 내가 뭐 잘못했는지 찾아야함
			// CE발생 --> 예외처리(catch 추가)
			
			// AutoCommit 끄기
			// 이 친구 편하니까 얘가 켜져있으면 우리가 직접 트랜잭션을 제어할 일이 없음, 직접 해보기 위해서 오토커밋 끔, 실제로 나중에는 이거 다 켜놓고 할거임
			// 지금은 공부하는거고 이 단계에서 트랜잭션 처리 해야하는구나 알아야하니까 끄고 함
			// 커넥션 객체 가지고 메소드 호출하면 끌 수 있음
			conn.setAutoCommit(false);
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement(); // new Statement(conn); 요런느낌, stmt 객체를 커넥션으로 만드는데 생성자와 매개변수 전달할거 넣는거
			// 얘 올리기가 끝 아님 안알아가게 변수 미리 선언해둔것(Statement stmt)에 담음
			
			// Statement 만든것으로 실행
			// 4, 5) DB에 완성된 SQL문을 전달하면서 실행도 하고 결과도 받고
			// 이번에 보내는건 INSERT문, Statement 객체의 업데이트 메소드 호출하면서 인자로 실행할 SQL문 전달
			// 아까 적어둔 SQL 구문에서 SyntaxErrorException 발생할 수 있음
			result = stmt.executeUpdate(sql);
			// INSERT 시 값에 문제가 있을 수 있음
			// 자료형이 맞지 않음
			// 제약조건에 위배
			// 데이터의 크기가 컬럼의 크기보다 큼
			// SQLException이 발생
			// 뭐가 문제였는지는 뜨는거 보고 알아서 고쳐야함
			// 사실 앞에서 걸러서 왔어야함, 유저 아이디 15바이트니까 length 봐서 15 넘으면 안되게 되돌린다든가(그것까지 하면 너무 그러니까 숙제로)
			// 아무튼 이런 문제가 일어나지 않는다면 결과가 수행될것이다!
			
			// 성공하면 int형의 1이 돌아옴, 그게 아니라면 0이 오니까
			// 이거 받아야함, 돌려줘야하니까, 이것도 변수 선언해뒀음
			// 오늘은 트랜잭션 처리 수동으로 하기로 했음
			// DML이니까 수행되었으면 트랜잭션 생김(실패했으면 볼거없음)
			// 단일 트랜잭션이니까 성공했을때는 트랜잭션 처리 해줘야함
			// 6) 트랜잭션 처리
			if(result > 0) {
				conn.commit();
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
			// JDBC용 객체는 할일이 다 끝남, 외부랑 묶인 애들이라 가비지 컬렉터가 안가져감, 직접 반납을 해야함
			// 7) 사용이 모두 끝난 JDBC용객체 자원반납
			// 생성된 순서의 역순으로 close() 호출(항상 생각)
			try {
				
				if(stmt != null) {
					stmt.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// 8) 결과반환
		return result; // 메소드 반환형 int로 수정하고 컨트롤러로 돌아감
		
	}
	
	public List<Member> findAll() {
		
		// 0) 필요한 변수 선언
		
		// Connection, Statement
		Connection conn = null; // 연결된 DB의 정보를 담는 객체
		Statement stmt = null; // SQL문을 실행하고 결과를 받기 위한 객체
		ResultSet rset = null; // SELECT문을 수행하고 조회 결과값들이 담길 객체
		
		String sql = """
				        SELECT
				               USERNO
				             , USERID
				             , USERPWD
				             , USERNAME
				             , EMAIL
				             , ENROLLDATE
				          FROM
				               MEMBER
				         ORDER
				            BY
				               ENROLLDATE DESC
					 """;
		
		try {
			
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracld:thin:@115.90.212.20:10000:XE"
											 , "CJ18"
											 , "CJ181234");
			// ExceptionHandling
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) SQL(SELECT)문 실행 후 결과 반환(ResultSet)
			rset = stmt.executeQuery(sql);
			
			// 6) Mapping
			// 서로 다른 형태의 데이터 모델 간의 연결을 정의하는 과정
			// 자바 == 객체 형태(데이터를 객체 형태로 다룸)
			// 구조를 맞춰주는 작업 --> 매핑, 행을 행의 정보를 담을 수 있는 클래스로 만들었음, 한 행에 대한 정보를 하나의 VO 객체를 이용해서 데이터를 이관(옮기는) 작업을 할 것
			// 관계형 데이터 베이스 == 테이블 형태(데이터를 테이블 형태로 다룸)
			
			// 현재 조회 결과는 ResultSet에 담겨있음
			// 한 행씩 뽑아서 VO객체의 필드에 담기
			// 문제는 조회결과가 몇행인지 알 수 없음(몇행이 올지 알수없음)
			// 이걸 활용하려고 ResultSet에서 조회결과가 있는지 없는지 확인할 수 있는 메소드를 배웠음
			// rset.next()
			// 커서를 한 줄 아래로 옮겨주고 존재한다 true / 존재하지 않는다 false
			// 반복문을 통해서 next 메소드를 호출해서 행 개수만큼 반복을 시켰음
			
			
			
			while(rset.next()) {
				
				// 여기에 들어왔다는 것은?
				// 현재 rset의 커서가 가리키고 있는 행의 데이터를
				// 하나하나씩 뽑아서 MemberVO의 필드에 대입
				Member member = new Member();
				
				// ResultSet객체로부터 어떤 컬럼의 값을 뽑을건지 메소드를 호출하면서 컬럼명을 명시
				// 당분간 세가지 정도의 메소드 기억
				// rset.getInt(컬럼명)    : 정수형 값을 int형으로 매핑할 때
				// rset.getString(컬럼명) : 문자열형 값을 String형으로 매핑할 때
				// rset.getDate(컬럼명)   : 날짜형 값을 java.sql.Date형으로 매핑할 때
				// 이걸 써서 Member의 필드에 값을 대입할것임 --> setter 호출해서 할것임
				// 항상 메소드를 호출할 때 인자값으로 어떤 컬럼의 값을 뽑을건지 컬럼명을 적어줘야함
				// 컬럼명 : 대소문자를 가리지 않음
				// 컬럼명말고 컬럼의 순번으로도 가능함
				// 권장사항 : 컬럼명으로 작성하고 대문자로 작성
				
				member.setUserNo(rset.getInt("USERNO"));
				// System.out.println(rset.getInt("USERNO"));
				// 메소드 호출 시 반환값이 있다면 값이라고 생각해야함
				// int userNo = rset.getInt("USERNO");
				// System.out.println("USERNO컬럼의 값 : " + userNo);
				
				// 컬럼명을 대소문자, 숫자로 대체 가능
				// 숫자는 컬럼의 순번을 의미하는것
				// 그렇지만 무조건 대문자로 쓰자!
				
				// 나머지 컬럼들도 값을 똑같이 빼서 Member객체의 필드에 값을 대입해야함
				// 생성자 호출이 아니라 setter 메소드 호출이기 때문에 순서는 크게 상관없음
				member.setUserId(rset.getString("USERID"));
				member.setUserPwd(rset.getString("USERPWD"));
				member.setUserName(rset.getString("USERNAME"));
				member.setEmail(rset.getString("EMAIL"));
				member.setEnrollDate(rset.getDate("ENROLLDATE"));
				// 주의사항 생각할것
				// 여기서 조심해야 할 것
				// 일어날 수 있는 문제들이 있음
				// 컬럼명을 적다가 오타내면?
				// 실제 조회된 ResultSet에 이런 이름의 컬럼이 없을것이고 없는 컬럼의 이름을 뽑아내려고 하는것이 되므로 --> 열 이름이 부적합합니다.
				// 컬럼명이 오타났을 때 SQLExeption발생
				
				// 컬럼에 별칭을 달아놓을 수도 있음
				// 별칭으로 조회해도 마찬가지로 SQLException, 열 이름이 부적합합니다.
				// 지금 get은 ResultSet에서 값을 뽑아내는것임, 테이블에서 뽑아내는 것이 아님
				// SELECT문에서 별칭으로 실행하면? 별칭으로 ResultSet으로 돌아오고, 원래 컬럼명으로 get하면 없는 컬럼명이라고 함
				// 테이블에 있는 데이터를 뽑아내는 게 아님!
				// 테이블 컬럼값을 뽑아내는 것이 아니고
				// ResultSet에서 조회된 결과값을 뽑아내는 것(SELECT 수행 결과에서 값을 뽑아내는것)
				
				// 한 행에 모든 컬럼의 데이터 값을
				// 각각의 필드에 담아 Member 객체로 옮겨담으면 끝!!!
				// 한 행이 하나의 VO 객체를 이용하는것으로??
				// 우리가 지금 돌려주는게 한 행이 아님, 모든 조회 결과를 다 돌려주려고 하는것(전체조회)
				
				// 조회된 Member 들을 싹 다 돌려보내야함
				// 배열 특) 크기 정해야됨
				// 지금같은 경우에 멤버가 몇행일지 모름, 추가한 만큼 멤버가 들어있을 것이므로 몇행일지 특정지을 수 없음
				// 배열은 가능하지만 매우 큰 크기로 만들어야겠지?
				// 조회 결과가 몇 행일지 특정지을 수 없음, 아 배열 쓰기 아리까리하다..
				// 여러 정보를 담아줄 저장소 ==> List(ORDER BY 해놨으니까, 이거 해놓은 대로 가면 좋겠다)
				// Set에도 정렬 보장해주는 Set이 있고, Map에도 있긴 한데, 이런 상황에서는 일반적으로 List를 좋아함, 편하니까(ArrayList)
				// List에 Member를 담아서 돌아가면 되겠다!
				// Generics Member로 달아주면 되겠다
				// List<Member> members = new ArrayList(); 여기서 만들면 반복문 반복할때마다 만들겠지? 의미없음
				// 선택의 영역, 일반적으로 생각했을때는 0번에서 변수 선언으로 두는 게 좋다, 그래야 이 안에서 이런것들 쓰는구나 하고 쉽게 파악할 수 있음
				// 다른 의견도 있음, 지금같은 경우에는 성립하지 않지만, 필요할때마다 선언해서 써야지, 하는 경우가 있음 --> 반복문 직전에서
				// 지금은 return이 어디 있어도 상관없음(finally에서 자원반납 할거니까)
				
				// List에 요소 추가는 add
				members.add(member);
				
			}
				
			} finally {
				
				// 7) 다 쓴 JDBC용 객체 자원반납(생성된 순서의 역순으로) => close
				try {
					
					if(rset != null) {
						rset.close();
					}
					
				} catch(SQLException e) {
					e.printStackTrace();
				}
				
				try {
					
					if(stmt != null) {
						stmt.close();
					}
					
				} catch(SQLException e) {
					e.printStackTrace();
				}
				
				try {
					
					if(conn != null) {
						conn.close();
					}
					
				} catch(SQLException e) {
					e.printStackTrace();
				}
			
			}
		
		// 8) 매핑된 객체들 결과 반환
		// 조회 결과들을 매핑해놓은 Member객체들의 주소값을 요소로 가지고 있는
		// List의 주소값을 반환
		return members;
	
	}
	
	public Member findById(String userId) {
		
		Member member = null; // 나중에 쓸 거 미리 선언
		
		// 0) 필요한 변수들 먼저 선언
		// JDBC 관련 인터페이스
		// Connection, Statement, ResultSet
		// 이게 공식처럼 정해진것은 아님, excuteQuery 했는데 그게 꼭 ResultSet으로 안들어갈수도 있음
		/*
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		*/
		// 자바에서 배운게 많으니까 변주를 줘보자
		
		// 이번에는 SQL문 먼저 준비해보기
		// 실행할 SQL문(완성형태로)
		/*
		 * SELECT
		 *        USERNO
		 *      , USERID
		 *      , USERPWD
		 *      , USERNAME
		 *      , EMAIL
		 *      , ENROLLDATE
		 *   FROM
		 *        MEMBER
		 *  WHERE
		 *        USERID = '사용자가 입력한 ID값'
		 * 
		 * 이렇게 생긴 SQL문이 필요함
		 * 
		 */
		String sql = """
						SELECT
						       USERNO
						     , USERID
						     , USERPWD
						     , USERNAME
						     , EMAIL
						     , ENROLLDATE
						  FROM
						       MEMBER
						 WHERE
						       USERID =
					 """;
		sql += "'" + userId + "'"; // 이건 별로 안좋음, 예쁘게 안들어감
		// System.out.println(sql);
		
		// SQL문 세팅했으니까 원래대로라면 뭐해야함? 드라이버 등록
		// 근데 드라이버 등록 과정이 아리까리함, 왜냐하면 이것은 프로그램 실행 시 딱 1회만 하면됨
		// 우리는 계속 JDBC 수행하는 모든 메소드들에서 계속 이걸 하고있음, 어쩔수없는 부분도 있음, 사용자가 어떤 메소드를 먼저 호출할지 모르니까
		// 이걸 어떻게 할 방법이 없을까?
		// 없으면 그냥 가여?
		try {
			
			// 1) JDBC Driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			// 3) Statement 객체 생성
			// 4) SQL실행
			// 5) ResultSet받아오기
			// 이 과정을 공식처럼 받아주는걸로 하지말고 다른 방법을 해보자 --> try with resource
			try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE", "CJ18", "CJ181234");
				Statement stmt = conn.createStatement();
				ResultSet rset = stmt.executeQuery(sql)){ // 각 객체는 ;로 구분하고, 보편적으로 제일 마지막에 ;는 생략함
				
				// 6) 현재 조회결과가 담긴 ResultSet객체에서
				// 조회결과가 존재하다면(없으면 담을것도 없지) VO객체의 필드에 옮겨담기(매핑작업하겠다는뜻)
				// 조회결과가 있는지 없는지는 커서를 내려서 확인 --> rset.next()
				// 지금 ID를 가지고 조회인데, ID 컬럼에는 UNIQUE 제약조건이 걸려있음, ID를 가지고 조회를 했을 때 결과가 있다면 최대 하나밖에 없을것임, 없으면 0개
				// 검사를 한번만 돌아보면 됨! --> if
				// ID가지고 검색(UNIQUE) 한 행만 조회 --> 나중에 돌아가는것도 머임?
				// 한 행에 대한 정보는 하나의 VO객체로 담자고 했음, 그걸 위해서 Member 객체를 만들었음, 그러면서 필드를 데이터베이스의 컬럼과 비슷하게 만들었음
				if(rset.next()) {
					
					// Member member = new Member();
					// 이렇게 하면 밖에서 return 해줄 수 없으니까, 변수 선언을 어디서 해주면 좋을까?
					// 아까 List 선언한 것 처럼 메소드 블록 최상단에서!
					// 그리고 나중에 member로 리턴하자! null값으로 초기화해두고
					
					// 조회결과가 있다면, 멤버 객체를 만들어서 매개변수 생성자 호출
					member = new Member(rset.getInt("USERNO")
									  , rset.getString("USERID")
									  , rset.getString("USERPWD")
									  , rset.getString("USERNAME")
									  , rset,getString("EMAIL")
									  , rset,getDate("ENROLLDATE"));
					
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 8) Controller에게 결과값 반환
		return member;
		
	}
	
	// 컨트롤러에서 정해서 왔음
	public List<Member> findByKeyword(String keyword) {
		
		// 0) 필요한 변수들
		// 나중에 반환할거 다 정해져있으니까
		List<Member> members = new ArrayList();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문
		/*
		 * SELECT
		 *        USERNO
		 *      , USERID
		 *      , USERPWD
		 *      , USERNAME
		 *      , EMAIL
		 *      , ENROLLDATE
		 *   FROM
		 *        MEMBER
		 *  WHERE
		 *        USERNAME LIKE '%사용자가입력한값%'
		 *  ORDER
		 *     BY
		 *        ENROLLDATE DESC
		 * 
		 */
		String sql = "SELECT "
				          + "USERNO"
				        + ", USERID"
				        + ", USERPWD"
				        + ", USERNAME"
				        + ", EMAIL"
				        + ", ENROLLDATE"
				     + "FROM "
				          + "MEMBER"
				    + "WHERE "
				          + "USERNAME LIKE '%" + keyword + "%'"
				    + "ORDER "
				       + "BY "
				          + "ENROLLDATE DESC";
		// 키워드 앞뒤 공백에 유의해서 작성
		
		try {
			
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성(DB와 연결)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE", "CJ18", "CJ181234");
			
			// 3) SQL문 실행하고 결과를 받아올 Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) SQL(SELECT)문을 실행 후 결과 받아오기
			rset = stmt.executeQuery(sql);
			
			// ResultSet이 오는데 결과가 있을수도 있고 없을수도 있음, 없을때는 딱히 뭐 안해줘도 되고 조회결과가 있을때는?
			// 조회결과가 나온걸로 View까지 들고 가야함, 20개가 나왔다면 6개의 컬럼이니 120개.. 너무많음
			// 한 행에 대한 결과는 VO객체 하나로, 객체들은 List에
			// 6) ResultSet객체에서 각 행에 접근하면서
			// 조회 결과가 있다면 컬럼의 값을 뽑아서 VO객체에 필드에 대입한 뒤
			// List의 요소로 추가함
			while(rset.next()) {
				
				// 리스트에 요소를 추가하면서 객체 생성하는 스타일로 해보자
				members.add(new Member(rset.getInt("USERNO")
									 , rset.getString("USERID")
									 , rset.getString("USERPWD")
									 , rset.getString("USERNAME")
									 , rset.getString("Email")
									 , rset.getString("ENROLLDATE")));
				// 순서에 유의, 필드 순서대로
				
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 7) 자원반납 => 생성된 순서의 역순으로 close()를 호출
			try {
				
				rset.close();
				stmt.close();
				conn.close(); // 시간관계상 생략(따로따로 if문)
				
			} catch(SQLException e) {
				
			}
			
		}
		
		// 8) 결과반환
		return members;
		
	}
	
	public int update(PasswordDTO pd) {
		
// 9:33 생각해야지~ 무슨 변수 쓸 지
		// UPDATE SQL문 쓸것임 --> 나중에 결과가 처리된 행의 개수(int)로 온다
		// DML이니까 -> (커밋이라고 하면 안되고)트랜잭션처리
		
		// DAO에서 제일 처음 뭐 해야함?
		// 0) 필요한 변수들 세팅
		Connection conn = null; // 커넥션 어따씀? DB랑 연결
		Statement stmt = null; // SQL문 실행
		int result = 0; // 결과값 받을 변수 선언
// 9:37 PK로 하는 이유? PK가 각 행을 식별하는 용도니까?
		String sql = "UPDATE "
						  + "MEMBER"
					  + "SET "
						  + "USERPWD = '" + pd.getNewPassword() + "' "
					+ "WHERE "
						  + "USERNO = (SELECT "
						   				   + "USERNO "
									  + "FROM "
										   + "MEMBER "
									 + "WHERE "
									       + "USERID = '" + pd.getUserId() + "'"
									   + "AND "
										   + "USERPWD = '" + pd.getUserPwd() + "')";
		
		try {
			
			// 1) JDBC Driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver"); // try-catch로 감싸고 예외처리, ClassNotFoundException 발생할 수 있음
			
			// 2) Connection 만들기
			// DriverManager에 스태틱 메소드가 있음
// 9:43 머가 필요해?
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE"
											 , "CJ18"
											 , "CJ181234");
			
			// 일부러 어렵게 해보자 --> 커밋끄기
			// 2_2) AutoCommint 끄기
			conn.setAutoCommit(false);
			
			// 3) Statement 만들기
			stmt = conn.createStatement();
			// 얘가 실행기니까 실행하고, 실행은 메소드 호출해서 한거라 바로 결과 돌아옴
			// 4, 5) SQL문(UPDATE) 실행 후 결과 받기
			result = stmt.executeUpdate(sql);
			// 예외가 두개 일어날 수 있음, DB 서버가 컴퓨터니까 한번에 받아서 요청을 처리할 수 있는 한계가 있음
// 9:49 Connection도 풀이 있음, 누군가 딴사람이 쓰고 있으면 뒤에 요청이 온 애들은 앞에 끝날 때 까지 기다려야함, 시간초과, ...?
			
			// 실행했으니까 이제 뭐해야해?
			// 6) 트랜잭션 처리
			// 이걸 잘해야함, 개발자는 트랜잭션 처리를 잘해야함
			// 나중에는 다중 트랜잭션이 어려움
			if(result > 0) {
				conn.commit();
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 7) 사용이 끝난 JDBC용 객체 반납 => 생성된 순서의 역순으로(close())
			try {
				
				if(stmt != null) {
					stmt.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// 8) 결과반환
		return result; // 반환형 수정
		
	}
	
	// 정해져있음 나중에 뭐 보낼 지
	public int delete(Member member) {
		
		// 0) UPDATE랑 똑같음
		int result = 0;
		Connection conn = null;
		Statement stmt = null;
		
		String sql = "DELETE "
				     + "FROM "
				          + "MEMBER "
				    + "WHERE "
				          + "USERID = '" + member.getUserId() + "'"
				      + "AND "
				          + "USERPWD = '" + member.getUserPwd() + "'";
				         
		// 1 ~ 6)
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@115.90.212.20:10000:XE", "CJ18", "CJ181234");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
			if(result > 0) {
				conn.commit();
				}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try { // 7)
				
				stmt.close();
				conn.close();
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return result; // 8)
		
	}
	
}
