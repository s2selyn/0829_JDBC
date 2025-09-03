package com.kh.board.model.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
						     , USERID
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
				
				boards.add(board); // 리스트에 담아줌
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 자원반납 해줘야징
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return boards;
		
	}
	
	// 내용도 상세하게 조회해보자
	public Board selectBoard(Connection conn, int boardNo) {
		
		Board board = null; // 나중에 반환해줄거
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		// 밑반찬 삼총사 준비 끝
		
		// 메인반찬
		// 텍스트 블록 이거 나온지 얼마안됨, 옛날엔 얼마나 불편했을까
		// 삭제여부 들고감, VO에 넣어줄거니까
		// 무슨문법 쓸지 모르니까 다 써보자 아까 오라클 썼으니까 이제 ANSI
		// 컬럼명 YN 이렇게 많이하는데 선생님은 맘에 안드심
		String sql = """
						SELECT
						       BOARD_NO
						     , BOARD_TITLE
						     , BOARD_CONTENT
						     , USERID
						     , CREATE_DATE
						     , DELETE_YN
						  FROM
						       BOARD
						  JOIN
						       MEMBER ON (USERNO = BOARD_WRITER)
						 WHERE
						       DELETE_YN = 'N'
						   AND
						       BOARD_NO = ?
					 """;
		
// 16:30 싹다 조회해서 VO에 담자??? 그리고 생각해야할일?
		try {
			
			pstmt = conn.prepareStatement(sql); // null로 초기화해둔것에 pstmt 주소값 담아줘야함, 그래야 참조해서 메소드 호출할 수 있음, 안그러면 NullPointerException 발생함
			pstmt.setInt(1, boardNo); // 바인딩
			rset = pstmt.executeQuery();
			if(rset.next()) {
				
				board = new Board(rset.getInt("BOARD_NO")
								, rset.getString("BOARD_TITLE")
								, rset.getString("BOARD_CONTENT")
								, rset.getString("USERID")
								, rset.getDate("CREATE_DATE")
								, rset.getString("DELETE_YN"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return board;
		
	}
	
	public int deleteBoard(Connection conn, int boardNo) {
		
		// try with resource 써서 알아서 반납, 커넥션은 앞에가서 반납할거임, 서비스에서는 커넥션이랑 보드넘버 보내야함
		try(PreparedStatement pstmt = conn.prepareStatement("""
															   UPDATE
															          BOARD
															      SET
															          DELETE_YN = 'Y'
															    WHERE
															          BOARD_NO = ?
															""")) {
			pstmt.setInt(1, boardNo);
			return pstmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	// 다음주 맛보기 복붙
	public void outputHTML(Connection conn) {
		
		FileWriter fos = null;
		BufferedWriter bw = null;
		
		try {
			fos = new FileWriter("Template_BOARD.html");
			bw = new BufferedWriter(fos);
			List<Board> boardList = selectBoardList(conn);
			String html = "<!DOCTYPE html>";
			html += "<html>";
			html += "<head><title>게시판이예용</title>";
			html += "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css'>";
			html += "<script src='https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js'></script>";
			html += "<script src='https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js'></script>";
			html += "<script src='https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js'></script>";
			html += "</head>";
			html += "<body>";
			html += "<h1 style='margin-bottom:30px; text-align:center'>JDBC 게시판 서비스입니다</h1>";
			html += "<table class='table'>";
			html += "<tr><th>제목</th><th>작성자</th><th>작성일</th></tr>";
			for(Board b : boardList) {
				html += "<tr>";
				html += "<td>" + b.getBoardTitle() + "</td>";
				html += "<td>" + b.getBoardWriter() + "</td>";
				html += "<td>" + b.getCreateDate() + "</td>";
				html += "</tr>";
			}
			html += "</table>";
			html += "</body>";
			html += "</html>";
			
			bw.write(html);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
