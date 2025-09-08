package com.kh.statement.model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.statement.model.dto.PasswordDTO;
import com.kh.statement.model.vo.Member;

public class MemberDao {
	
	private Properties prop = new Properties();
	
	// 메소드 호출할 때마다
	// xml파일로부터 Properties객체로 입력받는 코드를 써야함 중복이다
	// 무슨 메소드를 호출하든지 다 다르지만 앞은 똑같음, 기본생성자를 항상 부름
	// new MemberDao().XXX
	// MemberDao 기본생성자에 xml 넣어줌
	public MemberDao() {
		
		try {
			prop.loadFromXML(new FileInputStream("resources/member-mapper.xml")); // 파일이름규칙도 앞쪽이 내가 사용하려는 도메인, 대시하고 매핑할 목적으로 쓰는거니까 매퍼라고 지음
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int save(Connection conn, Member member) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		// 이걸 properties로 빼고싶은데 프로퍼티는 개행이 안되어서 개행하면 인식이 안됨 --> 지금 properties 적합하지 않은듯
		// 다른 친구 없을까 --> storeToXML 만들러 properties 실행클래스에 감
		// 이 인서트문을 외부에서 관리하고 싶음 --> 내용 복사해서 멤버매퍼에 빼자
		// 빼고왔음!
		
		// 여기 들어갈 문자열은 XML 파일에서 끌어올거임, 읽어오려면 스트림, 뭘 이용해서 키에있는 밸류를 가져옴?
		// JDBCTemplate에서 어떻게 가져옴? properties 있어야겠네,, 객체 있어야 가져옴
		// Properties prop = new Properties(); 필드로 두고 주석처리함
		
		// properties 참조해서 그냥로드말고 loadFromXML, 매개변수 타입은 InputStream, 설명 써있음
		// The XML document must have the following DOCTYPE declaration
		// 멤버매퍼 파일 resources 폴더로 옮김
		try {
			prop.loadFromXML(new FileInputStream("resources/member-mapper.xml")); // 예외처리
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 여기 들어가는 값은 이제 문자열 리터럴로 적는게 아니라
		// 아까 properties 객체에 참조해서 getProperty, 인자로는 entry의 key속성값(메소드명으로 맞춰서 만들어줄거니까 메소드명)
		String sql = prop.getProperty("save");
		
		try {
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, member.getUserId());
			pstmt.setString(2, member.getUserPwd());
			pstmt.setString(3, member.getUserName());
			pstmt.setString(4, member.getEmail());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
		
	}
	
	// 여기 SQL도 밖에 빼줘야지
	public List<Member> findAll(Connection conn) {
		
		List<Member> members = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// Properties prop = new Properties(); 이거 또해야해? 안하려면 어떻게해?
		// 클래스 선언부에 필드로 두자!
		
		// prop.loadFromXML(new FileInputStream); 이거도 또 쓰기 싫음
		// 이거 매번 하기 싫음, 메소드 호출할 때 이거 읽어와서 중복되는거 없애고 싶음 --> 기본생성자 호출할 때
		// DAO 부를 때 무조건 기본생성자 호출하니까, 컨트롤러에서 서비스 부를때도 항상 기본생성자 부르니까 거기도 커넥션 넣어둠
		// --> 올라가서 기본생성자 만들고옴
		
		// 여기 SQL도 멤버래퍼에 옮김
		// 안에 지워버림
		String sql = prop.getProperty("findAll");
		// SQL을 가져오는 시점이? MemberDao 객체를 만들 때 읽어옴, 만약에 오름차순으로 바뀐다면? 파일의 내용이 바뀐것임
		// 코드에 적은게 아니라 메소드 호출 시점에 이걸 읽어오니까 밖에서 SQL문 바꿔도 껐다켜지 않아도 바로 반영이 됨, 외부에서 읽어오니까(하드코딩안했음, 코드수정이 일어난것이 아님)
		
		// key를 잘못쓰거나 하면 못찾음, 없으면 얘가 뭘 돌려주냐? null을 돌려줌! key값을 못찾으면 반환값이 null이 돌아옴
		// String에 null이 들어가게 됨 --> 실행할 SQL 문은 비어 있거나 널일 수 없음 --> 아 내가 key값을 잘못썼구나 생각!
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				Member member = new Member(rset.getInt("USERNO")
										 , rset.getString("USERID")
										 , rset.getString("USERPWD")
										 , rset.getString("USERNAME")
										 , rset.getString("EMAIL")
										 , rset.getDate("ENROLLDATE"));
				
				members.add(member);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return members;
		
	}
	
	// 여기도 갈아엎
	public Member findById(Connection conn, String userId) {
		
		Member member = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 이거 멤버매퍼에 옮기고 내용 바꿈
		String sql = prop.getProperty("findById"); // 메소드명이랑 똑같이 해둔 entry 태그 key 속성값
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				
				member = new Member(rset.getInt("USERNO")
								  , rset.getString("USERID")
								  , rset.getString("USERPWD")
								  , rset.getString("USERNAME")
								  , rset.getString("EMAIL")
								  , rset.getDate("ENROLLDATE"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return member;
		
	}
	
	public List<Member> findByKeyword(Connection conn, String keyword) {
		
		List<Member> members = new ArrayList();
		
		PreparedStatement pstmt = null;
		
		ResultSet rset = null;
		
		// SQL문 빼기, 코드에 문자열 데이터를 적었던것을 외부에 텍스트로 빼놓고 파일을 입력받아서 쓰는것
		// properties은 개행이 안되서 보기좋게 만들기가.. xml이 더 편함
		String sql = prop.getProperty("findByKeyword");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				members.add(new Member(rset.getInt("USERNO")
									 , rset.getString("USERID")
									 , rset.getString("USERPWD")
									 , rset.getString("USERNAME")
									 , rset.getString("EMAIL")
									 , rset.getDate("ENROLLDATE")));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
			
		}
		
		return members;
		
	}
	
	public int update(Connection conn, PasswordDTO pd) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		// 여기도 SQL 수정, 하나의 SQL문은 하나의 entry 태그로 관리
		String sql = prop.getProperty("update");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pd.getNewPassword());
			pstmt.setString(2, pd.getUserId());
			pstmt.setString(3, pd.getUserPwd());
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
		
	}
	
	public int delete(Connection conn, Member member) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		// SQL문 밖으로 빼고 prop 이용하는걸로 수정
		String sql = prop.getProperty("delete");
		
		try {
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, member.getUserId());
			pstmt.setString(2, member.getUserPwd());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(pstmt);
			
		}
		
		return result;
		
	}
	
}
