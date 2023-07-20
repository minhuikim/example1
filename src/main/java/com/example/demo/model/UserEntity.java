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
	private String id;				// 유저에게 고유하게 부여되는 id
	
	@Column(nullable = false)
	private String username;		// 아이디로 사용할 유저네임. 이메일일 수도 그냥 문자열일 수도 있다.
	
	private String password;		// 패스워드
									// 이후 OAuth를 이용해 SSO(Single Sign On)을 구현하기 위해 DB에 null 입력 가능하도록 설정 후 회원가입을 구현하는 컨트롤러에서 password를 반드시 입력하도록 설정
	private String role;			// 사용자의 롤. 예 : 어드민, 일반사용자
	
	private String authProvider;	// 이후 OAuth에서 사용할 유저 정보 제공자 : github
}
