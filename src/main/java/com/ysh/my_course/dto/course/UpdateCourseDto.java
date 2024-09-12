package com.ysh.my_course.dto.course;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString()
public class UpdateCourseDto {

	// private String courseName;
	// private String maxAttendee;
	private String content;
	private Long fileId;
	
//	@JsonDeserialize(using = LocalDateDeserializer.class)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyyMMdd")
//	private LocalDate closingDt;
	
}
