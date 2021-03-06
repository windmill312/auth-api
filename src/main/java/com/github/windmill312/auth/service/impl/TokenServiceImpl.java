package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.model.TokenType;
import com.github.windmill312.auth.model.entity.TokenEntity;
import com.github.windmill312.auth.repository.TokenRepository;
import com.github.windmill312.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Transactional
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
    public Optional<TokenEntity> getTokenByPrincipalAndType(String token) {
        return tokenRepository.findByValue(token);
    }

    //todo add token type
    @Override
    public Optional<TokenEntity> getTokenByPrincipalAndType(UUID principalExtId, TokenType type) {
        return tokenRepository.findByPrincipalExtIdAndTokenType(principalExtId, type);
    }

    @Override
    public Optional<TokenEntity> getTokenByPrincipalAndTypeAndValue(UUID principalExtId, TokenType type, String value) {
        return tokenRepository.findByPrincipalExtIdAndTokenTypeAndValue(principalExtId, type, value);
    }

    @Override
    public void saveToken(TokenEntity token) {
        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(TokenEntity token) {
        tokenRepository.delete(token);
    }

    @Override
    public void revokeTokensByPrincipal(UUID principalExtId) {
        tokenRepository.deleteAllByPrincipalExtId(principalExtId);
    }
}
