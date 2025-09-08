package com.kh.statement.model.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kh.common.Template;
import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.dto.PasswordDTO;
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
	
	// findById, id를 가지고 조회를 하기 때문에 나중에 Memeber 반환, 앞에서는 사용자가 입력한 문자열값을 받아옴
	public Member findById(String userId) {
		
		// 이제 커넥션의 역할을 하는 SqlSession타입의 객체를 하나 받아옴
		SqlSession session = Template.getSqlSession();
		
		// SqlSession은 그냥 세션이라고 하면 안됨, Http, WebSocket 등 엄청 많이 씀, 그냥 세션이라고 하면 구분이 안되니까 명확하게 구분해야함
		
		// JDBC의 커넥션 역할을 대신하는 SqlSession 객체를 받아왔음, 그다음에 DAO에 가서 조회해오는게 목적
		Member member = memberDao.findById(session, userId); // DAO의 메소드를 호출하면서 SqlSession(커넥션의 역할) session을 넘겨주면서 SELECT문을 보낼 때 조건으로 사용할 userId도 넘겨줌
		// DAO 갔다오면 멤버가온다, 서비스에서는 이걸 리턴해줘야함
		
		// 리턴하기 전에 사용이 끝난 세션을 닫아줘야함
		session.close();
		
		return member;
		
	}
	
	// 키워드는 조회결과가 포함되어있는거 찾는거라서 여러 행으로 나올 수 있음, 여러행이 나오는거니까 나중에 반환할때는 List + 제네릭
	// 앞에서 문자열 형태의 키워드를 받아옴
	public List<Member> findByKeyword(String keyword) {
		
		// 서비스에서 가장 첫번째 해야할 일은 커넥션이지만 SqlSession이 대체함
		SqlSession session = Template.getSqlSession();
		
		// DAO의 메소드를 호출하면서 sql세션과 사용자가 입력한 스트링값을 넘겨줌
		// 또 중복이 있는데 다음에 하고 당분간은 마이바티스 익히는데 주력
		List<Member> members = memberDao.findByKeyword(session, keyword);
		// DAO 갔다오면 리스트가 돌아옴
		
		session.close();
		
		return members;
		
	}
	
	// update, delete
	public int update(PasswordDTO pd) {
		
		// 사실 서비스단은 별 차이 없고, 타입만 바뀜
// 컨트롤러도?
// 지금 뭐함? 마이바티스, 마이바티스는 영속성 프레임워크
// 영속성은 영구적으로 진행시키는 작업, 이걸 위해서 DB에 CRUD해서 집어넣고 조회하고
// 영속성 프레임워크는 DB에 CRUD 하는거 도와주는거, 원래 DAO가 하던 작업이니 DAO만 바뀌고 있음
// 지금은 한줄 쓰는거 아리까리하지만 JDBC 하는것처럼 주구장창 잡고있으면 되겠지, 이게 뭐하는건지 무슨작업인지 왜써야하지 쓰면뭐가좋지 생각해야함, 쓰는건 외워서 쓰는거고
// 이것도 또 바뀐다^^! 치고 돌아가는게 중요한게 아님
		SqlSession session = Template.getSqlSession();
		
		int result = memberDao.update(session, pd);
		
		if(result > 0) {
			session.commit();
		}
		
		session.close();
		
		return result;
		
	}
	
	public int delete(Member member) {
		
		SqlSession session = Template.getSqlSession();
		
		int result = memberDao.delete(session, member);
		
		if(result > 0) {
			session.commit();
		}
		
		session.close();
		
		return result;
		
	}

}
