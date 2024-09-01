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

import com.ysh.my_course.dto.AddUserDto;
import com.ysh.my_course.dto.UpdateUserDto;
import com.ysh.my_course.service.UserService;
import com.ysh.my_course.vo.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;
	
	@PostMapping("/user")
	public ResponseEntity<User> addUser(@RequestBody AddUserDto dto) {
		System.out.println(dto.getEmail() + " / " + dto.getPassword());
		User user = userService.addUser(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
