package com.kh.statement.controller;

import java.util.List;

import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

/**
 * VIEW에서 넘어온 요청을 처리해주는 클래스입니다.
 * 1. 메소드로 전달된 데이터를 가공처리 한 뒤 DAO로 전달합니다.
 * 2. DAO로부터 반환받은 결과에 따라서 VIEW로 응답결과를 반환
 * 
 */
public class MemberController {
	
	/**
	 * VIEW로 부터 전달받은 문자열값 다섯 개를 DAO로 전달하는 메소드
	 * 
	 * 매개변수 적어놓으면 파라미터에 요거 자동완성으로 올라옴
	 * @param userId : 사용자에게 입력받은 아이디 값(정규표현식 배우고 조건 쓸 수 있음)
	 * @param userPwd : 사용자에게 입력받은 비밀번호 값
	 * @param userName : 사용자에게 입력받은 이름 값
	 * @param email : 사용자에게 입력받은 이메일 값
	 * 
	 * 이런거 적을 수 있음, 하나하나 다 적으면 시간이 너무 오래 걸리니까 예시만 적고 넘어감
	 * @return : 반환 어떻게 할건지
	 * @throws : IllegalArgumentException : 잘못된 인자가 전달되면 발생할 수 있음
	 * 
	 */
	// 밖에서 불러야 하니까 public, 반환은 아직 뭘로 할 지 못 정해서 void, 메소드명 save, 매개변수는 총 네개(다 문자열)
	public int save(String userId, String userPwd, String userName, String email) {
		
		// 최종 목적은 이거 네개 가지고 DB의 테이블에 한 행 인서트 하는것
		// 넘거야하는데, 그냥 넘겨도 상관없는데 나중에 더 배우면 네개로 넘기면 돌릴 수 없음
		// 나중에는 DAO로 넘어갈때 무조건 한개로 넘어가야함 안그러면 코드를 엄청 주구장창 써야함
		// 당분간은 여기서 계속 데이터 가공
		// 1. 데이터 가공 => 넘어온 인자값이 두 개 이상일 시
		// 				   하나의 어딘가에 담을 것(VO, Map, DTO)
		// 하나일때는 담을 필요 없음, 인자값이 두개이상이면 무조건 담을것
		// 네개의 값을 받은 목적은 테이블에 한 행 인서트 하기 위한 값
		// 우리는 테이블의 한 행에 대한 정보는 Member에 담기로 했음, Member를 이 목적으로 만들었음, 테이블 한 행에 대한 데이터를 기록해야돼? 그럼 여기다 담아야지 하고 그 목적으로 이걸 만들었음
		// Map에 담으면 멤버를 만든 의미가 없음, 테이블에 한 행 담기 위한 값을 받은거니까 멤버에 담자, 이친구는 저걸(네개의 값) 담기위한 저장소를 가지고 있음(필드)
		// 테이블에 한 행 값 담아야지 하고 그 목적으로 만든 것
		// 전달된 인자값들을 Member객체의 필드에 담기! 방법 두가지가 있음
		// 1) 매개변수 생성자를 호출하여 객체 생성과 동시에 필드값을 대입하는 방법
		// 2) 기본생성자로 객체를 생성한 뒤 setter 메소드를 호출하는 방법
		Member member = new Member(userId, userPwd, userName, email); // 1번을 위해서 우리가 생성자를 추가로 만들었음
		// 데이터 가공하면 컨트롤러의 1절 끝
		// JDBC 이전에는 저장소를 컨트롤러 내부 최상단에서 ArrayList 만들어서 올렸음, 지금은 내 컴퓨터 말고 서버에다가 올려야함
		// 이렇게 만들어진 필드의 값을 담은 member의 주소값을 DAO로 넘겨주겠다
		
		// DAO가 메모리에 올라가 있는 상태여야 넘길 수 있음
		// 메모리에 올리려면 객체생성, new 하고 생성자 호출 --> 멤버를 전달 --> 메소드
		// DAO로 넘어감
		int result = new MemberDao().save(member);
		
		// 2. 요청 처리 후 결과값 반환
		return result;
		
	}
	
	public List<Member> findAll() {
		
		// 1. 데이터 가공 == 할 게 없음
		
		// 2. DAO 호출
		List<Member> members = new MemberDao().findAll();
		
		// Controller는 이제 뭐해줘야함? 여기 왜 왔음? View가 회원들 데이터가 필요해, 주지않을래? 해서 왔음
		// 이제 반환해줘야함
		// 3. 결과값 반환
		return members;
		
	}
	
	/**
	 * 사용자에게 입력받은 아이디 값을 이용해서 검색요청을 처리해주는 메소드
	 * 
	 * @param userId : 사용자가 입력한 검색하고자하는 문자열
	 * 
	 */
	public Member findById(String userId) {
		
		// 1. 데이터 가공 => 넘어가넘어가~
		
		// 2. 요청처리 => DAO 객체 생성 후 메소드 호출
		// 지금은 DAO밖에 없지만 곧있으면 늘어남, 사용자의 요청을 처리해줄 수 있는 애한테 떠넘기는것, 지금은 DB로 넘어가는것
		Member member = new MemberDao().findById(userId);
		// 요청을 처리해주고 결과를 반환해주는것
		// DAO로 넘어감
		
		// DAO에서 돌아오면 Member 타입의 member가 돌아옴, 이걸로 어떻게 해야함? View에게 요청 처리 후 결과값을 반환해야함
		// 3. 요청 처리 후 결과값을 View에게 반환
		return member;
		
	}
	
	public List<Member> findByKeyword(String keyword) {
		
		// 뷰에서 생각하고 넘어왔어야하는데 여기서 생각을 해보자
		// 결과값이 나중에 어떻게 돌아올까??
		// 이번에 보낼 SQL문은 SELECT, 결과가 ResultSet으로 올텐데 이걸 매핑해서 가져오겠지? 뭘로 매핑? Member의 필드에 조회된 결과를 담아서
		// 이름에 이 키워드가 포함된 친구들을 전부다 가져올건데, Member 하나에 다 담을 수 있나? 없음, 근데 Member가 몇개인지 모름, 어딘가에 담겨와야함
		// SELECT -> ResultSet -> Member -> List<Member>
		List<Member> members = new MemberDao().findByKeyword(keyword);
		
		return members;
		// 이렇게 여기서 다 완성하고 가는거지, 얘는 뭐 많이 하면 안됨, 깔끔해야함
		// DAO ㄱㄱ
		// DAO 다녀오니 이건 아까 끝냈음, 바로 View ㄱㄱ
		
	}
	
	public int update(String userId, String userPwd, String newPassword) {
		
		// 1. 데이터 가공
		// 고민을 해야할 필요가 있음, 3개인데 DAO를 두번가야함, 한번에 가도 되겠다?
// 9:20 SQL문
		// 만약에 admin을 바꾸려면? 사용자가 입력한 아이디와 비밀번호를 들고가서 조회를 해야함(SELECT)
		// 있으면? UPDATE MEMBER SET USERPWD = '1111' WHERE USERID = 'admin' AND USERPWD = '1234;
		// 아니면 식별자가 USERNO니까 서브쿼리 써서 한번에
		// DAO를 한번만 가도됨
		// 가공이라고 하지만 실질적으로 우리가 하는게 뭐 하는거임? 인자값이 2개 이상일 때 어디 하나에 담을지?
		// VO는 newPassword를 담을데가 없음, 그럼 어떻게 함?
		// 안해본걸로 해보자, 하나 더 만들러 ㄱㄱ --> model 패키지에 새로 DTO 만들고옴
		
// 9:27 가공...?
		// DTO에 새로은 값들을 담아주잣!!
		PasswordDTO pd = new PasswordDTO(userId, userPwd, newPassword);
		// 가공했음!
		
		// 가공한다음 뭐해야함?
		// 2. 요청처리
		// 아이디랑 비밀번호랑 바꿀비밀번호줄테니까
		// 이거 아이디랑 비밀번호 맞는지 확인하고 바꿀비밀번호로 비밀번호컬럼값 바꿔줘
		// 이거 컨트롤러가 못함
		// DAO야 이거 줄게 해줘
		// 뭐 줘야함? pd, 여기에 다 있음
		int result = new MemberDao().update(pd);
		
		// 3. 뷰로 결과반환(DAO가 컨트롤러에게 준걸 다시 뷰로)
		return result; // 반환형 수정
		
	}
	
	// 뷰에서 이미 다 정하고 왔음 나중에 뷰에다가 정수 돌려줄거임
	public int delete(String userId, String userPwd) {
		
		// 1. 데이터 가공
		// VO에 담아도 되겠다, setter로 넣어야겠다, 두개받는 생성자 없으니까
		Member member = new Member();
		member.setUserId(userId);
		member.setUserPwd(userPwd);
		
		// DAO로 갈것임, member 넘길거고 컨트롤러가 DAO에서 나중에 뭐 받을거임? 정수, DML구문이니까 처리된 행이 몇행인지
		// 2. 요청 처리
		int result = new MemberDao().delete(member);
		
		// 받은 정수를 뷰에게 다시 뷰야 받아라~
		// 3. 결과반환
		return result;
		
		// DAO로 delete 만들러
		
	}

}
