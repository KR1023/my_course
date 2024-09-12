package com.ysh.my_course.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.ysh.my_course.domain.UploadedFile;
import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.course.AddCourseDto;
import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.dto.course.UpdateCourseDto;
import com.ysh.my_course.dto.enroll.RequestEnrollDto;
import com.ysh.my_course.repository.CourseRepository;
import com.ysh.my_course.repository.EnrollmentRepository;
import com.ysh.my_course.repository.UploadedFileRepository;
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
	private final UploadedFileRepository fileRepository;
	
	public Course addCourse(AddCourseDto dto) {
		User user = userRepository.findByEmail(dto.getUserEmail());
		
		UploadedFile file = null;
		
		if(dto.getFileId() != null) {
			file = fileRepository.findById(dto.getFileId())
					.orElseThrow(() -> new IllegalArgumentException(String.format("File not found : [%d]", dto.getFileId())));
		}
		
		
		Course course = Course.builder()
				.courseName(dto.getCourseName())
				.content(dto.getContent())
				.maxAttendee(dto.getMaxAttendee())
				.closingDt(dto.getClosingDt())
				.file(file)
				.user(user)
				.build();
		
		return courseRepository.save(course);
	}
	
	public ResponseCourseDto getCourse(Long courseId) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Course is not found.[courseId : %s]", courseId)));
		
		if(course.getFile() == null) {
			course.setFile(new UploadedFile());
		}
		
		ResponseCourseDto responseDto = ResponseCourseDto.builder()
				.id(course.getId())
				.courseName(course.getCourseName())
				.maxAttendee(course.getMaxAttendee())
				.content(course.getContent())
				.createdDt(course.getCreatedDt())
				.closeDt(course.getClosingDt())
				.userEmail(course.getUser().getEmail())
				.refFilepath(course.getFile().getRefFilepath())
				.build();
		
		return responseDto;
	}
	
	public Page<ResponseCourseDto> getCourses(int pageNo, int pageSize, String auth, String userEmail) throws IllegalArgumentException{
		List<Course> courseList = null;
		
	 	List<ResponseCourseDto> dtoList = new ArrayList<>();
	 	
	 	if(auth.equals("manage")) {
	 		courseList = courseRepository.findByUserEmail(userEmail, Sort.by(Sort.Direction.DESC, "id"));
	 		
	 		dtoList = courseList.stream()
					.map(e -> {
						if(e.getFile() != null) {
							return 
									ResponseCourseDto
									.builder()
									.id(e.getId())
									.courseName(e.getCourseName())
									.maxAttendee(e.getMaxAttendee())
									.content(e.getContent())
									.createdDt(e.getCreatedDt())
									.closeDt(e.getClosingDt())
									.userEmail(e.getUser().getEmail())
									.refFilepath(e.getFile().getRefFilepath())
									.build();
						}else {
							return ResponseCourseDto
									.builder()
									.id(e.getId())
									.courseName(e.getCourseName())
									.maxAttendee(e.getMaxAttendee())
									.content(e.getContent())
									.createdDt(e.getCreatedDt())
									.closeDt(e.getClosingDt())
									.userEmail(e.getUser().getEmail())
									.refFilepath(null)
									.build();
						}
					})
					.collect(Collectors.toList());
	 		
	 	}else {
	 		courseList = courseRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	 		
	 		dtoList = courseList.stream()
					.map(e -> {
						if(e.getFile() != null) {
							return 
									ResponseCourseDto
									.builder()
									.id(e.getId())
									.courseName(e.getCourseName())
									.maxAttendee(e.getMaxAttendee())
									.content(e.getContent())
									.createdDt(e.getCreatedDt())
									.closeDt(e.getClosingDt())
									.userEmail(e.getUser().getEmail())
									.refFilepath(e.getFile().getRefFilepath())
									.build();
						}else {
							return ResponseCourseDto
									.builder()
									.id(e.getId())
									.courseName(e.getCourseName())
									.maxAttendee(e.getMaxAttendee())
									.content(e.getContent())
									.createdDt(e.getCreatedDt())
									.closeDt(e.getClosingDt())
									.userEmail(e.getUser().getEmail())
									.refFilepath(null)
									.build();
						}
					})
					.collect(Collectors.toList());
	 	}
		
		
		PageRequest pageRequest= PageRequest.of(pageNo, pageSize);
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), dtoList.size());
		
		Page<ResponseCourseDto> responseList = new PageImpl<ResponseCourseDto>(dtoList.subList(start, end), pageRequest, dtoList.size());
		
		log.info(responseList.toString());
		return responseList;
	}
	
	@Transactional
	public ResponseCourseDto updateCourse(Long courseId, UpdateCourseDto dto) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Course is not found. [courseId : %s]", courseId)));
		UploadedFile file = fileRepository.findById(dto.getFileId())
				.orElseThrow(() -> new IllegalArgumentException(String.format("File is not found. [fileId : %s]", dto.getFileId())));
		
		course.update(dto.getCourseName(), Integer.parseInt(dto.getMaxAttendee()), dto.getContent(), dto.getClosingDt(), file);
		
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
		LocalDate now = LocalDate.now();
		
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException((String.format("Course is not found.[courseId : %s]", dto.getCourseId()))));
		User user = userRepository.findByEmail(dto.getUserEmail());
		
		Enrollment enrollment = enrollRepository.findByCourseAndUser(course, user);
		
		if(now.isAfter(course.getClosingDt())) {
			return ResponseEntity.status(HttpStatus.OK).body("closed");
		}
		
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
		LocalDate now = LocalDate.now();
		
		Course course = courseRepository.findById(dto.getCourseId())
							.orElseThrow(() -> new IllegalArgumentException((String.format("Course is not found.[courseId : %s]", dto.getCourseId()))));
		User user = userRepository.findByEmail(dto.getUserEmail());
		
		if( user != null) {
			Enrollment enrollment = enrollRepository.findByCourseAndUser(course, user);
			
			if(enrollment != null) {
				log.info(String.format("enrollment's courseId : %d / userEmail : %s",enrollment.getCourse().getId(), enrollment.getUser().getEmail()));
				return ResponseEntity.status(HttpStatus.OK).body("alreadyEnrolled");
			}
			
			if(now.isAfter(course.getClosingDt())) {
				return ResponseEntity.status(HttpStatus.OK).body("closed");
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
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
	}
	
	@Transactional
	public String cancelCourse(RequestEnrollDto dto) throws IllegalArgumentException, Exception{
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException((String.format("Course is not found.[courseId : %s]", dto.getCourseId()))));
		User user = userRepository.findByEmail(dto.getUserEmail()); 
		
		if(user != null) {
			enrollRepository.deleteByCourseAndUser(course, user);
			return "success";
		}else {
			return "userNotFound";
		}
	}
}
