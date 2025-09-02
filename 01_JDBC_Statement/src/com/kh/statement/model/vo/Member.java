package com.kh.statement.model.vo;

import java.sql.Date;
import java.util.Objects;

/*
 * VO(Value Object) => 값 객체(값을 담기 위한 용도, 무슨 값?)
 * => 테이블의 한 행에 대한 데이터를 기록할 저장용 객체(일단 지금 기본적ㄱ으로)
 * 
 * VO가 가져야할 특성 == 불변성
 * 이친구는 지금 setter가 있음, 중간에 필드값이 언제든지 바뀔 수 있으므로 세터를 가지면 안됨
 * 무조건 생성자를 이용해서 값을 담아야함, 값에 변화가 일어나면 안되니까
 * 
 * 지금부터 만들 프로그램은 회원 관리 프로그램
 * 회원의 정보들을 이런것들을 저장해놔야지 하고 테이블 구성해둠
 * 새로운 회원이 들어온다는 것은 한 명의 회원의 정보는 한 행으로 들어올 것
 * 추가할거면 값이 여섯개 필요함, 이걸 자바에서 들고가야함, 실질적으로 업무 해결에 필요한 데이터들, 이것들을 담을 무언가가 필요함 --> VO가 그 역할을 하겠다
 * Member는 테이블의 한 행의 데이터를 담을 수 있도록 구성할 것 --> 클래스가 가지는 속성을? 필드(객체의 속성을 담는 친구) --> 필드 구성을 테이블의 모양으로 구성(테이블의 모양, 테이블이 가지는 컬럼)
 * 클래스가 가질 수 있는 건 세개밖에 없음(필드 생성자 메소드)
 * 
 * VO클래스의 필드 구성 자체를 MEMBER테이블의 컬럼들과 유사하게 구성(똑같이는 할 수 없음, 오라클의 데이터 타입이랑 자바에서의 데이터 타입이 다름)
 * 최대한 유사하게 구성을 해서 테이블에 담을 데이터 또는 테이블에서 조회하는 데이터를 이 친구를 객체로 올려가지고 거기에 담을 수 있도록 만들것
 * USERNO와 ENROLLDATE 컬럼은 사용자에게 값을 입력받지 않고 ENROLLDATE는 SYSDATE 넣을거고 USERNO는 시퀀스로 만들것 --> 생성자를 만들 때 이 두개만 빠진 생성자를 추가로 만들 것
 * SEQUENCE 및 DEFAULT값을 조건으로 사용하는 경우
 * 해당 필드를 제외한 나머지 필드값을 초기화 할 수 있는 생성자를 추가로 구성해둘 것
 * 
 */
public class Member {
	
	// VO클래스 만들 때 항상 만들면서 추상화를 진행했는데 이제는 항상 테이블이 먼저 만들어지고 그다음 클래스가 만들어지는것, 테이블이 있어야 무멀 넣을지가 정해진거니까
	// 앞으로는 만들 때 크게 고민할 필요가 없음, 테이블 모양대로 만들면 됨
	private int userNo; // USERNO NUMBER PRIMARY KEY, DB의 숫자값으로 들어갈건데 자바에서는 int, 들어가는 숫자가 22억이 넘을리 없을것같고
	private String userId; // USERID VARCHAR2(15) UNIQUE NOT NULL, 오라클에서 최대 15자의 문자열 형태의 데이터는 자바에서 스트링
	private String userPwd; // USERPWD VARCHAR2(20) NOT NULL
	private String userName; // USERNAME VARCHAR2(15) NOT NULL
	private String email; // EMAIL VARCHAR2(30)
	private Date enrollDate; // ENROLLDATE DEFAULT SYSDATE NOT NULL <-- java.sql.Date로 import
	// 오라클의 날짜형식과 자바의 날짜 형식은 완전히 다름, 어떻게 할 지 정해야함
	// 자바에서 SQL 타입의 데이터를 다루기 위한 친구는 따로 API로 구현되어 있음, 처음이니까 정석대로 Date 타입으로
	// 필드부를 DB의 테이블을 참고하여 컬럼과 유사하게 구성
	
	// 필드부 다음에 생성자부
	// 기본 생성자
	public Member() {
		super();
	}
	
	// USERNO는 시퀀스를 가지고 만들어줄거고 ENROLLDATE는 디폴트 값이 있음
	// 추가로 생성자를 만들 때 기본값으로 넣는 필드 두개 뺀 나머지만 초기화해주는 생성자를 하나 더 넣어줌
	public Member(String userId, String userPwd, String userName, String email) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
	}
	// 기본값이 존재하는 컬럼을 제외하고 나머지 필드값만 초기화 해주는 생성자 추가
	
	// 필드 전부 있는 생성자
	public Member(int userNo, String userId, String userPwd, String userName, String email, Date enrollDate) {
		super();
		this.userNo = userNo;
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
		this.enrollDate = enrollDate;
	}

	// 메소드부는 똑같음
	// 게터세터
	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	// toString
	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", email=" + email + ", enrollDate=" + enrollDate + "]";
	}

	// hashCode
	// 만약에 필드값이 같다면 테이블에 데이터가 똑같다는 것
	// PK가 있기 때문에 PK가 똑같으면 같은 행이라는 뜻, 그러면 같은 데이터라고 인식을 시켜야함, 그러므로 VO는 equals, hashCode가 오버라이딩 되어있어야함
	@Override
	public int hashCode() {
		return Objects.hash(email, enrollDate, userId, userName, userNo, userPwd);
	}

	// equals
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		return Objects.equals(email, other.email) && Objects.equals(enrollDate, other.enrollDate)
				&& Objects.equals(userId, other.userId) && Objects.equals(userName, other.userName)
				&& userNo == other.userNo && Objects.equals(userPwd, other.userPwd);
	}

}
