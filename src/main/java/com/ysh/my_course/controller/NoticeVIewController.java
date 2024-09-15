package com.ysh.my_course.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ysh.my_course.dto.notice.ResponseNoticeDto;
import com.ysh.my_course.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoticeVIewController {

	private final NoticeService noticeService;
	
	@GetMapping("/notice")
	public String noticeList(@RequestParam(name="page", defaultValue="0", required=false) int pageNo, @RequestParam(name="noticeName", required=false) String noticeName, Model model) {
		log.info(String.format("Called noticeListView : [ pageNo : %d ]", pageNo));
		
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
}
