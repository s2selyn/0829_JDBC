package com.kh.statement.controller;

import java.util.List;

import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.vo.Member;

public class MemberController {
	
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

}
