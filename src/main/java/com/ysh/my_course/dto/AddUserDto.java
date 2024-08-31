package com.ysh.my_course.dto;

import com.ysh.my_course.vo.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto {
	
	private String email;
	private String name;
	private String password;
	private String phone;
	
	public User toEntity() {
		return User.builder()
				.email(email)
				.name(name)
				.password(password)
				.phone(phone)
				.build();
	}
}