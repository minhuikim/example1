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
	[요청]
 	POST http://localhost:8080/auth/signup
	Body JSON raw :
	{
	    "username": "hello@world.com",
	    "password": "12345"
	}

	[응답]
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
			// 요청을 이용해 저장할 유저 만들기
			UserEntity user = UserEntity.builder()
					.username(userDTO.getUsername())
					.password(userDTO.getPassword())
					.build();
			// 서비스를 이용해 리포지터리에 유저 저장
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder()
					.id(registeredUser.getId())
					.username(registeredUser.getUsername())
					.build();

			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			// 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity
					.badRequest()
					.body(responseDTO);
		}
	}

	/*
	[요청]
 	POST http://localhost:8080/auth/signin
	Body JSON raw :
	{
	    "username": "hello@world.com",
	    "password": "12345"
	}

	[응답]
	1. jwt 적용 전 토큰 미발행
	{
	    "token": null,
	    "username": "hello@world.com",
	    "password": null,
	    "id": "402880848973de39018973defa350001"
	}

	2.jwt 적용 후 토큰 발행 - 토큰을 https://jwt.io/페이지에서 디코딩(HS512, Base64)하면 alg, sub, iss, iat, exp등의 정보가 출력된다.
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
			// Token 생성
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(user.getUsername())
					.id(user.getId())
					.token(token) // tokenProvider 생성 후 추가
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
