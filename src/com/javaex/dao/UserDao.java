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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
