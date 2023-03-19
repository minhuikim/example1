package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	/* 
	 * 2023-02-26 ���� ����
	 * �������� :
		Column "T.USERID" not found;
			��ȸ �������� userId�����Ͽ� �߰� ���� Ȯ�� (�̻����) -> userId �÷��� user�� ����
	*/ 

	@Autowired
	private TodoRepository repository;
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		// (1) ������ ��ƼƼ�� ��ȿ���� Ȯ���Ѵ�. 
		validate(entity);
		
		try {
			// (2) ��ƼƼ�� �����Ѵ�.
			repository.delete(entity);
		} catch(Exception e) {
			// (3) exception �߻� �� id�� exception�� �α��Ѵ�.
			log.error("error deleting entity ", entity.getId(), e);
			
			// (4) ��Ʈ�ѷ��� exception�� ������. �����ͺ��̽� ���� ������ ĸ��ȭ�ϱ� ���� e�� �������� �ʰ� �� exception ������Ʈ�� �����Ѵ�.
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// (5) �� Todo ����Ʈ�� ������ �����Ѵ�.
		return retrieve(entity.getUsers());
	}
	
	public List<TodoEntity> update(final TodoEntity entity) {
		// (1) ������ ��ƼƼ�� ��ȿ���� Ȯ���Ѵ�. 
		validate(entity);
		
		// (2) �Ѱܹ��� ��ƼƼ id�� �̿��� TodoEntity�� �����´�. �������� �ʴ� ��ƼƼ�� ������Ʈ �� �� ���� �����̴�.
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		original.ifPresent(todo -> {
			// (3) ��ȯ�� TodoEntity�� �����ϸ� ���� �� entity�� ������ ���� �����.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			// (4) �����ͺ��̽��� �� ���� �����Ѵ�.
			repository.save(todo);
		});
		
		
		// (3)~(4) �ٸ� �ۼ� ���	
//		if(original.isPresent()) {
//			// (3) ��ȯ�� TodoEntity�� �����ϸ� ���� �� entity�� ������ ���� �����.
//			final TodoEntity todo = original.get();
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entity.isDone());
//			
//			// (4) �����ͺ��̽��� �� ���� �����Ѵ�.
//			repository.save(todo); 
//		}
		
		// Retrieve Todo���� ���� �޼��带 �̿��� ������ ��� Todo����Ʈ�� �����Ѵ�.
		return retrieve(entity.getUsers());
	}
	
	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserIdQuery(userId);
	}
	
	public List<TodoEntity> create(final TodoEntity entity) {
		//Validations
		validate(entity);
		
		repository.save(entity);
		
		log.info("Entity id : {} is saved.", entity.getId());
		
		return repository.findByUserIdQuery(entity.getUsers());
	}
	
	// �����丵�� �޼���
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUsers() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
	
	public String testService() {
		// TodoEntity ����
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		// TodoEntity ����
		repository.save(entity);
		// TodoEntity �˻�
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		
		return savedEntity.getTitle();
	}
}
