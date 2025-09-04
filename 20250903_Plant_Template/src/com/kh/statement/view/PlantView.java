package com.kh.statement.view;

import java.util.List;
import java.util.Scanner;

import com.kh.statement.controller.PlantController;
import com.kh.statement.model.vo.Plant;

public class PlantView {
	
	PlantController pc = new PlantController();
	Scanner sc = new Scanner(System.in);
	
	public void mainMenu() {
		
		while(true) {
			
			System.out.println("식물 관리 프로그램~");
			System.out.println("메뉴 선택!");
			System.out.println("1. 식물 추가하기");
			System.out.println("2. 전체 조회하기");
			System.out.println("3. 번호로 식물 찾기");
			System.out.println("4. 키워드로 식물 찾기");
			System.out.println("5. 식물 정보 변경");
			System.out.println("6. 식물 삭제");
			System.out.println("0. 프로그램 종료");
			System.out.print("메뉴 선택 > ");
			int menuNo = sc.nextInt();
			sc.nextLine();
			
			switch(menuNo) {
			case 1 : insertPlant(); break;
			case 2 : findAll(); break;
			case 3 : findByNo(); break;
			case 4 : findByKeyword(); break;
			case 5 : update(); break;
			case 6 : delete(); break;
			case 0 : System.out.println("프로그램을 종료합니다."); return;
			default : System.out.println("메뉴를 다시 선택해주세요");
			}
			
		}
		
	}
	
	// 나만의 테이블에 INSERT
	private void insertPlant() {
		
		System.out.print("식물의 이름을 입력 > ");
		String plantName = sc.nextLine();
		
		System.out.print("식물의 색상을 입력 > ");
		String plantColor = sc.nextLine();
		
		int result = pc.insertPlant(plantName, plantColor);
		
		if(result > 0) {
			System.out.println("식물 추가에 성공했습니다.");
		} else {
			System.out.println("식물 추가에 실패했습니다.");
		}
		
	}
	
	// 전체 조회
	private void findAll() {
		
		System.out.println("\n식물 전체 조회");
		
		List<Plant> plants = pc.findAll();
		
		System.out.println("\n조회된 총 식물의 수는 " + plants.size() + "개 입니다.");
		
		if(plants.isEmpty()) {
			System.out.println("식물엄슴");
		} else {
			
			for(Plant plant : plants) {
				
				System.out.println(plant);
				
			}
			
		}
		
	}
	
	// 유니크 제약조건 걸려있는 컬럼으로 조회
	private void findByNo() {
		
		System.out.println("\n번호로 식물 찾아보기");
		
		System.out.print("번호를 입력해보세영 > ");
		int plantNo = sc.nextInt();
		sc.nextLine();
		
		Plant plant = pc.findByNo(plantNo);
		
		if(plant != null) {
			System.out.println(plant);
		}
	}
	
	// LIKE키워드 써서 조회
	private void findByKeyword() {
		
		System.out.print("\n검색하고자 하는 키워드 입력 > ");
		String keyword = sc.nextLine();
		List<Plant> plants = pc.findByKeyword(keyword);
		
		if(!plants.isEmpty()) {
			
			for(int i = 0; i < plants.size(); i++) {
				
				System.out.println((i + 1) + "번째 조회 결과");
				System.out.println(plants.get(i));
				
			}
			
		} else {
			System.out.println("결과없음");
		}
		
	}
	
	private void update() {
		
		// 이름과 색깔을 받아서 색깔을 변경
		System.out.print("바꾸고 싶은 식물의 이름 > ");
		String plantName = sc.nextLine();
		
		System.out.print("현재 식물의 색깔 > ");
		String plantColor = sc.nextLine();
		
		System.out.print("새로워질 식물의 색깔 > ");
		String newColor = sc.nextLine();
		
		// 생성자 호출하면서 전달? 은 아닌 것 같은디용??
		pc.update();
		
	}
	
	private void delete() {
		pc.delete();
	}

}
