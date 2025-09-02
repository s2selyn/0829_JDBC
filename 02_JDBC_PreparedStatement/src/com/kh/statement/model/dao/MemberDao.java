package com.kh.statement.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

public class MemberDao {
	
	// 밖에서는 이 값에 접근할 필요 없음
	private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	// 여기서 한발자국 더 간다면?
	// final 키워드를 붙여서 작업하면 밑에서 고쳐질 위험성도 줄어듦
	
	// 기왕 드라이버 빼놓는거 조금 더 생각해보면, URL이라든지..
	private final String URL = "jdbc:oracle:thin:@115.90.212.20:10000:XE";
	
	// 여기에 플러스? 사용자이름이라든지..
	private final String USERNAME = "CJ18";
	
	// 여기에 플러스? 비밀번호까지..
	// 한번 해봐서 안다, 모든 메소드에서 동일한 이 값을 계속 쓰고있음
	private final String PASSWORD = "CJ181234";
	
// 12:40 이렇게 필드로 두면? 의미도 명확해지고 어쩌구저쩌구
	
	// DAO 다시 만들기
	/*
	 * DAO(Data Access Object)
	 * DAO는 큰-넓은 의미, 외부로 나가는(DB로 나가지만 파일도 했음, 다른 플랫폼으로도 나갈 수 있음) 작업을 총칭하는 패키지
	 * 이 안에서 조금씩 세분화될 수 있음(레포지토리, 매퍼 등)
	 * 
* 지금 시점 DAO에서는 DataBase 관련된 작업(CRUD, insert update delete 등?)를 전문적으로 담당하는 객체
	 * DAO안에 있는 모든 메소드를 데이터베이스 관련된 작업으로 구성할 것
	 * 
	 * SQL문 중에서
	 * SELECT / INSERT / UPDATE / DELETE를 하는 작업이 여기에 들어가게 될 것
	 * 
	 * 메소드를 구성할 때 중요한 것
	 * 하나의 메소드는 하나의 SQL문만 실행할 것!
	 * 
	 * DAO는 컨트롤러는 통해 호출된 기능을 수행할것, 무조건 이렇게 써야한다는게 아님
	 * 내일부터는 새로운 친구 추가, 오늘 안끝날수도?
	 * Controller를 통해 호출된 기능을 수행! -> 02번 프로젝트까지만
	 * DB에 직접 접근한 후 해당 SQL문을 실행한 후 결과 받아오기(JDBC 사용)
	 * 
	 */
	
	/*
	 * JDBC용 객체
	 * 
	 * - Connection : DB와의 연결정보를 담는 객체(연결정보는? IP주소, Port번호, 사용자이름, 비밀번호)
	 * ID를 오라클 데이터베이스에서는 username(사용자이름)이라고 함
	 * 
	 * Connection으로 Statement를 만들 수 있다
	 * - Statement : Connection에 담겨있는 연결정보 DB에 SQL문을 보내서 실행하고 결과도
	 * 				 받아오는 다재다능 객체
	 * 
	 * SELECT일 경우에 ResultSet이 필요함
	 * - ResultSet : 실행한 SQL문이 SELECT문일 경우 조회된 결과가 처음 담겨있는 객체
	 * 
	 * 이렇게 3총사가 있었는데, 02번 프로젝트에서는 새로운 친구가 들어옴
	 * - PreparedStatement : SQL문을 미리 준비하는 개념
	 * 						 미완성된 SQL문을 미리 전달하고 실행하기 전 완성형태로 만든 뒤
	 * 						 SQL문을 실행함
	 * 						 미완성된 SQL문에 사용자가 입력한 값들이 들어갈 수 있도록 공간을
	 * 						 확보해놓음 ==> ?(placeholder/위치홀더)
	 * Pre의 의미? 전? 미리? prepared는 준비된, 준비해놓는것, 뭘 준비하냐? SQL문을 미리 준비
	 * Statement쓸때는 SQL문을 완성된 형태로 만들어야한다고 했음
	 * 완성된 형태가 있다는건? 미완성된게 있다는 뜻이겠지
	 * 정식 명칭은 placeholder/위치홀더, 시원하게 빵꾸뚫는다고 표현
	 * 
	 * 여섯개의 메소드를 Statement 대신 PreparedStatement를 써보자
	 * - Statement와 PreparedStatement는 부모자식 관계(Statement가 부모클래스?)
	 * 
	 * - 차이점
	 * 
	 * 1) Statement는 완성된 SQL문, PreparedStatement는 미완성된 SQL문
	 * 
	 * 2) 객체 생성 방법(둘다 커넥션 객체로 만드는 것은 맞음, 메소드가 다름)
	 * 		Statement == 커넥션객체.createStatement()
	 * 		PreparedStatement == 커넥션객체.prepareStatement(sql); <-- 요게 핵심!!
	 * SQL문을 prepareStatement 만들 때 인자로 전달해야함
	 * 
	 * 3) SQL 문 실행
	 * 		Statement == stmt.executeXXX(sql);
	 * Statement는 인자값으로 SQL문을 전달함
	 * 		PreparedStatement == pstmt.excuteXXX();
	 * PreparedStatement는 미리 SQL문을 보냈으므로 안넣고 그냥 돌림
	 * 미완성된 애를 돌리는 거라서 돌리기 전에
	 * ? 위치홀더에 실제 값을 Binding해준 뒤 실행한다.
	 * pstmt.setString()
	 * pstmt.setInt()
	 * 
* 11:35 -- JDBC 절차(메소드에서 해야하는 작업), DAO에서 메소드를 구현한다, JDBC ??
	 * 
	 * 0) 필요한 변수들 세팅
	 * 1) JDBC Driver 등록 : 해당 DBMS에서 제공하는 클래스를 String형으로 동적으로 등록
	 * 2) Connection 객체 생성 : DB와의 세션연결, 연결할 때 필요한 정보를 인자로 전달(URL, 사용자이름, 비밀번호)
	 * 3) Statement였는데 이제는 그게 아니라
	 * 3_1) PreparedStatement 객체 생성 : Connection객체 생성(메소드가 다르고, 미완성된 SQL문을 생성과 동시에 꼭 전달!!!!)
	 * 3_2) SQL문이 미완성 상태일 수 있음, 현재 미완성된 SQL문을 완성형태로 만들어주기
	 * => 미완성일 경우에만 해당 / 완성된 경우에는 생략
	 * 4) SQL문 실행 : executeXXX() => SQL을 절대로 인자로 전달하지 않음!!! 절~~~대로 절~~~~~~대로(왜냐? 앞에서 보냈으니까)
	 * 			   > SELECT : executeQuery()
	 * 			   > INSERT/UPDATE/DELETE : executeUpdate()
	 * 5) 결과받기 : 
	 * 			   > SELECT : ResultSet(조회된 데이터들이 담겨있음)
	 * 			   > INSERT/UPDATE/DELETE : int(처리된 행의 개수가 몇개인지 count 세서 오는 것)
	 * 
	 * 6) SELECT : ResultSet에 담겨있는 컬럼값들을 커서를 옮겨가며 한 행씩 접근해서 하나하나 뽑아서
	 * 			   VO객체의 필드에 매핑(옮겨담기) -> VO객체가 여러 개일 경우 -> VO들을 List의 요소로 관리
	 *    INSERT/UPDATE/DELETE : 트랜잭션 처리
	 * 
	 * 7) 사용이 다 끝난 JDBC용 객체들을 생성의 역순으로 자원 반납 -> close() 메소드 호출
	 * 8) 결과 반환
	 *    SELECT -> 6에서 만든거(뭐일지 모름)
	 *    INSERT/UPDATE/DELECT -> 처리된 행의 개수
	 * 
	 * 바뀐 건 없고 3번만 수정됨, 어제 했던거랑 똑같은거 하는건데 3번만 바뀜
	 * 
	 */
	
	// 컨트롤러에서 DAO의 save를 부르고 반환타입은 int로 해둠
	public int save(Member member) {
		// 컨트롤러에서 save 부를 때 인자값 넘겨줌 --> 사용자가 뷰에서 입력한 값들을 필드로 가지고 있는 멤버의 주소값
		// 멤버의 필드들 값을 뽑아서 INSERT 하는 것이 목적
		// member를 받아서 쓰기 위해서 매개변수로 Member 같이 생긴 member 받아야겠다
		
		// 0) 필요한 변수들을 선언해보자!
		// 일단 연결은해야함
		Connection conn = null; // DB와의 세션 연결
		
		// sql문 실행해야하니까 statement 있어야하는데 이제 진화해버림! Statement의 자식임, 큰 차이 없음
		// 하는짓도 똑같음
		PreparedStatement pstmt = null; // SQL문 실행 후 결과 받기
		
		// 결과돌려받아야하니까, INSERT니까 처리된 행의 개수가 돌아옴
		int result = 0;
		
		// SQL문 있어야함
		/*
		 * INSERT
		 *   INTO
		 *        MEMBER
		 * VALUES
		 *        (
		 *      , SEQ_USERNO.NEXTVAL
		 *      , '사용자가 입력한 아이디'
		 *      , '사용자가 입력한 비밀번호'
		 *      , '사용자가 입력한 이름'
		 *      , '사용자가 입력한 이메일'
		 *      , SYSDATE
		 *        )
		 * 
		 * 사용자가 입력한건 member에서 뽑아서 쓰면 되고
		 * 뭐가 어려움? 줄맞춤, 앞뒤로 따옴표 붙여야하고 죽겠네
		 * PreparedStatement를 사용하면 이런 어려움을 쉽게 해결할 수 있다
		 * 
		 */
		String sql = """
				        INSERT
				          INTO
				               MEMBER
				        VALUES
				               (
				               SEQ_USERNO.NEXTVAL
				             , ?
				             , ?
				             , ?
				             , ?
				             , SYSDATE
				               )
					 """; // 변수 자리에 게터 써서 넣어야하는데 뭐가들어갈지 모르겠다 --> ?로 씀
		// 진짜로 모르겠잖아, 물음표 씀, 따옴표도 신경쓰지 않음
		// 훨씬 편해짐
		// 세팅했다
		
		// 드라이버 등록해야함, 어차피 할 거 미리 하겠다 --> 예외처리
		try {
			
			// 1) JDBC Driver등록
			Class.forName(DRIVER);
			// 이거 해야 연결할수있음
			
			// 2) Connection 객체 생성(DB와 연결하겠다!!!!!)
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			// 트랜잭션 생각해야하니까 편한 오토커밋 꺼버리기
			conn.setAutoCommit(false);
			
			// 3) 원래 Statement 만들기인데 우리 진화했으니까
			// 3_1) PreparedStatement객체 생성(SQL문을 미리 전달해해야함!!!!!!!!!!)
			// 똑같이 커넥션으로 만드는건 맞는데 메소드가 다름
			pstmt = conn.prepareStatement(sql); // 인자가 없으면 돌아가지도 않음, 빵꾸뚫린 sql문을 여기에 미리 준비시키는것
			// 이렇게 해둔것은 돌아갈 수 없음 실제로 돌리려면 빵꾸를 채워주는 작업을 해야함
			// ID, PWD, NAME, EMAIL이 빵꾸뚫려있음, 이 네개를 채워주지 않으면 SQL문이 실행될 수 없음
			
			// 3_2) 미완성된 SQL문일 경우 완성시켜주기
			// 위치홀더에 값 바인딩(저 자리랑 내 값을 전달해서 묶어주기)
			// pstmt에 메소드가 있음, 값을 세팅하고싶은거니까 set 뭐시기로 감
			// pstmt.setXXX(?의 위치, 실제값);
			// 눈치코치 set머시기는 넣는거 get머시기는 주는거, 모든 자바개발자는 똑같이 생각함, 우리도 그렇게 생각해야함
			// 인자를 두개 전달해야함, 어떤 위치에 넣을 것이냐, 어떤 값을 바인딩할것이냐
			// 첫번째 빵꾸에 값을 채운다고 가정해보자
			// 사용자가 입력한 아이디값이 들어가야함, 코드상에 사용자가 입력한 아이디값은 어디에 있음? member의 필드중에 userId 필드에(컨트롤러에서 거기에 담아옴)
			// sql은 미리 전달했음, set뒤에 자료형이 붙음
			// 몇번째 --> 1, 멤버의 userId 가지고 오고싶은거니까 --> member.getUserId()
			pstmt.setString(1, member.getUserId());
			// 두번째 빵꾸에 비밀번호값 넣어야함, 무슨형태의 값? 문자열형태 --> setString
			// 몇번째 --> 2, 멤버의 userPwd값, 필드값 반환받아야하니까 --> member.getUserPwd()
			pstmt.setString(2, member.getUserPwd());
			pstmt.setString(3, member.getUserName());
			pstmt.setString(4, member.getEmail());
			// 넣을 때 앞뒤로 홑따옴표 붙여줘야함, 근데 얘가 알아서 붙인다!
			// 위치홀더를 올바르게 다 채우지 못했다.(개수 다른거)
			// (위치홀더 잘못쓰면 SQL문 실행하는데서 문제가 생김 -> 물음표로 돌 수가 없는데 세개만 채우면 예외발생)
			// 자료형이 컬럼의 자료형과 맞지않는 값을 Bind(타입 다른거)
			
// 12:18 지금 단계에서 두개정도만 기억하면 될 듯
			// pstmt.setString(홀더순번, 값)
			// => '값' (양옆에 홑따옴표를 감싼 상태로 알아서 Bind)
			// 숫자는 따옴표가 붙으면 안됨, 그런 경우에는 setInt로 담으면 됨
			// bind할 때 안붙임
			// pstmt.setInt(홀더순번, 값)
			// => 값 (알아서 잘 들어감 따옴표 안붙임)
			
			// Statement 할때보다 편해짐
			// 미완성된 SQL문을 완성형태로 만들었음
			
			// 실행하고 결과도 받아와야함
			// 4, 5) DB에 전달된 SQL문을 실행하고 결과(처리된 행 수) 받기
			// 실행은 뭐가지고 하나요? pstmt, 실행할 때 메소드는 executeUpdate, SQL문을 아까 던짐, 이거 할 때는 인자로 넣으면 안됨, 넣으면 메소드를 자기께 아니고 부모꺼 부르게됨
			result = pstmt.executeUpdate();
			
			// 6) 트랜잭션 처리
			if(result > 0) {
				conn.commit();
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 7) 다쓴 JDBC용 객체 자원반납 => 생성의 역순으로 close() 호출하기
			try {
				
				if(pstmt != null) {
					pstmt.close();
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
		return result;
		
	}
	
	// 2번은 findAll, 인자는 따로 없고 멤버들을 담은 리스트의 주소를 반환
	public List<Member> findAll() {
		
		// 0) 필요한 변수 세팅
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null; // 결과가 돌아올것 필요 SELECT문이니까
		
// 12:35 나중에 돌려줘야함, 멤버 객체만 담을 수 있는 리스트가 필요함
		List<Member> members = new ArrayList();
		
		// SQL문을 미리 준비 안하고 가보자
		try { 
			
			// 1) JDBC Driver등록
			Class.forName(DRIVER);
			// 이거 계속하기 열받음 오타라도 줄이고싶음,복붙도 좋은 방법
			// 실수를 줄일 수 있는 방법들을 생각해서 활용해야함
			// 조금 더 기술적으로 개발자스럽게 접근해본다면?
			// 문자열 형태의 데이터값을 계속 반복해서 활용하고 있음, 값의 반복, 재활용 --> 변수
			// 클래스 내부의 메소드들에서 모두 동일하게 사용하고싶으니까 필드로 빼놓는게 좋겠다
			
			// 2) Connection 생성
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			// 와~ 이게 생산성 향상이지, 보기편하고 쓰기편하고 오타날까봐 노심초사 안해도되고
			
			// 3) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(
										  """
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
										   """
										  ); // SQL문을 인자로 전달해야하는데 미리 준비 안해서 없음, 여기서 만들어
			// 만들면서 SQL을 미리 준비시킨 것
			// Statement와 별 차이가 없음, 완성된 SQL이니까 똑같이 동작할것임
			// 웬만하면 PreparedStatement를 쓰는 것이 좋음
			
			// 아까 INSERT랑 뭐가 다름? 걔는 구멍을 채워줘야 돌아감
			// 얘는 미완성된 친구가 아니고 충분히 돌릴 수 있는 완성된 상태 --> 바로 실행하면 됨!
			// 실행하고 결과도 받아와야지
			// 4, 5) SQL(SELECT)문을 실행 후 결과(ResultSet)받기
			// 조심해야할 것 executeQuery할 때 인자 넣지 말자
			rset = pstmt.executeQuery();
			
			// 6) 결과값 매핑
			// 조회결과가 존재하는가를 먼저 판단한 뒤
			// 존재할 경우 한 행씩 접근해서 컬럼의 값을 뽑아서 VO필드에다가 매핑(시원하게 ㅎ)
			while(rset.next()) { // 전체조회했으니까 몇개일지모름, 결과가 어떻게 오지? 생각을 잘 해야함
				
				// 들어오면 조회 결과가 있다
				Member member = new Member();
				// 왜 member 객체를 만들어요? 필드에 넣어주려고?
				
				// 필드에 뭘 넣어주고 싶은것임? 현재 커서가 존재하는 행에서 무슨 컬럼값을 넣고싶음?
				member.setUserNo(rset.getInt("USERNO")); // 데이터가 rset에 들어있으므로 접근해서 내놔(get), int해야함(USERNO가 NUMBER이므로)
				member.setUserId(rset.getString("USERID"));
				member.setUserPwd(rset.getString("USERPWD"));
				member.setUserName(rset.getString("USERNAME"));
				member.setEmail(rset.getString("EMAIL"));
				member.setEnrollDate(rset.getDate("ENROLLDATE"));
				
				// 이게 반복할때마다 생기니까 여러개임, 리스트에 담아야함
				members.add(member);
				
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
// 14:13 이유
			// 7) 사용이 모두 끝난 JDBC용 객체 반납(생성된 순서의 역순으로)
			// 우리는 체감이 안됨, 몇천면, 몇만명, 몇십만명이 해서 그만큼 남음
			// 트래픽이 몰리면 몰릴수록 이게 많이 생겨나는거니까
			// 나 하나 쯤이야 하면 안댐 지구를 지켜야해 개발자가 많으니까 지구온난화가 오는거아니에요
			// AI가 겁나 쓴다고요 전기, 연산하려고 그래픽카드 돌려가지고
			try {
				
				if(rset != null) {
					rset.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
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
		
		// 8) 컨트롤러에게 결과반환
		return members;
		
	}
	
	/*
	 * PreparedStatement가 Statement보다 좋음
	 * 
	 * 왜냐하면?
	 * 1. 구문분석 및 컴파일 최적화
	 * 
	 * stmt.executeUpdate(sql);
	 * pstmt.executeUpdate();
	 * 
	 * SQL을 실행할 때 SQL을 분석하는게 필요함, 자바에서 쓰는 문자열형태를 DB에서 돌릴 수 있게 컴파일의 과정이 필요함
	 * Statement는 이 메소드를 호출할 때마다 SQL을 전달하기 때문에 그때마다 컴파일을 해서 그에 대한 연산이 들어감
* 14:24 똑같은 SQL을 수행한다고 했을 때 pstmt는....?
	 * 그 부분에서 성능최적화가 되어있음
	 * 
	 * Statement는 매 번 SQL문을 파싱하고 실행하지만
	 * PreparedStatement는 SQL쿼리를 최초 1회망 파싱하고 실행계획을 캐싱(메모리에올림)
	 * 
	 * 재사용적인 측면에서 훨씬 효율적임
	 * 
	 * 2. DB서버에 대한 트래픽 감소
	 * 1과 연관, stmt는 매번 SQL을 통째로 보냄
	 * pstmt는 실행하기 전에 값만 보냄(우리가 바인딩하는거)
	 * 바인딩 효율적인 측면이 pstmt가 훨씬 좋음
	 * DB서버도 결국 마지막에는 컴퓨터로 들어가야함, 케이블로 들어가는데 케이블은 한번에 받을 수 있는 한계가 있음
	 * 
	 * 쿼리 자체는 한 번만 전송하고 이후에는 바인딩할 값(parameter)만 전송하기 때문에 효율적
	 * 
	 * 우리가 느끼기에 그렇게 어마어마한 차이가 나는 건 아니지만
	 * 똑같은 쿼리를 계속 반복해야한다면 반복할수록 pstmt가 더 좋음
	 * 
	 * 동일쿼리를 반복 실행할 때, 높은 트래픽이 몰리는 애플리케이션일 때 더욱더 효율적이다.
	 * 
	 * DB작업할때는 리소스를 많이 잡아먹는게 계획 세울 때, 처음 커넥션 만들때도 계획을 세움, 여기도 자원을 많이 잡아먹음
	 * SQL은 실행할 때마다 계획을 세움, 어떤계정, 테이블, 테이블에 컬럼이 뭐있는지 등등 --> 얘도 자원을 많이 잡아먹음, 연산이 많이 들어가는 부분
	 * pstmt는 그부분을 줄일 수 있어서 효율적임
	 * 
	 * 3. SQL Injection 방지
	 * 예) 인증 기능 구현(보통 로그인, 사용자가 맞는지 확인하는 과정)
	 * -- 사용자의 아이디와 비밀번호를 가지고 이메일을 알려주는 기능
	 * SELECT EMAIL FROM MEMBER WHERE USERID = 'admin' AND USERPWD = '1234';
	 * 사용자가 일치하는걸 입력하면 이메일을 얻을 수 있을것임
	 * 만약에 Statement로 만든다고 가정하면
	 * SELECT
	 *        EMAIL
	 *   FROM
	 *        MEMBER
	 *  WHERE
	 *        USERID = '" + m.getUserId() + "'"
	 *    AND
	 *        USERPWD = '" + m.getUserPwd() + "'"
	 * 앞뒤로 홑따옴표, getter로 넣겠지
	 * 만약에 사용자가 id와 비밀번호에 적고싶은걸 적어버렸다면?
	 * 사용자의 입력값 == ' OR '1='1
	 * admin이 들어갈 자리에 ' OR '1'='1 이 들어가면 여기에 해당하는 데이터가 모두 나오게됨
	 * OR 연산자가 AND 연산보다 우선순위가 높음
	 * 개발 공부를 좀 해서 쿼리 쓸 때 우리가 쓸 것을 예상하고 이렇게 쓴거겠죠
	 * 앞은 빈문자가 되고, 뒤의 것은 참, 둘다 TRUE가 되어서 다 뚫림
	 * UNION 같은 것을 이용할 수도 있고
	 * 이걸 SQL injection이라고 함(주입공격)
	 * Statement는 이걸 막을 수가 없음
	 * 
	 * PreparedStatement는 인젝션 방지가 됨 ==> 보안 적인 측면에도 훨씬 좋음
	 * 
* 14:36 여러가지 측면에서 효율적이므로 PreparedStatement를 쓰자
	 * 
	 */
	
	// ID가 똑같은 애들 찾아서 똑같은거 반환해주는거
	// 사용자가 입력한 아이디 전달했음
	// 나중에 DAO에서 Member형의 무언가를 돌려줘야함
	public Member findById(String userId) {
		
		Member member = null; // JDBC 절차와는 무관하니까 0번에서 빠져도 되지 않을까? 결국 이거 돌려줘야하는데
		
		// 0) 필요한 변수들 선언
		Connection conn = null; // 얘는 무조건 있어야지
		PreparedStatement pstmt = null; // SQL문 실행해야하니까
// 14:40 뭘 준비함? SQL, Injection 공격을 방지할 수 있음
		// Injection은 SQL 말고도 종류가 많음(XSS 등)
		// OWASP Top Ten, 유명한 사이버공격 순위 점유율
		
		ResultSet rset = null; // SELECT문 하는거니까 결과 받으려면
		
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
						       USERID = ?
					 """;
		// PreparedStatement 쓸거니까 홑따옴표 안붙이고 위치홀더 써버리기, 미완성 SQL문
		// 준비 다한듯
		
		try {
			
			// 1) JDBC Driver 등록
			Class.forName(DRIVER);
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			// 3_1) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql); // 접속 해야 SQL 편집기 열 수 있음, 메소드를 호출할 때 준비해둔 SQL문을 인자로 전달해야함
			
			// 물음표가 보이는데? 완성을 해야함
			// 3_2) 값 채우기
			// 빵꾸 한개 뚫림, 값을 묶어주는 방법 --> pstmt의 set뭐시기 메소드 --> SQL에서 따옴표 앞뒤로 붙여야함 --> setString
			pstmt.setString(1, userId); // 몇번째 빵꾸에 묶을건지 쓰고 뭘 넣을지 씀
			
			// 완성했으니까 실행할 수 있음
			// 4, 5) SQL문 실행
			// execute뭐시기로 할 수 있음, SELECT문임, 질의를 날려야 함 --> Query
			rset = pstmt.executeQuery(); // 메소드 설명에 never null, 이게 중요함
			// SELECT 문 수행하면 결과가?
			// 클라이언트가 DB서버에 리퀘스트를 보내는것
			// 서버는 리퀘스트를 받으면 반드시 응답(RESPONSE)을 줘야함
			// 서버의 response가 null이 아니고 ResultSet(SELECT문 수행 결과)이 온 것
			// 무조건 ResultSet이 오는거고 이게 0행이온것임
			// 이걸 하면 무조건 ResultSet이 온다는 뜻
			
			// 6) rset에 값있나없나 판단 후 있다 VO필드에 매핑
			// 결과가 몇개옴? 많아봤자 ID로 조회했으니, UNIQUE 제약조건이니까 하나임
			// 만약에 조회결과가 없었어, 그럼 null이 온다고 쳐, 그럼 이거 못함
			// 조회결과가 없어도 rset을 참조해서 메소드 호출을 할 수 있어야함
			if(rset.next()) {
				
				// if문 블럭 안에 들어온다는 것은 조회가 됐다, UNIQUE 제약조건인데 하나 있었다
				member = new Member(rset.getInt("USERNO") // 매개변수 생성자 이용, 순서 중요
								  , rset.getString("USERID")
								  , rset.getString("USERPWD")
								  , rset.getString("USERNAME")
								  , rset.getString("EMAIL")
								  , rset.getDate("ENROLLDATE"));
				
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 7) JDBC 다 썼따~~ 자원 반납은 생성의 역순으로
			try {
// 15:10 null이면? NullPointerException
				if(rset != null) { // UE
					rset.close();
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
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
		
		// 8) 결과 반환(먼저 작성해놓고 시작)
		return member;
		
	}
	
	// 이름 키워드로 검색
	public List<Member> findByKeyword(String keyword) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		// 여기까진 조회니까 확정
		
		// 여러개 조회될 수 있으니 리스트에 담아가자
		List<Member> members = new ArrayList();
		
		// 실행할 SQL문
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
						       USERNAME LIKE '%'||?||'%'
						 ORDER
						    BY
						       ENROLLDATE DESC
					 """;
		// 이렇게 해도 되나요? --> 연결 연산자 써서 수정
		
		// 예외처리 딸깍딸깍으로 하자
		try {
			
			Class.forName(DRIVER);
			
			// 딸깍딸깍 예쁜거 골라서 해야함
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			pstmt = conn.prepareStatement(sql); // 만들면서 SQL문 넣어야함
			
			// 빵꾸채워서 SQL문 완성
			// pstmt 참조해서 setString 호출
			// addBatch는 SQL문 모아다가 한꺼번에 실행하는 시스템.. 정말 형편없죠
			// pstmt.setString(1, "%" + keyword + "%"); 문자열 더하기 하는거라서 별로 마음에 안든대요
			// 문자열 더하기 안하고싶은데 다른 방법이 없을까요? 이렇게 해도 되긴 하지만 문자열 더하기 너무 하기 싫다, 효율 최대로 땡기고싶다 --> SQL문 수정
			pstmt.setString(1, keyword);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				members.add(new Member(rset.getInt("USERNO")
									 , rset.getString("USERID")
									 , rset.getString("USERPWD")
									 , rset.getString("USERNAME")
									 , rset.getString("EMAIL")
									 , rset.getDate("ENROLLDATE")));
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			// CE는 try-catch 딸깍 가능
			try {
				
				if(rset != null) { // UCE는 개발자가 해줘야함
					rset.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return members;
		
	}
	
	// 업데이트
// 16:01 PasswordDTO 계층간의 데이터를 담고 싶은데 ?? getter/setter 가질 수 있음
	// VO setter 가질 수 없음, DTO 배우기 전이라 setter 만들어서 썼음
	public int update(PasswordDTO pd) {
		// update 할 일 : 전달받은 값을 가지고 값이 존재하는 행을 찾아서 정보를 갱신해줌
		// 얘가 맡은 일 : SQL문 실행하고 결과받아오기
		
		// 0)
		int result = 0; // var로 하면 타입추론, 들어오는거에 맞춰서, 옛날엔 너무 느려져서 못썼지만 지금은 쓸만함
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = """
						UPDATE
						       MEMBER
						   SET
						       USERPWD = ?
						 WHERE
						       USERID = ?
						   AND
						       USERPWD = ?
					 """; // UPDATE문도 WHERE가 먼저 돈다, 그럼 만약에 바인딩할때 순서를 이렇게 가야하나? 는 아니고 무조건 앞에서부터 ? 위치 셈
		// 일렬로 있다고 가정하고 맨 왼쪽부터 1, 2, 3, ... 지금은 위에서부터 차례로(USERID의 물음표가 맨 처음이 아님)
		
		try {
			
			// 1) Driver 등록, 귀찮았지만 우리에게는 변수도 있고 딸깍도 있다, 알고 써야함, 모르고 쓰면 의미 없음
			Class.forName(DRIVER);
			
			// 2)
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			conn.setAutoCommit(false);
			
			// 3)
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pd.getNewPassword());
			pstmt.setString(2, pd.getUserId());
			pstmt.setString(3, pd.getUserPwd());
			
			// 4, 5)
			result = pstmt.executeUpdate();
			
			// 6) 오토커밋 껐으니까 커밋까지
			if(result > 0) {
				conn.commit();
			}
			
		} catch (ClassNotFoundException e) { // ClassNotFoundException | SQLException으로 작성해서 여기에 한번에 하는건 구닥다리 자바에서는 못할수도 있음
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 7) 딸깍딸깍 + 널체크
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// 8)
		return result;
		
	}
	
	public int delete(Member member) {
		
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = """
						DELETE
						  FROM
						       MEMBER
						 WHERE
						       USERID = ?
						   AND
						       USERPWD = ?
					 """;
		
		try {
			
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			pstmt = conn.prepareStatement(sql);
			
			// 이번엔 오토커밋 안끔
			
			pstmt.setString(1, member.getUserId());
			pstmt.setString(2, member.getUserPwd());
			result = pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
		
	}
	
}
