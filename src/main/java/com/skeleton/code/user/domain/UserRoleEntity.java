package com.skeleton.code.user.domain;

import com.skeleton.code.common.domain.constants.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "user_roles",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_role_user_id_role", columnNames = {"user_id", "role"})
    }
)
public class UserRoleEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public UserRoleEntity(Long userId, RoleType role) {
        this.userId = userId;
        this.role = role;
    }
}
