package com.kh.board.model.vo;

import java.sql.Date;
import java.util.Objects;

public class Board {
	
	// VO의 모양새 : DB의 BOARD 테이블의 컬럼과 비슷하게
	private int boardNo; 		 // BOARD_NO NUMBER
	private String boardTitle;   // BOARD_TITLE VARCHAR2
	private String boardContent; // BOARD_CONTENT VARCHAR2
	private String boardWriter; 	 // BOARD_WRITER NUMBER FOREIGN KEY(USERNO) 번호가 오면 파싱하면 됨
	private Date createDate; 	 // CREATE_DATE DATE
	private String deleteStatus; // DELETE_STATUS CHAR(1), char형이면 관리하기 어려우니까(rset에서 getChar없음) 스트링으로 관리
	// 실제 테이블에 있는 내용을 지우면 살리기 쉽지않음, 보통 삭제기능을 구현할때는 진짜로 삭제하지 않고 삭제여부 컬럼을 만들어서 이 값만 Y였다가 N이었다가 쏙쏙 바꿔쭘
	// 지우고나서 잘못지웠으면 살려주세요 하는데, 삭제하고 커밋하면 살리기 쉽지않음, 백업한 데이터를 긁어와야함, 보통은 삭제할때 DELETE 안하고 삭제여부 컬럼을 만들어서 이 값을 업데이트 하는 형식으로 구현
	// 컬럼 모양으로 필드를 구성, 테이블 컬럼의 자료형에 맞춰서 구성했지만 작업한 다음에 한번쯤 생각해볼 여지가 있음
	// INSERT 할때는 작성자가 숫자여도 상관없는데 SELECT하면 int로 받을 수 없음, 필드를 조회용, 삽입용 따로 구성하는 방법이 있는데, 지금은 후딱 끝내려고 스트링으로 변경
	
	// setter 빼고 구성할것임(불변성을 지키기 위해)
	
	// 모든 필드에대한 매개변수생성자(세터가 없으니 기본생성자는 의미가 없음, 빼자)
	public Board(int boardNo, String boardTitle, String boardContent, String boardWriter, Date createDate,
			String deleteStatus) {
		super();
		this.boardNo = boardNo;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardWriter = boardWriter;
		this.createDate = createDate;
		this.deleteStatus = deleteStatus;
	}

	// getter
	public int getBoardNo() {
		return boardNo;
	}

	public String getBoardTitle() {
		return boardTitle;
	}

	public String getBoardContent() {
		return boardContent;
	}

	public String getBoardWriter() {
		return boardWriter;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	// equals, hashCode
	@Override
	public int hashCode() {
		return Objects.hash(boardContent, boardNo, boardTitle, boardWriter, createDate, deleteStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		return Objects.equals(boardContent, other.boardContent) && boardNo == other.boardNo
				&& Objects.equals(boardTitle, other.boardTitle) && Objects.equals(boardWriter, other.boardWriter)
				&& Objects.equals(createDate, other.createDate) && Objects.equals(deleteStatus, other.deleteStatus);
	}
	
}
