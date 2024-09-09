package com.ysh.my_course.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ysh.my_course.dto.file.ResponseFileDto;
import com.ysh.my_course.service.FileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FileController {

	private final FileService fileService;
	
	@PostMapping("/image")
	public ResponseEntity<ResponseFileDto> uploadTest(MultipartHttpServletRequest request) {
		List<MultipartFile> files = request.getFiles("files");
		
		ResponseFileDto dto = fileService.storeFile(files);
		
		if(dto == null) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal_error");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
}
