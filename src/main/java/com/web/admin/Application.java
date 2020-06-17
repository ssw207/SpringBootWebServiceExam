package com.web.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // jpa entity의 생성, 수정시간을 자동으로 관리하는 어노테이션을 사용할수 있게함
@SpringBootApplication // 스프링부트 자동설정, bean관리 자동설정, 현위치 이하의 스프링 설정을 읽는다.
public class Application {
    public static void main(String[] args) {
        // 내장 was실행 -> 언제나 같은 환경에서 배포할수 있게함
        SpringApplication.run(Application.class, args);
    }
}
