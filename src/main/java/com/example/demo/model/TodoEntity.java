package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoEntity {
	private String id;		// 오브젝트 id
	private String userId;	// 오브젝트 생성 유저 아이디
	private String title;	// Todo 타이틀 예) 운동 하기
	private boolean done;	// true - todo를 완료한 경우 (checked)
}
