package com.kh.statement.model.service;

// 하나하나 적으면 나중에 일일히 고쳐줘야함, 원래는 메소드명 하나씩 적는게 정석
// 이 클래스 안의 모든 스태틱 메소드는 import되어서 사용할 수 있음 --> 사용할 때 클래스명을 안붙일 수 있음, 메소드만 작성해서 호출해서 사용할 수 있음
// 이렇게 하면 이 클래스 내부에서는 스태틱 메소드 호출할 때 클래스명 안붙여서 쓸 수 있다, 너무 날로먹는것같으니까 DAO에서는 원래대로 하자, 계속 스태틱 쓴다는것을 인지해야함
import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

// 클라이언트의 요청처리를 제어흐름은 컨트롤러가 담당하고 핵심 로직 실행은 서비스가 담당할 것, 분리분리~
// 클라이언트의 요청처리
// 제어흐름 --> 컨트롤러
// 핵심로직 실행 --> 서비스

/*
 * 실질적 우리 밥줄, 여기가 하는일이많음
 * 
 * 비즈니스 로직 실행(의사결정코드) -> 데이터 가공, 중복 체크, 연산 처리, 암호화(이런것들 전부 서비스에서 담당)
 * 트랜잭션 관리
 * 여러 DAO를 조합(지금은 멤버 DAO만 쓰는데 테이블이 많아지면 그만큼 DAO 늘어남)
 * 예외 처리 및 변환
 * 보안 및 권한 검사
 * 
 * 비즈니스 로직이라는 표현은? 내가 이 기능을 생각했을 때 실질적으로 돌아야하는 동작들
 * 
 */
public class MemberService {
	
	// private MemberDao md = new MemberDao();
	private Connection conn = null;
// 컨트롤러에서 만들어서 호출하고 있으니까 여기서 기본생성자를 만들어주고, 기본생성자가 호출될 때 필드값을 getConnection으로 초기화해버리면
// 생겨날때 Connection을 받아올테니까 매번 커넥션 객체생성은 안해도 되지않을까? 반납은 서비스에서 해주되, 얘를 필드로
	public MemberService() {
		this.conn = getConnection();
	}
	
	// 데이터 가공을 이미 컨트롤러에서 다 해놔서 만들어놓은걸 쓸것임
	/*
	 * Service : 비즈니스로직 / 의사결정코드를 작성하는 부분
	 * 			 Controller -> Service의 메소드를 호출
	 * 			 Service에서 Connection을 생성해서 DAO로 전달
	 * 			 만약 SQL문을 수행해야하는데 필요한 값이 있다면 Controller로부터 전달받아서
	 * 			 Connection과 같이 넘겨줄 것
* 10:08 			 (넘어갔다가) DAO에서 DB작업이 끝나면(DAO에 있는 메소드가 Service를 호출하므로 Service로 리턴할건데???) Service단에서 결과에 따른 트랜잭션 처리도 진행
	 * 
	 * 			 => Service를 추가함으로 DAO는 순수하게 SQL문을 처리하는 부분만 남겨둘 것
	 * 
	 * 컨트롤러와 DAO 사이에 Service라는 계층을 만들었음
	 * 컨트롤러에는 DAO의 메소드가 아니라 서비스의 메소드를 호출할것임
	 * 
	 */
	
	public int save(Member member) {
		
		// 서비스에서 커넥션을 만들것임, 커넥션을 만드려면? 드라이버 등록, 템플릿에 만들어놓은 메소드 호출
		// 호출하려면? 스태틱 메소드의 호출은? 클래스명 작성 -> 참조 -> 호출하고 싶은 메소드 호출
		// JDBCTemplate.resistDriver(); 얘는 돌때 한번만하면되는데 굳이 여기 없어도 되지않나? 다른데 넣어도 되지않나? --> 선택지는 여러개 있었는데 MemberRun(메인 메소드)에 넣음
		// MemberService의 생성자에 넣어도 되고, static블록을 만들어서 넣어도 되고(그럼 한번만 돌테니까)
		// 드라이버등록이라는것은 멤버와 관련된 핵심코드는 아님, 멤버에 작업하는것과는 연관이 없는 다른 코드라 서비스에 넣기는 애매해(서비스는 멤버와 관련된거니까)
		// 메인에 넣는 거 괜찮은 선택이다! 드라이버 등록은 메인 메소드로 날려보냄
		
		// Connection 객체 생성
		// 템플릿에 해놨는데, 어떻게 갖다씀? 클래스명 -> 참조연산자 -> 메소드 호출 --> 커넥션 타입이 반환됨
		// Connection conn = getConnection();
		
		// DAO 호출 시 Connection객체 전달
		// +
		// Controller가 넘겨준 사용자가 입력한 값이 필드에 담겨있는
		// Member 참조변수를 함께 넘겨줌
		// 회원 추가하고 있으니까 DAO 입장에서는 INSERT 하려면 뭐가 있어야함? 커넥션만 넘길 수 없음, DAO가 save 하려면 사용자가 입력한 추가하고 싶은 값이 필요함, 그건 member에 담아뒀으니까 conn이랑 같이 member도 넘겨줘야함
		int result = new MemberDao().save(conn, member); // --> DAO ㄱㄱ
		
		// DAO에서 지나치고 넘어온 작업이 있음! 트랜잭션 처리
		// 트랜잭션 관리는 여기서함, 지금은 큰의미가 없지만 나중에는 하나의 서비스에서 여러개 DAO의 메소드를 호출한것임
// 10:28 DAO에서는 하나의 메소드가 하나의 SQL문을 수행하도록 만들어야하는데, 하나의 SQL문이 세개를 한다면 하나의 트랜잭션으로 묶어서 처리해야함??
		// 6) 트랜잭션 처리
		// 성공했으면 커밋, 실패했으면 롤백 해줘야함, 성공실패여부는 result(처리된 행의 개수)가 0보다 크면 성공이겠다
		if(result > 0) {
			commit(conn);
		}
		
		// 커넥션 이제 자기 할 일 다했음, 트랜잭션 처리 해줬으니까
		// 7_2) 자원반납
		close(conn);
		
		// md = null;
		
		// 컨트롤러로 결과 반납해주기
		return result;
		
	}

// 10:35 요약
	/*
	 * 
	 */
	
	// 인자로 전해준 게 없으니까 리스트만 돌려주면 될 듯
	public List<Member> findAll() {
		
		// 1) Connection 객체 생성
		// Connection conn = getConnection();
		// 이렇게 나눠나서 코드가 간결해지고 있음! 알차다~
		// 근데 마음에 안들어 클래스이름 쓰기 귀찮음, 안쓰는방법? 스태틱 메소드만 가지고 있는 클래스인데, 스태틱 메소드 호출할때마다 클래스이름 붙이기 번거로운 상황에서 메소드만 import해올 수 있는 방법이 있음
		// import문 수정하고 클래스 이름 쓴 곳 수정함
		
// 이 다음에 할 일
		// 2) DAO호출해서 반환받기
		// Service에서 받아온 Connection 넘겨주기 + 만약에 Controller가 넘겨준 값이 있다면
		// 같이 넘겨줄 것
		// 이번엔 컨트롤러가 서비스에게 넘겨준게 딱히 없음
		// DAO 필드로 뺄까? 빼는게 낫겠지? 왜 굳이 만들어서 쓰고있음? 아냐 만드는게 맞아
		// 왜냐하면 컨트롤러에서 서비스를 필드로 안빼놨음 서비스 처음하는거라서
		// 만약에 필드로 받아놓으면 그때마다 4바이트씩 생겨서 공간을 먹음
		// 나중에 서비스가 죽을때 DAO도 같이 죽게 해야함, 근데 언제 사라질 지 모름
		// 만약에 여기서 필드로 두겠다고 하면? 명시적으로 자원반납할때 널값을 대입해서 메모리 누수 없이 명확하게 반납될 수 있도록 코드를 짜야함
		// 물론 서비스가 죽으면 DAO가 죽겠지만, 개발자가 명시적으로 작성해주는게 좋긴한데 그렇게하면 자바의 장점중에 하나가 사라짐.. 내가 메모리 생각 안해도 돼서 개발하기 편한게 장점인데 이걸 내가 써버리면? 자바의 장점, 자바를 사용하는 큰 이유중에 하나가 사라져버림
		List<Member> members = new MemberDao().findAll(conn); // 컨트롤러에서 딱히 넘겨준게 없기 때문에 커넥션만 같이 넘겨줌
		// DAO 갔다왔더니 리스트가 오긴 왔음
		
		// 아까(DAO에서) 여기서 커넥션 자원 반납하기로 했음
		// 3) Connection 반납
		close(conn);
		
		// 리스트가 온 걸 컨트롤러에게 돌려줘야함, 컨트롤러가 달라고 해서 갔다온것임
		// 4) 결과반환
		return members;
		
	}
	
	// 유니크 제약조건이니까 결과 하나로 옴, 한행의 데이터는 하나의 VO객체의 필드에 (매핑해서)담아감, 반환형 Member
	// 서비스 분리했지만 아직 많이 복잡하지 않음, 나중엔 얘만 복잡해짐
	public Member findById(String userId) {
		
		// 1) Connection 객체 받아오기
		// Connection conn = getConnection(); // 아 이거도 중복인데? DAO는 필드로 안두지만, 커넥션은 필드로 두면? 요청이 들어올때마다 만들어야하잖아
		// 일단 만들어놓고 널값으로 초기화
		
// 진짜 1) DAO호출(Service 생성자에서 받아온 Connection + Controller가 준 두개??
		Member member = new MemberDao().findById(conn, userId); // DAO ㄱㄱ
		// 갔다와서 멤버로 받아줌
		
		// 다음에 커넥션 받으려면 반드시 커넥션 반납
		// 2) Connection 객체 반납
		close(conn);
		
		// 3) Controller에게 결과 반환
		return member;
		
	}
	
	public List<Member> findByKeyword(String keyword) {
		
		// 할 일 !
// 11:40 커넥션 만들고 ~~~~ 하기
		// 1) Connection 만들기 => 기본생성자에서 벌써함!
		
		// 2) DAO 호출! 나중에 결과 List<Member>로 받아야함
		List<Member> members = new MemberDao().findByKeyword(conn, keyword); // 연결해야하니까 커넥션 전달하고 키워드 전달
		
		// 3) Connection 반납
		close(conn);
		
		// 4) 결과반환(컨트롤러에게 DAO서 받은거 돌려줘야함
		return members;
		
		// 이렇게 해놓고 DAO ㄱ
		
	}
	
	// 의사결정코드
// 12:14 당분간은 내가 뭐하는건지 생각, DB에서 어떤작업을 어떻게 하는거지?
	public int update(PasswordDTO pd) { // 받은거 매개변수로
		
		// 회원의 비밀번호를 수정해야한다 == Member테이블에서 한 행 UPDATE
		// 똑같이 하면 재미없음
		// 비밀번호 수정
		// 실패할 수 있음, SQL문이 UPDATE MEMBER SET USERPWD = 머시기 WHERE USERID = 머시기 AND USERPWD = 머시기
		// 이렇게 생긴걸 쓸텐데, ID가 없거나 비번잘못썼거나 새 비번이 컬럼보다 크다든가 실패할 수 있는 원인이 여러가지
		// 이런것들을 구분해서 처리를 할 필요가 있음, ID나 비번이 없으면 없다고 알려주고, 다른건 다르게 예외를 일으키는 식으로
		// 비밀번호 컬럼이 20바이트인데 PasswordDTO의 새 길이가 20바이트 넘으면 DAO에 갈 필요가 없음, 애초에 컬럼에 들어갈 수 없는 값으로 DB로 갔다올 필요가 없고 그전에 자르는게 맞지
		// 값이 유효한 값인지 유효한 값이 아닌지 체크를 서비스단에서 해줌
		if(pd.getNewPassword().length() > 20) { // 이렇다면 문제가 있음
			
			// return 0; 뒤에 갈 필요가 없으니까 0을 리턴하거나
			// throw new RuntimeException("너무 긴 비밀번호예용~~"); throw는 직접 개발자가 예외를 발생시킬 수 있음
			// 비밀번호가 길면 업데이트가 안될텐데 DB갈필요없는데 해서 억지로 예외를 발생해서 다음으로 넘어가지 않도록 막는 방법
			// 값의 유효성 검증, 예외 발생시키기 모두 서비스단에서 진행함
			return 0; // 지금 예외처리 안배웠으니 그냥 0리턴
			
		}
			
		// 없는 아이디 입력했을 수도 있음
		// ID로 검증하는 기능은 이미 DAO에 구현해뒀음, SELECT문을 날렸을 때 존재하는가 그렇지 않은가는 만들어놓은 기능으로 구현할 수 있음
		Member member = new MemberDao().findById(conn, pd.getUserId());
		if(member == null) { // member가 널이면 아이디가 없는것임
			
			// throw new RuntimeException("존재하지 않는 아이디입니다.");
			return 0; // 지금 예외처리 안배웠으니 그냥 0리턴
			
		} // 없는 아이디면 여기서 예외 일어나서 와장창 됐을거고
		
		// 그게 아니라면 이제서야 비로소 가서 업데이트 할 수 있는 권한이 생긴것
		int result = new MemberDao().update(conn, pd);
		
		if(result > 0) {
			commit(conn);
		}
		
		close(conn);
		
		return result;
		
	}
	
	public int delete(Member member) {
		
		// 회원의 정보를 삭제해야지 == Member테이블에서 한 행 DELETE
		
		// 서비스에서 생각해야 할 것들
		// 1) Connection 만들어야지
		// 벌써 했다!
		
		// 2) 매개변수로 받은거하고 커넥션하고 DAO로 넘겨야지
		// 정수값 돌아올것임 DML구문이기때문에
// ID, 비밀번호 들어있는 객체 넘길거임 ~~ 설명
		int result = new MemberDao().delete(conn, member);
		
		// 3) DML이니까 다녀오면 트랜잭션처리
		if(result > 0) {
			commit(conn);
		}
		
		// 4) 트랜잭션 끝나면 Connection할 일 없으니까 반납해야지
		close(conn);
		
		// 5) 결과 반환해줘야지
		return result;
		
		// 만들어놓고 DAO로 넘어감
		
	}

}
