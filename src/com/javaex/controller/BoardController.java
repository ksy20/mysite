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
				
				BoardVo postVo = new BoardVo();
				postVo.setUserNo(userNo);
				postVo.setTitle(title);
				postVo.setContent(content);
				
				new BoardDao().write(postVo);
				
				WebUtil.redirect(request, response, "/mysite/board?action=list");
			}
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
