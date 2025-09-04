package com.kh.board.controller;

import java.util.List;

import com.kh.board.model.dto.BoardDTO;
import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;

public class BoardController {
	
	public int insertBoard(BoardDTO bd) {
		
		// 뭐로 돌아갈진 모르겠지만 메소드명 알고 인자로 넘긴거 있음
		// 뷰로부터 받았는데 얘는 그냥 흐름처리, 중간다리, 중간제어자 역할, 딱히 하는게 없음, 가공도 앞에서 DTO로 담아왔음
		// 받아온 bd를 DAO로 바로가지않음! 오늘 중간다리가 새로 생김! 보드서비스에게 받아라 하고 넘김
		int result = new BoardService().insertBoard(bd);
		// 갔다오니 int result 왔음, 뷰에 돌려주면 끝
		return result; // 한줄로 쓸수도 있지만 그럼 컨트롤러가 왜있는지 의미가.. 최소 두줄 ㅎㅎ
		
	}
	
	public List<Board> selectBoardList() {
		
		// 컨트롤러도 이거 자기한테 없음, 서비스야 전체좀줘
		List<Board> boards = new BoardService().selectBoardList(); // 나중에 어떻게 와요? 보드 리스트
		// 컨트롤러도 이거 뷰로 반납하겠지
		return boards;
		
	}
	
	public Board selectBoard(int boardNo) {
		return new BoardService().selectBoard(boardNo); // 그냥 의미상실의 길을 선택해버림, 서비스에게 보드내놔함
	}
	
	// 앞에서 보드넘버 받아와야함
	public int deleteBoard(int boardNo) {
		return new BoardService().deleteBoard(boardNo);
	}

}
