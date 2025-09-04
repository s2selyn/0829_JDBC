package com.kh.statement.run;

import com.kh.common.JDBCTemplate;
import com.kh.statement.view.EmployeeView;

public class EmployeeRun {

	public static void main(String[] args) {
		
		JDBCTemplate.resistDriver();
		
		EmployeeView ev = new EmployeeView();
		
		ev.mainMenu();

	}

}
