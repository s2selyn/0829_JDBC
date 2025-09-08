package com.kh.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Template {
	
	// Connection객체의 역할을 대신할 SqlSession객체를 만들어서 반환해주는 메소드를 구현할 것
	// 앞처럼 스태틱 메소드로 구성
	// 바깥에서 불러야하니까 접근제한자 public, 객체 안만들고 메소드 호출할거니까 static, SqlSession객체를 만들어서 반환할거니까 반환타입은 SqlSession
	public static SqlSession getSqlSession() { // 패키지 이름은 옛날 마이바티스의 이름인 ibatis
		
		// 커넥션 돌려주는 것 처럼 예외처리가 필요함
		SqlSession sqlSession = null;
		
		// SqlSession객체를 만들어서 반환할건데 어떻게 만드는지 모름 --> 공식문서 봐야징
		// Factory를 만들어야함, 이걸 하려면 팩토리 빌더가 필요함, 팩토리빌더를 만들어서 팩토리를 만들고, 팩토리를 이용해서 세션을 만들어야지
		// 세션이 커넥션 역할을 하기 때문에 커넥션을 만들기 위해 필요한 정보는 xml에 있음, 이 파일을 읽어와서 커넥션을 만들어야함
		// 파일의 내용을 읽는 방법이 필요함, 이건 공식문서에 Resources를 제공하니까 이걸 이용해서 얻을 수 있다고 친절하게 적혀있음
		
		// mybatis-config.xml 파일에 있는 내용을 읽어와서
		// 해당 DB와 접속된 SqlSession객체를 반환(읽어올땐 inputstream으로 받아올것임, 이걸 얻는 방법은 마이바티스 리소시스에서 제공해줌, 스트림에서 읽어오려면 파일의 경로가 필요함, 우리는 resource안에 있는데, 이 경로를 적으면 안되고, 실제 실행 될 때의 파일 위치가 어디일지를 적어야함
// ??? 10:35 이건 어디있음? bin! 얘가 최상위 폴더인데, 얘 바로밑에 우리 config파일이 있음
		// 경로는 문자열로 적어야함
		String config = "mybatis-config.xml";
// ??? 10:49 bin 폴더에 만들어지는데 이건 우리가 xml 파일 위치를 곧바로 참조가능한건가?
		
		try {
			
// ??? 10:38 어떻게 스트림 만든다고여?
			// 이 메소드 인자로 우리 경로를 넣어줌, 반환타입은 인풋스트림(파일 데이터 읽어줄 수 있음)
			InputStream stream = Resources.getResourceAsStream(config);
			
			// 최종목적 SqlSession 만들기, 이걸 위해 팩토리가 필요, 팩토리 만들기 위해서 팩토리 빌더가 필요
			
			// 1단계 : SqlSessionFactoryBuilder 만들기
			// 만드는 방법 : 기본생성자를 호출한다.
			// new SqlSessionFactoryBuilder(); --> 반환타입이 sql 세션 팩토리 빌더
			
			// 이걸로 팩토리 만들어야함
			// 2단계 : SqlSessionFactory 만들기
			// 만드는 방법 : 메소드를 호출한다.
			// .build(접속 내용을 담은 파일을 읽어온 입력스트림); --> 반환타입이 sql 세션 팩토리
			// 메소드 체이닝으로 갈게요 다로 객체 안올리고 --> 만들어놓은 친구에서 build를 호출함, 두번째거 매개변수타입이 inputstream인것
			// 이러면 반환타입이 sql세션팩토리
			
			// 세션팩토리 만들었으니 이걸로 sql세션 만들수있음
			// 3단계 : SqlSession 만들기
			// 만드는 방법 : 메소드를 호출한다.
			// .openSession(); --> 반환타입이 sql 세션
			sqlSession = new SqlSessionFactoryBuilder().build(stream).openSession();
// ??? 받아와서 써야하니까? 반환해야하니까? 변수에 담음
			
			// 이 템플릿에서는 이것만 만들면 되고 더이상의 무언가가 없음, JDBCTemplate보다 간결함
			
			// 만약에 xml 잘못적으면 여기서 예외발생함, 미리 확인해보자
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sqlSession;
		
	}

}
