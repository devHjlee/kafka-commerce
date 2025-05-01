package com.kafkacommerce.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.kafkacommerce.common.entity.BaseEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column
    private String nickname;

    @Column
    private String phoneNumber;

    @Column
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column
    private java.time.LocalDateTime lastLoginAt;

    @Builder
    private User(String email, String password, String name, UserRole role, String nickname, String phoneNumber, UserStatus status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.status = status != null ? status : UserStatus.ACTIVE;
    }

    public static User createUser(String email, String password, String name, String nickname, String phoneNumber) {
        return User.builder()
            .email(email)
            .password(password)
            .name(name)
            .role(UserRole.USER)
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .status(UserStatus.ACTIVE)
            .build();
    }

    public void updateProfile(String nickname, String phoneNumber) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }
} 