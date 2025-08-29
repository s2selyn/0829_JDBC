package com.aclass.test.run;

public class ReflactionRun {

	public static void main(String[] args) {
		
		// 내가 여기서 TestRun을 쓰고싶으면 여기서 써서 코드로 작성해서 씀
		try {
			Class<?> c = Class.forName("com.aclass.test.run.SelectRun");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
// 설명?ㅅ?
		// 실행할 때 결정할 수 있도록 만들어주는 기술이 리플렉션
		// 들어가는 인자값이 뭐냐에 따라서 대입될 c가 달라지기 때문에 실행하는 친구의 타입 또는 실행객체의 실제 인스턴스
		// 이런것들을 코드가 아니라 실행할 때 결정할 수 있음
		// 왜 이렇게 하냐면 내가 만약에 오라클을 쓸 때 드라이버 등록을 하는데..
		// 실제 이걸 가져다 놓고 쓰면 무조건 오라클 DB에만 쓸 수 있는 코드가 됨
		// 실제 제품은 이렇게 돌아가지 않음, 제품은 하나고 DB는 우리가 팔 회사들이 쓰는 DB에 맞춰서
		// 코드로 적어두면 매번 코드를 바꿔야겠지
		// 이렇게 해두면 여기만 바꿔도 모든 DB에서 동일하게 쓸수있게됨
		
	}

}
