package com.ysh.my_course.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.my_course.domain.Course;
import com.ysh.my_course.dto.course.AddCourseDto;
import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.dto.course.UpdateCourseDto;
import com.ysh.my_course.dto.enroll.RequestEnrollDto;
import com.ysh.my_course.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CourseController {
	private final CourseService courseService;
	
	@PostMapping("/course")
	public ResponseEntity<Course> addCourse(HttpServletRequest request, @RequestBody AddCourseDto dto){
		HttpSession session = request.getSession();
		
		log.info(String.format("Called addCourse [dto : %s]", dto.toString()));
		Course course = courseService.addCourse(dto);
		return ResponseEntity.status(HttpStatus.OK).body(course);
	}
	
	@GetMapping("/course/{courseId}")
	public ResponseEntity<ResponseCourseDto> getCourse(HttpServletRequest request, @PathVariable(name = "courseId") Long courseId){
		HttpSession session = request.getSession();
		
		log.info("Called getCourse : [courseId = " + courseId + "]");
		ResponseCourseDto course = courseService.getCourse(courseId);
		
		return ResponseEntity.status(HttpStatus.OK).body(course); 
	}
	
	@GetMapping("/course")
	public ResponseEntity<Page<ResponseCourseDto>> getCourses(@RequestParam(required= false, defaultValue="0", value="page") int pageNo, HttpServletRequest request) throws IllegalArgumentException{
		
		log.info("Called getCourses");
		
		Page<ResponseCourseDto> list = courseService.getCourses(pageNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@PutMapping("/course/{courseId}")
	public ResponseEntity<ResponseCourseDto> updateCourse(HttpServletRequest request, @PathVariable(name = "courseId") Long courseId, @RequestBody UpdateCourseDto dto){
		HttpSession session = request.getSession();
		log.info("updateCourse");
		ResponseCourseDto responseDto = courseService.updateCourse(courseId, dto);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
	
	@DeleteMapping("/course/{courseId}")
	public ResponseEntity<String> deleteCourse(HttpServletRequest request, @PathVariable(name = "courseId") Long courseId){
		HttpSession session = request.getSession();
		log.info(String.format("deleteCourse: [CourseId] : %d", courseId));
		courseService.deleteCourse(courseId);
		
		return ResponseEntity.status(HttpStatus.OK).body("deleteSuccess");
	}
	
	/*
	 *	수강 확인/신청/취소  
	 */
	@PostMapping("/enroll/check")
	public ResponseEntity<String> checkEnrollment(@RequestBody RequestEnrollDto dto, HttpServletRequest request){
		HttpSession session = request.getSession();
//		if(session.getAttribute("loginEmail") == null) {
//			return ResponseEntity.status(HttpStatus.OK).body("cantCheck");
//		}
		try {
			log.info(String.format("Called checkEnrollment [courseId : %d, userEmail : %s]", dto.getCourseId(), dto.getUserEmail()));
			
			return courseService.checkEnrollment(dto);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}
	
	
	@PostMapping("/enroll")
	public ResponseEntity<String> enrollCourse(@RequestBody RequestEnrollDto dto, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail") == null) {
			return ResponseEntity.status(HttpStatus.OK).body("needToLogin");
		}
		try {
			return courseService.enrollCourse(dto);
		}catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/enroll/cancel")
	public ResponseEntity<String> cancelCourse(@RequestBody RequestEnrollDto dto, HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail") == null) {
			return ResponseEntity.status(HttpStatus.OK).body("needToLogin");
		}
		
		try {
			String result = courseService.cancelCourse(dto);
			if(result.equals("success"))
				return ResponseEntity.status(HttpStatus.OK).body("deleteSucceeded");
			else if(result.equals("userNotFound"))
				return ResponseEntity.status(HttpStatus.OK).body("userNotFound");
			else
				return ResponseEntity.status(HttpStatus.OK).body("error");
		}catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
