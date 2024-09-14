package com.ysh.my_course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.user.ResponseUserDto;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String>{

	User findByEmail(String email);
	
	List<ResponseUserDto> findByAuthNot(String auth);
	
	@Transactional
	void deleteByEmail(String email);
}
