package com.ysh.my_course.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ysh.my_course.domain.Course;
import com.ysh.my_course.dto.course.CourseManageDtoInterface;

public interface CourseRepository extends JpaRepository<Course, Long>{

	@Query(value = "SELECT \r\n"
			+ "	distinct\r\n"
			+ "	`course`.id,\r\n"
			+ "    `course`.course_name,\r\n"
			+ "    `course`.max_attendee,\r\n"
			+ "    (SELECT count(*) FROM enrollment WHERE course_id = `course`.id) applicant,\r\n"
			+ "    `course`.content,\r\n"
			+ "    `course`.created_dt,\r\n"
			+ "    `course`.closing_dt close_dt,\r\n"
			+ "    `course`.user_email,\r\n"
			+ "    `user`.`name` inst_name,\r\n"
			+ "    `file`.id file_id,\r\n"
			+ "    `file`.filename,\r\n"
			+ "    `file`.ref_filepath\r\n"
			+ "FROM my_course.course `course`\r\n"
			+ "LEFT JOIN uploaded_file `file`\r\n"
			+ "ON `course`.file_id = `file`.id\r\n"
			+ "LEFT JOIN `user` `user`\r\n"
			+ "ON `course`.user_email = `user`.email\r\n"
			+ "LEFT JOIN enrollment `enroll`\r\n"
			+ "ON `course`.id = `enroll`.course_id\r\n"
			+ "ORDER BY `course`.id DESC", nativeQuery = true)
	List<CourseManageDtoInterface> findAllCourses(Sort by);
	
	@Query(value = "SELECT \r\n"
			+ "	distinct\r\n"
			+ "	`course`.id,\r\n"
			+ "    `course`.course_name,\r\n"
			+ "    `course`.max_attendee,\r\n"
			+ "    (SELECT count(*) FROM enrollment WHERE course_id = `course`.id) applicant,\r\n"
			+ "    `course`.content,\r\n"
			+ "    `course`.created_dt,\r\n"
			+ "    `course`.closing_dt close_dt,\r\n"
			+ "    `course`.user_email,\r\n"
			+ "    `user`.`name` inst_name,\r\n"
			+ "    `file`.id file_id,\r\n"
			+ "    `file`.filename,\r\n"
			+ "    `file`.ref_filepath\r\n"
			+ "FROM my_course.course `course`\r\n"
			+ "LEFT JOIN uploaded_file `file`\r\n"
			+ "ON `course`.file_id = `file`.id\r\n"
			+ "LEFT JOIN `user` `user`\r\n"
			+ "ON `course`.user_email = `user`.email\r\n"
			+ "LEFT JOIN enrollment `enroll`\r\n"
			+ "ON `course`.id = `enroll`.course_id\r\n"
			+ "WHERE `course`.user_email = ?1\r\n"
			+ "ORDER BY `course`.id DESC", nativeQuery = true)
	List<CourseManageDtoInterface> findByUserEmail(String userEmail, Sort by);
	
	@Query(value = "SELECT \r\n"
			+ "	distinct\r\n"
			+ "	`course`.id,\r\n"
			+ "    `course`.course_name,\r\n"
			+ "    `course`.max_attendee,\r\n"
			+ "    (SELECT count(*) FROM enrollment WHERE course_id = `course`.id) applicant,\r\n"
			+ "    `course`.content,\r\n"
			+ "    `course`.created_dt,\r\n"
			+ "    `course`.closing_dt close_dt,\r\n"
			+ "    `course`.user_email,\r\n"
			+ "    `user`.`name` inst_name,\r\n"
			+ "    `file`.id file_id,\r\n"
			+ "    `file`.filename,\r\n"
			+ "    `file`.ref_filepath\r\n"
			+ "FROM my_course.course `course`\r\n"
			+ "LEFT JOIN uploaded_file `file`\r\n"
			+ "ON `course`.file_id = `file`.id\r\n"
			+ "LEFT JOIN `user` `user`\r\n"
			+ "ON `course`.user_email = `user`.email\r\n"
			+ "LEFT JOIN enrollment `enroll`\r\n"
			+ "ON `course`.id = `enroll`.course_id\r\n"
			+ "WHERE `course`.user_email = ?1 AND `course`.course_name like (%?2%)\r\n"
			+ "ORDER BY `course`.id DESC", nativeQuery = true)
	List<CourseManageDtoInterface> findByUserEmailAndCourseNameContaining(String userEmail, String courseName, Sort by);
	
	@Query(value = "SELECT \r\n"
			+ "	distinct\r\n"
			+ "	`course`.id,\r\n"
			+ "    `course`.course_name,\r\n"
			+ "    `course`.max_attendee,\r\n"
			+ "    (SELECT count(*) FROM enrollment WHERE course_id = `course`.id) applicant,\r\n"
			+ "    `course`.content,\r\n"
			+ "    `course`.created_dt,\r\n"
			+ "    `course`.closing_dt close_dt,\r\n"
			+ "    `course`.user_email,\r\n"
			+ "    `user`.`name` inst_name,\r\n"
			+ "    `file`.id file_id,\r\n"
			+ "    `file`.filename,\r\n"
			+ "    `file`.ref_filepath\r\n"
			+ "FROM my_course.course `course`\r\n"
			+ "LEFT JOIN uploaded_file `file`\r\n"
			+ "ON `course`.file_id = `file`.id\r\n"
			+ "LEFT JOIN `user` `user`\r\n"
			+ "ON `course`.user_email = `user`.email\r\n"
			+ "LEFT JOIN enrollment `enroll`\r\n"
			+ "ON `course`.id = `enroll`.course_id\r\n"
			+ "WHERE `course`.course_name like (%?1%)\r\n"
			+ "ORDER BY `course`.id DESC", nativeQuery = true)
	List<CourseManageDtoInterface> findByCourseNameContaining(String courseName, Sort by);
}
