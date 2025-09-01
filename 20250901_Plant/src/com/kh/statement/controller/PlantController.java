package com.kh.statement.controller;

import com.kh.statement.model.dao.PlantDao;

public class PlantController {
	
	PlantDao pd = new PlantDao();
	
	// 나만의 테이블에 INSERT
	public void insertPlant(String plantName, String plantColor) {
		new PlantDao().insertPlant(plantName, plantColor);
		
	}
	
	// 전체 조회
	public void findAll() {
		
	}
	
	// 유니크 제약조건 걸려있는 컬럼으로 조회
	public void findByNo() {
		
	}
	
	// LIKE키워드 써서 조회
	public void findByKeyword() {
		
	}
	
}
