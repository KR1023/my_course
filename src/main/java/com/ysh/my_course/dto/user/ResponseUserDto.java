package com.ysh.my_course.dto.user;

import java.time.LocalDateTime;

public interface ResponseUserDto {
	String getEmail();
	String getName();
	String getPhone();
	String getAuth();
	LocalDateTime getCreatedDt();
	String getAuth(String auth);
}
