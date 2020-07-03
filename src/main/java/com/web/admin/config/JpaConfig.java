package com.web.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // jpa auditing 활성화 , 테스트를 위해 Application.class에서 분리
public class JpaConfig {

}
