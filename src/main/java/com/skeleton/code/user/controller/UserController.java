package com.skeleton.code.user.controller;

import com.skeleton.code.user.dto.request.SignupRequest;
import com.skeleton.code.user.dto.response.SignupResponse;
import com.skeleton.code.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        var response = userService.signup(request);
        return ResponseEntity.ok(response);
    }
}
