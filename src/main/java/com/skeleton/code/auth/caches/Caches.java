package com.skeleton.code.auth.caches;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class Caches {

    /**
     * key : token
     * value : username
     */
    public final static Cache<String, String> BLOCKED_TOKENS = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .build();

    /**
     * key : username
     * value : refresh_token
     */
    public final static Cache<String, String> REFRESH_TOKENS = Caffeine.newBuilder()
        .expireAfterWrite(14, TimeUnit.DAYS)
        .build();

    /**
     * key : username
     * value : none
     */
    public final static Cache<String, String> BLOCKED_USERNAMES = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .build();
}
