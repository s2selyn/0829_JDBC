package com.kh.board.model.service;

import java.sql.Connection;
import java.util.List;

import com.kh.board.model.dao.BoardDAO;
import com.kh.board.model.dto.BoardDTO;
import com.kh.board.model.vo.Board;
import com.kh.common.JDBCTemplate;
import com.kh.statement.model.dao.MemberDao;
import com.kh.statement.model.vo.Member;

public class BoardService {
	
	// 멤버때 했던것처럼 커넥션 필드로 두고 기본생성자에서 커넥션 초기화해주자
	private Connection conn = null;
	
	public BoardService() {
		conn = JDBCTemplate.getConnection();
	}
	
	public int insertBoard(BoardDTO bd) { // 뭘로 돌아갈진 모르겠고 서비스한테 DTO 받았음
		
		// 서비스가 컨트롤러에게 전달받은것!
		// 내가 입력한 값을 가지고
		// BOARD테이블에 한 행 INSERT해줘~
		
		int result = 0;
		
		// 커넥션은 위에 필드로 빼놨음, 일단 뭘해야하냐?
		// 1. 값의 유효성 검증
		// 빈문자열이나 공백문자가 들어오면 제목 이런데 들어오면 애매함, 제목이 클릭안되면 안됨
		if("".equals(bd.getBoardTitle().trim())) { // 사용자가 입력한 제목을 가져와서 trim 하면 앞뒤로 스페이스바 날아감
			return result; // 더이상 진행하지 못하도록 리턴함, 유효하지 않은 값이 들어온것이므로, DB 가기도 전인 이상한거
		}
		
		// 만약에 들어온 값이 이렇다고 가정(작성자에서 우리가 아이디를 받았음)
		// 제목 : 안녕하세요, 내용 : 반갑습니다, 아이디 : admin
		// 우리가 이 내용대로 INSERT 할 수 있나요? BOARD 테이블을 봤을 때, 작성자 항목은 아이디가 들어갈 수 있는게 아니라 MEMBER테이블의 USERNO만 들어갈 수 있음
		// admin을 입력했다면 USERNO 1이 들어가야함, 이거가지고 INSERT 못함, 이걸로 DAO 넘어갈 수 없음
		// 2. 인증 / 인가
		// 이 데이터로 글을 쓸 수 있는가?
		// 실제로 USERNO 1을 얻어내기 위해서는 뭘 해봐야함? 입력받은 이 아이디가 있는지 없는지 알아야함 --> 테이블에 가서 SELECT 해야함 --> 서비스에서 테이블가나? 아님, DAO가 가야함
		// INSERT 하기 전에 먼저 SELECT 해봐야함
		// new BoardDAO().searchId(conn, bd.getBoardWriter());
		// 그냥 해달라고 하면 못찾음, 커넥션 줘야함, 입력받은 아이디값이나 bd를 넘김, bd에서 아이디만 빼서 줘도 되고
		// 이러면 안됨!!!!
		// 왜? 아이디값으로 회원을 조회하는 기능은 이미 멤버에 만들어놓음, 이거 똑같이 쓸거아니에요. 똑같은 중복코드를 또 만드는게 됨, 만들어놓은거 써
		Member member = new MemberDao().findById(conn, bd.getBoardWriter());
		
// 15:00
		
		if(member != null) { // 사용자가 입력한 아이디가 DB에 있다는 뜻
			
			// 3. 데이터 가공
			// DAO 아직 못감, member에서 유저넘버 겟해서 값을 뽑아
			int userNo = member.getUserNo();
			
			// 어디 넣어줌? 이때 VO를 쓰겠다, 진짜 INSERT 할 수 있는 값이니까
			Board board = new Board(0,
									bd.getBoardTitle(),
									bd.getBoardContent(),
									String.valueOf(userNo), // 숫자인데 문자로 넣어줘야함???
									null,
									null);
			
			// INSERT 할수있는 값들로 구성됨, 드디어 다 만들었음
			// DAO 호출
			result = new BoardDAO().insertBoard(conn, board);
			// 갔다왔더니 정수가 왔음
			// 서비스 입장에서도 컨트롤러한테 결과를 돌려줘야함, 메소드 반환타입이 int여야함 --> 수정
			
		}
		
		// 끝났으니까 트랜잭션 처리
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}
		
		// 자원반납
		JDBCTemplate.close(conn);
		
		return result; // if를 돌리든 안돌리든 나중에 이건 돌려줘야하니까 위에서 미리 선언, 성공했을때만 1 가고 나머지는 초기값 0이 가게됨
		
	}
	
	public List<Board> selectBoardList() { // 다 정해져있음
		
		// 리스트는 서비스한테 없음, 얘도 DAO야~~~
		// 근데 얘는 줄게있음, 그냥 주면 안됨
		List<Board> boards = new BoardDAO().selectBoardList(conn); // 갔다오면 List올것임
		
		// 돌려주기 전에 해야 할일 -> 사용이 끝난 커넥션 보내주기
		JDBCTemplate.close(conn);
		
		// 서비스 입장에서도 이 리스트를 돌려줘야함
		return boards;
		
	}

}
