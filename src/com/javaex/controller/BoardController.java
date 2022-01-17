package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.GuestDao;
import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;


@WebServlet("/board")
public class BoardController extends HttpServlet {
	
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Board");
		String act = request.getParameter("action");
		
		if ("list".equals(act)) {
			
				System.out.println("action > list");

				List<BoardVo> boardList = new BoardDao().getList();

				request.setAttribute("boardList", boardList);

				WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}else if ("writeForm".equals(act)) {
				System.out.println("action > writeForm");

			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		}else if("write".equals(act)) {
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				
				HttpSession session = request.getSession();
				UserVo user = (UserVo)session.getAttribute("authUser");
				int userNo = user.getNo();
				
				BoardVo boardVo = new BoardVo();
				boardVo.setUserNo(userNo);
				boardVo.setTitle(title);
				boardVo.setContent(content);
				
				new BoardDao().write(boardVo);
				
				WebUtil.redirect(request, response, "/mysite/board?action=list");
		}else if ("mofifyForm".equals(act)) {
				HttpSession session = request.getSession();
				int no = ((UserVo)session.getAttribute("gBoard")).getNo();
				
				BoardDao boardDao = new BoardDao();
				BoardVo boardVo  = boardDao.gBoard(no);
				
				request.setAttribute("boardVo", boardVo);
				WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");//파일 위치.jsp
		}else if ("modify".equals(act)) {
				
				HttpSession session = request.getSession();
				int no = ((UserVo)session.getAttribute("gBoard")).getNo();
				
				String name = request.getParameter("name");
				String password = request.getParameter("title");
				String gender = request.getParameter("content");
				
				UserVo vo = new UserVo(no, "", name, password, gender);
				
				UserDao dao= new UserDao();
				dao.update(vo);
				
				UserVo sVo = (UserVo)session.getAttribute("authUser");
				sVo.setName(name);
				
				WebUtil.redirect(request, response, "/mysite/board?action=list");
				
		}else if ("delete".equals(act)) {
			
				System.out.println("board > delete");
			
				int no = Integer.parseInt(request.getParameter("no"));
				String delete = request.getParameter("delete");
			
				BoardDao Dao = new BoardDao();
				Dao.delete(no);
			
				WebUtil.redirect(request, response, "/mysite/board?action=list");
		}
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
