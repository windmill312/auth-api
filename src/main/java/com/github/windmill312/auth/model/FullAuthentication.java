package com.github.windmill312.auth.model;

import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.TokenEntity;

public class FullAuthentication {

    private TokenEntity accessToken;
    private TokenEntity refreshToken;
    private PrincipalEntity principal;

    public FullAuthentication(
            TokenEntity accessToken,
            TokenEntity refreshToken,
            PrincipalEntity principal) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.principal = principal;
    }

    public TokenEntity getRefreshToken() {
        return refreshToken;
    }

    public TokenEntity getAccessToken() {
        return accessToken;
    }

    public PrincipalEntity getPrincipal() {
        return principal;
    }
}
