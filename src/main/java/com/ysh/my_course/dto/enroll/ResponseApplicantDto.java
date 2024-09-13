package com.ysh.my_course.dto.enroll;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseApplicantDto {
	
	private String email;
	private String name;
	private String phone;
	
	@Builder
	public ResponseApplicantDto(String email, String name, String phone) {
		this.email = email;
		this.name = name;
		this.phone = phone;
	}
}
