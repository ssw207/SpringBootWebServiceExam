package com.web.admin.config.auth;

import com.web.admin.config.auth.dto.OAuthAttributes;
import com.web.admin.config.auth.dto.SessionUser;
import com.web.admin.web.domain.user.User;
import com.web.admin.web.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * OAuth인증 성공후 후속 처리 클래스
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService(); // ?
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // ?

        //OAuth2 서비스 ID (구글,네이버,카카오...)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //PK
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // ?
        //OAuth2 유저 정보
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); // ?

        //OAuth2 정보가 변경되었으면 업데이트한다. (이름, 사진등)
        User user = saveOrUpdate(attributes);
        //세션에 유저정보 저장
        httpSession.setAttribute("user", new SessionUser(user)); // SessionUser는

        //??
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey()))
                                    , attributes.getAttirbutes()
                                    , attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        return userRepository.findByEmail(attributes.getEmail())
                        .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                        .orElse(attributes.toEntity());
    }


}
