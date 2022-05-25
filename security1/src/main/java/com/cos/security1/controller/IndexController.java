package com.cos.security1.controller;

import com.cos.security1.models.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
