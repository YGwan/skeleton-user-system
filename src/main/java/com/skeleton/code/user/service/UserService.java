package com.skeleton.code.user.service;

import com.skeleton.code.user.domain.UserEntity;
import com.skeleton.code.user.domain.UserRepository;
import com.skeleton.code.user.dto.request.SignupRequest;
import com.skeleton.code.user.dto.response.SignupResponse;
import com.skeleton.code.user.dto.response.UserResponse;
import com.skeleton.code.user.exception.UserErrorCode;
import com.skeleton.code.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.skeleton.code.user.exception.UserErrorCode.USERNAME_ALREADY_EXISTS;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(USERNAME_ALREADY_EXISTS);
        }

        var entity = request.toEntity();
        userRepository.save(entity);
        entity.encryptPassword(request.password(), passwordEncoder);
        return SignupResponse.of(entity);
    }

    public UserResponse findByUsername(String username) {
        var user = getByUsername(username);
        return UserResponse.of(user);
    }

    private UserEntity getByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
