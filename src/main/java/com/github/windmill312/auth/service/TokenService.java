package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.TokenType;
import com.github.windmill312.auth.model.entity.TokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface TokenService {
    TokenEntity generateToken();

    TokenEntity generateToken(long tokenTtlSeconds);

    Optional<TokenEntity> getTokenByPrincipalAndType(String token);

    Optional<TokenEntity> getTokenByPrincipalAndType(UUID principalExtId, TokenType type);

    Optional<TokenEntity> getTokenByPrincipalAndTypeAndValue(UUID principalExtId, TokenType type, String value);

    void saveToken(TokenEntity token);

    void revokeToken(TokenEntity token);

    void revokeTokensByPrincipal(UUID principalExtId);
}
