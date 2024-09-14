package com.ysh.my_course.dto.enroll;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ResponseEnrollDto {
	Long getEId();
	String getCourseName();
	String getInstName();
	Long getCourseId();
	LocalDateTime getEnrolledDt();
}