package com.ysh.my_course.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

	List<Course> findByUserEmail(String userEmail);

	List<Course> findByUserEmail(String userEmail, Sort by);
}
