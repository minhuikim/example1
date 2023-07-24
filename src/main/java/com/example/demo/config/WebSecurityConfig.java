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
// ���ø����̼� ���� �� o.s.s.web.DefaultSecurityFilterChain �α� Ȯ�� ����
public class WebSecurityConfig  {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	/*
	[��û]
	signup, signin ��û �� ����� ��ū���� todo ��û
	GET http://localhost:8080/todo
	Authorization (Type)Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4ODA4NDg5ODcxYjNmMDE4OTg3MWI1NjA5MDAwMCIsImlzcyI6ImRlbW8gYXBwIiwiaWF0IjoxNjkwMTg4ODY1LCJleHAiOjE2OTAyNzUyNjV9.cWnIZmuVcUBJ_oN8BNItKeIDyr0_GXEvZWjwUYGC_oFOCa32QVLhTObCK8sR3w_N6--gZjmkXVNlR5_1aIchqw
	
	[����]
	{
	    "error": null,
	    "data": []
	}
	- �߸��� ��û(������ ��ū)�� ���� ��� : 403 Forbidden ���� ���
	
	[���ø����̼� �α�]
	c.e.d.security.JwtAuthenticationFilter   : Authenticated user ID : 4028808489871b3f0189871b56090000
	- �߸��� ��û(������ ��ū)�� ���� ��� : io.jsonwebtoken.SignatureException: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted
	 * */
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// http ��ť��Ƽ ����
		return http.cors().and()	// WebMvcConfig���� �̹� ���������Ƿ� �⺻ cors ����
			.csrf().disable()		// csrf�� ���� ������� �����Ƿ� disabled
			.httpBasic().disable()	// token�� ����ϹǷ� basic ���� disabled
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()	// session ����� �ƴ��� ����
//			.authorizeRequests().antMatchers("/", "/auth/**").permitAll() ���� ����, �Ʒ�ó�� �ۼ�
            .authorizeHttpRequests((authz) -> authz
            		.requestMatchers("/", "/auth/**").permitAll() 	// /�� /auth/** ��δ� ���� ���� ����
            		.anyRequest().authenticated())					// /�� /auth/**�̿��� ��� ��δ� ������
			// filter ��� : �� ��û���� CorsFilter ���� �Ŀ� jwtAuthenticationFilter�� �����Ѵ�.
			.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class)
			.build();
	}
}
