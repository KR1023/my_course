package com.ysh.my_course.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysh.my_course.domain.User;
import com.ysh.my_course.dto.user.AddUserDto;
import com.ysh.my_course.dto.user.RequestLoginDto;
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
		
		boolean result = userService.login(dto.getEmail(), dto.getPassword());
		
		if(result) {
			HttpSession session = request.getSession();
			session.setAttribute("session", session.getId());
			session.setAttribute("loginEmail", dto.getEmail());
			
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
		User user = userService.getUserByEmail(email);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(){
		List<User> userList = userService.getUsers();
		return ResponseEntity.status(HttpStatus.OK).body(userList);
	}
	
	@PutMapping("/user/{email}")
	public ResponseEntity<User> updateUser(@PathVariable(name = "email") String email, @RequestBody UpdateUserDto dto){
		User updatedUser = userService.updateUser(email, dto);
		
		return ResponseEntity.ok().body(updatedUser);
	}
	
	@DeleteMapping("/user/{email}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "email") String email){
		userService.deleteUser(email);
		return ResponseEntity.ok().body("Deleted");
	}
}
