package com.ysh.my_course.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.my_course.dto.course.AddCourseDto;
import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.dto.course.UpdateCourseDto;
import com.ysh.my_course.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CourseController {
	private final CourseService courseService;
	
	@PostMapping("/course")
	public ResponseEntity<String> addCourse(HttpServletRequest request, @RequestBody AddCourseDto dto){
		HttpSession session = request.getSession();
		
		log.info(dto.toString());
		courseService.addCourse(dto);
		return ResponseEntity.status(HttpStatus.OK).body("addedCourse");
	}
	
	@GetMapping("/course/{courseId}")
	public ResponseEntity<ResponseCourseDto> getCourse(HttpServletRequest request, @PathVariable(name = "courseId") Long courseId){
		HttpSession session = request.getSession();
		
		log.info("[courseId = " + courseId + "]");
		ResponseCourseDto course = courseService.getCourse(courseId);
		
		
		
		return ResponseEntity.status(HttpStatus.OK).body(course); 
	}
	
	@GetMapping("/course")
	public ResponseEntity<List<ResponseCourseDto>> getCourses(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		log.info("getCourses");
		
		List<ResponseCourseDto> list = courseService.getCourses();
		
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
}
