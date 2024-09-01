package com.ysh.my_course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

	@GetMapping("/login")
	public String loginPage() {
		return "auth/loginForm";
	}
	
	@GetMapping("/register")
	public String registerForm() {
		return "auth/registerForm";
	}
}
