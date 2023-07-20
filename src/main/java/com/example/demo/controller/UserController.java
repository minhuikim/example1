package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	/*
	[��û]
 	POST http://localhost:8080/auth/signup
	Body JSON raw : 
	{
	    "username": "hello@world.com",
	    "password": "12345"
	}
	  
	[����]
	{
	    "token": null,
	    "username": "hello@world.com",
	    "password": null,
	    "id": "402880848973de39018973defa350001"
	}
	 * */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			if(userDTO == null || userDTO.getPassword() == null) {
				throw new RuntimeException("Invalid Password value.");
			}
			// ��û�� �̿��� ������ ���� �����
			UserEntity user = UserEntity.builder()
					.username(userDTO.getUsername())
					.password(userDTO.getPassword())
					.build();
			// ���񽺸� �̿��� �������͸��� ���� ����
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder()
					.id(registeredUser.getId())
					.username(registeredUser.getUsername())
					.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			// ���� ������ �׻� �ϳ��̹Ƿ� ����Ʈ�� ������ �ϴ� ResponseDTO�� ������� �ʰ� �׳� UserDTO ����
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity
					.badRequest()
					.body(responseDTO);
		}
	}
	
	/*
	[��û]
 	POST http://localhost:8080/auth/signin
	Body JSON raw : 
	{
	    "username": "hello@world.com",
	    "password": "12345"
	}
	  
	[����]
	{
	    "token": null,
	    "username": "hello@world.com",
	    "password": null,
	    "id": "402880848973de39018973defa350001"
	}
	 * */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity user = userService.getByCredentials(
				userDTO.getUsername(),
				userDTO.getPassword());
		
		if(user != null) {
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(user.getUsername())
					.id(user.getId())
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login failed.")
					.build();
			return ResponseEntity
					.badRequest()
					.body(responseDTO);
		}
	}
}
