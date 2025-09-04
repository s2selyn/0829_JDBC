package com.kh.statement.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;
import java.util.function.Function;

import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

public class MemberService { // 얘 뜯어고치기
	
	// save랑 delete랑 코드블럭 똑같이 생김... 이것만 그럴까? 중복되는게 너무많다, 주석 지우니 너무 잘보이네 ㅎ
	// 유효성 검증 예외처리 이런게 지금 없어서 간결하게 바꾸고싶음
	// 자원반납, 트랜잭션 처리 이런건 꼭 해야하는게 맞는데, 그게 멤버를 새롭게 추가하는거랑 직접 연관이 있냐? 그건 또 아닌듯
	// 이 메소드는 핵심 로직이 멤버 추가하는거임, 자원반납이나 트랜잭션 처리는 또 따로 DB랑 관련됨
	
	// 중복제거 할것임, 크게 두 분류로 나눌 수 있음
	// 1. 반환타입 int, executeUpdate 쓰는 애들
	// 2. 반환타입 다른거, executeQuery 쓰는 애들
	// 어떤거 호출하는지, 어떤거 전달하는지 다름
	// 함수형 인터페이스 만들어서 람다로 처리해줄계획
	// 쉬운애들만 바꿔보자, DML은 좀 어려울지도
	
	private Connection conn = null;
	
	public MemberService() {
		this.conn = getConnection();
	}
	
	public int save(Member member) {
		
		int result = new MemberDao().save(conn, member);
		
		if(result > 0) {
			commit(conn);
		}
		
		close(conn);
		
		return result;
		
	}
	
	// 함수형 인터페이스 중에 앞에 타입을 받아서 뒤쪽 결과를 반환해줌
// T 제네릭은 뭐가들어오든 반환해주는거?
	// Function<Connection, T>
	
	// 서비스 안에서 호출할거라 밖에서 못부르게 접근제한자 private
	// 각각의 친구들 반환타입이 서로 다름, 리스트 또는 그냥 멤버만
	// 뭘로 돌아갈 지 알 수 없으니 외부에서(쓰는쪽에서) 정할 수 있도록 제네릭을 달아서 쓸 수 있게 해줌 --> 타입은 바깥에서 정할 수 있게 하겠다
	// 공통은 executeQuery를 돌리는 친구들 --> executeQuery
	// Function 타입의 매개변수를 받아서~
	
	// 람다와 함수형 인터페이스를 이용하면 이런 식으로 중복을 제거할 수 있다.
	// 실제는 나중에 이렇게 못돌림 서비스에서 할게 많음, JDBC에서는 이렇게 해볼 수 있을듯! 이런 방법도 있다
	private <T> T executeQuery(Function<Connection, T> daoFunction) {
		// 넘어오는 함수를 매개변수로 받는것임, 들어온걸로 각각의 함수를 호출해서 실행시킬 수 있음
		
		// 커넥션 초기화
		Connection conn = null;
		
		// 결과는 돌아갈게 다르니까 T로 받음
		T result = null;
		
		// 필드에 getConnection
		conn = getConnection();
		
		// 전달받은 daoFunction에, 인터페이스 보면 apply라는 메소드 있음, 이걸 수행해서 커넥션을 전달
		result = daoFunction.apply(conn);
		
		// 커넥션을 닫고
		close(conn);
		
		// 메소드 호출부분으로 결과를 돌려줌
		return result;
		
	}
	
	// 기존의 코드를 넘겨서 보낼 것
	public List<Member> findAll() {
		return executeQuery(new MemberDao()::findAll);
	}
	
	public Member findById(String userId) {
		return executeQuery(conn -> new MemberDao().findById(conn, userId));
	}
	
	public List<Member> findByKeyword(String keyword) {
		return executeQuery(conn -> new MemberDao().findByKeyword(conn, keyword));
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
