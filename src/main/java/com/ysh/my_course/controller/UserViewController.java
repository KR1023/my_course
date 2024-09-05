package com.ysh.my_course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserViewController {

	@GetMapping("/login")
	public String loginPage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail") != null)
			return "redirect:/test";
		
		return "auth/loginForm";
	}
	
	@GetMapping("/register")
	public String registerForm() {
		return "auth/registerForm";
	}
}
