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
@RequestMapping("test")	// ���ҽ�
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

	// /test ��δ� �̹� �����ϹǷ� /test/testRequestBody�� ����
	/*
	 * ����Ʈ�ǿ��� Body - raw - JSON ���·� { "id" : 123, "message" : "Hello ?" } ��û �� Hello World! ID 123 Message : Hello ? ��ȯ
	 * */
	@GetMapping("/testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		return "Hello World! ID " + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
	}

	/*
	 * localhost:8080/test/testResponseBody ��û ��
	   {
		    "error": null,
		    "data": [
		        "Hello World! I'm REsponseDTO"
		    ]
		}
		��ȯ
	 * */
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testControllerResponseBody() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm REsponseDTO");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}

	/*
	 * localhost:8080/test/testResponseEntity ��û ��
		{
		    "error": null,
		    "data": [
		        "Hello World! I'm ResponseEntity. And you got 400!"
		    ]
		}
		��
		400 Bad Request
		��ȯ
	 * */
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		// http status�� 400���� ����.
		return ResponseEntity.badRequest().body(response);
	}

}
