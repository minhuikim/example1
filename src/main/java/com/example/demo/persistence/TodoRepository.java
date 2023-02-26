package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{
	
//	List<TodoEntity> findByUserId(String userId);
	
	/* 
	 * 2023-02-26 ���� ����
	 * �������� :
	 	Validation failed for query for method public abstract
			nativeQuery=true �߰�
		could not prepare statement 
			���̺� �� TodoEntity -> todo�� ����
		Column "T.USERID" not found;
			��ȸ �������� userId�����Ͽ� �߰� ���� Ȯ�� (�̻����) -> userId �÷��� user�� ����
	*/ 
//	@Query(value="select * from TodoEntity t where t.userId = ?1", nativeQuery=true)
//	@Query(value="select * from todo ", nativeQuery=true)
	@Query(value="select * from todo t where t.users = ?1", nativeQuery=true)
	List<TodoEntity> findByUserIdQuery(String users);
}
