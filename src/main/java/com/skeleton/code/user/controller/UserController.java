package com.skeleton.code.user.controller;

import com.skeleton.code.auth.authenticationManager.CustomUserDetails;
import com.skeleton.code.user.dto.request.SignupRequest;
import com.skeleton.code.user.dto.response.SignupResponse;
import com.skeleton.code.user.dto.response.UserResponse;
import com.skeleton.code.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
}
