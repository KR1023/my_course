package com.ysh.my_course;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MyCourseApplication extends SpringBootServletInitializer{

	@Value("${IMG.PATH}")
	private String uploadDir;
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MyCourseApplication.class);
	}
	
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
 