package com.kh.statement.model.service;

import com.kh.statement.model.dao.PlantDao;
import com.kh.statement.model.vo.Plant;

public class PlantService {
	
	// 여기서 할 일? 일단 많은디 머해야함...
	// 컨트롤러에서 부른다 일단, 여기서 DAO 가야함
	
	// 계속 부를거니까 DAO 만들어놓음? 합성???
	PlantDao pd = new PlantDao();
	
	public int insertPlant(Plant plant) { // plant 객체 받아와서 어쩌구저쩌구 해서 DAO 보내줘야함
		
		// int result = 0;
		
		// 부르면서 plant 넘겨줌
		int result = pd.insertPlant(plant);
		// 추가하고 오면 int로 결과 돌아올거임? 굳이 따로 선언할 필요가?
		
		// 보내주고 나서 돌아오는거 다시 컨트롤러에 되돌려줘야함
		return result;
		
	}

}
