package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.model.Authentication;
import com.github.windmill312.auth.model.FullAuthentication;
import com.github.windmill312.auth.model.Subsystem;
import com.github.windmill312.auth.model.TokenType;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.TokenEntity;
import com.github.windmill312.auth.service.AuthenticationService;
import com.github.windmill312.auth.service.CredentialsService;
import com.github.windmill312.auth.service.PrincipalService;
import com.github.windmill312.auth.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private static final long TOKEN_INACTION_MAX_SECONDS = 1800L;

    private final PrincipalService principalService;
    private final TokenService tokenService;
    private final CredentialsService credentialsService;

    public AuthenticationServiceImpl(
            PrincipalService principalService,
            TokenService tokenService,
            CredentialsService credentialsService) {
        this.principalService = principalService;
        this.tokenService = tokenService;
        this.credentialsService = credentialsService;
    }

    @Override
    public FullAuthentication authenticateAny(PrincipalEntity principal) {
        if (principal.getSubsystemId() != Subsystem.EXTERNAL_SERVICE.getId()) {
            Instant now = Instant.now();

            TokenEntity accessToken = tokenService.getTokenByPrincipalAndType(principal.getExtId(), TokenType.ACCESS)
                    .orElse(tokenService.generateToken());

            if (accessToken.getValidTill().compareTo(now) < 0) {
                tokenService.revokeToken(accessToken);
                accessToken = tokenService.generateToken();
            } else {
                accessToken.setLastAccess(Instant.now());
            }

            accessToken.setPrincipalExtId(principal.getExtId());
            accessToken.setTokenType(TokenType.ACCESS);
            tokenService.saveToken(accessToken);

            TokenEntity refreshToken = tokenService.getTokenByPrincipalAndType(principal.getExtId(), TokenType.REFRESH)
                    .orElse(
                        tokenService.generateToken()
                            .setValue(generateRefreshToken()));

            if (refreshToken.getValidTill().compareTo(now) < 0) {
                tokenService.revokeToken(refreshToken);
                refreshToken = new TokenEntity()
                        .setValue(generateRefreshToken());
            } else {
                refreshToken.setLastAccess(Instant.now());
            }

            refreshToken.setPrincipalExtId(principal.getExtId());
            refreshToken.setTokenType(TokenType.REFRESH);
            tokenService.saveToken(refreshToken);

            return new FullAuthentication(accessToken, refreshToken, principal);
        } else {
            throw new AuthException("Insufficient permissions");
        }

    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    @Override
    public FullAuthentication authenticateService(String serviceId, String serviceSecret) {
        PrincipalEntity principal = credentialsService.getPrincipal(serviceId, serviceSecret);

        return authenticateAny(principal);
    }

    @Override
    public Authentication getAuthentication(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new AuthException("Incorrect token");
        }

        TokenEntity tokenEntity = tokenService.getTokenByPrincipalAndType(token).orElseThrow(() -> {
            log.info("Incorrect token");
            return new AuthException("Incorrect token");
        });
        validateToken(tokenEntity);

        tokenEntity.setLastAccess(Instant.now());
        tokenService.saveToken(tokenEntity);

        PrincipalEntity principal = principalService.getPrincipalByExternalId(tokenEntity.getPrincipalExtId());

        return new Authentication(tokenEntity, principal);
    }

    @Override
    public void revokeAuthentication(String token) {
        TokenEntity authTokenEntity = tokenService.getTokenByPrincipalAndType(token).orElseThrow(() -> {
            log.info("Incorrect token");
            return new AuthException("Incorrect token");
        });
        tokenService.revokeToken(authTokenEntity);
    }

    private void validateToken(TokenEntity tokenEntity) {
        Instant now = Instant.now();

        if (tokenEntity.getValidFrom().compareTo(now) > 0) {
            throw new AuthException("AuthToken is not accessible yet");
        }

        if (tokenEntity.getValidTill().compareTo(now) < 0) {
            tokenService.revokeToken(tokenEntity);
            throw new AuthException("AuthToken expired");
        }

        if (now.getEpochSecond() - tokenEntity.getLastAccess().getEpochSecond() > TOKEN_INACTION_MAX_SECONDS) {
            tokenService.revokeToken(tokenEntity);
            throw new AuthException("Session expired");
        }
    }
}
