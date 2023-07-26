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
	 * 2023-02-26 ���� ����
	 * �������� :
		Column "T.USERID" not found;
			��ȸ �������� userId�����Ͽ� �߰� ���� Ȯ�� (�̻����) -> userId �÷��� user�� ����
	*/
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;		// ������Ʈ id
	private String userId;	// ������Ʈ ���� ���� ���̵�
	private String title;	// Todo Ÿ��Ʋ ��) � �ϱ�
	private boolean done;	// true - todo�� �Ϸ��� ��� (checked)
}
