package com.kh.statement.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.common.JDBCTemplate;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;


// 문제 1. 중복이 너무 많음
// 문제 2. DAO에서 할 일은 SQL문을 실행하고 결과를 받아오는건데, 이걸 하기 위해서 이거 이외에 하는 일이 너무 많음
// 드라이버 등록하고 커넥션 만들고 자원반납하는데 닫고 트랜잭션 처리도 하고... 얘는 외부(DB)와의 입출력해서 SQL보내고 결과 받아오면 할일 끝인데 이걸 하기 위해서 너무 많은 일을 하고있음
// 어느날 갑자기 오라클 딴거로 바꿔야지 하면 다 바뀔 수 있는건데
// DAO 메소드 각각의 JDBC코드를 중복을 없앰과 동시에 각각의 메소드가 하고있는 일이 너무 많기 때문에 하고있는일을 전부 다른곳으로 옮기고 예쁘게 만들자
public class MemberDao {
	
	// 필요없는거 딴데 옮기고 중복코드를 딴데 분리하기!
	// 우리가 자바에서 중복을 제거하는 방법은? 값의 중복은 변수로 처리하고, 코드의 중복은 메소드로 처리
	// 지금은 여기서 중복되는 코드를 뺀다고 해도 그 메소드가 여기에 있으면 안됨, 이친구는 외부와의 입출력만을 담당하는 친구니까!
	// JDBC 관련 작업들, 그중에서 반복적으로 쓰이는 구문을 분리해놓을 클래스를 하나 만들자 --> JDBCTemplate 클래스 생성
	
	// 뭐부터 해야함? 메소드 만들기, save
	public int save(Connection conn, Member member) { // 데이터 가공해서 보냈으니까 Member로 온다
		
		// 계획
		// 0) 필요한 변수 세팅~~~
		// 1) Driver등록
		// 2) Connection --> 1, 2) 이런것들은 따로 분리했음
		// 이 작업들은 DAO가 가지기에는 약간 아리까리함
		// 할일이 SQL실행만 하고 결과를 받아오고 매핑할거있으면 하는게 할일 끝
		// 모델에 새로운 친구를 넣어주자! --> MemberService 클래스 생성
		
		// 멤버만 넘어오지 않음, 서비스에서 커넥션도 넘겨주니까 매개변수 자리에 커넥션 타입도 추가
		// 이거 하면 다시 똑같이 함
		
		// 0) 필요한 변수 세팅~~~
		PreparedStatement pstmt = null;
		int result = 0;
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
					 """;
		// 커넥션은 매개변수로 받았으니까 준비 안해도 됨
		
		// 드라이버 등록은 벌써 하고왔음, 커넥션 생성해야하는데 벌써 받아왔음 --> 1, 2) 는 하고 넘어옴
		// 1) Driver등록 --> 하고옴
		// 2) Connection --> 하고옴
		
		try {
			
			// 3_1) PreparedStatement객체 생성(SQL문 미리보내기)
			// PreparedStatement 만들기
			// 커넥션 객체 참조해서 메소드 호출할 때 SQL문을 인자로 전달
			pstmt = conn.prepareStatement(sql); // 예외 처리
			// 뭐가 반환됨? pstmt 반환됨
			
			// 3_2) 미완성된 SQL문일 경우 묶어줄 값 전달하기
// 10:20 설명
			pstmt.setString(1, member.getUserId());
			pstmt.setString(2, member.getUserPwd());
			pstmt.setString(3, member.getUserName());
			pstmt.setString(4, member.getEmail());
			
			// 실행하고 결과를 받아오기(정수로 올것임)
			// 4, 5) DB에 완성된 SQL문을 실행한 결과(int) 받기
			// SQL실행은 어떻게? 실행담당자 pstmt -> 참조연산자 -> executeUpdate(DML이니까) -> 정수가 돌아옴(처리된 행의 개수)
			result = pstmt.executeUpdate();
			
			// 트랜잭션 처리 해야하는데 이건 서비스에서 할거임, 6번 패스
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 자원반납 해야하는데 메소드 호출 어디서 함? 돌아가는 코드는 템플릿에 있지만 여기서 해야함
			
			// 커넥션은 트랜잭션 처리 해야하니까 서비스에서 써야함(6번 안했음)
			// 7_1) 할 일이 다 끝난 PreparedStatement객체만 반납
			JDBCTemplate.close(pstmt);
			
		}
		
		// 8) 결과반환
		// 컨트롤러가 부른게 아니라 서비스가 불렀음, 서비스로 돌아감
		return result;
		
	}
	
	// 밖에서 불러야하니까 public, 반환형 List<Member>, 메소드도 정해짐, 매개변수 넘겨준게 있음
	public List<Member> findAll(Connection conn) {
		
		// 0) 필요한 변수 선언 먼저!
		// PreparedStatement, ResultSet, sql, List(조회된 결과를 담아서 뽑아갈 수 있도록 해줘야함)
		List<Member> members = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
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
					 """; // 정렬은 꼭 해주는게 좋다
		
		try {
			
			// 원래 드라이버 등록하고 커넥션 만들기 해야하는데 그거 다 해서 옴, 그 다음 단계
			// 3_1) PreparedStatement 객체 생성(sql문을 인자로 전달하기)
			// 하면서 예외처리
			pstmt = conn.prepareStatement(sql);
			// 원래 미완성이면 채워줘야하는데 이거 완성됨
			
			// 실행하고 결과받기 해야함
			// 4, 5) SQL(SELECT)을 실행 후 결과(ResultSet)받기
			// ResultSet은 절대 NULL일 수 없다
			rset = pstmt.executeQuery();
			
			// ResultSet 받아왔다!
			// 6) 조회결과 여부 판단 후 => rset.next()
			// 있다면 컬럼값을 객체 필드에 매핑
			// 있는지 없는지는 어떻게 판단? 커서 내려서(next 메소드 호출), 행이 몇개인지는 모름, 없을수도 많을수도 --> while
			while(rset.next()) {
				
				// while문 안에 들어왔다는거는 행이 있다는 뜻
				Member member = new Member(rset.getInt("USERNO")
										 , rset.getString("USERID")
										 , rset.getString("USERPWD")
										 , rset.getString("USERNAME")
										 , rset.getString("EMAIL")
										 , rset.getDate("ENROLLDATE"));
				// 행까지 오면 열에 가야함, 열에서 값 뽑는 방법은? rset에 참조해서 get으로 내놔, 인자로 컬럼명 전달(숫자로 넣으면 나중에 컬럼 추가되면 싹다고쳐야하고 알아보기 힘들어짐)
				
				// 멤버 담기만 하면 while 끝나면 날아감, 누군가는 얘의 주소를 가리키고 있어야 함
				// members에 담기로 함
				members.add(member);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 자원반납해야함
			
			// 7) 사용이 다 끝난 JDBC용 객체 반납
			// 생성의 역순으로 JDBCTemplate의 close 메소드를 호출
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			// 커넥션도 다 썼음, 트랜잭션 처리 안해주니까, 근데 여기서 닫냐 서비스에서 닫냐를 골라야함
			// 지금 단계에서는 어디서해도 상관없긴함, 서비스에서 닫는걸로함
			
		}
		
		// 8) 결과반환
		return members; // 리턴하면 서비스로 돌아감 다시 고!
		
	}
	
	// 밖에서 불러야하니까 접근제한자 퍼블릭, 유니크제약조건으로 셀렉트하는거니까 VO, 매개변수 두개 보냄
	public Member findById(Connection conn, String userId) {
		
		Member member = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
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
					 """; // 얘도 슬슬 마음에 안들지만 내일 고치자
		// 이까지 0번한거
		
		try {
			
			// 1, 2)는 해왔음
			// 3) PreparedStatement 만들기
			pstmt = conn.prepareStatement(sql);
			
			// 이번에 빵꾸가 뚫려있음, 뭐해줘야함? 여기는 묶어준다고 표현(파라미터를 보내서 위치랑 값이랑 짝지어줌, 묶어줌)
			pstmt.setString(1, userId); // 앞뒤로 따옴표 붙여줘야하니까 getString선택
			rset = pstmt.executeQuery(); // 실행하고 결과받기까지 하면 ResultSet 결과값이 돌아옴
			
			// 돌아온걸로 매핑하기전에 일단 커서 하나 내려보고 있나없나 체크하고
			// 이번엔 결과가 많아야 한개, 체크 한번만 하면 됨 --> if
			if(rset.next()) {
				
				member = new Member(rset.getInt("USERNO")
								  , rset.getString("USERID")
								  , rset.getString("USERPWD")
								  , rset.getString("USERNAME")
								  , rset.getString("EMAIL")
								  , rset.getDate("ENROLLDATE"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return member;
		
	}
	
	public List<Member> findByKeyword(Connection conn, String keyword) {
		
		// 서비스에서 받은거 매개변수 작성
		
		// 나중에 돌려줘야할 리스트
		List<Member> members = new ArrayList();
		
		// SQL문 실행할 객체
		PreparedStatement pstmt = null;
		
		// SELECT문 실행할거니까
		ResultSet rset = null;
		
		// SQL문 저기 쓰면 지저분하니까 미리 선언
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
					 """;
// 나중에 바인딩할때 묶는거 선호하지 않으니까 이렇게 함
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
// 위치홀더가 하나 있기 때문에
			pstmt.setString(1, keyword);
			
			// 반드시 executeQuery를 호출해서 ResultSet타입의 결과를 받아와야함
			rset = pstmt.executeQuery();
			
// 반복문을 통해서 커서 내려서 행이 존재하는지 판별해서 리스트에 요소(컬럼의 값이 필드에.....?) 추가
			while(rset.next()) {
				
// 12:00 생성자로 rset에 get해서 값을 가져와서 필드값으로 대입
				members.add(new Member(rset.getInt("USERNO")
									 , rset.getString("USERID")
									 , rset.getString("USERPWD")
									 , rset.getString("USERNAME")
									 , rset.getString("EMAIL")
									 , rset.getDate("ENROLLDATE")));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			// conn 닫으면 안됨, 서비스 가서 닫기로 했으니까
			
		}
		
		return members;
		
	} // 아까 컨트롤러랑 서비스 다 만들고 왔으니까 실행해보기
	
	// 정수값을 돌려주는 update메소드, 커넥션과 PasswordDTO 객체를 받음
	public int update(Connection conn, PasswordDTO pd) {
		
		// 결과를 반환해줄 정수 하나, SQL문 넣을 PreparedStatement 하나
		int result = 0;
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
					 """;
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pd.getNewPassword());
			pstmt.setString(2, pd.getUserId());
			pstmt.setString(3, pd.getUserPwd());
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
		
	}
	
	public int delete(Connection conn, Member member) {
		
		// 0)
		int result = 0;
		PreparedStatement pstmt = null;
		
		// PreparedStatement를 사용하고 있기 때문에 sql문을 위치홀더를 이용해서 퉁칠수있음
		String sql = """
						DELETE
						  FROM
						       MEMBER
						 WHERE
						       USERID = ?
						   AND
						       USERPWD = ?
					 """;
		
		// 1 ~ 2) 앞에서 다해왔음
		
		try {
			
			// 3_1)
			// pstmt 객체는 커넥션 객체를 이용해서 생성하는데 꼭 인자로 sql을 전달해줘야함
			pstmt = conn.prepareStatement(sql);

			// 3_2)
// 반드시 위치홀더의 값을 윛ㅁㅇㄴㄻㄴㅇㄹ
			pstmt.setString(1, member.getUserId());
			pstmt.setString(2, member.getUserPwd());
			
			// 4, 5)
			result = pstmt.executeUpdate();
			
			// 6) Service로 돌아가서 진행
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			// 7)
			JDBCTemplate.close(pstmt);
			
		}
		
		// 8)
		return result;
		
	}
	
}
