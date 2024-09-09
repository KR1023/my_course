package com.ysh.my_course.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class SimpleUtil {

	public static void main(String[] args) {
		SimpleUtil util = new SimpleUtil();
		System.out.println(util.getDateString());
	}
	public String getDateString(){
		Date dt = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String result = format.format(dt);
		
		return result;
	}
}
