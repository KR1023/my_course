package com.ysh.my_course.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CourseViewController {

	private final CourseService courseService;
	
	@GetMapping("/course")
	public String courseList(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		log.info("Called courseList View");
		try {
			Page<ResponseCourseDto> dtoList = courseService.getCourses(pageNo);
			model.addAttribute("list", dtoList);
		}catch(IllegalArgumentException e) {
			return "error/error_400";
		}
		
		return "course/list";
	}
}
