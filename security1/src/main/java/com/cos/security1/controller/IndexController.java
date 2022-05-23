package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
