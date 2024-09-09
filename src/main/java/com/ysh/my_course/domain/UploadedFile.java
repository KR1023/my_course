package com.ysh.my_course.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UploadedFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@Column
	public String filename;
	
	@Column
	public String contentType;
	
	@Column
	public String realPath;
	
	@Column
	public String refFilepath;
	
	@Column
	@CreationTimestamp
	private LocalDateTime uploaded_dt;
	
	@Builder
	public UploadedFile(String filename, String contentType, String realPath, String refFilepath) {
		this.filename = filename;
		this.contentType = contentType;
		this.realPath = realPath;
		this.refFilepath = refFilepath;
	}
}
