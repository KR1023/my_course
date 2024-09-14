package com.ysh.my_course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ysh.my_course.domain.User;
import com.ysh.my_course.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserViewController {
	private final UserService userService;
	
	@GetMapping("/login")
	public String loginPage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail") != null)
			return "redirect:/course";
		
		return "auth/loginForm";
	}
	
	@GetMapping("/register")
	public String registerForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail") != null)
			return "redirect:/course";
		
		return "auth/registerForm";
	}
	
	@GetMapping("/my-page")
	public String loginUserView(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		if(userEmail == null) {
			return "error/error_403";
		}
		
		User user = userService.getUserByEmail(userEmail);
		model.addAttribute("user", user);
		
		return "user/myPage";
		
	}
}
