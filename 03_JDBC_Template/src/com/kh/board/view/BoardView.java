package com.kh.board.view;

import java.util.List;
import java.util.Scanner;

import com.kh.board.controller.BoardController;
import com.kh.board.model.dto.BoardDTO;
import com.kh.board.model.vo.Board;

public class BoardView {
	
// 여기 코멘트 있음
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
			case 1 : selectBoard(); break; // 목록을 보면 하나 눌러서 상세조회할건데, 식별할수있는게 게시글 번호니까 사용자에게 게시글 번호를 입력받아서 내용을 조회하는 기능으로 구현하자
			case 2 : insertBoard(); break;
			case 3 : deleteBoard(); break;
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
		
		// 조회해온 내용 출력해야하는데 없을수도 있고 있을수도 있음
		// 있으면 있는거 뿌려주고 없으면 없는거 뿌려주기
		if(!boards.isEmpty()) {
			
			// 여기 if블록에 들어왔다는것은 비어있지 않은거니까 뭐라도 하나 있다는건데 몇개 있을지 모르고 있는건 다 뿌려줘야함, 뭐써서 출력? forEach 써보자, 향상된 for문 너모 마니 씀^_^
			boards.stream().map(b -> "\n게시글번호 : " + b.getBoardNo()
								   + "\t제목 : " + b.getBoardTitle()
								   + "\t\t작성자 : " + b.getBoardWriter()
								   + "\t작성일 : " + b.getCreateDate())
						   .forEach(System.out::print);
			// 자바 탭문자에 한계가 있어서 예쁘게 안나옴
			
		} else {
			
			System.out.println("게시글이 존재하지 않습니다.");
			System.out.println("첫 게시글의 주인공이 되어보세요!");
			
		}
		
	}
	
	private void selectBoard() {
		
		// 전체보기하면 게시글 번호가 나오니까 이걸 하나 입력받자
		System.out.println("게 시 글 상 세 조 회 서 비 스 입 니 다.");
		System.out.print("조회할 게시글 번호를 입력하세요 > ");
		int boardNo = sc.nextInt();
		sc.nextLine();
		
		// 입력받는다고 뷰가 뭐 아나요? 쩌기 DB까지 가야 안다 가자가자
		// DB가려면 일단 컨트롤러, 그냥 가도 되나? 특정 게시글을 보고싶은거니까 입력한거 숫자 들고가야함
		Board board = bc.selectBoard(boardNo); // 나중에 뭘로와? 하나로 해온거니까 board, 많아야 한개, PK로 조회하러가는거니까
		
		// 조회결과가 있을수도 있고 없을수도 있음
		// 조회결과가 있는지없는지는 board가 null인지 확인, null이면 조회결과가 없는거, null이 아니면 조회결과가 있는거
		if(board != null) {
			
			// 있다는건 무조건 한개임, 반복문 필요없음
			System.out.println("\n\n제목 : " + board.getBoardTitle());
			System.out.println("\n작성자 : " + board.getBoardWriter());
			System.out.println("\n작성일 : " + board.getCreateDate());
			System.out.println("\n본문 : ");
			System.out.println("--------------------------------------------------");
			System.out.println(board.getBoardContent());
			System.out.println("--------------------------------------------------");
			
		} else {
			System.out.println("존재하지 않는 게시글 번호입니다.");
		}

// 16:45 아이디어
		// 보통 상세페이지 조회하면 내가 뭐 하기전까지 그 페이지에 그대로 있음, 센스발휘해주자
		while(true) {
			
			System.out.println("목록으로 돌아가시려면 돌아가기를 입력하세요.");
			String exit = sc.nextLine();
			if("돌아가기".equals(exit)) {
				return;
			}
			
		}
		
	}
	
	private void deleteBoard() {
		
		System.out.print("주세요 보드번호 > ");
		int boardNo = sc.nextInt();
		sc.nextLine();
		
		if(bc.deleteBoard(boardNo) > 0) {
			System.out.println("지우기 성공~");
		} else {
			System.out.println("못지움 까비 ~");
		}
		
	}

}
