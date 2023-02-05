package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")	// ���ҽ�
public class TestController {

	@GetMapping
	public String testController() {
		return "Hello World";
	}
	
	@GetMapping("/testGetMapping")
	public String testControllerWithPath() {
		return "Hello World! testGetMapping";
	}
	
	// GET���� localhost:8080/test/123 ��û �� Hello World! ID 123 ��ȯ
	@GetMapping("/{id}")
	public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
		return "Hello World! ID " + id;
	}
	
	// GET���� localhost:8080/test/testRequestParam?id=123 ��û �� Hello World! ID 123 ��ȯ
	@GetMapping("/testRequestParam")
	public String testControllerRequestParam(@RequestParam(required = false) int id) {
		return "Hello World! ID " + id;
	}
}
