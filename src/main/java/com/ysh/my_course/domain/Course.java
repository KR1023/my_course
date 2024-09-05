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
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String courseName;
	
	@Column
	private int maxAttendee;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdDt;
	
	@ManyToOne
	@JoinColumn(name = "user_email")
	private User user;
	
	@Builder
	public Course(String courseName, String content, String maxAttendee, User user) {
		this.courseName = courseName;
		this.content = content;
		this.maxAttendee = Integer.parseInt(maxAttendee);
		this.user = user;
	}
	
	public void update(String courseName, int maxAttendee, String content) {
		this.courseName = courseName;
		this.maxAttendee = maxAttendee;
		this.content = content;
	}
}
