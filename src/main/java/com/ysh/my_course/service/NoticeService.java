package com.ysh.my_course.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ysh.my_course.domain.Notice;
import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.notice.RequestNoticeDto;
import com.ysh.my_course.dto.notice.RequestUpdateDto;
import com.ysh.my_course.dto.notice.ResponseNoticeDto;
import com.ysh.my_course.repository.NoticeRepository;
import com.ysh.my_course.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService {
	private final NoticeRepository noticeRepository;
	private final UserRepository userRepository;

	public Notice addNotice(String userEmail, RequestNoticeDto dto) {
		log.info(String.format("Called addNotice [ userEmail : %s, DTO : %s", userEmail, dto.toString()));
		
		User user = userRepository.findByEmail(userEmail);
		
		Notice notice = noticeRepository.save(Notice.builder()
							.title(dto.getTitle())
							.content(dto.getTitle())
							.user(user)
							.build());
		return notice;
	}

	public Page<ResponseNoticeDto> getNoticeList(int pageNo, String noticeName) throws Exception{
		List<Notice> noticeList = null; 
			
		if(noticeName != null) {
			noticeList = noticeRepository.findByTitleContaining(noticeName, Sort.by(Sort.Direction.DESC, "noticeId"));
		}else {
			noticeList = noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeId"));
		}
			
		
		List<ResponseNoticeDto> dtoList = noticeList.stream()
											.map(e -> ResponseNoticeDto.builder()
													.noticeId(e.getNoticeId())
													.title(e.getTitle())
													.content(e.getContent())
													.createdDt(e.getCreatedDt())
													.username(e.getUser().getName())
													.build())
											.collect(Collectors.toList());
				
		PageRequest pageRequest= PageRequest.of(pageNo, 10);
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), dtoList.size());
		
		Page<ResponseNoticeDto> responseList = new PageImpl<ResponseNoticeDto>(dtoList.subList(start, end), pageRequest, dtoList.size());
		
		return responseList;
	}

	public ResponseNoticeDto getNotice(Long noticeId) {
		Optional<Notice> optional = noticeRepository.findById(noticeId);
		Notice notice = optional.get();
		
		ResponseNoticeDto dto = ResponseNoticeDto.builder()
									.noticeId(notice.getNoticeId())
									.title(notice.getTitle())
									.content(notice.getContent())
									.createdDt(notice.getCreatedDt())
									.username(notice.getUser().getName())
									.build();
		return dto;
	}

	@Transactional
	public void updateNotice(String userEmail, Long noticeId, RequestUpdateDto dto) throws Exception{
		Optional<Notice> optional = noticeRepository.findById(noticeId);
		Notice notice = optional.get();
		
		if(!userEmail.equals(notice.getUser().getEmail())) {
			throw new Exception("FORBIDDEN");
		}
		
		notice.updateNotice(dto.getTitle(), dto.getContent());
		
		noticeRepository.save(notice);
		
	}

	@Transactional
	public void deleteNotice(Long noticeId) throws Exception{
		noticeRepository.deleteById(noticeId);
	}
	
}
