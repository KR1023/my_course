package com.ysh.my_course.vo;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User{

	@Id
	@Column(name = "email")
	private String email;
	
	@Column
	private String name;
	
	@Column
	private String password;
	
	@Column
	private String salt;
	
	@Column
	private String phone;
	
	@Column
	@ColumnDefault("'normal'")
	private String auth;
	
	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdDt;
	
	@Builder
	public User(String email, String name, String password, String phone) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
	}
	
	public void update(String name, String password, String phone) {
		this.name = name;
		this.password = password;
		this.phone = phone;
	}
	
	@Builder
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
