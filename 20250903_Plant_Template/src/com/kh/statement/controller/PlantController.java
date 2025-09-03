package com.kh.statement.controller;

import java.util.List;

import com.kh.statement.model.dao.PlantDao;
import com.kh.statement.model.service.PlantService;
import com.kh.statement.model.vo.Plant;

public class PlantController {
	
	// PlantDao pd = new PlantDao(); 얘는 이제 필요없나?
	PlantService ps = new PlantService();
	
	// 나만의 테이블에 INSERT
	public int insertPlant(String plantName, String plantColor) {
		
		Plant plant = new Plant(plantName, plantColor);
		int result = new PlantService().insertPlant(plant);
		
		return result;
		
	}
	
	// 전체 조회
	public List<Plant> findAll() {
		
		List<Plant> plants = new PlantDao().findAll();
		return plants;
		
	}
	
	// 유니크 제약조건 걸려있는 컬럼으로 조회
	public Plant findByNo(int plantNo) {
		
		Plant plant = new PlantDao().findByNo(plantNo);
		return plant;
	}
	
	// LIKE키워드 써서 조회
	public List<Plant> findByKeyword(String keyword) {
		
		List<Plant> plants = new PlantDao().findByKeyword(keyword);
		return plants;
		
	}

	public void update() {
		pd.update();
	}
	
	public void delete() {
		pd.delete();
	}
	
}
