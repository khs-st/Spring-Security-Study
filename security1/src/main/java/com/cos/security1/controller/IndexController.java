package com.cos.security1.controller;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
    @GetMapping("/login")
    public @ResponseBody String login(){
        return "login";
    }

    @GetMapping("/join")
    public @ResponseBody String join(){
        return "join";
    }
    
    @GetMapping("/joinProc")
    public @ResponseBody String joinProc(){
        return "회원가입 완료";
    }


}
