package com.web.admin.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // @LoginUser 어노테이션을 붙일수 있는 위치 설정 - 매서드의 파라미터에만 가능
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser { // 어노테이션 클래스 선언
}
