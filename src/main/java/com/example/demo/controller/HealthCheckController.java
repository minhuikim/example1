package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 20230830 앱 상태 확인용 api 추가
// AWS 로드밸런서는 기본 경로인 “/”에 HTTP 요청을 보내 앱이 동작하는지 확인한다. 일래스틱 빈스톡은 이를 기반으로 앱이 실행중인지 주의가 필요한 상태인지 확인하고 AWS 콘솔 화면에 표시한다.
@RestController
public class HealthCheckController {

	@GetMapping("/")
	
	public String healthCheck() {
		return "The service is up and running...";
	}
}
