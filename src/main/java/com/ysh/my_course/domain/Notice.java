package com.ysh.my_course.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Notice {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="notice_id")
	@Id
	private Long noticeId;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	@Column
	@CreationTimestamp
	private LocalDateTime createdDt;
	
	@ManyToOne
	@JoinColumn(name = "user_email")
	private User user;
	
	@Builder
	public Notice(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}
	
	public void updateNotice(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
