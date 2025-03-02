package com.skeleton.code.auth.utils;

import com.skeleton.code.auth.exception.AuthErrorCode;
import com.skeleton.code.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    public static String generate(String secretKey, Map<String, Object> claims, int expiredTime) {
        var issuedAt = new Date();
        var accessTokenExpiredAt = new Date(System.currentTimeMillis() + expiredTime);
        return createToken(secretKey, claims, issuedAt, accessTokenExpiredAt);
    }

    private static String createToken(String secretKey, Map<String, Object> claims, Date issuedAt, Date expiredAt) {
        return Jwts.builder()
            .addClaims(claims)
            .setIssuer("skeleton")
            .setIssuedAt(issuedAt)
            .setExpiration(expiredAt)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public static String getUsernameFromToken(String secretKey, String token) {
        var claims = getClaims(secretKey, token);
        return claims.get("username").toString();
    }

    public static String getClaimValue(String secretKey, String token, String key) {
        var claims = getClaims(secretKey, token);
        return claims.get(key).toString();
    }

    public static Claims getClaims(String secretKey, String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }
    }
}
