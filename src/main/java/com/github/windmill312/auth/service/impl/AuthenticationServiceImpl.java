package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.model.Authentication;
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

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

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

    //todo create enum with subsystemCodes
    @Override
    public Authentication authenticateAny(PrincipalEntity principal) {
        if (principal.getSubsystemId() != 4) {
            Instant now = Instant.now();

            TokenEntity token = tokenService.getToken(principal.getExtId()).orElse(tokenService.generateToken());

            if (token.getValidTill().compareTo(now) < 0) {
                tokenService.revokeToken(token);
                token = tokenService.generateToken();
            } else {
                token.setLastAccess(Instant.now());
            }

            token.setPrincipalExtId(principal.getExtId());
            tokenService.saveToken(token);

            return new Authentication(token, principal);
        } else {
            throw new AuthException("Insufficient permissions");
        }

    }

    @Override
    public Authentication authenticateService(String serviceId, String serviceSecret) {
        PrincipalEntity principal = credentialsService.getPrincipal(serviceId, serviceSecret);

        return authenticateAny(principal);
    }

    @Override
    public Authentication getAuthentication(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new AuthException("Incorrect token");
        }

        TokenEntity tokenEntity = tokenService.getToken(token).orElseThrow(() -> {
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
        TokenEntity authTokenEntity = tokenService.getToken(token).orElseThrow(() -> {
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
