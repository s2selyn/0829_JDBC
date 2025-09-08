package com.kh.statement.run;

import com.kh.common.JDBCTemplate;
import com.kh.statement.view.PlantView;

public class PlantRun {
	
	public static void main(String[] args) {
		
		// 드라이버 등록 여기로 가져오기
		JDBCTemplate.resistDriver();
		
		PlantView pv = new PlantView();
		
		pv.mainMenu();
		
	}

}
