package com.kh.javascript;

public class Run {

	public static void main(String[] args) {
		
		Div div = new Div();
		
		System.out.println(div.id);
		System.out.println(div.className);
		System.out.println(div.tagName);
		
		// 처음에 일단 주소를 찾아감 --> div
		// 참조 --> .
		// 속성의 공간 --> id
		// 공간에 값을 대입하고 싶음 --> =
		div.id = "아이디"; // 이게 public 필드에 값을 set하는방법
		
	}

}
