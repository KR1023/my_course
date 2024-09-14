package com.ysh.my_course.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.enroll.ResponseEnrollDto;
import com.ysh.my_course.dto.user.ResponseUserDto;
import com.ysh.my_course.service.CourseService;
import com.ysh.my_course.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserViewController {
	private final UserService userService;
	private final CourseService courseService;
	
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
		List<ResponseEnrollDto> enrollList = courseService.getEnrollList(0, userEmail);
		model.addAttribute("user", user);
		model.addAttribute("enroll", enrollList);
		
		return "user/myPage";
		
	}
	
	@GetMapping("/user/manage")
	public String manageUser(@RequestParam(name="page", defaultValue="0", required=false) int pageNo, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		if(userEmail == null) {
			return "error/error_403";
		}
		
		User user = userService.getUserByEmail(userEmail);
		
		Page<ResponseUserDto> userList = userService.getUsers(pageNo);
		
		model.addAttribute("manage", "user");
		model.addAttribute("list", userList);
		
		return "manage/user/user_manage";
	}
}
