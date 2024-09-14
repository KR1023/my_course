package com.ysh.my_course.dto.user;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseUserDtoImpl implements ResponseUserDto{
	
	private String email;
	private String name;
	private String phone;
	private String auth;
	private LocalDateTime createdDt;
	
	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	@Override
	public String getPhone() {
		// TODO Auto-generated method stub
		return phone;
	}
	@Override
	public String getAuth(String auth) {
		switch(auth) {
		case "normal" :
			return "수강생";
		case "instructor" :
			return "강사";
		case "manage" : 
			return "관리자";
		case "admin" : 
			return "최고관리자";
		default:
			return auth;
		}
	}
	@Override
	public LocalDateTime getCreatedDt() {
		// TODO Auto-generated method stub
		return createdDt;
	}
}
