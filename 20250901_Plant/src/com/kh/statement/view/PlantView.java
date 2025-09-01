package com.kh.statement.view;

import java.util.Scanner;

import com.kh.statement.controller.PlantController;

public class PlantView {
	
	PlantController pc = new PlantController();
	Scanner sc = new Scanner(System.in);
	
	public void mainMenu() {
		
		System.out.println("식물 관리 프로그램~");
		System.out.println("메뉴 선택!");
		System.out.println("1. 식물 추가하기");
		System.out.println("2. 전체 조회하기");
		System.out.println("3. 번호로 식물 찾기");
		System.out.println("4. 키워드로 식물 찾기");
		System.out.println("0. 프로그램 종료");
		System.out.print("메뉴 선택 > ");
		int menuNo = sc.nextInt();
		sc.nextLine();
		
		while(true) {
			
			switch(menuNo) {
			case 1 : insertPlant(); break;
			case 2 : findAll(); break;
			case 3 : findByNo(); break;
			case 4 : findByKeyword(); break;
			case 0 : System.out.println("프로그램을 종료합니다."); break;
			default : System.out.println("메뉴를 다시 선택해주세요"); break;
			}
			
		}
		
	}
	
	// 나만의 테이블에 INSERT
	private void insertPlant() {
		
		System.out.print("식물의 이름을 입력 > ");
		String plantName = sc.nextLine();
		
		System.out.print("식물의 색상을 입력 > ");
		String plantColor = sc.nextLine();
		pc.insertPlant(plantName, plantColor);
	}
	
	// 전체 조회
	private void findAll() {
		pc.findAll();
	}
	
	// 유니크 제약조건 걸려있는 컬럼으로 조회
	private void findByNo() {
		pc.findByNo();
	}
	
	// LIKE키워드 써서 조회
	private void findByKeyword() {
		pc.findByKeyword();
	}

}
