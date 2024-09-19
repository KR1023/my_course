package com.ysh.my_course.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ysh.my_course.domain.UploadedFile;
import com.ysh.my_course.dto.file.ResponseFileDto;
import com.ysh.my_course.repository.UploadedFileRepository;
import com.ysh.my_course.utils.SimpleUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {
	
	@Value("${IMG.PATH}")
	private String uploadDir;
	
	private final UploadedFileRepository fileRepository;
	private final SimpleUtil util;
	
	public ResponseFileDto storeFile(List<MultipartFile> files) {
		
		int uploadDirIdx = uploadDir.lastIndexOf("\\");
		String rootDir = null;
	
		if(uploadDirIdx != -1) {
			rootDir = uploadDir.substring(uploadDirIdx).replace('\\','/');
		}else {
			uploadDirIdx = uploadDir.lastIndexOf("/");
			rootDir = uploadDir.substring(uploadDirIdx);
		}
		
		for(MultipartFile file : files) {
			String dateString = util.getDateString();
			
			String refFilepath = String.format("%s/%s_%s", rootDir, dateString, file.getOriginalFilename());
			 
					
			Path filePath = Paths.get(uploadDir, dateString + "_" + file.getOriginalFilename());
			
			try {
				Files.copy(file.getInputStream(), filePath);
				log.info(String.format("filePath : %s", filePath.toString()));
				UploadedFile uFile = fileRepository.save(UploadedFile.builder()
															.filename(file.getOriginalFilename())
															.contentType(file.getContentType())
															.realPath(filePath.toString())
															.refFilepath(refFilepath)
															.build());
				
				return ResponseFileDto.builder()
						.fileId(uFile.getId())
						.filename(file.getOriginalFilename())
						.filePath(filePath.toString())
						.refFilePath(String.format("%s", refFilepath))
						.build();
			} catch (IOException e) {
				log.error(e.getMessage());
				return null;
			}
		}
		
		return null;
	}
	
}
