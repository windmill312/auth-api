package com.github.windmill312.auth.model;

import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.TokenEntity;

public class Authentication {

    private TokenEntity accessToken;
    private PrincipalEntity principal;

    public Authentication(
            TokenEntity accessToken,
            PrincipalEntity principal) {
        this.accessToken = accessToken;
        this.principal = principal;
    }

    public TokenEntity getAccessToken() {
        return accessToken;
    }

    public PrincipalEntity getPrincipal() {
        return principal;
    }
}
