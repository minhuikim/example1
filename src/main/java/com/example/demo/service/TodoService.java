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
	 * 2023-02-26 에러 수정
	 * 수정내용 :
		Column "T.USERID" not found;
			조회 쿼리에서 userId제거하여 추가 에러 확인 (이상없음) -> userId 컬럼명 user로 변경
	*/ 

	@Autowired
	private TodoRepository repository;
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		// (1) 저장할 엔티티가 유효한지 확인한다. 
		validate(entity);
		
		try {
			// (2) 엔티티를 삭제한다.
			repository.delete(entity);
		} catch(Exception e) {
			// (3) exception 발생 시 id와 exception을 로깅한다.
			log.error("error deleting entity ", entity.getId(), e);
			
			// (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// (5) 새 Todo 리스트를 가져와 리턴한다.
		return retrieve(entity.getUsers());
	}
	
	public List<TodoEntity> update(final TodoEntity entity) {
		// (1) 저장할 엔티티가 유효한지 확인한다. 
		validate(entity);
		
		// (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		original.ifPresent(todo -> {
			// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			// (4) 데이터베이스에 새 값을 저장한다.
			repository.save(todo);
		});
		
		
		// (3)~(4) 다른 작성 방식	
//		if(original.isPresent()) {
//			// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
//			final TodoEntity todo = original.get();
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entity.isDone());
//			
//			// (4) 데이터베이스에 새 값을 저장한다.
//			repository.save(todo); 
//		}
		
		// Retrieve Todo에서 만든 메서드를 이용해 유저의 모든 Todo리스트를 리턴한다.
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
	
	// 리팩토링한 메서드
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
		// TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		// TodoEntity 저장
		repository.save(entity);
		// TodoEntity 검색
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		
		return savedEntity.getTitle();
	}
}
