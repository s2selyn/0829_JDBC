package com.kh.statement.view;

import java.util.List;
import java.util.Scanner;

import com.kh.statement.controller.EmployeeController;
import com.kh.statement.model.vo.Employee;

public class EmployeeView {
	
	// 입력받기 할 스캐너 필드에 둠...? 메인메뉴 안에 두면 안되나? ㅁㄹ 일단 여기 ㄱ
	Scanner sc = new Scanner(System.in);
	
	// 메소드마다 컨트롤러한테 던질거니까 필드로
	EmployeeController ec = new EmployeeController();
	
	public void mainMenu() {
		
		// 메인메소드에서 호출할 메인화면
		// 종료하기 전까지 반복으로 유지시킴 -- 앗 이거 안했다 해줌 진짜해줌
		
		while(true) {
			
			System.out.println();
			System.out.println("===== 사원 관리 어쩌구 저쩌구 =====");
			System.out.println("1. 사원 다 조회하기");
			System.out.println("2. 부서명으로 같은 부서 사람들 찾기");
			System.out.println("3. 직급명으로 같은 직급 사람들 찾기");
			System.out.println("4. 사원 자세히 들여다보고싶음");
			System.out.println("5. 급여가 높아 기쁜 사람들 5인");
			System.out.println("6. 급여가 낮아 슬픈 사람들 5인");
			System.out.println("7. 사원 입사시키기");
			System.out.println("8. 사원 정보 수정");
			System.out.println("9. 사원 퇴사시키기");
			System.out.println("0. 프로그램 종료");
			
			// 메뉴 입력받기
			System.out.print("메뉴를 골라바여 > " );
			int menuNo = sc.nextInt();
			sc.nextLine();
			
			// 입력받은 메뉴에 따라 메소드 호출
			switch(menuNo) {
			case 1 : findAll(); break;
			case 2 : findByDept(); break;
			case 3 : findByJob(); break;
			case 4 : break;
			case 5 : break;
			case 6 : break;
			case 7 : break;
			case 8 : break;
			case 9 : break;
			case 0 : System.out.println("ㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂ"); return;
			default : System.out.println("잘못된 메뉴 선택");
			}
		
		}
		
	}
	
	// 1. 전체조회
	private void findAll() {
		
		// 요청을 컨트롤러에 보내야함
		// 컨트롤러 객체 생성해서 메소드 호출
		// 근데 뷰에서 메소드 호출할때마다 객체생성 하기싫음 필드로 ㄱ
		List<Employee> result = ec.findAll();
		// 딱히 전달할 인자는 없을 것 같고
		// 결과 반환받으면 정수형 변수로 받아줌 --> 리스트가 돌아오니까 이걸로 받음
		
		// 반환받은 결과에 따라 출력문작성
		// 결과가 있으면 출력하고 없으면 없다고 알려줘야함
		// 결과가 있는지 없는지는 result에 조건 달아서 확인
		if(!result.isEmpty()) { // 뻥이아니면
			
			// System.out.println(result); // 이렇게 해도 되나? ㅎㅎ 한줄로 질질나옴
			// 안에 몇개인지는 모르겠는데 반복해서 다 출력해줘야함
			for(int i = 0; i < result.size(); i++) {
				
				System.out.println("조회된 사원 정보입니다.");
				System.out.println((i + 1) + "번 째 사원");
				System.out.println(result.get(i));
				
			}
			
		} else {
			System.out.println("회사 망해서 아무도 없음");
		}
		
	}
	
	// 2. 부서명 입력받아서 부서가 같은 사람들 찾기
	private void findByDept() {
		
		// 이 메소드 선택했으면 여기 와서 입력받아야함
		System.out.print("부서명 입력해보시지! > ");
		String deptKeyword = sc.nextLine();
		
		// 컨트롤러에 입력받은거 전달하면서 메소드 호출함
		List<Employee> result = ec.findByDept(deptKeyword);
		// 결과 받아옴
		
		// 받아온 결과에 따라서 출력
		if(!result.isEmpty()) {
			
			// 뻥이 아니면 다 출력
			for(int i = 0; i < result.size(); i++) {
				
				System.out.println("입력하신 부서와 같은 부서의 사람들입니다.");
				System.out.println((i + 1) + "번 째 사원");
				System.out.println(result.get(i));
				
			}
			
		} else {
			
			System.out.println("같은 부서 사람들이 없는 외로운 사람이네요");
			
			// 아 근데 없는 부서면 DAO까지 갈 필요 없지 않나? 이거 검증은 어케하더라? 아마도 서비스인가?
			// 일단 뷰에서 하는건 아닌게 확실함
			
		}
		
	}
	
	// 3. 직급명 입력받아서 직급명 같은 사람들 찾기
	private void findByJob() {
		
		// 이거도 입력받고
		System.out.print("직급명으로 사람을 찾아봅시다다다 입력ㄱㄱ > ");
		String jobKeyword = sc.nextLine();
		
		// 입력받은거 전달하면서 컨트롤러의 메소드 호출
		List<Employee> result = ec.findByJob(jobKeyword);
		// 결과 받아오기, 이거도 리스트로 올거임
		
		// 받아온 결과에 따라서 출력
		if(!result.isEmpty()) {
			
			// 뻥이 아니면 다 출력
			for(int i = 0; i < result.size(); i++) {
				
				System.out.println("입력하신 직급과 같은 부서의 사람들입니다.");
				System.out.println((i + 1) + "번 째 사원");
				System.out.println(result.get(i));
				
			}
			
		} else {
			
			System.out.println("같은 직급의 사람들이 없는 외로운 사람이네요");
			
			// 아 근데 없는 부서면 DAO까지 갈 필요 없지 않나? 이거 검증은 어케하더라? 아마도 서비스인가?
			// 일단 뷰에서 하는건 아닌게 확실함
			
		}
		
	}

}
