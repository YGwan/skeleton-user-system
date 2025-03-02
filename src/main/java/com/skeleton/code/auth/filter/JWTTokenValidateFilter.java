package com.skeleton.code.auth.filter;

import com.skeleton.code.auth.authenticationManager.CustomUserDetails;
import com.skeleton.code.auth.exception.AuthErrorCode;
import com.skeleton.code.auth.exception.AuthException;
import com.skeleton.code.auth.service.AuthService;
import com.skeleton.code.auth.utils.JwtUtils;
import com.skeleton.code.auth.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JWTTokenValidateFilter extends OncePerRequestFilter {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = TokenUtils.tokenFromHeader(request).orElseThrow();
            var userDetails = userDetailFromToken(token);
            if (authService.isBlockedUser(userDetails.username()) || authService.isBlockedToken(token)) {
                throw new AuthException(AuthErrorCode.TOKEN_INVALID);
            }

            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    private CustomUserDetails userDetailFromToken(String token) {
        var username = JwtUtils.getUsernameFromToken(secretKey, token);
        var roles = authService.getRoleNamesByUsername(username);
        var email = JwtUtils.getClaimValue(secretKey, token, "email");

        return new CustomUserDetails(
            username,
            "",
            email,
            roles
        );
    }
}
