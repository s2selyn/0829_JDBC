package com.kh.board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.board.model.vo.Board;
import com.kh.common.JDBCTemplate;

public class BoardDAO {
	
	// 나중에 뭘로 돌아갈까? INSERT 하는건데, DML이니까 intro
	public int insertBoard(Connection conn, Board board) {
		
		// 0) 변수 선언
		PreparedStatement pstmt = null;
		int result = 0;
		
		String sql = """
						INSERT
						  INTO
						       BOARD
						       (
						       BOARD_NO
						     , BOARD_TITLE
						     , BOARD_CONTENT
						     , BOARD_WRITER
						       )
						VALUES
						       (
						       SEQ_BNO.NEXTVAL
						     , ?
						     , ?
						     , ?
						       )
					 """;
		
		// 커넥션으로 pstmt 객체 생성
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, Integer.parseInt(board.getBoardWriter())); // 따옴표 들어가면 안됨
// 15:15 넣을 값은 어디에 들어있음? BoardWriter에 들어있는데 얘는 타입이 String임, 이 메소드는 int 인자받아야함 String으로는 못들어감, int형으로 바꿔줘야함 --> "1" 이렇게 생겼을텐데? 래퍼클래스에 파싱메소드
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result; // 서비스로 돌아감
		
	}
	
	// 보드는 시퀀스로 만들기 때문에 게시글이 갑자기 1다음에 632만번째 나올리가없음, 정렬을 보드넘버로 해보자
	public List<Board> selectBoardList(Connection conn) {
		
		PreparedStatement pstmt = null;
		
		// SELECT니까 ResultSet 필요함
		ResultSet rset = null;
		
		// 나중에 결과받을 리스트
		List<Board> boards = new ArrayList();
		
		// 목록에서는 번호, 제목, 작성자, 날짜정도만(일반적으로 내용은 안나옴)
		// BOARD만으로 USERID 조회 못하니까 JOIN(오라클구문 썼음)
		// 지우면 나중에 삭제컬럼을 Y로 바꿀거니까 조회할때는 N인 애들만
		String sql = """
						SELECT
						       BOARD_NO
						     , BOARD_TITLE
						     , USER_ID
						     , CREATE_DATE
						  FROM
						       BOARD
						     , MEMBER
						 WHERE
						       BOARD_WRITER = USERNO
						   AND
						       DELETE_YN = 'N'
						 ORDER
						    BY
						       BOARD_NO DESC
					 """;
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			// SQL 구멍없으니 시원하게 가져오자
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				//VO로 가져가야하는데 딱맞는 생성자가 없어서 그냥 없는거 null값 넣자
				
				Board board = new Board(rset.getInt("BOARD_NO")
									  , rset.getString("BOARD_TITLE")
									  , null
									  , rset.getString("USERID")
									  , rset.getDate("CREATE_DATE")
									  , null);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
