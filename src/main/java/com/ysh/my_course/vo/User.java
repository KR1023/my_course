package com.ysh.my_course.vo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails{

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
	@ColumnDefault("normal")
	private String auth;
	
	@Column
	@CreationTimestamp
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("user"));
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
}
