package com.kh.statement.controller;

import java.util.List;

import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.service.MemberService;
import com.kh.statement.model.vo.Member;

// Service 생겼으니까 여기 뜯어고쳐야함, Service는 우리 밥줄^_^
/*
 * 컨트롤러의 역할
 * 
 * 지금은 뷰를 멤버뷰라는 클래스로 만들어서 멤버컨트롤러를 호출함
 * 다음에 새로운 뷰를 만들 기술을 배우면 그때부터는 더이상 컨트롤러를 필드로 만들어서 쓸 수없음, 자바말고 다른걸로 만들건데 거기서는 못함
 * 컨트롤러도 갱신이 일어나야함
 * 
 * 클라이언트(View)의 요청 수신
 * (원래는 DAO를 불렀는데 앞으로는) 적절한 서비스 메소드 호출(정말로 DAO는 SQL 실행하고 결과만 받을것임)
 * 그러나서 돌아가는건 똑같지
 * 뷰 선택 및 데이터 전달
 * 
 */
public class MemberController {
	
	public int save(String userId, String userPwd, String userName, String email) {
		
		Member member = new Member(userId, userPwd, userName, email);
		
		int result = new MemberService().save(member); // 이제 MemberDao 안부르고 MemberService 부를것임
		
		return result;
		
	}
	
	// 뷰에서 전체조회 해주세요 해서 가는거임
	// 원래 DAO 호출했지만 대신 서비스 호출
	// 편하당, 서비스에서 뭘 돌려줄 지 정해져있음 -> List<Member>
	public List<Member> findAll() {
		
		List<Member> members = new MemberService().findAll();
		
		return members;
		
	}
	
// 무슨 메소드?
	// DAO 부르는 부분을 서비스 부르는 부분으로 수정
	public Member findById(String userId) {
		
		Member member = new MemberService().findById(userId);
		
		return member;
		
	}
	
	// 키워드로 검색은 여러개의 결과가 나올 수 있으므로 멤버들을 리스트로 반환받음
	// DAO 부르는 부분을 서비스로 수정
	public List<Member> findByKeyword(String keyword) {
		
		List<Member> members = new MemberService().findByKeyword(keyword);
		
		return members;
		
	}
	
	// 마무리니까 update, delete 한꺼번에, DAO 부르는 부분을 서비스로 수정
	public int update(String userId, String userPwd, String newPassword) {
		
		PasswordDTO pd = new PasswordDTO(userId, userPwd, newPassword);
		
		int result = new MemberService().update(pd);
		
		return result;
		
	}
	
	public int delete(String userId, String userPwd) {
		
		Member member = new Member();
		member.setUserId(userId);
		member.setUserPwd(userPwd);
		
		int result = new MemberService().delete(member);
		
		return result;
		
	}

}
