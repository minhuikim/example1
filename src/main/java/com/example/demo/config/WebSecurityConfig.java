package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
// 애플리케이션 실행 시 o.s.s.web.DefaultSecurityFilterChain 로그 확인 가능
public class WebSecurityConfig  {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	/*
	[요청]
	signup, signin 요청 후 발행된 토큰으로 todo 요청
	GET http://localhost:8080/todo
	Authorization (Type)Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4ODA4NDg5ODcxYjNmMDE4OTg3MWI1NjA5MDAwMCIsImlzcyI6ImRlbW8gYXBwIiwiaWF0IjoxNjkwMTg4ODY1LCJleHAiOjE2OTAyNzUyNjV9.cWnIZmuVcUBJ_oN8BNItKeIDyr0_GXEvZWjwUYGC_oFOCa32QVLhTObCK8sR3w_N6--gZjmkXVNlR5_1aIchqw
	
	[응답]
	{
	    "error": null,
	    "data": []
	}
	- 잘못된 요청(변형된 토큰)을 보낼 경우 : 403 Forbidden 에러 출력
	
	[어플리케이션 로그]
	c.e.d.security.JwtAuthenticationFilter   : Authenticated user ID : 4028808489871b3f0189871b56090000
	- 잘못된 요청(변형된 토큰)을 보낼 경우 : io.jsonwebtoken.SignatureException: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted
	 * */
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// http 시큐리티 빌더
		return http.cors().and()	// WebMvcConfig에서 이미 설정했으므로 기본 cors 설정
			.csrf().disable()		// csrf는 현재 사용하지 않으므로 disabled
			.httpBasic().disable()	// token을 사용하므로 basic 인증 disabled
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()	// session 기반이 아님을 선언
//			.authorizeRequests().antMatchers("/", "/auth/**").permitAll() 지원 종료, 아래처럼 작성
            .authorizeHttpRequests((authz) -> authz
            		.requestMatchers("/", "/auth/**").permitAll() 	// /와 /auth/** 경로는 인증 하지 않음
            		.anyRequest().authenticated())					// /와 /auth/**이외의 모든 경로는 인증함
			// filter 등록 : 매 요청마다 CorsFilter 실행 후에 jwtAuthenticationFilter를 실행한다.
			.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class)
			.build();
	}
}
