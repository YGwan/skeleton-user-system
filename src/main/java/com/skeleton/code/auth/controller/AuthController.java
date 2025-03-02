package com.skeleton.code.auth.controller;

import com.skeleton.code.auth.dto.LogInRequest;
import com.skeleton.code.auth.dto.ReissueRequest;
import com.skeleton.code.auth.dto.Tokens;
import com.skeleton.code.auth.service.AuthService;
import com.skeleton.code.auth.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Tokens> login(@RequestBody LogInRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        var result = authService.createToken(authentication);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        var tokenOpt = TokenUtils.tokenFromHeader(request);
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        authService.blockToken(tokenOpt.get(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<Tokens> reissue(
        @RequestBody ReissueRequest request
    ) {
        var response = authService.reissue(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}
