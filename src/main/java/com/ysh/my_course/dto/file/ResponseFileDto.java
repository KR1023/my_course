package com.ysh.my_course.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseFileDto {

	private Long fileId;
	private String filename;
	private String filePath;
	private String refFilePath;
	
	@Builder
	public ResponseFileDto(Long fileId, String filename, String filePath, String refFilePath) {
		this.fileId = fileId;
		this.filename = filename;
		this.filePath = filePath;
		this.refFilePath = refFilePath;
	}
}
