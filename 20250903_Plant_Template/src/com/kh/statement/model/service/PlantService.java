package com.kh.statement.model.service;

import java.sql.Connection;

import com.kh.common.JDBCTemplate;
import com.kh.statement.model.dao.PlantDao;
import com.kh.statement.model.vo.Plant;

public class PlantService {
	
	// 여기서 할 일? 일단 많은디 머해야함...
	// 컨트롤러에서 부른다 일단, 여기서 DAO 가야함
	
	// 계속 부를거니까 DAO 만들어놓음? 합성???
	// PlantDao pd = new PlantDao();
	
	// 커넥션 여기서 만들어놔야함
	// 필드로 놓고 기본생성자로 초기화
	private Connection conn = null;
	
	public PlantService() {
		this.conn = JDBCTemplate.getConnection();
	}
	
	public int insertPlant(Plant plant) { // plant 객체 받아와서 어쩌구저쩌구 해서 DAO 보내줘야함
		
		// int result = 0;
		
		// 부르면서 plant 넘겨줌
		
		// 커넥션이랑 같이 넘겨줌?
		int result = new PlantDao().insertPlant(conn, plant);
		// 추가하고 오면 int로 결과 돌아올거임? 굳이 따로 선언할 필요가?
		// 여기 왜 ClassNotFound예외처리 뜸..?
		
		// 커넥션 돌아오면 반납해야함 템플릿 스태틱 메소드 호출해야 여기서 예외처리 안함
		JDBCTemplate.close(conn);
		
		// 보내주고 나서 돌아오는거 다시 컨트롤러에 되돌려줘야함
		return result;
		
	}

}
