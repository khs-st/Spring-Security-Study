package com.cos.security1.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    // 구글 정보 provider: "google", providerId: "google 에서 가져온 username"
    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;

}
