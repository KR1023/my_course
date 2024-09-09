package com.ysh.my_course.dto.course;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

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
	
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyyMMdd")
	private LocalDate closingDt;
	
	private Long fileId;
	private String userEmail;
	
	
}
