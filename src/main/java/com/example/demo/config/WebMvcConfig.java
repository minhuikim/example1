package com.example.demo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 리액트로 만든 TodoList 페이지 실행 후 서비스를 불러올 때 CORS에러 방지를 위해 작성
// CORS : Cross-Origin Resource Sharing, 처음 리소스를 제공한 도메인이 현재 요청하려는 도메인과 다르더라도 요청을 허락해주는 웹 보안 방침
@Configuration	// 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {
	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 모든 경로에 대해
		registry.addMapping("/**")
			// Origin이 http:localhost:3000에 대해
			.allowedOrigins("http://localhost:3000")
			// GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(MAX_AGE_SECS);
	}
}
