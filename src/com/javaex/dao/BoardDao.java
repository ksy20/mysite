package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {
	
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

			//연결하기
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
	
	//전체 가져오기
	public List<BoardVo> getList() {
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();

		getConnection();
		try {
			// SQL문 준비
			String query = "";
			query += " select   us.name, ";
			query += "          bo.no, ";
			query += "          bo.title, ";
			query += "          bo.content, ";
			query += "          bo.hit, ";
			query += "          to_char(bo.reg_date, 'yy-mm-dd hh:mi'), ";
			query += "          bo.user_no ";
			query += " from     board bo, users us ";
			query += " where    bo.user_no= us.no ";
			query += " order by bo.no desc ";

			// 쿼리
			pstmt = conn.prepareStatement(query);

			// 실행
			rs = pstmt.executeQuery();
			
			// 결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String name = rs.getString("name");
							
				BoardVo boardVo = new BoardVo(no, title, content, hit, regDate, userNo, name);
				boardList.add(boardVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return boardList;
	}
	
	//하나만 가져오기
	public BoardVo gBoard(int no) {
		
	BoardVo vo = null;
	int userNo = 0;
	getConnection();
				
	try {
		// 3. SQL문 준비 / 바인딩 / 실행
		//문자열
		String query = "";
		query += " select   us.name, ";
		query += "          bo.no, ";
		query += "          bo.title, ";
		query += "          bo.content, ";
		query += "          bo.hit, ";
		query += "          to_char(bo.reg_date, 'YY-MM-DD HH:MI'), ";
		query += "          bo.user_no";
		query += " from     board bo, users us ";
		query += " where    bo.user_no = us.no ";
		query += " and		bo.no = ? ";
					
		//쿼리
		pstmt = conn.prepareStatement(query);

		//바인딩
		pstmt.setInt(1, no);

		//실행
		rs = pstmt.executeQuery();

		// 4.결과처리
		while (rs.next()) {
			int num = rs.getInt("no");
			String title = rs.getString("title");
			String content = rs.getString("content");
			int hit = rs.getInt("hit");
			String regDate = rs.getString("reg_date");
			userNo = rs.getInt("user_no");
			String name = rs.getString("name");
			
			vo = new BoardVo(num, title, content, hit, regDate, userNo, name);
		}

	} catch (SQLException e) {
				System.out.println("error:" + e);
	} 
				
	close();
		
	return vo;
	} 
	
	//insert 하기
	public void write(BoardVo boardVo) {

		try {
			getConnection();

			String query ="";
			query += " insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, 0,  sysdate, ?) " ;

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, boardVo.getTitle());    
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUser_no());
			
			int count = pstmt.executeUpdate();  
			System.out.println(count + " 건이 등록되었습니다.");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
	}
	
	//삭제
	public void delete(int num) {
		getConnection();

		try {

			// SQL문 준비
			String query = "";
			query += " delete board ";
			query += " where no = ? ";

			// 쿼리
			pstmt = conn.prepareStatement(query);

			// 바인딩
			pstmt.setInt(1, num);

			// 실행
			int count = pstmt.executeUpdate();

			// 결과처리
			System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
	}

	//수정
	public int update (BoardVo vo) {
		
		int count = 0;
		getConnection();
		
		try {
			//sql준비
			//문자열
			String query="";
			
			query += " update board ";
			query += " set    title = ?, ";
			query += "        content = ?, ";
			query += " where  no = ? ";	
			
			//쿼리문
			pstmt = conn.prepareStatement(query);
			
			//바인딩
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());
			
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
