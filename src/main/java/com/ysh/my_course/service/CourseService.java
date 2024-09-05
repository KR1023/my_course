package com.ysh.my_course.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ysh.my_course.domain.Course;
import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.course.AddCourseDto;
import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.dto.course.UpdateCourseDto;
import com.ysh.my_course.repository.CourseRepository;
import com.ysh.my_course.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

	private final UserRepository userRepository;
	private final CourseRepository courseRepository;
	
	public Course addCourse(AddCourseDto dto) {
		User user = userRepository.findByEmail(dto.getUserEmail());
		Course course = Course.builder()
				.courseName(dto.getCourseName())
				.content(dto.getContent())
				.maxAttendee(dto.getMaxAttendee())
				.user(user)
				.build();
		
		return courseRepository.save(course);
	}
	
	public ResponseCourseDto getCourse(Long courseId) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Course is not found.[courseId : %s]", courseId)));
		ResponseCourseDto responseDto = ResponseCourseDto.builder()
											.id(course.getId())
											.courseName(course.getCourseName())
											.maxAttendee(course.getMaxAttendee())
											.content(course.getContent())
											.createdDt(course.getCreatedDt())
											.userEmail(course.getUser().getEmail())
											.build();
		return responseDto;
	}
	
	public List<ResponseCourseDto> getCourses(){
		List<Course> courseList = courseRepository.findAll();
		
		List<ResponseCourseDto> dtoList = courseList.stream()
				.map(e -> ResponseCourseDto
						.builder()
						.id(e.getId())
						.courseName(e.getCourseName())
						.maxAttendee(e.getMaxAttendee())
						.content(e.getContent())
						.createdDt(e.getCreatedDt())
						.userEmail(e.getUser().getEmail())
						.build())
				.collect(Collectors.toList());
		
		return dtoList;
	}
	
	@Transactional
	public ResponseCourseDto updateCourse(Long courseId, UpdateCourseDto dto) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Course is not found. [courseId : %s]", courseId)));
		
		course.update(dto.getCourseName(), Integer.parseInt(dto.getMaxAttendee()), dto.getContent());
		
		courseRepository.save(course);
		
		ResponseCourseDto response = ResponseCourseDto.builder()
											.id(course.getId())
											.courseName(course.getCourseName())
											.maxAttendee(course.getMaxAttendee())
											.content(course.getContent())
											.createdDt(course.getCreatedDt())
											.userEmail(course.getUser().getEmail())
											.build();
		return  response;
		
	}
	
	public void deleteCourse(Long courseId) {
		courseRepository.deleteById(courseId);
	}
}
