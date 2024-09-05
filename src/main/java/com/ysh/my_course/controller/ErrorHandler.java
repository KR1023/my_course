package com.ysh.my_course.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandler implements ErrorController{

	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if(status != null) {
			int statusCode = Integer.parseInt(status.toString());
			
			switch(statusCode) {
			case 400:
				return "error/error_400";
			case 404:
				return "error/error_404";
			default:
				return "error";	// 기본 에러 페이지
			}
		}
		
		return "error";
	}
}
