package com.kh.statement.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

public class MemberDao { // 얘 뜯어고치기
	
	// 원래 properties 객체 올려서 작업했지만 이제 안함
	// 커넥션 타입의 변수, 매개변수 멤버타입 받았는데 커넥션만 sqlSession으로 바꿔줌
	public int save(SqlSession session, Member member) {
		
		// 원래 이렇게 지난주 내내 썼음
		/*
		 * int result = 0;
		 * PreparedStatement pstmt = null;
		 * String sql = prop.getProperty("save");
		 * try{
		 * 	pstmt = conn.preparedStatement(sql);
		 * 	pstmt.setString(1, member.getUserId());
		 * 	pstmt.setString(2, member.getUserPwd());
		 * 	...
		 * 	result = pstmt.executeUpdate();
		 * 	} catch(IOException e) {
		 * 		e.printStackTrace();
		 * 	} finally{
		 * 		JDBCTemplate.close(pstmt);
		 * 	}
		 * 	return result;
		 * }
		 * 
		 * SqlSession이 제공하는 메소드를 통해 SQL문을 찾아서 실행하고 결과도 받아볼 수 있음
		 * sqlSession.SQL문메소드("매퍼파일의namespace.해당SQL문의id속성값");
		 * 
		 */
		
		// 마이바티스 사용하기 위해서 한가지 준비를 더 해야함
		// 원래는 xml을 그냥 만들어서 거기에 엔트리태그를 써서 작업했음
		// 마이바티스는 마이바티스용 xml파일을 만들어줘야함
		// 마이바티스용 매퍼 파일을 만들어보도록 하자
		// resource에서 xml 파일 생성
		
		// 만들어놓은 save 호출해서 insert 하고싶음, 이 역할을 pstmt가 했음, 이건 커넥션으로 만드는데 이제 커넥션 없음
		// pstmt 역할을 할 누군가는? sqlSession, 얘는 커넥션과 stmt 역할을 둘 다 하는 다재다능한 친구
		// SqlSession이 제공하는 메소드를 쓰고싶으면 session에 참조연산자로, inser 메소드 두개가 오버로딩 되어있음
		// 매개변수 하나인것은 어떤 태그를 골라서 실행할것인지 --> id를 부름
		// 매개변수 두개인것중에 첫번째는 어떤거 실행할지, 두번째는 insert할 때 필요한 앞에서 넘어온 값(insert 실행할 때 필요한 인자값이 있다면 그것을 파라미터로 받아주는 매개변수, 무슨 타입일지 모르니까 다형성 적용해서 Object)
		// 이번에는 member를 넘겨야함, 여기에 값이 있으니까, 두번째 메소드 써야함
		// 들어갈 SQL문은 우리가 태그에 붙여놓은 id속성의 값인 save를 적으면 되는데! 문제는? 매퍼가 하나가 아닐 수 있음
		// 보드매퍼가 있을 수 있지, 매퍼가 다르니까 id는 똑같아도 상관없음, 보드에도 멤버에도 save가 있게 되니 save부른다고 하면 구분이 안됨 --> 앞에 어떤 매퍼파일의 save를 호출할 지 적어줌
		// 매퍼파일 구분방법을 만들어줘야함 --> 멤버매퍼로 다시 돌아감, 가서 네임스페이스 속성값 작성하고 왔음
		// memberMapper 참조해서 메소드 적음
		// int result = session.insert("memberMapper.save", member);
		
		// 위치홀더랑 값이랑 바인딩 해줘야함 --> xml에서 작성
		// 매퍼 입장에서는 넘어온 member가 뭔지 모름 Object 자료형이 왔음, 부모타입으로 넘어왔음, 그러면 멤버의 게터메소드를 호출할 수 없음
		// 안에서 쟤를 받았을 때 멤버타입으로 쓰겠다는 명시해주는 과정이 필요함 --> xml에서 작성
		
		// DML 구문 호출하는 메소드니까 반환타입이 int로 찍힘, 우리는 int로 받아줘야함
		
		// 이거 한줄로 싹날렸다.. 결과반환만 하면 됨
		// return result; 근데 이럴거면 변수로 받을 필요가? 변수면 재사용할게 있어야하는데 재사용 안하네? 그럼 그냥
		return session.insert("memberMapper.save", member);
		
		// 서비스로 돌아감
		
	}
	
	public List<Member> findAll(SqlSession session) {
		
// SqlSession 타입의 객체로 해결 가능
		// 주소에 참조에서 select메소드 호출(One 아니면 List 두개만 사용하면 됨, 우리 작업할땐 이거 두개만 씀)
		// 뭘 생각? SQL문 실행하면 결과가 몇행이 돌아올까? 이걸 생각
		// ID로 조회하면 많이 돌아와봐야 한개, 걔는 SelectOne 메소드 호출
		// 지금은 전체조회니까 몇개돌아올지 모름, SelectList 메소드 호출
		// 오버로딩 잘되어있음, 3개 있다
		// 첫번째는 SQL문만 어떤 매퍼의 뭐 실행할건지
		// 두번째는 인자(파라미터)를 줘야한다면 첫번째 인자로 SQL문, 두번째 인자로 파라미터
		// 세번째는 페이징 처리할건데 당분간은 쓸일없고 나중에
		// member-mapper에서 작성하고옴
		// 어떤 매퍼 파일에서 가져올거냐? 매퍼의 메인스페이스 속성값(매퍼 여는 태그에 달아놓는거) --> 안에 SQL문이 많겠지? 참조연산자 --> 내가 실행하고 싶은 SQL 태그의 id 속성값, 우리는 메소드명이랑 맞추기로 햇으니까 메소드명
		// List<Member> member = session.selectList("memberMapper.findAll");
		// selectList는 반환타입이 List
		
// 12:35 이것도 그냥 반환하면 되니까 한줄로 쓰기 가능
		// 조회결과가 존재하지 않는다면 빈 리스트를 반환
		return session.selectList("memberMapper.findAll");
		// 서비스로 돌아감
		
		/*
		 * 얘는 조회 결과가 없으면 뭘 반환할까?
		 * 만들고 나서 제일 좋은 건 모르는것에 대해서 고민하기보다는 결과를 확인하고 거꾸로 돌아감
		 * 머리붙잡고 고민하는것보다는 결과를 보고 거짓말을 안하니까 어떻게 되어야 이게 나오는지 확인해야함
		 * 리스트가 오는데 비어있는게 옴, null이 올 수는 없음(이러면 NullPointerException), List는 100% 만들어져서 옴, 비어있는지 아닌지는 메소드로 판별
		 * 
		 * 어차피 ???
		 * 나만 그런게 아니라.. 마이바티스를 사용하는 사람들은 모두 똑같은 생각을 하고 개발을 하겠지
		 * 나름 강의실 안에서의 코딩컨벤션을 만드는 과정을 하고 있음
		 * 프로젝트 기간이 되면 이 반에 있는 불특정 다수와 작업을 해야하는데, 선생님 입장에서 제일 먼저 시켜야 하는건 각 인원의 코드 스타일을 통일시켜야 서로 알아볼 수 있고 하다말아도 그다음에 뭐할지 알아볼 수 있음
		 * 다 똑같이 같은 스타일로 작업해야하니까
		 * 내맘대로 해도 돌아가긴 하겠지만, 그렇게 했을때의 팀 전체의 생산성, 반 전체에 대한 생산성은 글쎄?
		 * 선생님이 작성하는 코드는 이유가 있으니 그때그때의 양식은 잘 따라가보자, 한꺼번에 설명하기는 어려운 부분이 있고 앞으로 알려주실테니 웬만하면 코드작성 스타일은 맞추자
		 * 메소드 안의 비즈니스 로직은 맘대로 해도 되지만 전체적인 구조와 틀 스타일은 우리반 안에서의 일하는 방식으로 맞추자
		 * 
		 * 아직 전체 자바개발자들의 약속이 많이 남음, 지금은 갸우뚱 할 수 있지만 천천히 가보자
		 * 
		 */
		
	}
	
	// 서비스에서 불러야하니까 접근제한자는 public, 한 행의 조회결과를 보내야하니까 Member
	public Member findById(SqlSession session, String userId) {
		
		// 계속 반복해서 치면서 방법이 없으니 이렇게 쓰고있음
		
		// DAO로 넘어오면 무슨 작업을 해야함? DB에 접근하는건 Connection이 하는거고
		// DAO는 SQL문 실행하고 결과받아오는것만 해야함, 마이바티스 하면서 진짜 이부분만 남겼음
		
		// SQL 실행은 stmt가 했음, 그 역할을 SqlSession이 겸함
		// SqlSession이 가지고 있는 메소드를 호출해서 해야함, SELECT문을 할거니까 select, 내가 원하는 SQL문을 실행할 때 결과가 몇개 돌아오는지 생각 --> PK로 조회하는거니까 한 행이 온다
		// selectOne이 두개있음, 인자 하나받는 친구는 sql문 수행할 때 따로 전달할게 없는것, 인자 두개받는 친구는 sql문 수행할 때 전달할 게 추가로 있는것
// 지금은 두개를 보내야함, 앞에는 어느 매퍼의 어떤 sql을 실행할건지 적음(memberMapper, id는 메소드명으로 할것임), 뒤에는 매개변수로 받아온 문자열 userId
		// Member member = session.selectOne("memberMapper.findById", userId); // 작성해놓고 매퍼 ㄱㄱ
		// 매퍼에서 작성하고 돌아오면 Member 타입으로 돌아옴
		
		// 돌려줄 때 member 주소를 쓰면 되는데 두줄로 쓸 필요가 없음
		// 조회결과가 존재하지 않다면 null 반환(공식문서에 다 써있음)
		return session.selectOne("memberMapper.findById", userId);
		
		// 개발이라는게 순수하게 코드작성만 놓고 보면 코드작성이 제일 쉬움, 개발이라는 파트가 엄청 큰데 코드쓰는게 굉장히 작다
		// 이걸위해 문법, 속성, 객체지향, 기술문서읽기 등 학습량, 절차가 방대한데 이것만 끝내면 코드작성은 제일 빨리 끝나는 금방하는, 할거없는 작업
		// 이걸 위해 공부하는 시간이 오래걸림, 차라리 SQL쓰는게 시간 더 오래걸릴지도
		
		/*
		 * 컬럼하나 빼먹어서 ResultSet에 포함이 안되었으니 매핑이 안됨
		 * 
		 * 지금은 성공했을때를 가정함, 실패했을때는? 없는 아이디를 조회했다면?
		 * 존재하지 않는 아이디입니다 하고 출력되는데.. 없는거 조회하면 어떻게 되는거임?
		 * member == null 이면 이게 출력됨, 얘는 어디서 온애임? 얘는 컨트롤러, 근데 여기서 만들어진것도 아니고 서비스에서 왔는데 서비스도 지가만든게 아니라 DAO에서 받아온거임
		 * 결론적으로 SELECT 했는데 ResultSet이 왔는데 0 행이 온것임, 0행이면 null을 돌려준다는거겠지?
		 * null이 돌아왔으니까 뷰에서 이렇게 출력했겠지, 결론적으로 이 메소드 호출 결과가 null이어야함
		 * 코드짜는기능을 배웠으면 이제 생각해야 할일
		 * 계획세우고 코드짜고 동작되는걸 확인하면 왜이렇게 나왔지를 돌아가서 공부해야함, 컴퓨터는 거짓말을 하지않으니까 얘가 동작하는대로 내가 이해해야함
		 * 
		 */
		
	}
	
	// 어차피 하나의 메소드가 하나의 SQL밖에 못부름, 앞에서 한 구조가 똑같음 계속
	public List<Member> findByKeyword(SqlSession session, String keyword) {
		
		// 이제 여기서 DAO 왔으니까 뭐함? SQL 실행해야함, 누구가지고? SqlSession 객체
		// 메소드 호출해서, 결과가 여러 행 있을 수 있으니까 selectList, 2번 불러야함, SQL문 실행할 때 키워드를 넘겨줘야함
		return session.selectList("memberMapper.findByKeyword", keyword); // 첫번째 인자로 어떤 매퍼의? 매퍼파일의 네임스페이스 속성값, 이러면 매퍼까지 감, 매퍼까지 갔는데 select문이 많음, 각 셀렉문 식별하는 방법은 id, 안정했지만 어차피 메소드명 할거임
		// keyword는 매퍼 안의 SQL문 돌릴 때 필요한 값이니까 넘겨줌
		// 이렇게 하면 나중에 뭐가 반환됨? 멤버 주소값을 담고 있는 리스트가 반환되거나, 조회된 결과가 없으면 빈 리스트
		
	}
	
	public int update(SqlSession session, PasswordDTO pd) {
		
// ...?
		return session.update("memberMapper.update", pd);
		
	}
	
	public int delete(SqlSession session, Member member) {
		return session.delete("memberMapper.delete", member);
	}
	
// 16:03 요약
	
}
