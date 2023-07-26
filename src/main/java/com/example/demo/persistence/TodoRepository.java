package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{

	List<TodoEntity> findByUserId(String userId);

	/*
	 * 2023-02-26 에러 수정
	 * 수정내용 :
	 	Validation failed for query for method public abstract
			nativeQuery=true 추가
		could not prepare statement
			테이블 명 TodoEntity -> todo로 변경
		Column "T.USERID" not found;
			조회 쿼리에서 userId제거하여 추가 에러 확인 (이상없음) -> userId 컬럼명 user로 변경
			
		2023-07-26 TodoEntity 사용하도록 수정
	*/
	@Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1")
	List<TodoEntity> findByUserIdQuery(String users);
}
