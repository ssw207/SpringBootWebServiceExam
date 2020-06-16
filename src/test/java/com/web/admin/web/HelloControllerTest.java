package com.web.admin.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

// 인텔리 제이 기준 static은 alt+ctl+o 단축키로 자동 import가 되지 않는다.. 추가 확인필요
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class) // Junit이 내장 실행자? 외에 다른 실행자를 실행하게 한다. spring과 junit 사이 연결자 역할
@WebMvcTest // web mvc @Controller 테스트릉 위한 어노테이션. @Service, @Compoent, @Repository 어노테이션은 테스트 불가능
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc; // 웹 API테스트를 위한 객체

    @Test
    public void hello가_리턴된다() throws Exception {
        mvc.perform(get("/hello")) // /hello 주소로 Http요청 실행
                .andExpect(status().isOk()) // 요청의 Http status를 검증
                .andExpect(content().string("hello")); // Http 응답본문의 내용을 검증
    }

    @Test
    public void dto테스트() throws Exception {
        String name ="myname";
        int amount = 1000;

        mvc.perform(
                get("/hello2")
                        .param("name",name)
                        .param("amount", String.valueOf(amount))) // parm으로 String만 전달가능
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
