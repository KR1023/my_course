package com.ysh.my_course.dto.course;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseCourseDto {

	private Long id;
	private String courseName;
	private int maxAttendee;
	private String content;
	private LocalDateTime createdDt;
	private String userEmail;
	
	@Builder
	public ResponseCourseDto(Long id, String courseName, int maxAttendee, String content, LocalDateTime createdDt, String userEmail) {
		this.id = id;
		this.courseName = courseName;
		this.maxAttendee = maxAttendee;
		this.content = content;
		this.createdDt = createdDt;
		this.userEmail = userEmail;
	}
}
