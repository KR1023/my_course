package com.ysh.my_course.dto.enroll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestEnrollDto {

	private Long courseId;
	private String userEmail;
}
