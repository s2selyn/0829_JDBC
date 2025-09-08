package com.kh.statement.model.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kh.common.Template;
import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.vo.Member;

public class MemberService { // 얘 또 뜯어고치기
	
	// 여기 메소드에서 DAO를 계속 호출할테니 필드로 두자
	private MemberDao memberDao = new MemberDao();
	
	public int save(Member member) {
		
		// 원래 이런 코드들 적었음
		/*
		 * Connection conn = JDBCTemplate.getConnection();
		 * 
		 * int result = new MemberDao().save(conn, member);
		 * 
		 * if(result > 0) {
		 * 		JDBCTemplate.close(conn);
		 * }
		 * 
		 * return result;
		 * 
		 */
		
		// 커넥션의 역할을 sqlSession이 대신할것임, 커넥션을 받아오는 것 대신에 sqlSession을 받아오자
		SqlSession session = Template.getSqlSession(); // 얘가 커넥션을 대체함
		
		// DAO 부를 때 커넥션 보냈는데, 이제 세션보내줌, 보내는 친구만 달라짐 --> DAO ㄱㄱ
		int result = memberDao.save(session, member);
		// 갔다오면 int형의 무언가가 온다
		
		// 돌아온걸로 뭐해야함? DML 했어, insert 했으니까 커밋(트랜잭션 처리) 해야함
		// 아까 config 파일 만들 때 트랜잭션 managed 했으면 이것도 생략가능, 우리느 JDBC 했으니까 직접 작업
		if(result >0) {
			session.commit();
		}
		
		// 트랜잭션 처리 했으면 이것도 외부와의 연결이니까 자원반납 해줘야함
		session.close();
		
		// 결과 반환
		return result;
		
		// 마이바티스 파일에 매퍼를 등록하는 과정이 필요함 --> 마이바티스 config 다시 열어서 작업
		
	}
	
	// findAll 메소드, 반환타입은 전체조회라서 List<Member>, 컨트롤러에게 딱히 받을것도 없고
	public List<Member> findAll() {
		
		// 서비스에서 원래 어떤거 했었지?
		// 커넥션을 받아왔는데, 그걸 SqlSession이 대신 하기로 했음
		SqlSession session = Template.getSqlSession();
		// 얘는 커넥션 받아오는거고
		
		// 그다음에는 DAO 호출, 필드로 빼놨음, 그러면서 메소드 호출, 원래는 커넥션을 넘겨줘야하지만 세션이 커넥션의 역할을 대신함
		List<Member> members = memberDao.findAll(session); // --> DAO ㄱㄱ
		// 갔다오면 List의 주소가 돌아온다
		
		// 자원반납 해줘야함
		session.close();
		
		// 결과(List의 주소값) 반환
		return members;
		
	}

}
