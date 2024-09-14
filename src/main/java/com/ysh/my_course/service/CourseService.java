package com.ysh.my_course.service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.ysh.my_course.dto.course.CourseManageDtoInterface;
import com.ysh.my_course.dto.course.ResponseCourseDto;
import com.ysh.my_course.dto.course.UpdateCourseDto;
import com.ysh.my_course.dto.enroll.RequestEnrollDto;
import com.ysh.my_course.dto.enroll.ResponseApplicantDto;
import com.ysh.my_course.dto.enroll.ResponseEnrollDto;
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
		int applicantCnt = Long.valueOf(enrollRepository.countByCourse(course)).intValue();
		
		if(course.getFile() == null) {
			course.setFile(new UploadedFile());
		}
		
		ResponseCourseDto responseDto = ResponseCourseDto.builder()
				.id(course.getId())
				.courseName(course.getCourseName())
				.maxAttendee(course.getMaxAttendee())
				.applicant(applicantCnt)
				.content(course.getContent())
				.createdDt(course.getCreatedDt())
				.closeDt(course.getClosingDt())
				.userEmail(course.getUser().getEmail())
				.instName(course.getUser().getName())
				.fileId(course.getFile().getId())
				.filename(course.getFile().getFilename())
				.refFilepath(course.getFile().getRefFilepath())
				.build();
		
		return responseDto;
	}
	
	public Page<CourseManageDtoInterface> getCourses(int pageNo, int pageSize, String courseName, String auth, String userEmail) throws IllegalArgumentException{
		List<CourseManageDtoInterface> courseList = null;
		
	 	if(auth.equals("manage")) {
	 		if(courseName != null)
	 			courseList = courseRepository.findByUserEmailAndCourseNameContaining(userEmail, courseName, Sort.by(Sort.Direction.DESC, "id"));
	 		else
	 			courseList = courseRepository.findByUserEmail(userEmail, Sort.by(Sort.Direction.DESC, "id"));
	 		
	 	}else {
	 		
	 		if(courseName != null) {
	 			courseList = courseRepository.findByCourseNameContaining(courseName, Sort.by(Sort.Direction.DESC, "id"));
	 		}else {
	 			courseList = courseRepository.findAllCourses(Sort.by(Sort.Direction.DESC, "id"));
	 		}
	 	}
		
		
		PageRequest pageRequest= PageRequest.of(pageNo, pageSize);
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), courseList.size());
		
		Page<CourseManageDtoInterface> responseList = new PageImpl<CourseManageDtoInterface>(courseList.subList(start, end), pageRequest, courseList.size());
		
		log.info(responseList.toString());
		return responseList;
	}
	
	@Transactional
	public String updateCourse(Long courseId, UpdateCourseDto dto) {
		log.info(String.format("Called updateCourse : [courseId : %d, DTO : %s]", courseId, dto.toString()));
		
		try {
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new IllegalArgumentException(String.format("Course is not found. [courseId : %s]", courseId)));
			UploadedFile file = null;
			
			if(dto.getFileId() != null) {
				file = fileRepository.findById(dto.getFileId())
						.orElseThrow(() -> new IllegalArgumentException(String.format("File is not found. [fileId : %s]", dto.getFileId())));
			}
			
			course.update(dto.getContent(), file);
			courseRepository.save(course);
			
			return  "success";
		}catch(Exception e) {
			return "error";
		}
	}
	
	@Transactional
	public void deleteCourse(Long courseId) {
		enrollRepository.deleteByCourseId(courseId);
		
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Course is not found. [courseId : %d]", courseId)));
		
		if(course.getFile() != null) {
			UploadedFile file = course.getFile();
			
			try {
				File savedFile = new File(file.getRealPath());
				if(savedFile.exists())
					savedFile.delete();
				
			}catch(Exception e) {
				log.error(e.getMessage());
			}
			
		}
		
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
	
	public List<ResponseApplicantDto> getApplicantList(Long courseId) {
		List<Enrollment> enrollList = enrollRepository.findByCourseId(courseId);
		
		List<ResponseApplicantDto> userList = new ArrayList<>();
		
		for(Enrollment e : enrollList) {
			User user = e.getUser();
			userList.add(ResponseApplicantDto.builder()
					.email(user.getEmail())
					.name(user.getName())
					.phone(user.getPhone())
					.build());
		}
		
		return userList;
	}

	public List<ResponseEnrollDto> getEnrollList(int pageNo, String userEmail) {
		
		List<ResponseEnrollDto> enrollList = enrollRepository.getEnrollListByUserEmail(userEmail);
		
		/*
		PageRequest pageRequest = PageRequest.of(pageNo, 10);
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), enrollList.size());
		
		Page<ResponseEnrollDto> responseList = new PageImpl<ResponseEnrollDto>(enrollList.subList(start, end), pageRequest, enrollList.size());
		*/
		
		return enrollList;
	}
}
