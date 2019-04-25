package com.github.windmill312.auth.model;

import java.time.Instant;

public class OAuthToken {

    private String accessToken;
    private String tokenType;
    private Instant expiresIn;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public OAuthToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public OAuthToken setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public Instant getExpiresIn() {
        return expiresIn;
    }

    public OAuthToken setExpiresIn(Instant expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OAuthToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
