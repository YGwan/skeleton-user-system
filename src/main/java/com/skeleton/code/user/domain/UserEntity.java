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

    private String email;

    private String password;

    private String name;

    private boolean isDeleted = false;

    public UserEntity(String email, String password, String name, boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.isDeleted = isDeleted;
    }
}
