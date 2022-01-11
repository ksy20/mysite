package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/user");
		
		String act = request.getParameter("action");
		
		if("joinForm".equals(act)) {
			System.out.println("usesr>joinForm");
			
//			UserDao userDao = new UserDao();
//			userDao.insert(null);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");//파일 위치.jsp
	
		}else if("join".equals(act)) {
			System.out.println("user > join");
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo uvo = new UserVo(id, password, name, gender);
			UserDao uDao = new UserDao();
			
			uDao.insert(uvo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/JoinOk.jsp");	
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
