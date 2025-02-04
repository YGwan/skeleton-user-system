package com.skeleton.code.user.domain;

import com.skeleton.code.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private boolean isDeleted = false;

    public UserEntity(String username, String password, String email, boolean isDeleted) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isDeleted = isDeleted;
    }
}
