package com.ysh.my_course.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

	@Id
	@Column(name = "email")
	private String email;
	
	@Column
	private String name;
	
	@Column
	private String password;
	
	@Column
	private String phone;
	
	@Column
	private String auth;
	
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
}
