package com.example.demo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// ����Ʈ�� ���� TodoList ������ ���� �� ���񽺸� �ҷ��� �� CORS���� ������ ���� �ۼ�
// CORS : Cross-Origin Resource Sharing, ó�� ���ҽ��� ������ �������� ���� ��û�Ϸ��� �����ΰ� �ٸ����� ��û�� ������ִ� �� ���� ��ħ
@Configuration	// ������ ������ ���
public class WebMvcConfig implements WebMvcConfigurer {
	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// ��� ��ο� ����
		registry.addMapping("/**")
			// Origin�� http:localhost:3000�� ����
			.allowedOrigins("http://localhost:3000")
			// GET, POST, PUT, PATCH, DELETE, OPTIONS �޼��带 ����Ѵ�.
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(MAX_AGE_SECS);
	}
}
