package com.kh.statement.run;

import com.kh.common.JDBCTemplate;
import com.kh.common.Template;
import com.kh.statement.view.MemberView;

public class MemberRun {

	public static void main(String[] args) {
		
		// 스태틱 블록임, 시작할때 하면 여기서 등록하고 돌것임
		// JDBCTemplate.resistDriver();
		
		// 마이바티스로 바꾸고 jdbc 주석처리
		// Template.getSqlSession();
		// 확인하고 주석처리
		
		MemberView mv = new MemberView();
		
		mv.mainMenu();
		
	}

}
