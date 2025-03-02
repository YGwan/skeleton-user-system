package com.skeleton.code.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String email);

    boolean existsByUsername(String username);

    Optional<UserEntity> findByIdAndIsDeletedFalse(Long userId);

    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);
}
