package com.web.admin.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class) // Junit이 내장 실행자? 외에 다른 실행자를 실행하게 한다. spring과 junit 사이 연결자 역할
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void index페이지가_조회된다() {
        String body = restTemplate.getForObject("/", String.class);
        assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
    }
}
