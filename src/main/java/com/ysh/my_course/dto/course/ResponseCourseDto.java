package com.ysh.my_course.dto.course;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ResponseCourseDto {

	private Long id;
	private String courseName;
	private int maxAttendee;
	private String content;
	private LocalDateTime createdDt;
	private LocalDate closeDt;
	private String userEmail;
	private String refFilepath;
	
	@Builder
	public ResponseCourseDto(Long id, String courseName, int maxAttendee, String content, LocalDateTime createdDt, LocalDate closeDt, String userEmail, String refFilepath) {
		this.id = id;
		this.courseName = courseName;
		this.maxAttendee = maxAttendee;
		this.content = content;
		this.createdDt = createdDt;
		this.closeDt = closeDt;
		this.userEmail = userEmail;
		this.refFilepath = refFilepath;
	}
}
