package com.ysh.my_course.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")	// src/main/resource
public class ConfigUtil {

	private final Environment env;
	
	public String getProperty(String key) {
		return env.getProperty(key);
	}
}
