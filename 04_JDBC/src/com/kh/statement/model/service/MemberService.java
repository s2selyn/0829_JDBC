package com.kh.statement.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

public class MemberService { // 얘 뜯어고치기
	
	private Connection conn = null;
	
	public MemberService() {
		this.conn = getConnection();
	}
	
	// save랑 delete랑 코드블럭 똑같이 생김... 이것만 그럴까? 중복되는게 너무많다, 주석 지우니 너무 잘보이네 ㅎ
	// 유효성 검증 예외처리 이런게 지금 없어서 간결하게 바꾸고싶음
	// 자원반납, 트랜잭션 처리 이런건 꼭 해야하는게 맞는데, 그게 멤버를 새롭게 추가하는거랑 직접 연관이 있냐? 그건 또 아닌듯
	// 이 메소드는 핵심 로직이 멤버 추가하는거임, 자원반납이나 트랜잭션 처리는 또 따로 DB랑 관련됨
	public int save(Member member) {
		
		int result = new MemberDao().save(conn, member);
		
		if(result > 0) {
			commit(conn);
		}
		
		close(conn);
		
		return result;
		
	}

	public List<Member> findAll() {
		
		List<Member> members = new MemberDao().findAll(conn);
		
		close(conn);
		
		return members;
		
	}
	
	public Member findById(String userId) {
		
		Member member = new MemberDao().findById(conn, userId);
		
		close(conn);
		
		return member;
		
	}
	
	public List<Member> findByKeyword(String keyword) {
		
		List<Member> members = new MemberDao().findByKeyword(conn, keyword);
		
		close(conn);
		
		return members;
		
	}
	
	public int update(PasswordDTO pd) {
		
		if(pd.getNewPassword().length() > 20) {
			return 0;
		}
		
		Member member = new MemberDao().findById(conn, pd.getUserId());
		
		if(member == null) {
			return 0;
		}
		
		int result = new MemberDao().update(conn, pd);
		
		if(result > 0) {
			commit(conn);
		}
		
		close(conn);
		
		return result;
		
	}
	
	public int delete(Member member) {
		
		int result = new MemberDao().delete(conn, member);
		
		if(result > 0) {
			commit(conn);
		}
		
		close(conn);
		
		return result;
		
	}

}
