package com.web.admin.config.auth.dto;

import com.web.admin.web.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * 인증된 사용자 정보 DTO
 * User Entity에 직접 Serializable 구현시 관계를 맻는 다른 Entity도 직렬화의 대상이 되므로 dto에서 구현
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
