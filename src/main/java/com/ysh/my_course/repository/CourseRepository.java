package com.ysh.my_course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{
	
}
