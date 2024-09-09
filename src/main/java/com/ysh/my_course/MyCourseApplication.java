package com.ysh.my_course;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MyCourseApplication {

	@Value("${IMG.PATH}")
	private String uploadDir;
	
	public static void main(String[] args) {
		SpringApplication.run(MyCourseApplication.class, args);
	}

	@PostConstruct
	public void init() {
		File directory = new File(uploadDir);
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}
}
 