package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("test")	// 리소스
public class TestController {

	@Autowired
	private TodoService service;

	@GetMapping("/test")
	public ResponseEntity<?> testTodo() {
		String str = service.testService();
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public String testController() {
		return "Hello World";
	}

	@GetMapping("/testGetMapping")
	public String testControllerWithPath() {
		return "Hello World! testGetMapping";
	}

	// GET으로 localhost:8080/test/123 요청 시 Hello World! ID 123 반환
	@GetMapping("/{id}")
	public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
		return "Hello World! ID " + id;
	}

	// GET으로 localhost:8080/test/testRequestParam?id=123 요청 시 Hello World! ID 123 반환
	@GetMapping("/testRequestParam")
	public String testControllerRequestParam(@RequestParam(required = false) int id) {
		return "Hello World! ID " + id;
	}

	// /test 경로는 이미 존재하므로 /test/testRequestBody로 지정
	/*
	 * 포스트맨에서 Body - raw - JSON 형태로 { "id" : 123, "message" : "Hello ?" } 요청 시 Hello World! ID 123 Message : Hello ? 반환
	 * */
	@GetMapping("/testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		return "Hello World! ID " + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
	}

	/*
	 * localhost:8080/test/testResponseBody 요청 시
	   {
		    "error": null,
		    "data": [
		        "Hello World! I'm REsponseDTO"
		    ]
		}
		반환
	 * */
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testControllerResponseBody() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm REsponseDTO");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}

	/*
	 * localhost:8080/test/testResponseEntity 요청 시
		{
		    "error": null,
		    "data": [
		        "Hello World! I'm ResponseEntity. And you got 400!"
		    ]
		}
		와
		400 Bad Request
		반환
	 * */
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		// http status를 400으로 설정.
		return ResponseEntity.badRequest().body(response);
	}

}
