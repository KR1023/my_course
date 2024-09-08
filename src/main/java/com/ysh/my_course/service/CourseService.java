package com.ysh.my_course.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ysh.my_course.domain.Course;
import com.ysh.my_course.domain.Enrollment;
import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.course.AddCourseDto;
import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.dto.course.UpdateCourseDto;
import com.ysh.my_course.dto.enroll.RequestEnrollDto;
import com.ysh.my_course.repository.CourseRepository;
import com.ysh.my_course.repository.EnrollmentRepository;
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
	private final EnrollmentRepository enrollRepository;
	
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
	
	public Page<ResponseCourseDto> getCourses(int pageNo) throws IllegalArgumentException{
	 	List<Course> courseList = courseRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		
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
		
		PageRequest pageRequest= PageRequest.of(pageNo, 15);
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), dtoList.size());
		
		Page<ResponseCourseDto> responseList = new PageImpl<ResponseCourseDto>(dtoList.subList(start, end), pageRequest, dtoList.size());
		
		return responseList;
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
	
	/**
	 * 수강 신청 / 취소
	 */
	
	public ResponseEntity<String> checkEnrollment(RequestEnrollDto dto){
		/**
		 * 1. 이미 수강 신청한 강좌.
		 * 2. 정원 초과 강좌.
		 * 3. 마감된 강좌.
		 * 4. 신청 가능 강좌
		 */
		Date date = new Date();
		log.info("date " + date);
		
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException((String.format("Course is not found.[courseId : %s]", dto.getCourseId()))));
		User user = userRepository.findByEmail(dto.getUserEmail());
		
		Enrollment enrollment = enrollRepository.findByCourseAndUser(course, user);
		
		if(enrollment != null) {
			return ResponseEntity.status(HttpStatus.OK).body("alreadyEnrolled");
		}
		
		int maxAttendee = course.getMaxAttendee();
		long enrolled = enrollRepository.countByCourse(course);
		
		if(enrolled == maxAttendee) {
			return ResponseEntity.status(HttpStatus.OK).body("overloaded");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}
	
	public ResponseEntity<String> enrollCourse(RequestEnrollDto dto) {
		Course course = courseRepository.findById(dto.getCourseId())
							.orElseThrow(() -> new IllegalArgumentException((String.format("Course is not found.[courseId : %s]", dto.getCourseId()))));
		User user = userRepository.findByEmail(dto.getUserEmail());
		
		Enrollment enrollment = enrollRepository.findByCourseAndUser(course, user);
		
		if(enrollment != null) {
			log.info(String.format("enrollment's courseId : %d / userEmail : %s",enrollment.getCourse().getId(), enrollment.getUser().getEmail()));
			return ResponseEntity.status(HttpStatus.OK).body("alreadyEnrolled");
		}
		
		int maxAttendee = course.getMaxAttendee();
		long enrolled = enrollRepository.countByCourse(course);
		
		log.info(String.format("maxAttendee : %d / enrolled cnt : %d", maxAttendee, enrolled));
		
		if(enrolled < maxAttendee) {
			enrollRepository.save(Enrollment.builder()
					.course(course)
					.user(user)
					.build());
			
			return ResponseEntity.status(HttpStatus.OK).body("enrolled");
		}else if(enrolled == maxAttendee){
			return ResponseEntity.status(HttpStatus.OK).body("overloaded");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
	}
	
}
