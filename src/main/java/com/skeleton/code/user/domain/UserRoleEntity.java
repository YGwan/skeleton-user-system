package com.skeleton.code.user.domain;

import com.skeleton.code.common.domain.constants.RoleType;
import com.skeleton.code.user.domain.converter.UserRoleConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_role")
public class UserRoleEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Convert(converter = UserRoleConverter.class)
    private Set<RoleType> roles = new HashSet<>();

    public UserRoleEntity(Long userId, Set<RoleType> roles) {
        this.userId = userId;
        this.roles = roles;
    }

    public List<String> roleNames() {
        return roles.stream()
            .map(Enum::name)
            .toList();
    }
}
