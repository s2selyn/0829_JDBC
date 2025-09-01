package com.kh.statement.model.dao;

import java.sql.Connection;
import java.sql.Statement;

public class PlantDao {
	
	// 나만의 테이블에 INSERT
	public void insertPlant(String plantName, String plantColor) {
		
		Connection conn = null;
		Statement stmt = null;
		int result = 0;
		
		String sql = "";
		
		// 드라이버 등록
		// 서버 연결
		// 수동커밋설정
		// 편집기 열기
		// SQL문 실행
		// 트랜잭션 처리
		// 자원반납
		
	}
	
	// 전체 조회
	public void findAll() {
		// getter
	}
	
	// 유니크 제약조건 걸려있는 컬럼으로 조회
	public void findByNo() {
		// getter
	}
	
	// LIKE키워드 써서 조회
	public void findByKeyword() {
		// getter???
	}

}
