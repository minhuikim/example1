package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;				// �������� �����ϰ� �ο��Ǵ� id
	
	@Column(nullable = false)
	private String username;		// ���̵�� ����� ��������. �̸����� ���� �׳� ���ڿ��� ���� �ִ�.
	
	private String password;		// �н�����
									// ���� OAuth�� �̿��� SSO(Single Sign On)�� �����ϱ� ���� DB�� null �Է� �����ϵ��� ���� �� ȸ�������� �����ϴ� ��Ʈ�ѷ����� password�� �ݵ�� �Է��ϵ��� ����
	private String role;			// ������� ��. �� : ����, �Ϲݻ����
	
	private String authProvider;	// ���� OAuth���� ����� ���� ���� ������ : github
}
