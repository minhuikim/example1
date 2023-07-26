package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "todo")
public class TodoEntity {

	/*
	 * 2023-02-26 에러 수정
	 * 수정내용 :
		Column "T.USERID" not found;
			조회 쿼리에서 userId제거하여 추가 에러 확인 (이상없음) -> userId 컬럼명 user로 변경
	*/
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;		// 오브젝트 id
	private String userId;	// 오브젝트 생성 유저 아이디
	private String title;	// Todo 타이틀 예) 운동 하기
	private boolean done;	// true - todo를 완료한 경우 (checked)
}
