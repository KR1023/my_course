package com.ysh.my_course.dto.course;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseManageDto {
	private Long id;
	private String courseName;
	private int maxAttendee;
	private int applicant;
	private String content;
	private LocalDateTime createdDt;
	private Date closeDt;
	private String userEmail;
	private String instName;
	private Long fileId;
	private String filename;
	private String refFilepath;
}
