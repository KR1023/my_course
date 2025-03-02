package com.ysh.my_course.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.user.AddUserDto;
import com.ysh.my_course.dto.user.RequestLoginDto;
import com.ysh.my_course.dto.user.ResponseUserDto;
import com.ysh.my_course.dto.user.UpdateUserDto;
import com.ysh.my_course.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpServletRequest request, @RequestBody RequestLoginDto dto) {
		log.info(dto.getEmail() + " / " + dto.getPassword());
		
		Map<String, String> result = userService.login(dto.getEmail(), dto.getPassword());
		
		if(result.get("result").equals("true")) {
			HttpSession session = request.getSession();
			session.setAttribute("session", session.getId());
			session.setAttribute("loginEmail", dto.getEmail());
			session.setAttribute("username", result.get("username"));
			session.setAttribute("userAuth", result.get("userAuth"));
			
			return ResponseEntity.status(HttpStatus.OK).body("loginSuccess");
		}else {
			return ResponseEntity.status(HttpStatus.OK).body("failed");
		}
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		log.info(String.format("Called Logout [logout email : %s ]", session.getAttribute("loginEmail")));
		session.invalidate();
		
		return ResponseEntity.status(HttpStatus.OK).body("logoutSuccess");
	}
	
	@PostMapping("/check")
	public ResponseEntity<String> checkUserEmail(@RequestBody String toCheck){
		User user = userService.getUserByEmail(toCheck);
		log.info(String.format("Called checkUserEmail : [toCheck : %s]", toCheck));
		
		if(user == null)
			return ResponseEntity.status(HttpStatus.OK).body("empty");
		else 
			return ResponseEntity.status(HttpStatus.OK).body("exists");
	}
	
	@PostMapping("/user")
	public ResponseEntity<String> addUser(@RequestBody AddUserDto dto) {
		System.out.println("encrypted_pwd : " + dto.getPassword());
		try {
			userService.addUser(dto);
		}catch(Exception e) {
			if(e.getMessage().equals("user already exists."))
				return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
			else {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			}
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Created");
	}
	
	@GetMapping("/user/{email}")
	public ResponseEntity<User> getUser(@PathVariable(name = "email") String email){
		log.info(String.format("Called getUser : [ email : %s ]", email));
		
		User user = userService.getUserByEmail(email);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@GetMapping("/users")
	public ResponseEntity<Page<ResponseUserDto>> getUsers(@RequestParam(name="page", defaultValue="0", required=false) int pageNo){
		Page<ResponseUserDto> userList = userService.getUsers(pageNo);
		return ResponseEntity.status(HttpStatus.OK).body(userList);
	}
	
	@PutMapping("/user/{email}")
	public ResponseEntity<User> updateUser(@PathVariable(name = "email") String email, @RequestBody UpdateUserDto dto){
		log.info(String.format("Called updateUser : [email: %s, DTO : %s]", email, dto.toString()));
		try{
			User updatedUser = userService.updateUser(email, dto);
			return ResponseEntity.ok().body(updatedUser);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		return ResponseEntity.internalServerError().body(null);
		
	}
	
	@DeleteMapping("/user/{email}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "email") String email, HttpServletRequest request){
		log.info(String.format("Called deleteUser : [ email : %s ]", email));
		
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		
		userService.deleteUser(email);
		if(email.equals(userEmail))
			session.invalidate();
		
		return ResponseEntity.ok().body("Deleted");
	}
	
	/**
	 * 권한 변경
	 */
	@PutMapping("/user/auth/{email}")
	public ResponseEntity<String> updateAuth(@PathVariable(name="email") String email, @RequestBody String auth, HttpServletRequest request){
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("loginEmail");
		if(userEmail == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("forbidden");
		}
		
		log.info(String.format("Called updateAuth [ email : %s, auth : %s ]", email, auth));
		try {
			userService.updateUserAuth(email, auth);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("changedAuth");
		
	}
}
