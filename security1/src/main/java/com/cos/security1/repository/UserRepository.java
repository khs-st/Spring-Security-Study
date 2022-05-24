package com.cos.security1.repository;

import com.cos.security1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

//CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 등록 된다. JpaRepository가 들고 있기 때문
public interface UserRepository extends JpaRepository<User, Long> {
    // findBy 규칙 -> Username 문법
    // select * from user where username=?
    User findByUsername(String username);
}
