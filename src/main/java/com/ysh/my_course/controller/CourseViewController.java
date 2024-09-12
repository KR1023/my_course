package com.ysh.my_course.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
			Page<ResponseCourseDto> dtoList = courseService.getCourses(pageNo, 15, "common", null);
			model.addAttribute("list", dtoList);
		}catch(IllegalArgumentException e) {
			return "error/error_400";
		}
		
		return "course/list";
	}
	
	@GetMapping("/course/{courseId}")
	public String courseDetail(@PathVariable(value = "courseId") Long courseId, Model model) {
		ResponseCourseDto course = courseService.getCourse(courseId);
		
		model.addAttribute("course", course);
		return "course/detail";
		
	}
	@GetMapping("/course/add")
	public String addCourseView(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail") == null) {
			return "error/error_403";
		}
		
		return "course/add";
	}
	
	/**
	 * 강의 관리
	 */
	@GetMapping("/course/manage")
	public String manageCourse(@RequestParam(name = "page", defaultValue = "0", required=false) int pageNo, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		
		if(userEmail == null) {
			return "error/error_403";
		}
		
		Page<ResponseCourseDto> dtoList = courseService.getCourses(pageNo, 10, "manage", userEmail);
		model.addAttribute("list", dtoList);
		return "manage/course/course_manage";
	}
	
	@GetMapping("/course/update/{courseId}")
	public String updateCourse(@PathVariable(name="courseId") Long courseId, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		
		if(userEmail == null) {
			return "error/error_403";
		}
		
		ResponseCourseDto course = courseService.getCourse(courseId);
		
		model.addAttribute("course", course);
		
		return "manage/course/course_update";
	}
}
