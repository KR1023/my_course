package com.ysh.my_course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ysh.my_course.dto.AddUserDto;
import com.ysh.my_course.dto.UpdateUserDto;
import com.ysh.my_course.repository.UserRepository;
import com.ysh.my_course.vo.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	
	public User addUser(AddUserDto dto) throws Exception {
		User user = userRepository.findByEmail(dto.getEmail());
		if(user != null) {
			throw new Exception("user already exists.");
		}else {
			return userRepository.save(User.builder()
					.email(dto.getEmail())
					.name(dto.getName())
					.password(dto.getPassword())
					.phone(dto.getPhone())
					.build());
		}
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	@Transactional
	public User updateUser(String email, UpdateUserDto dto) {
		User user = userRepository.findByEmail(email);
		user.update(dto.getName(), dto.getPassword(), dto.getPhone());
		return user;
	}
	
	public void deleteUser(String email) {
		userRepository.deleteByEmail(email);
	}
}
