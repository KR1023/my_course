package com.ysh.my_course.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
	List<Notice> findAll(Sort sort);
	
	List<Notice> findByTitleContaining(String noticeName, Sort sort);
}
