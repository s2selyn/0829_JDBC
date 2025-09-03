package com.kh.statement.view;

import java.util.List;
import java.util.Scanner;

import com.kh.statement.controller.MemberController;
import com.kh.statement.model.vo.Member;

// Alt + Shift + j --> 라이브러리 주석
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
	
	// 사용자한테 뭔가 값을 입력받고 나중에 출력을 해주는 용도로 만들어주는 클래스
	// 입력을 받은걸 가지고 DB까지 가서 인서트하거나 업데이트하거나 딜리트하거나 또는 셀렉을 할때에 WHERE 조건절에 조건으로 사용하거나 이렇게 쓸 예정
	// 나중에 출력을 할 때는 출력문을 통해서 DB에 멤버테이블에 있는 데이터를 가지고 와서 화면상에 띄워줄것
	
	// 입력을받아야되는데 뷰클래스 안에 있는 메소드들은 전부 스캐너가 필요할것 --> 메소드 레벨로 선언하지 않고 어차피 여기서 다 써야되니까 필드로 구성
	private Scanner sc = new Scanner(System.in); // 뷰 클래스가 객체로 올라감과 동시에 같이 올라가게끔 구성
	
	// 뷰는 입력 받으면 끝, 출력해주면 끝이고 실질적으로 DB랑 왔다갔다는 DAO가 함(DB에 작업하고 DB에 인서트하고 뭐시기하고)
	// 뷰가 DAO까지 입력받은걸 넘겨줘야함, 근데 다이렉트로 가요? 아니면 누가 중간에 껴요? 중간에 컨트롤러를 거쳐서 넘어갈거임
	// 컨트롤러도 뷰 안에 가진 멤버 함수 안에서는 다 사용을 해야함, 컨트롤러도 필드로 구성
	private MemberController mc = new MemberController();
	// 컨트롤러가 DAO를 호출하게 만들것이므로 컨트롤러의 멤버를 사용하고싶은것임 --> 굳이 상속을 안하고 컨트롤러를 필드로 두고 컨트롤러의 멤버들을 호출해서 사용 --> 합성, Composition
	
	// 이 안에 프로그램 실행하면 제일 먼저 보여줄 메인 화면 만들거
	// 메소드를 만들면 주석을 또 예쁘게 작성
	// 클래스 들어오면 한눈에 보기 좋고 다른 클래스에서 객체를 생성해서 호출할 때 설명이 나옴 사용하는 쪽에서 이 클래스에 들어와보지 않아도 볼 수 있음
	// 메소드도 설명 보임 요요도를 확인할 수 있음
	// 주석을 예쁘게 잘 달아두면 나중에 다시볼때도, 다른 사람이 볼때도 굉장히 좋겠다
	// 지금은 배우는 과정이니까 이렇게까지 적을 필요는 없음, 나중에 프로젝트 할 때라든지 아니면 회사 가서라든지 가서는 항상 주석을 이렇게 예쁘게
	/**
	 * 프로그램 구동 시 사용자에게 메인화면을 출력해주는 메소드입니다.
	 * 
	 */
	public void mainMenu() {
		
		// 프로그램 시작하면 메인 메소드에서 제일 처음에 객체로 올려서 호출할 메소드
		// 프로그램 실행하면 뜨는 메인화면이 떴다고 가정했을 때 출력해줄 내용
		// 기본적으로 계속해서 프로그램을 종료하기 전까지는 반복해서 화면을 보여줄거니까 while문 사용
		while(true) {
			
			// 안에는 출력해 줄 수 있는 방법이 출력문 밖에 없음, 출력문으로 출력해줄것
			// 뷰는 지금 우리한테 별로 그렇게 중요한 요소가 아님, 뷰에서 뭘 해야한다 이건 중요
			// 뷰에 적는 코드들은 별로 중요하지 않음, 왜? 다음주에 배울 HTML CSS JavaScript로 대체가 되는 친구니까
			// 지금은 우리가 텍스트로 이렇게 보여주고 있지만 우리 웹개발자니까 웹브라우저에서 화면이 나와야함, 지금 이걸 화면으로 쓸 순 없음, 다음주에 배울 친구들로 화면은 대체될것임
			// 뷰는 대체가 되어야함, 그래서 여기에 적는 코드들은 별로 중요하지 않음
			// 그렇지만 여기서 해야 할 일은 변하지 않음 --> 사용자에게 무언가를 보여주고 사용자한테 값을 입력받아서 그것을 컨트롤러한테 보내줘야됨
			System.out.println(" ---- 회원 관리 프로그램 ---- ");
			
			// 회원 관리 프로그램에서 총 여섯가지 구현
			// Plant에서 하던거랑 똑같음, 항상 우리가 하는 일은 똑같음
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 조회");
			System.out.println("4. 회원 이름 키워드로 조회");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("9. 프로그램 종료");
			System.out.print("메뉴를 선택해주세요 > ");
			int menuNo = sc.nextInt(); // 스캐너가 가지고 있는 메소드를 이용해서 값을 입력받음
			sc.nextLine();
			
			// 사용자가 정수값을 뭘 입력했냐에 따라서 다른 기능을 수행할 수 있도록 switch문
			// 예외처리는 생략하고 JDBC에 집중
			switch(menuNo) {
			case 1 : save(); break; // 회원 추가할래요 했으면 뷰에서는? 웹사이트에서 내가 회원 가입해야지 하고 누루면 뭐가 나와야함? 아이디 비밀번호 입력하세요 정보를 입력할 수 있는 화면이 나와야함
			case 2 : findAll(); break;
			case 3 : findById(); break;
			case 4 : findByKeyword(); break;
			case 5 : update(); break;
			case 6 : delete(); break;
			case 9 : System.out.println("프로그램을 종료합니다."); return; // 종료를 입력했을 때는 return으로 메인메소드로 보냄
			default : System.out.println("잘못된 메뉴 선택입니다."); // 이상한 거 누른거니까 그냥 메시지 정도만 출력
			}
			
		}
		
	}
	
	// 항상 기능을 만들 때는 기존에 있는 걸 생각
	// 지금 필요한게 테이블에 넣을 여섯개의 값, INSERT 할 때 한 행으로 넣어야하니까 총 여섯개의 값이 필요함
	// USERNO는 시퀀스로 넣기로 함, ENROLLDATE는 가입시점에 들어가니까 SYSDATE넣으면됨, 사용자한테 네개를 입력받아야함 --> 아이디 PWD 네임 이메일
	/**
	 * MEMBER 테이블에 INSERT할 값을 사용자가 입력받는 화면을 출력해주는 메소드
	 * 
	 * 컬럼에 INSERT할 값들을 모두 입력받은 후 입력받은 값 컨트롤러로 전달
	 * 
	 */
	private void save() {
		
		System.out.println("--- 회원 추가 ---");
		
		// 입력받고 이런거 우리 너무 익숙
		System.out.print("아이디를 입력해주세요 > ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호를 입력해주세요 > ");
		String userPwd = sc.nextLine();
		
		System.out.print("성함을 입력해주세요 > ");
		String userName = sc.nextLine();
		
		System.out.print("이메일을 입력해주세요 > ");
		String email = sc.nextLine();
		
		// 일단 View에서 할 일은 끝남 -> 컨트롤러로 요청 처리
		// DB가 붙었으니까 회원 추가해주세요 하면 INSERT 하는게 목적임, 최종 목적은 DB에 INSERT하는거
		// INSERT에 필요한 값 다 받았으면 끝, 네개의 값을 컨트롤러로 전달, 메소드 호출하면서 호출 시 인자값으로 보냄
		// 앞에서 하던거에 JDBC 붙인거
		int result = mc.save(userId, userPwd, userName, email);
		// 컨트롤러 ㄱㄱ
		// 저쪽에서 뭔일이 일어나는지 모름, 근데 정수가 오긴 왔음, 온거가지고 결과를 출력해줘야함
		// 사용자가 이용하는거니까 됐으면 됐다, 안됐으면 안됐다 보여줘야함
		// 뭐가 왔는지 모르니까 일단 변수에 담아둠, int에 왔으니까 int에 담음
		
		// 돌아온 결과로 성공실패 출력해줘야함(그게 얘 일이지)
		// 성공실패 보여줄 내용이 다름 --> 경우가 두개니까 조건 if
		// 조건식을 만들 때 뭘 봐야 성공실패를 알수있음? result
		// Controller에서 받은 반환값으로 결과 출력
		if(result > 0) { // result가 0보다 크면 성공한거
			
			// if 블록은 성공했을때 가는거
			System.out.println("회원 가입에 성공했습니다.");
			
		} else {
			
			// else블록은 실패했을때 가는거
			System.out.println("회원 가입에 실패했습니다.");
			
		}
		
	}
	
	
	/**
	 * 회원 전체 조회 요청 시 Member 테이블에 존재하는 모든 회원의 정보를 출력하는 메소드
	 * 
	 */
	private void findAll() {
		
		// 전체조회 하는데 뷰에서 왔다갔다 할 순 없음, 나중에 값을 반환받아서 출력해줘야함
		// 문제가 생길 때 고치는 법을 알아야함, 왜안되는지도 알아야하고
		// 선생님이 알려주시는거 듣고 되면 넘어가면 안됨, 이제 되는건 별로 중요하지 않고
		// 뭐가 문제인지 알고 고치는법을 알면 나혼자도 할수있음
		
		System.out.println("\n회원 전체 조회");
		
		// 회원 멤버 테이블에 있는 모든 데이터를 출력해줄건데 뷰 입장에서 여기서 해줄수없음
		// 컨트롤러한테, 중간다리한테 데이터 좀 달라고 해야함 --> 바로 컨트롤러에게 요청, 회원들의 데이터값 요청 이렇게 하자
		// 데이터 좀.. Controller에게 회원들의 데이터 값 요청
		// 요청하려면 메소드 호출, 보낼건 딱히 없고 그냥 해달라고 하자 --> 컨트롤러로 넘어감
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
