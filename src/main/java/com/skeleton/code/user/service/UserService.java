package com.skeleton.code.user.service;

import com.skeleton.code.user.domain.UserRepository;
import com.skeleton.code.user.dto.request.SignupRequest;
import com.skeleton.code.user.dto.response.SignupResponse;
import com.skeleton.code.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.skeleton.code.user.exception.UserErrorCode.EMAIL_ALREADY_EXISTS;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException(EMAIL_ALREADY_EXISTS);
        }

        var entity = request.toEntity();
        userRepository.save(entity);
        return SignupResponse.of(entity);
    }
}
