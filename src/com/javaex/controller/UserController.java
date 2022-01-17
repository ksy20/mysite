package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			
			
			//파라미터 vo로 만들기
			UserVo uvo = new UserVo(id, password, name, gender);
			
			//vo를 userdao의 insert() 로 저장하기
			UserDao uDao = new UserDao();
			uDao.insert(uvo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/JoinOk.jsp");
			
		}else if("loginForm".equals(act)) {
			System.out.println("user > loginform");
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
		
		}else if("login".equals(act)) {
			System.out.println("user > login");
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			//System.out.println(id);
			//System.out.println(password);
			
			UserDao dao = new UserDao();
			UserVo authVo= dao.getUser(id, password);
			//System.out.println(authVo);
			
			if(authVo == null) {//로그인 실패
				System.out.println("로그인 실패");
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
			}else {//로그인 성공
				System.out.println("로그인 성공");
				
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);
				
				WebUtil.redirect(request, response, "/mysite/main");
			}
			
		}else if("logout".equals(act)) {
			System.out.println("user > logout");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite/main");
			
		}else if("modifyForm".equals(act)) {
			
			HttpSession session = request.getSession();
			int no = ((UserVo)session.getAttribute("gUser")).getNo();
			
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.gUser(no);
			
			//포워드
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");//파일 위치.jsp
		}else if ("modify".equals(act)) {
			
			HttpSession session = request.getSession();
			int no = ((UserVo)session.getAttribute("gUser")).getNo();
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo vo = new UserVo(no, "", name, password, gender);
			
			UserDao dao= new UserDao();
			dao.update(vo);
			
			UserVo sVo = (UserVo)session.getAttribute("authUser");
			sVo.setName(name);
			
			WebUtil.redirect(request, response, "/mysite/main");
			
		}
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
