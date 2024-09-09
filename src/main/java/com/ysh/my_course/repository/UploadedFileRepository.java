package com.ysh.my_course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysh.my_course.domain.UploadedFile;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long>{
}
