package com.kh.statement.view;

import java.util.List;
import java.util.Scanner;

import com.kh.statement.controller.MemberController;
import com.kh.statement.model.vo.Member;

public class MemberView {
	
	MemberController mc = new MemberController();
	Scanner sc = new Scanner(System.in);
	
	private void findAll() {
		
		System.out.println("\n회원 전체 조회");
		
		// 데이터 좀.. Controller에게 회원들의 데이터 값 요청
		List<Member> members = mc.findAll();
		
		// 뷰에서 2절
		// 출력해줘야돼, 뭘? Member의 필드
		// 조회 결과가 있을수도, 없을수도 있음(회원 삭제기능 만들거니까)
		// 있으면 다 보여주면 되고, 없으면 없습니다 보여줘야함 --> 조건
		System.out.println("\n조회된 총 회원수는 " + members.size() + "명 입니다.");
		if(members.isEmpty()) {
			System.out.println("조회결과가 존재하지 않습니다.");
		} else {
			
			for(Member member : members) {
				
				System.out.println("==============================");
				System.out.println(member.getUserId() + "번 회원의 정보");
				System.out.print("아이디 : " + member.getUserId() + ", ");
				System.out.print("비밀번호 : " + member.getUserPwd() + ", ");
				System.out.print("이름 : " + member.getUserName() + ", ");
				System.out.print("이메일 : " + member.getEmail() + ", ");
				System.out.print("가입일 : " + member.getEnrollDate());
				System.out.println();
				// toString있으니까 변수명만 적어도됨
				
			}
			
		}
		
	}
	
	/**
	 * 사용자로부터 회원의 아이디를 입력받앙서
	 * Member테이블로부터 아이디값을 비교해서 조회한 뒤
	 * 동일한 아이디값을 가진 행의 데이터를 출력해주는 메소드
	 * 
	 */
	private void findById() {
		
		System.out.println("\n아이디로 검색 서비스입니다.");
		// 사용자에게 아이디를 입력받겠음
		System.out.print("아이디를 입력해주세요 > ");
		String userId = sc.nextLine();
		// 뷰는 아이디를 입력받았으면 1차적으로 자기가 할 일은 끝났음
		// 아이디를 입력받았으면 컨트롤러로 넘겨줘야함
		Member member = mc.findById(userId);
		// 컨트롤러로 넘어감
		
		// 컨트롤러에서 돌아오면 Member 타입의 member가 돌아옴
		// 조회 결과가 있었을수도 있고 없었을수도 있음 --> 이걸 구분해서 보여줘야함
		// if-else
		// 1. 조회결과가 존재하지 않았을 경우 == null(DAO에서 변수를 초기화할 때 null로 초기화했으니까)
		// 2. 조회결과가 존재할 경우		  == Member 객체의 주소 값(이게 아니면 말이 안됨, 뽑아올 수 없음)
		/*
		 * 자바에서 값의 종류(그렇게 많지 않음, 5개) == 자료형(종류, 형태)
		 * 정수 = byte, short, int, long
		 * 실수 = float, double
		 * 문자 = char
		 * 논리값 = boolean
		 * 
		 * 주소값 = 나머지 다
		 * 
		 * member는 값을 대입해둔 변수! 정수 실수 문자 논리값 다 아님, 남은건 주소값
		 * 
		 */
		if(member != null) { // 있었는지 없었는지 조건식, null이 아니라는 것은 주소값이 왔다는거고, 그럼 내용을 출력
			
			System.out.println(userId + "님의 검색 결과입니다.");
			System.out.println("==================================================");
			System.out.print("아이디 : " + member.getUserId() + ", "); 
			System.out.print("비밀번호 : " + member.getUserPwd() + ", ");
			System.out.print("이름 : " + member.getUserName() + ", ");
			System.out.print("이메일 : " + member.getEmail() + ", ");
			System.out.print("가입일 : " + member.getEnrollDate());
			System.out.println();
			// printf 써서 출력해도 괜찮겠다! 5개 빵꾸뚫어서
			
		} else {
			System.out.println("존재하지 않는 아이디 입니다.");
		}
		
	}
	
	private void findByKeyword() {
		
		System.out.println("\n회원 이름 키워드로 검색");
		System.out.print("검색하고자 하는 키워드를 입력해주세요 > ");
		String keyword = sc.nextLine();
		
		// 키워드 받은걸 가지고 컨트롤러에 넘겨줘야겠지
		List<Member> members = mc.findByKeyword(keyword);
		// 여기서 생각을 해야하는데 넘어가서 생각하자 컨트롤러 ㄱㄱ
		// 다녀오니 뭐가옴? List의 주소값
		
		// 뷰에서는 뭘 해줘야 할까요?
		// 조회결과가 있을수도 있고 없을수도 있음
		// 없으면 없다고 알려줘야하고 있으면 있다고 알려줘야함
		// 조회결과가 있을때 없을때 결과를 다르게 출력하고싶은거니까 조건문을 써서
		// 뭘 보면 있는지 없는지 알수있음? members
		if(members.isEmpty()) {
			
			// 여기 들어왔다는 것은 비어있다는 뜻
			System.out.println("조회 결과가 존재하지 않습니다.");
			
		} else {
			
			// 여기 오면 있다는건데, 있을때는 어떻게 해줘야함? 가지고온건 다 출력해줘야함, 몇갠지 모름
			// 최소 1개는 있음, 리스트의 요소 개수만큼 출력해줘야함
			for(int i = 0; i < members.size(); i++) {
				
				System.out.println((i + 1) + "번 째 조회 결과!");
				System.out.println(members.get(i));
				
			}
			
		}
		
	}

}
