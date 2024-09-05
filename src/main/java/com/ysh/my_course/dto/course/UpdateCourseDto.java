package com.ysh.my_course.dto.course;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCourseDto {

	private String courseName;
	private String maxAttendee;
	private String content;
	
}
