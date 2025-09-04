package com.kh.statement.view;

import java.util.List;
import java.util.Scanner;

import com.kh.board.view.BoardView;
import com.kh.statement.controller.MemberController;
import com.kh.statement.model.vo.Member;

//Alt + Shift + j
/**
 * 
 * MemberView 클래스는 JDBC 실습을 위해 생성하였으며,
 * 사용자에게 입력 및 출력을 수행하는 메소드를 제공합니다.
 * 
 * @author 홍길동
 * @version 0.1
 * @date 2025-09-01
 * 
 */
public class MemberView {
	
	private Scanner sc = new Scanner(System.in);
	private MemberController mc = new MemberController();
	
	/**
	 * 프로그램 구동 시 사용자에게 메인화면을 출력해주는 메소드입니다.
	 * 
	 */
	public void mainMenu() {
		
		/*
		 * 2025 / 09 / 03 오늘의 실습 겸 숙제
		 * 
		 * 어제했던 결과물을 -> Template 버전으로 변경하기
		 * 
		 */
		
		while(true) {
			
			System.out.println(" ---- 회원 관리 프로그램 ---- ");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 조회");
			System.out.println("4. 회원 이름 키워드로 조회");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("7. 게시판 서비스로 이동!");
			System.out.println("9. 프로그램 종료");
			System.out.print("메뉴를 선택해주세요 > ");
			int menuNo = sc.nextInt();
			sc.nextLine();
			
			switch(menuNo) {
			case 1 : save(); break;
			case 2 : findAll(); break;
			case 3 : findById(); break;
			case 4 : findByKeyword(); break;
			case 5 : update(); break;
			case 6 : delete(); break;
			case 7 : new BoardView().mainMenu(); break;
			case 9 : System.out.println("프로그램을 종료합니다."); return;
			default : System.out.println("잘못된 메뉴 선택입니다.");
			}
			
		}
		
	}
	
	/**
	 * MEMBER 테이블에 INSERT할 값을 사용자가 입력받는 화면을 출력해주는 메소드
	 * 
	 * 컬럼에 INSERT할 값들을 모두 입력받은 후 입력받은 값 컨트롤러로 전달
	 * 
	 */
	private void save() {
		
		System.out.println("--- 회원 추가 --- ");
		
		System.out.print("아이디를 입력해주세요 > ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호를 입력해주세요 > ");
		String userPwd = sc.nextLine();
		
		System.out.print("성함을 입력해주세요 > ");
		String userName = sc.nextLine();
		
		System.out.print("이메일을 입력해주세요 > ");
		String email = sc.nextLine();
		
		// 일단 View에서 할 일은 끝남 -> 컨트롤러로 요청 처리
		int result = mc.save(userId, userPwd, userName, email);
		
		// Cotroller에서 받은 반환값으로 결과 출력
		if(result > 0) {
			System.out.println("회원 가입에 성공했습니다.");
		} else {
			System.out.println("회원 가입에 실패했습니다.");
		}
		
	}
	
	
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
				System.out.println(member.getUserNo() + "번 회원의 정보");
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
	
	private void update() {
		
		System.out.println("\n회원 정보 수정 서비스입니다.");
		
		// 아이디랑 비밀번호랑 새 비밀번호 받아서
		// 아이디랑 비밀번호가 있는거 있으면 새 비밀번호로 바꾸기
		System.out.print("아이디를 입력해 주세요 > ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호를 입력해주세요 > ");
		String userPwd = sc.nextLine();
		
		System.out.print("새 비밀번호를 입력해주세요 > ");
		String newPassword = sc.nextLine();
		
		// 3개 받았으니 뷰는 이제 끝남, 이 3개를 컨트롤러에 넘겨줘야함
		int result = mc.update(userId, userPwd, newPassword);
		// 컨트롤러 ㄱㄱ
		
		// 다녀왔다! 결과를 받아줘야하니까 int result = 로 받음
		if(result > 0) { // 0보다 크다면 비밀번호 변경에 성공
			System.out.println("비밀번호 변경에 시원하게 성공했습니다.");
		} else { // 0보다 큰게 아니라면 에잉 쯧쯧 ㄱㄱ
			System.out.println("비밀번호도 모르냐 에잉 쯧쯧");
		}
		
	}
	
	private void delete() {
		
		// 아이디 비번 받아서 날리자
		System.out.println("안녕히가세요 ~ ");
		
		System.out.print("아이디 주세요 > ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호 주세요 > ");
		String userPwd = sc.nextLine();
		
		// 다 만들어놓고 가자, 이번에 쓸 SQL문은 DELETE, 나중에 결과가 정수로 옴
		int result = mc.delete(userId, userPwd);
		
		// 뷰에서는 결과 받아서 성공했는지 안했는지 체크한다음에 성공했으면 성공했습니다, 실패했으면 실패했습니다
		if(result > 0) {
			System.out.println("성공했습니다.");
		} else {
			System.out.println("실패했습니다.");
		}
		// 이제 컨트롤러 ㄱㄱ
		
	}

}
