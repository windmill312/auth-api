package com.github.windmill312.auth.model;

import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.TokenEntity;

public class Authentication {

    private TokenEntity tokenEntity;
    private PrincipalEntity principal;

    public Authentication(
            TokenEntity tokenEntity,
            PrincipalEntity principal) {
        this.tokenEntity = tokenEntity;
        this.principal = principal;
    }

    public TokenEntity getTokenEntity() {
        return tokenEntity;
    }

    public PrincipalEntity getPrincipal() {
        return principal;
    }
}
