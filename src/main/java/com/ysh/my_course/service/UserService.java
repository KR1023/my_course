package com.ysh.my_course.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.user.AddUserDto;
import com.ysh.my_course.dto.user.UpdateUserDto;
import com.ysh.my_course.repository.EnrollmentRepository;
import com.ysh.my_course.repository.UserRepository;
import com.ysh.my_course.utils.ConfigUtil;
import com.ysh.my_course.utils.CryptoUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final EnrollmentRepository enrollRepository;
	private final CryptoUtil cryptoUtil;
	private final ConfigUtil configUtil;
	
	public Map<String, String> login(String email, String password) {
		Map<String, String> result = new HashMap<String, String>();
		
		String secretKey = configUtil.getProperty("AES.KEY");
		String iv = configUtil.getProperty("AES.IV");
		
		try {
			String decrypted = cryptoUtil.decryptAES256(secretKey, iv, password);
			
			User user = userRepository.findByEmail(email);
			
			if(user == null) 
				result.put("result", "false");
			
			result.put("userAuth", user.getAuth());
			
			String salt = user.getSalt();
			String encPwd = user.getPassword();
			
			String toCheck = cryptoUtil.getEncrypt(decrypted, salt);
			
			if(toCheck.equals(encPwd))
				result.put("result", "true");
			else
				result.put("result", "false");
			
		}catch(Exception e) {
			log.error(e.getMessage());
		}
			return result;
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
	public User updateUser(String email, UpdateUserDto dto) throws Exception{
		String secretKey = configUtil.getProperty("AES.KEY");
		String iv = configUtil.getProperty("AES.IV");
		
		User user = userRepository.findByEmail(email);
		
		if(dto.getPassword() != null) {
			String decrypted = cryptoUtil.decryptAES256(secretKey, iv, dto.getPassword());
			String userSalt = user.getSalt();
			String encryptedPassword = cryptoUtil.getEncrypt(decrypted, userSalt);
			
			user.update(dto.getName(), encryptedPassword, dto.getPhone());
		}else {
			user.update(dto.getName(), user.getPassword(), dto.getPhone());
		}
		
		userRepository.save(user);
		
		return user;
	}
	
	@Transactional
	public void deleteUser(String email) {
		enrollRepository.deleteByUserEmail(email);
		userRepository.deleteByEmail(email);
	}
	

}
