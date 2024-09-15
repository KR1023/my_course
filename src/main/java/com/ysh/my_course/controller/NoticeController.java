package com.ysh.my_course.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.my_course.domain.Notice;
import com.ysh.my_course.dto.notice.RequestNoticeDto;
import com.ysh.my_course.dto.notice.RequestUpdateDto;
import com.ysh.my_course.dto.notice.ResponseNoticeDto;
import com.ysh.my_course.service.NoticeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor	
@RequestMapping("/api")
@RestController
public class NoticeController {
	
	private final NoticeService noticeService;
	
	@GetMapping("/notice")
	public ResponseEntity<Page<ResponseNoticeDto>> getNoticeList(@RequestParam(name="page", defaultValue="0", required=false) int pageNo, @RequestParam(name="noticeName", required=false) String noticeName){
		log.info(String.format("Called getNoticeList [ pageNo : %d, noticeName : %s ]", pageNo, noticeName));
		
		try {
			Page<ResponseNoticeDto> list = noticeService.getNoticeList(pageNo, noticeName);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
	}
	
	@PostMapping("/notice")
	public ResponseEntity<Notice> addNotice(@RequestBody RequestNoticeDto dto, HttpServletRequest request){
		log.info(String.format("Called addNotice [ DTO : %s ]", dto.toString()));
		
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		
		if(userEmail == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		
		Notice notice = noticeService.addNotice(userEmail, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(notice);
		
	}
	
	@GetMapping("/notice/{noticeId}")
	public ResponseEntity<ResponseNoticeDto> getNotice(@PathVariable(name="noticeId") Long noticeId, HttpServletRequest request){
		log.info(String.format("Called getNotice [ noticeId : %d ]", noticeId));
		
		HttpSession session = request.getSession();
		
		try {
			ResponseNoticeDto notice = noticeService.getNotice(noticeId);
			return ResponseEntity.status(HttpStatus.OK).body(notice);
		}catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PutMapping("/notice/{noticeId}")
	public ResponseEntity<String> updateNotice(@PathVariable(name="noticeId") Long noticeId, HttpServletRequest request, @RequestBody RequestUpdateDto dto){
		log.info(String.format("Called updateNotice [ noticeId : %d, DTO : %s ]", noticeId, dto.toString()));
		
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		
		if(userEmail == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("forbidden");
		}
		
		try {
			noticeService.updateNotice(userEmail, noticeId, dto);
			return ResponseEntity.status(HttpStatus.OK).body("updateSucceeded");
		}catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
		}
		
	}
	
	@DeleteMapping("/notice/{noticeId}")
	public ResponseEntity<String> deleteNotice(@PathVariable(name="noticeId") Long noticeId, HttpServletRequest request){
		log.info(String.format("Called deleteNotice [ noticeId : %d]", noticeId));
		
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		
		if(userEmail == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
		}
		
		try {
			noticeService.deleteNotice(noticeId);
			
			return ResponseEntity.status(HttpStatus.OK).body("deleteSucceeded");
		}catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
			
		}
	}
	
}
