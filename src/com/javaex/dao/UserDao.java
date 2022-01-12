package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {
	
	//필드
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//닫기
	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	//저장 메소드(회원가입)
	public int insert(UserVo vo) {
		
		int count = 0;
		getConnection();
		
		try {
			//sql준비
			//문자열
			String query="";
			
			query += " insert into users " ;
			query +=" values (seq_users_no.nextval, ?, ?, ?, ?) ";		
			
			//쿼리문
			pstmt = conn.prepareCall(query);
			
			//바인딩
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());
			
			//실행
			count = pstmt.executeUpdate();
			
			//결과 처리
			System.out.println(count + "건이 처리되었습니다");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
	}
	
	//회원 정보 가져오기 로그인용
	public UserVo getUser(String id, String password) {
		UserVo vo = null;
		getConnection();
				
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			//문자열
			String query = "";
			query += " select no, ";
			query += "       name ";
			query += " from users ";
			query += " where id = ? ";
			query += " and password = ? ";
					
			//쿼리
			pstmt = conn.prepareStatement(query);

			//바인딩
			pstmt.setString(1, id);
			pstmt.setString(2, password);

			//실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while(rs.next()) {
						
				int no = rs.getInt("no");
				String name = rs.getString("name");
				
				vo = new UserVo(no, name);
			}

		} catch (SQLException e) {
					System.out.println("error:" + e);
		} 
				
		close();
		return vo;
	}
	
	
	public UserVo gUser(int num) {
		
		UserVo vo = null;
		getConnection();
				
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			//문자열
			String query = "";
			query += " select  no, ";
			query += "         id,";
			query += "         password,";
			query += "         name,";
			query += "         gender ";
			query += " from    users ";
			query += " where   no=? ";
					
			//쿼리
			pstmt = conn.prepareStatement(query);

			//바인딩
			pstmt.setString(1, "no");

			//실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while(rs.next()) {
						
				int no = rs.getInt("no");
				String id = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				
				vo = new UserVo(no, id, password, name, gender);
			}

		} catch (SQLException e) {
					System.out.println("error:" + e);
		} 
				
		close();
		return vo;
		
	} 
		
	public int update (UserVo vo) {
		
		int count = 0;
		getConnection();
		
		try {
			//sql준비
			//문자열
			String query="";
			
			query += " update users ";
			query += " set    id = ?, ";
			query += "        password = ?, ";
			query += "        name = ?, ";
			query += "        gender = ? ";
			query += " where  no = ? ";	
			
			//쿼리문
			pstmt = conn.prepareCall(query);
			
			//바인딩
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());
			pstmt.setInt(5, vo.getNo());
			
			
			//실행
			count = pstmt.executeUpdate();
			
			//결과 처리
			System.out.println(count + "건이 수정되었습니다");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
