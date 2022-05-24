package com.cos.security1.auth;

import com.cos.security1.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 /login 주소 요청오면 가로채서 로그인 진행한다.
// 로그인 진행이 완료 되면 시큐리티 session을 만들어준다. (Security ContexHolder)
// 오브젝트 -> Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 한다.
// User 오브젝트 타입 -> UserDetails 타입 객체여야 한다.
// Security Session -> Authentication -> UserDetails(PrincipalDetails)
// 나중에 User 오브젝트에 접근하려면 Security Session 에서 정보 꺼내야한다.

public class PrincipalDetails implements UserDetails {

    private User user; // 컴포지션

    public PrincipalDetails(User user){
        this.user=user;
    }

    // 해당 User의 권한을 return 하는 곳이다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        
        // 예시로 1년 동안 로그인 안한 회원을 휴면처리 하겠다고 한다면
        // User에 loginDate를 담고 user.getLoginDate로 가져와
        // 현재시간 - 로그인시간을 계산하여 true 나 false 반환하여 처리한다.
        
        return true;
    }
}
