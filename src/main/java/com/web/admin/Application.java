package com.web.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 스프링부트 자동설정, bean관리 자동설정, 현위치 이하의 스프링 설정을 읽는다.
public class Application {
    public static void main(String[] args) {
        // 내장 was실행 -> 언제나 같은 환경에서 배포할수 있게함
        SpringApplication.run(Application.class, args);
    }
}
