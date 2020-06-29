package com.web.admin.config.auth;

import com.web.admin.web.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2 console 사용 위하
                .and()
                    .authorizeRequests()
                        .antMatchers("/","/css/**","/js/**","/images/**","/h2-console/**").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()// Oauth2 로그인 성공이후 사용자 정보를 가져올때 설정을 담당
                            .userService(customOAuth2UserService); // 로그인 성공시 후속조치를 진행할 UserService 구현체 등록
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }
}
