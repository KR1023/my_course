package com.ysh.my_course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ysh.my_course.domain.Course;
import com.ysh.my_course.domain.Enrollment;
import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.enroll.ResponseEnrollDto;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
	long countByCourse(Course course);
	
	Enrollment findByCourseAndUser(Course course, User user);
	
	void deleteByCourseAndUser(Course course, User user);

	List<Enrollment> findByCourseId(Long courseId);

	void deleteByCourseId(Long courseId);
	
	void deleteByUserEmail(String userEmail);

	@Query(value="SELECT \r\n"
			+ "	`enroll`.enrollment_id `e_id`,\r\n"
			+ "    `course`.course_name,\r\n"
			+ "    `user`.name inst_name,\r\n"
			+ "    `course`.id course_id,\r\n"
			+ "    `enroll`.enrolled_dt\r\n"
			+ "FROM my_course.enrollment `enroll`\r\n"
			+ "LEFT JOIN course `course`\r\n"
			+ "ON `enroll`.course_id = `course`.id\r\n"
			+ "LEFT JOIN user `user`\r\n"
			+ "ON `course`.user_email = `user`.email\r\n"
			+ "WHERE `enroll`.user_email = ?1", nativeQuery=true)
	List<ResponseEnrollDto> getEnrollListByUserEmail(String userEmail);
}
