package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * cmd로 실행
 * 	1. 프로젝트 경로로 이동
 * 		D:\eclipswork\example1\demo
 * 	2. 실행 명령어
 * 		gradlew bootRun
 * */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
