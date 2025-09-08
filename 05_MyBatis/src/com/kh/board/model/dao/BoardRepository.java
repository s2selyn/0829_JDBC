package com.kh.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kh.board.model.vo.Board;

public class BoardRepository {
	
	public List<Board> selectBoardList(SqlSession session) {
		
		// 설정하고 세팅
		// 보드매퍼에서 리스트를 담아가든 단수보드를 담아가든할거임
		// SELECT니까 반환형에 이거 적어줘야할텐데 풀클래스명 적기 힘들어, 별칭을 미리 등록해주면 좋다, 별칭등록은 config파일의 typeAlias에 추가
		// 매퍼도 미리 준비하러가자
		
		// SQL문 실행한 결과를 돌려줘야함
// ???????
		// 어디있는 SQL문? boadr-mapper파일에 적어둔 namespace 속성값을 적어줌, 이 안에 있는 select문 두개중에 id속성값을 적어서 구분
		return session.selectList("boardMapper.selectBoardList");
		
	}
	
	public Board selectBoard(SqlSession session, int boardNo) {
		return session.selectOne("boardMapper.selectBoard", boardNo);
	}

}
