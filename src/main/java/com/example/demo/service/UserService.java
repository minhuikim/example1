package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

// 20230727 스프링 시큐리티의 BCryptPasswordEncoder 사용하도록 수정
// BCryptPasswordEncoder : 패스워드에 랜덤한 값을 붙여 인코딩 (랜덤한 값을 Salt, Salt를 붙여 인코딩하는것을 Salting이라고 함)
@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public UserEntity create(final UserEntity userEntity) {
		if(userEntity == null || userEntity.getUsername() == null) {
			throw new RuntimeException("Invalid arguments");
		}
		final String username = userEntity.getUsername();
		if(userRepository.existsByUsername(username)) {
			log.warn("Username already exists {}", username);
			throw new RuntimeException("Username already exists");
		}

		return userRepository.save(userEntity);
	}

	public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
		final UserEntity originalUser = userRepository.findByUsername(username);
		
		if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
		return null;
	}
}
