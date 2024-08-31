package com.ysh.my_course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.vo.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String>{

	User findByEmail(String email);
	
	@Transactional
	void deleteByEmail(String email);
}
