package com.web.admin.web;

import com.web.admin.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello2")
    public HelloResponseDto hello2(String name, int amount) {
        return new HelloResponseDto(name, amount);
    }
}
