package com.ysh.my_course.dto.course;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CourseManageDtoInterface {
	Long getId();
	String getCourseName();
	int getMaxAttendee();
	int getApplicant();
	String getContent();
	LocalDateTime getCreatedDt();
	LocalDate getCloseDt();
	String getUserEmail();
	String getInstName();
	Long getFileId();
	String getFilename();
	String getRefFilepath();
}
