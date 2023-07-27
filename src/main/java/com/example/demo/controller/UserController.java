package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
	20220727 password�� passwordEncoder�� �ؼ� ���������� ����
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
					.password(passwordEncoder.encode(userDTO.getPassword()))
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
	1. jwt ���� �� ��ū �̹���
	{
	    "token": null,
	    "username": "hello@world.com",
	    "password": null,
	    "id": "402880848973de39018973defa350001"
	}

	2.jwt ���� �� ��ū ���� - ��ū�� https://jwt.io/���������� ���ڵ�(HS512, Base64)�ϸ� alg, sub, iss, iat, exp���� ������ ��µȴ�.
	{
	    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4ODA4NDg5ODcxYjNmMDE4OTg3MWI1NjA5MDAwMCIsImlzcyI6ImRlbW8gYXBwIiwiaWF0IjoxNjkwMTg4ODY1LCJleHAiOjE2OTAyNzUyNjV9.cWnIZmuVcUBJ_oN8BNItKeIDyr0_GXEvZWjwUYGC_oFOCa32QVLhTObCK8sR3w_N6--gZjmkXVNlR5_1aIchqw",
	    "username": "hello@world.com",
	    "password": null,
	    "id": "4028808489871b3f0189871b56090000"
	}
	 * */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity user = userService.getByCredentials(
				userDTO.getUsername(),
				userDTO.getPassword(),
				passwordEncoder);

		if(user != null) {
			// Token ����
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(user.getUsername())
					.id(user.getId())
					.token(token) // tokenProvider ���� �� �߰�
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
