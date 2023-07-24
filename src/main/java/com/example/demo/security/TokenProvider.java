package com.example.demo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	// NMA8JPctFuna59f5
	private static final String SECRET_KEY = "FlRpX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M1Oicegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

	// JWT라이브러리를 이용해 JWT토큰 생성, SECRET_KEY를 개인키로 사용
	public String create(UserEntity userEntity) {
		// 기한 지금으로부터 1일로 설정
		Date expiryDate = Date.from(
			Instant.now()
				.plus(1, ChronoUnit.DAYS));

		// JWT Token 생성
		return Jwts.builder()
				// header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				// payload에 들어갈 내용
				.setSubject(userEntity.getId()) // sub
				.setIssuer("demo app") 			// iss
				.setIssuedAt(new Date()) 		// iat
				.setExpiration(expiryDate) 		// exp
				.compact();
	}

	// 토큰 디코딩, 파싱, 위조여부 확인 후 유저 아이디 리턴
	// 라이브러리 사용하지 않을 경우 json을 생성, 서명, 인코딩, 디코딩, 파싱하는 작업을 해야함
	public String validateAndGetUserId(String token) {
		// parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
		// 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명 후, token의 서명과 비교
		// 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
		// 그 중 우리는 userId가 필요하므로 getBody를 부른다.
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject();
	}

}
