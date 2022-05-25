package com.cos.security1.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글에서 받은 userRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration :" + userRequest.getClientRegistration()); //registrationId로 어떤 OAuth 로 로그인 했는지 확인가능하다.
        System.out.println("userRequest.getAccessToken :" + userRequest.getAccessToken());
        //System.out.println("userRequest.getTokenValue :" + userRequest.getAccessToken().getTokenValue());

        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 return(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원프로필 받음.(loadUser 함수 호출) -> 구글로부터 회원프로필 정보 받아옴
        System.out.println("loadUser(userRequest).getAttributes():"+super.loadUser(userRequest).getAttributes());

        OAuth2User oauth2User = super.loadUser(userRequest);
        //회원가입 강제로 진행
        return super.loadUser(userRequest);
    }
}
