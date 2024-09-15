package com.ysh.my_course.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ysh.my_course.dto.notice.ResponseNoticeDto;
import com.ysh.my_course.service.NoticeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoticeVIewController {

	private final NoticeService noticeService;
	
	@GetMapping("/notice")
	public String noticeList(@RequestParam(name="page", defaultValue="0", required=false) int pageNo, @RequestParam(name="noticeName", required=false) String noticeName, Model model) {
		log.info(String.format("Called noticeListView : [ pageNo : %d, noticeName : %s ]", pageNo, noticeName));
		
		try {
			Page<ResponseNoticeDto> list = noticeService.getNoticeList(pageNo, noticeName);
			model.addAttribute("list", list);
			model.addAttribute("noticeName", noticeName);
			return "notice/noticeList";			
			
		}catch(Exception e) {
			log.error(e.getMessage());
			return "error/error_500";
		}
	}
	
	@GetMapping("/notice/{noticeId}")
	public String getNotice(@PathVariable(name="noticeId") Long noticeId, HttpServletRequest request, Model model) {
		log.info(String.format("Called getNotice : [ noticeId : %d ]", noticeId));
		
		HttpSession session = request.getSession();
		
		
		try {
			ResponseNoticeDto notice = noticeService.getNotice(noticeId);
			model.addAttribute("userAuth", session.getAttribute("userAuth"));
			model.addAttribute("notice", notice);
			
			return "notice/detail";
			
		}catch(Exception e) {
			log.error(e.getMessage());
			return "error/error_500";
		}
	}
	
	@GetMapping("/notice/add")
	public String addNoticePage(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		String userAuth = (String)session.getAttribute("userAuth");
		
		if(userEmail == null) {
			return "error/error_403";
		}
		
		if(userEmail != null && (userAuth.equals("normal") || userAuth.equals("instructor"))) {
			return "error/error_403";
		}
		
		model.addAttribute("userAuth", userAuth);
		model.addAttribute("userEmail", userEmail);
		
		return "notice/noticeAdd";
	}
}
