package com.kh;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesRun {

	public static void main(String[] args) {
		
		// Map의 일종인데 문자열로 key-value를 다룰 수 있음
		Properties prop  = new Properties();
		prop.setProperty("A", "B");
		
		// 외부 파일에서 입력받을 때 많이 사용
		// store는 출력하는 메소드, properties가 현재 가지고 있는 것을 외부로 출력
		// 첫번째 인자, 두번째 인자는 코멘트, 예외처리 해주기
		try {
			
			// prop.store(new FileOutputStream("driver.properties"), "setting for DBMS");
			prop.storeToXML(new FileOutputStream("member-mapper.xml"), "MEMBER SQL");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 실행하고 파일 생성된것 확인, 주석처리
		// 파일에서 URL, 사용자이름, 비밀번호 작성
		// key는 내맘대로 자기가쓰고싶은대로
		
	}

}
