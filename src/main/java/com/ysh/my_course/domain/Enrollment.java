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

@Getter
@NoArgsConstructor
@Entity
public class Enrollment {
	// 강좌 신청 테이블
	
	/*
	 * 1. 강의 ID
	 * 2. User ID
	 * 
	 * 
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long enrollmentId;
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;
	
	@ManyToOne
	@JoinColumn(name = "user_email")
	private User user;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime enrolledDt;
	
	@Builder
	public Enrollment(Course course, User user) {
		this.course = course;
		this.user = user;
	}
	
}
