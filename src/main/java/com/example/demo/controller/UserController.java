package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	1. jwt ���� �� ��ū �̹���
	{
	    "token": null,
	    "username": "hello@world.com",
	    "password": null,
	    "id": "402880848973de39018973defa350001"
	}

	2.jwt ���� �� ��ū ���� - ��ū�� https://jwt.io/���������� ���ڵ�(HS512, Base64)�ϸ� alg, sub, iss, iat, exp���� ������ ��µȴ�.
	{
	    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4ODA4NDg5ODY3ODUzMDE4OTg2Nzg3NTJhMDAwMCIsImlzcyI6ImRlbW8gYXBwIiwiaWF0IjoxNjkwMTc4MTkxLCJleHAiOjE2OTAyNjQ1OTF9.CCQTg_e-FWJZHNPu3PnSEdBgL0_lbA5cL8o6T1jj7DKSjDB0CQ2bXHF-BIOHWnG_TRBWXdBFw63ngWeHKHsCxA",
	    "username": "hello@world.com",
	    "password": null,
	    "id": "402880848986785301898678752a0000"
	}
	 * */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity user = userService.getByCredentials(
				userDTO.getUsername(),
				userDTO.getPassword());

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
