package com.ysh.my_course.dto.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class RequestNoticeDto {

	private String title;
	private String content;
}
