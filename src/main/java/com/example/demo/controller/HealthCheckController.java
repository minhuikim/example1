package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 20230830 �� ���� Ȯ�ο� api �߰�
// AWS �ε�뷱���� �⺻ ����� ��/���� HTTP ��û�� ���� ���� �����ϴ��� Ȯ���Ѵ�. �Ϸ���ƽ ������ �̸� ������� ���� ���������� ���ǰ� �ʿ��� �������� Ȯ���ϰ� AWS �ܼ� ȭ�鿡 ǥ���Ѵ�.
@RestController
public class HealthCheckController {

	@GetMapping("/")
	
	public String healthCheck() {
		return "The service is up and running...";
	}
}
