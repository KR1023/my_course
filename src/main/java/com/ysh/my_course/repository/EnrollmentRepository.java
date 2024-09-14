package com.ysh.my_course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.domain.Course;
import com.ysh.my_course.domain.Enrollment;
import com.ysh.my_course.domain.User;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
	long countByCourse(Course course);
	
	Enrollment findByCourseAndUser(Course course, User user);
	
	void deleteByCourseAndUser(Course course, User user);

	List<Enrollment> findByCourseId(Long courseId);

	void deleteByCourseId(Long courseId);
	
	void deleteByUserEmail(String userEmail);
}
