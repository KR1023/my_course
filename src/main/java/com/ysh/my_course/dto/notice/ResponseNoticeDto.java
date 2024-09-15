package com.ysh.my_course.dto.notice;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseNoticeDto {

	private Long noticeId;
	private String title;
	private String content;
	private LocalDateTime createdDt;
	private String username;
	
	@Builder
	public ResponseNoticeDto(Long noticeId, String title, String content, LocalDateTime createdDt, String username) {
		this.noticeId = noticeId;
		this.title = title;
		this.content = content;
		this.createdDt = createdDt;
		this.username = username;
	}
}
