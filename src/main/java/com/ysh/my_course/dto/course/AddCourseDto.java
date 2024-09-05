package com.ysh.my_course.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddCourseDto {

	private String courseName;
	private String content;
	private String maxAttendee;
	private String userEmail;
	
}
