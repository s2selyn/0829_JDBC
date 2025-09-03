package com.kh.statement.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.common.JDBCTemplate;
import com.kh.statement.model.vo.Plant;

public class PlantDao {
	
	private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@115.90.212.20:10000:XE";
	private final String USERNAME = "CJ18";
	private final String PASSWORD = "CJ181234";
	
	// 나만의 테이블에 INSERT
	public int insertPlant(Plant plant) {
		
		// 템플릿 만들었음
		// 스태틱 메소드 전부 임포트 하는 방법이 있는데 클래스명 그냥 씀
		JDBCTemplate.getConnection();
		// Connection conn = null; 그럼 이거 생략? 이거 어디서 해야해!!!!
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String sql = """
						INSERT
						  INTO
						       PLANT
						       (
						       PLANT_NO
						     , PLANT_NAME
						     , PLANT_COLOR
						     , ENROLL_DATE
						       )
						VALUES
						       (
						       SEQ_PLANTNO.NEXTVAL
						     , ?
						     , ?
						     , SYSDATE
						       )
					 """;
		
		try {
			
			// 드라이버 등록
			Class.forName(DRIVER);
			
			// 서버 연결
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			// 수동커밋설정
			conn.setAutoCommit(false);
			
			// 편집기 열기
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, plant.getPlantName());
			pstmt.setString(2, plant.getPlantColor());
			
			// SQL문 실행
			result = pstmt.executeUpdate();
			
			// 트랜잭션 처리
			if(result > 0) {
				conn.commit();
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {	// 자원반납
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
		
	}
	
	// 전체 조회
	public List<Plant> findAll() {
		// getter
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		List<Plant> plants = new ArrayList();
		
		String sql = """
						SELECT
						       PLANT_NO
						     , PLANT_NAME
						     , PLANT_COLOR
						     , ENROLL_DATE
						  FROM
						       PLANT
						 ORDER
						    BY
						       ENROLL_DATE DESC
					 """;
		
		try {
			
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				Plant plant = new Plant();
				
				plant.setPlantNo(rset.getInt("PLANT_NO"));
				plant.setPlantName(rset.getString("PLANT_NAME"));
				plant.setPlantColor(rset.getString("PLANT_COLOR"));
				plant.setEnrollDate(rset.getDate("ENROLL_DATE"));
				
				plants.add(plant);
				
			}
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(rset != null) {
					rset.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return plants;
		
	}
	
	// 유니크 제약조건 걸려있는 컬럼으로 조회
	public Plant findByNo(int plantNo) {
		// getter
		
		Plant plant = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = """
						SELECT
						       PLANT_NO
						     , PLANT_NAME
						     , PLANT_COLOR
						     , ENROLL_DATE
						  FROM
						       PLANT
						 WHERE
						       PLANT_NO = ?
					 """;
		
		try {
			
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, plantNo);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				plant = new Plant(rset.getInt("PLANT_NO")
								, rset.getString("PLANT_NAME")
								, rset.getString("PLANT_COLOR")
								, rset.getDate("ENROLL_DATE"));
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(rset != null) {
					rset.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return plant;
		
	}
	
	// LIKE키워드 써서 조회
	public List<Plant> findByKeyword(String keyword) {
		// getter???
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		List<Plant> plants = new ArrayList();
		
		String sql = """
						SELECT
						       PLANT_NO
						     , PLANT_NAME
						     , PLANT_COLOR
						     , ENROLL_DATE
						  FROM
						       PLANT
						 WHERE
						       PLANT_NAME LIKE '%'||?||'%'
						 ORDER
						    BY
						       ENROLL_DATE DESC
					 """;
		
		try {
			
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				plants.add(new Plant(rset.getInt("PLANT_NO")
								   , rset.getString("PLANT_NAME")
								   , rset.getString("PLANT_COLOR")
								   , rset.getDate("ENROLL_DATE")));
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(rset != null) {
					rset.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return plants;
	}

	public void update() {
		
	}

	public void delete() {
		
	}

}
