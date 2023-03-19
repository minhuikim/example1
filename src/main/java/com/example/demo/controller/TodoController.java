package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

	@Autowired
	private TodoService service;
	
	/*
	 [��û]
	 1.	POST localhost:8080/todo
	 	Body JSON raw : { "title" : "�� ����Ʈ1" }
	 
	 2.	DELETE localhost:8080/todo
	 	Body JSON raw : {
		    "id": "4028808486f82ce00186f82cf5b50000"
		}
	 
	 [����]
	 {
	    "error": null,
	    "data": []
	}
	 * */
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
		try {
			String temporaryUserId = "temporary-user"; // temporary user id
			
			// (1) TodoEntity�� ��ȯ�Ѵ�.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) �ӽ� ���� ���̵� �������ش�. ������ ������ �ΰ� ����� ���� ������ �� ����(temporary-user)�� �α��� ���� ��� ������ ���ø����̼��� ���̴�.
			entity.setUsers(temporaryUserId);
			
			// (3) ���񽺸� �̿��� entity�� �����Ѵ�.
			List<TodoEntity> entities = service.delete(entity);
			
			// (4) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// (5) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (6) ResponseDTO�� �����Ѵ�.
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// (8) Ȥ�� ���ܰ� ���� ��� dto��� error�� �޽����� �־� �����Ѵ�.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	/*
	 [��û]
	 1.	POST localhost:8080/todo
	 	Body JSON raw : { "title" : "�� ����Ʈ1" }
	 
	 2.	PUT localhost:8080/todo
	 	Body JSON raw : {
		    "id": "4028808486f820ea0186f820fea20000",
		    "title": "�� ����Ʈ1 - ���� �׽�Ʈ",
		    "done": true
		}
	 
	 [����]
	 {
	    "error": null,
	    "data": [
	        {
	            "id": "4028808486f820ea0186f820fea20000",
	            "title": "�� ����Ʈ1 - ���� �׽�Ʈ",
	            "done": true
	        }
	    ]
	}
	 * */
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
		String temporaryUserId = "temporary-user"; // temporary user id
		
		// (1) dto�� entity�� ��ȯ�Ѵ�.
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// (2) id�� temporaryUserId�� �ʱ�ȭ�Ѵ�. ����� 4�� ������ �ΰ����� ������ �����̴�.
		entity.setUsers(temporaryUserId);
		
		// (3) ���񽺸� �̿��� entity�� ������Ʈ�Ѵ�.
		List<TodoEntity> entities = service.update(entity);
		
		// (4) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (5) ��ȯ�� TodoDTO����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ �Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (6) ResponseDTO�� �����Ѵ�.
		return ResponseEntity.ok().body(response);
	}
	
	/*
	 [��û]
	 1.	POST localhost:8080/todo
	 	Body JSON raw : { "title" : "�� ����Ʈ1" }
	 
	 2.	GET localhost:8080/todo
	 
	 [����]
	 {
	    "error": null,
	    "data": [
	        {
	            "id": "4028808486f810d40186f810eafb0000",
	            "title": "�� ����Ʈ1",
	            "done": false
	        }
	    ]
	}
	  
	 * */
	@GetMapping
	public ResponseEntity<?> retrieveTodoList() {
		String temporaryUserId = "temporary-user"; // temporary user id
		
		// (1) ���� �޼����� retrieve �޼��带 ����� Todo����Ʈ�� �����´�.
		List<TodoEntity> entities = service.retrieve(temporaryUserId);
		
		// (2) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (3) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (4) ResponseDTO�� �����Ѵ�.
		return ResponseEntity.ok().body(response);
	}

	// test
	@GetMapping("/test")
	public ResponseEntity<?> testTodo() {
		String str = service.testService();
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	
	/*
	 * 2023-02-26 ���� ����
	 * �������� :
		Column "T.USERID" not found;
			��ȸ �������� userId�����Ͽ� �߰� ���� Ȯ�� (�̻����) -> userId �÷��� user�� ����
			 
	 [��û]
	 localhost:8080/todo
	 Body JSON raw : { "title" : "�� ����Ʈ1" }
	 
	 [��ȯ]
	 {
	    "error": null,
	    "data": [
	        {
	            "id": "40288084868d9ae601868d9af9b60000",
	            "title": "�� ����Ʈ1",
	            "done": false
	        }
	    ]
	}
	 * */
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
				
		try {
			String temporaryUserId = "temporary-user"; // temporary user id.
			
			// (1) TodoEntity�� ��ȯ�Ѵ�.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) id�� null�� �ʱ�ȭ�Ѵ�. ���� ��ÿ��� id�� ����� �ϱ� �����̴�.
			entity.setId(null);
			
			// (3) �ӽ� ���� ���̵� �������ش�. �� �κ��� 4�� ������ �ΰ����� ������ �����̴�. ������ ������ �ΰ� ����� �����Ƿ� �� ����(temporary-user)�� �α��� ���� ��� ������ ���ø����̼��� ���̴�.
			entity.setUsers(temporaryUserId);
			
			// (4) ���񽺸� �̿��� Todo ��ƼƼ�� �����Ѵ�.
			List<TodoEntity> entities = service.create(entity);
			
			// (5) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ�Ѵ�.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// (6) ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (7) ResponseDTO�� �����Ѵ�.
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// (8) Ȥ�� ���ܰ� ���� ��� dto ��� error�� �޽����� �־� �����Ѵ�.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
