package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestVo;


@WebServlet("/guest")
public class GuestBookController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("guestbook");
		String act = request.getParameter("action");
		
		if("add".equals(act)) {
			System.out.println("guest > add");
			
			String name = request.getParameter("name");
			String pass = request.getParameter("pass");
			String content = request.getParameter("content");
			
			GuestVo vo = new GuestVo(name, pass, content);
			GuestDao dao = new GuestDao();
			dao.insert(vo);
			
			WebUtil.redirect(request, response, "/mysite/guest");
			
		} else if("deleteForm".equals(act)) {
			System.out.println("guest > deleteForm");
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
			
		} else if("delete".equals(act)) {
			System.out.println("guest > delete");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String pass = request.getParameter("pass");
			
			GuestDao Dao = new GuestDao();
			Dao.delete(no, pass);
			
			WebUtil.redirect(request, response, "/mysite/guest");
			
		} else {
			System.out.println("guest > addlist");
			
			GuestDao Dao = new GuestDao();
			List<GuestVo> guestList = Dao.getList();
			
			request.setAttribute("guestList", guestList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
		}
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
