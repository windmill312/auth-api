package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.model.entity.TokenEntity;
import com.github.windmill312.auth.repository.TokenRepository;
import com.github.windmill312.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final long DEFAULT_TOKEN_TTL_DAYS = 30;

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public TokenEntity generateToken() {
        return generateToken(Duration.ofDays(DEFAULT_TOKEN_TTL_DAYS).getSeconds());
    }

    @Override
    public TokenEntity generateToken(long tokenTtlSeconds) {
        Instant createTime = Instant.now();
        Instant expireTime = createTime.plusSeconds(tokenTtlSeconds);

        return new TokenEntity()
                .setValue(UUID.randomUUID().toString())
                .setValidFrom(createTime)
                .setValidTill(expireTime);
    }

    @Override
    public Optional<TokenEntity> getToken(String token) {
        return tokenRepository.findByValue(token);
    }

    //todo add token type
    @Override
    public Optional<TokenEntity> getToken(UUID principalExtId) {
        return tokenRepository.findByPrincipalExtId(principalExtId);
    }

    @Override
    public void saveToken(TokenEntity token) {
        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(TokenEntity token) {
        tokenRepository.delete(token);
    }
}
