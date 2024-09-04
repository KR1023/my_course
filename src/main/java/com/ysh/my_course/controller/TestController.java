package com.ysh.my_course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TestController {
	@GetMapping("/test")
	public String test(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			String loginEmail = (String)session.getAttribute("loginEmail");
			
			System.out.println("loginEmail = " + loginEmail);
			if(loginEmail == null) {
				return "error/notFound";
			}else {
				return "home";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "error/notFound";
	}
}
