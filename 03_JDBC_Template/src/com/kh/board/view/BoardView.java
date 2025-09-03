package com.kh.board.view;

import java.util.List;
import java.util.Scanner;

import com.kh.board.controller.BoardController;
import com.kh.board.model.dto.BoardDTO;
import com.kh.board.model.vo.Board;

public class BoardView {
	
	// 일반적인 CRUD 작업 메소드명 지을 때 스타일
	/*
	 * SELECT / INSERT / UPDATE / DELETE
	 * 
	 * BOARD를 예시로 들어보겠음
	 * 
	 * 이건 공공기관, 정부기관 파견 갔을 때 이런 스타일(나랏돈으로 하는 스타일)
	 * INSERT : insertBoard()
	 * UPDATE : updateBoard()
	 * DELETE : deleteBoard()
	 * SELECT(단수행) : selectBoardList()
	 * SELECT(단일행) : selectBoard()
	 * 
	 * ----------
	 * 
	 * 이건 회사 스타일
	 * INSERT : save()
	 * UPDATE : update()
	 * DELETE : deleteByXXX()
	 * SELECT(다수행) : findAll()
	 * SELECT(단일행) : findByXXX()
	 * 
	 */
	
	// 입력 받아야하니까 스캐너 두기
	private Scanner sc = new Scanner(System.in);
	
	// 하는김에 컨트롤러도 필드로
	private BoardController bc = new BoardController();
	
	// 보드의 메인메뉴 구성
	public void mainMenu() {
		
		while(true) {
			
			System.out.println("게시판 서비스입니다!");
			
			// 게시판 들어오면 원래 게시글이 바로 보이니까 오자마자 전체 게시글 목록 조회해서 바로 보여주자
			// 전체 게시글 목록 조회
			// 따로 안빼고 그냥 여기서 바로 보여주자고 했음
			selectBoardList(); // 정부기관사업스타일로
			
			System.out.println("\n==================================================");
			System.out.println("1. 게시글 상세조회");
			System.out.println("2. 게시글 작성하기");
			System.out.println("3. 게시글 삭제하기");
			System.out.println("9. 회원 메뉴로 돌아가기");
			
			System.out.print("메뉴를 선택해주세요 > ");
			int menuNo = sc.nextInt();
			sc.nextLine();
			
			switch(menuNo) {
			case 1 : break;
			case 2 : insertBoard(); break;
			case 3 : break;
			case 9 : System.out.println("잘가시오 ~ "); return;
			}
			
		}
		
	}
	
	private void insertBoard() {
		
		System.out.println("게시글 작성 서비스입니당!");
		
		// 세개만 입력받을거임, title, content, writer
		System.out.print("아이디를 입력해주세요 > ");
		String userId = sc.nextLine();
		
		System.out.print("제목을 입력해주세요 > ");
		String boardTitle = sc.nextLine();
		
		System.out.print("본문을 입력해주세요 > ");
		String boardContent = sc.nextLine();
		// 이러면 뷰가 자신의 할 일 1절을 해버림
		// 뷰 1절 끝!
		
		// 이 세개를 궁극적으로 DB까지 가야함, 직접 못가고 건너감, 작업 흐름을 컨트롤러에게 떠넘김
		// 메소드를 호출하면서 떠넘김, 세개보내기 번거롭다 하나로 묶어보내자, 뷰계층에서 컨트롤러로 계층이동하고싶음, 데이터를 세개들고가고싶으니 DTO에 담아보내자
		int result = bc.insertBoard(new BoardDTO(boardTitle, boardContent, userId));
		// 컨트롤러 다녀오니 정수가 옴
		
		// 결과 확인해서 출력
		System.out.println();
		System.out.println();
		
		if(result > 0) {
			System.out.println("게시글 작성 성공!");
		} else {
			System.out.println("게시글 작성 실패..ㅠ");
		}
		
		System.out.println();
		System.out.println();
		
	}
	
	// 출력이니까 반환 할 거 없음 void
	private void selectBoardList() {
		
		// 보여주려면 얘 혼자 못보여줌
		System.out.println();
		
		// 뷰입장에서는 컨트롤러에게 게시글들 전체 줘라 해야함
		List<Board> boards = bc.selectBoardList(); // 나중에 보드가 들어간 리스트가 올것임
		
	}

}
