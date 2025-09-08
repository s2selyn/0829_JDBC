package com.kh.statement.model.vo;

import java.sql.Date;
import java.util.Objects;

/*
 * VO(Value Object) => 값 객체
 * => 테이블의 한 행에 대한 데이터를 기록할 저장용 객체
 * 
 * VO가 가져야할 특성 == 불변성
 * 이친구는 지금 setter가 있음, 중간에 필드값이 언제든지 바뀔 수 있으므로 세터를 가지면 안됨
 * 무조건 생성자를 이용해서 값을 담아야함, 값에 변화가 일어나면 안되니까
 * 
 * VO클래스의 필드 구성 자체를 MEMBER테이블의 컬럼들과 유사하게 구성
 * SEQUENCE 및 DEFAULT값을 조건으로 사용하는 경우
 * 해당 필드를 제외한 나머지 필드값을 초기화 할 수 있는 생성자를 추가로 구성해둘 것
 * 
 */
public class Member {
	
	private int userNo;
	private String userId;
	private String userPwd;
	private String userName;
	private String email;
	private Date enrollDate; // sql.Date로 import
	// DB의 테이블을 참고하여 컬럼과 유사하게 구성
	
	public Member() {
		super();
	}
	
	public Member(String userId, String userPwd, String userName, String email) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
	}
	// 기본값이 존재하는 컬럼을 제외하고 나머지 필드값만 초기화 해주는 생성자 추가
	

	public Member(int userNo, String userId, String userPwd, String userName, String email, Date enrollDate) {
		super();
		this.userNo = userNo;
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
		this.enrollDate = enrollDate;
	}

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

	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", email=" + email + ", enrollDate=" + enrollDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, enrollDate, userId, userName, userNo, userPwd);
	}

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
