package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{

	// ?1 은 메서드의 매개변수의 순서 위치이다
	@Query(value = "select * from TodoEntity t where t.userId = ?1", nativeQuery=true)
	List<TodoEntity> findByUserId(String userId);
}
