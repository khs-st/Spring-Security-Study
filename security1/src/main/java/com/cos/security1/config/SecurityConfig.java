package com.cos.security1.config;

import com.cos.security1.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //spring security 필터가 spring 필터 체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 둘다 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 return 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodPwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                //아래 3가지 주소가 아니면 누구나 접근가능하도록 설정
                .antMatchers("/user/**").authenticated() // 인증만 된다면 접속 가능한 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                //아래 추가 후에는 위의 3가지 주소 모두 로그인으로 이동
                .formLogin()
                .loginPage("/loginForm")
                // username 파라미터 변환 -> 로그인폼 name값과 다른 변수라면
                // PrincipalDetailsService의 String username를 받아오게 하기 위한 설정
                .usernameParameter("username")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/") // login 주소가 호출 되면 시큐리티가 가로채서 로그인 진행한다.
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인 완료 후 처리가 필요함
                // 1. 코드 받기(인증)
                // 2. 액세스토큰
                // 3. 사용자프로필 정보 가져옴
                // 4-1. 그 정보를 토대로 회원가입 자동으로 진행시키기도 한다.
                // 4-2. (이메일, 전홥너호, 이름, 아이디) 쇼핑몰 -> (집주소) 백화점몰 -> (vip등급,일반등급)
                // 구글 로그인 완료 시 코드x, (액세스토큰 + 사용자프로필정보 O )
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
