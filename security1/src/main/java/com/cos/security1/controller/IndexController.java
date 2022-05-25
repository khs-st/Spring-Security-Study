package com.cos.security1.controller;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.models.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){//DI 의존성 주입
        System.out.println("/test/login=========================");
        // 다운 캐스팅 과정 후 user 오브젝트 찾기 가능 -> UserDetails를 implements 했기 때문에 가능
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : "+principalDetails.getUser());

        // @AuthenticationPrincipal를 통해서 getUser 가능
        System.out.println("userDetails : "+userDetails.getUser());
        return "세션 정보 확인";
    } 
    
    // 구글 로그인
    @GetMapping("/test/oauth/login")
    public @ResponseBody String oAuthLoginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){//DI 의존성 주입
        System.out.println("/test/oauth/login=========================");
        // 다운 캐스팅 과정 후 Oauth user 오브젝트 찾기 가능
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : "+oAuth2User.getAttributes());
        // @AuthenticationPrincipal를 통해서 getAttributes 가능
        System.out.println("oAuth2User : "+oAuth.getAttributes());
        return "OAuth 세션 정보 확인";
    }

    // 정리: 스프링시큐리티는 자기만의 세션을 갖고 있다.
    // 시큐리티가 갖고 있는 세션이 있다. -> Authentication 객체가 갖고 있음
    // Authentication 객체 DI 가능하다.(UserDetails 타입과 OAuth2User 타입 2개 들고있다.)
    // UserDetails 는 일반 로그인, OAuth2User 는 OAuth 로그인
    // 둘다 되는 GetMapping을 만드려면 하나의 클래스를 만들어 둘다 상속받도록 하면 된다.

    //localhost:8080/
    //localhost:8080
    @GetMapping({"","/"})
    public String index(){
        //Mustache 기본폴더경로: src/main/resources/
        //viewResolver를 templates(prefix), .mustache(suffix)
        //viewResolver 설정이 application.yml에 되어있음
        return "index";
    }
    
    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }
    
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    //securityConfig 파일 생성 후 생성 전의 로그인페이지 동작안함.
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
    
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("USER");
        String rawPassword = user.getPassword();
        String encPassowrd = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassowrd);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole(ROLE_MANAGER) OR hasRole(ROLE_ADMIN)")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 장보";
    }
}
