package com.kh.statement.run;

import com.kh.common.JDBCTemplate;
import com.kh.statement.view.MemberView;

public class MemberRun {

	public static void main(String[] args) {
		
		// 스태틱 블록임, 시작할때 하면 여기서 등록하고 돌것임
		JDBCTemplate.resistDriver();
		
		MemberView mv = new MemberView();
		
		mv.mainMenu();
		
	}

}
