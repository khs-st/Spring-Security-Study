package com.cos.security1.oauth;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.models.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // password 암호화 설정
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글에서 받은 userRequest 데이터에 대한 후처리 함수
    // 서비스 호출(함수 종료) 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration :" + userRequest.getClientRegistration()); //registrationId로 어떤 OAuth 로 로그인 했는지 확인가능하다.
        System.out.println("userRequest.getAccessToken :" + userRequest.getAccessToken());
        //System.out.println("userRequest.getTokenValue :" + userRequest.getAccessToken().getTokenValue());

        OAuth2User oauth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 return(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원프로필 받음.(loadUser 함수 호출) -> 구글로부터 회원프로필 정보 받아옴
        System.out.println("loadUser(userRequest).getAttributes():"+oauth2User.getAttributes());

        //회원가입 강제로 진행 위한 정보
        String provider=userRequest.getClientRegistration().getClientId(); //google
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_"+providerId; // google_~~
        String email = oauth2User.getAttribute("email");
        String password = bCryptPasswordEncoder.encode("ssss");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity ==null){
            // 최초 구글 로그인 시 자동 회원가입
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        }
        // 일반 로그인(UserDetails) 뿐만이 아니라 구글 로그인 정보(Oauth2User)도 들고 있다.
        return new PrincipalDetails(userEntity,oauth2User.getAttributes());
    }
}
