package com.skeleton.code.auth.config;

public class Urls {

    public static final String[] PERMIT_URLS = new String[]{
        "/api/users/signup",
        "/api/auth/login",
        "/api/auth/reissue",
    };

    public static final String[] CONTENT_URLS = new String[]{
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/h2-console/**"
    };
}
