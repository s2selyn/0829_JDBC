package com.kh.statement.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

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
	
	// 
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
		return session.selectList("memberMapper.findAll");
		// 서비스로 돌아감
		
	}
	
}
