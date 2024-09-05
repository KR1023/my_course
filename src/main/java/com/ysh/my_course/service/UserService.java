package com.ysh.my_course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ysh.my_course.dto.AddUserDto;
import com.ysh.my_course.dto.UpdateUserDto;
import com.ysh.my_course.repository.UserRepository;
import com.ysh.my_course.utils.ConfigUtil;
import com.ysh.my_course.utils.CryptoUtil;
import com.ysh.my_course.vo.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final CryptoUtil cryptoUtil;
	private final ConfigUtil configUtil;
	
	public boolean login(String email, String password) {
		String secretKey = configUtil.getProperty("AES.KEY");
		String iv = configUtil.getProperty("AES.IV");
		
		try {
			String decrypted = cryptoUtil.decryptAES256(secretKey, iv, password);
			
			User user = userRepository.findByEmail(email);
			
			if(user == null) 
				return false;
			
			String salt = user.getSalt();
			String encPwd = user.getPassword();
			
			String toCheck = cryptoUtil.getEncrypt(decrypted, salt);
			
			if(toCheck.equals(encPwd))
				return true;
			else
				return false;
			
		}catch(Exception e) {
			log.error(e.getMessage());
		}
			return false;
	}
	
	public User addUser(AddUserDto dto) throws Exception {
		String secretKey = configUtil.getProperty("AES.KEY");
		String iv = configUtil.getProperty("AES.IV");
		
		String decrypted = cryptoUtil.decryptAES256(secretKey, iv, dto.getPassword());
		
		String createdSalt = cryptoUtil.getSalt();
		String encryptedPassword = cryptoUtil.getEncrypt(decrypted, createdSalt);
		
		User user = userRepository.findByEmail(dto.getEmail());
		if(user != null) {
			throw new Exception("user already exists.");
		}else {
//			return User.builder()
//					.email(dto.getEmail())
//					.password(dto.getPassword())
//					.build();
			return userRepository.save(User.builder()
					.email(dto.getEmail())
					.name(dto.getName())
					.password(encryptedPassword)
					.salt(createdSalt)
					.phone(dto.getPhone())
					.build());
		}
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	@Transactional
	public User updateUser(String email, UpdateUserDto dto) {
		User user = userRepository.findByEmail(email);
		user.update(dto.getName(), dto.getPassword(), dto.getPhone());
		return user;
	}
	
	public void deleteUser(String email) {
		userRepository.deleteByEmail(email);
	}
}
