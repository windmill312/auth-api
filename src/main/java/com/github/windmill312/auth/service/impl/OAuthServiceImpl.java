package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.model.Authentication;
import com.github.windmill312.auth.model.FullAuthentication;
import com.github.windmill312.auth.model.OAuthToken;
import com.github.windmill312.auth.model.Subsystem;
import com.github.windmill312.auth.model.entity.OAuthCode;
import com.github.windmill312.auth.model.entity.OAuthRefreshToken;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.TokenEntity;
import com.github.windmill312.auth.repository.OAuthCodeRepository;
import com.github.windmill312.auth.repository.OAuthRefreshTokenRepository;
import com.github.windmill312.auth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class OAuthServiceImpl implements OAuthService {
    Logger log = LoggerFactory.getLogger(OAuthServiceImpl.class);

    private static final long DEFAULT_CODE_TTL_DAYS = 1;
    private static final long DEFAULT_REFRESH_TOKEN_TTL_DAYS = 365;

    private final OAuthCodeRepository oAuthCodeRepository;
    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;
    private final TokenService tokenService;
    private final CredentialsService credentialsService;
    private final AuthenticationService authenticationService;
    private final PrincipalService principalService;

    @Autowired
    public OAuthServiceImpl(
            OAuthCodeRepository oAuthCodeRepository,
            OAuthRefreshTokenRepository oAuthRefreshTokenRepository,
            TokenService tokenService,
            CredentialsService credentialsService,
            AuthenticationService authenticationService,
            PrincipalService principalService) {

        this.oAuthCodeRepository = oAuthCodeRepository;
        this.oAuthRefreshTokenRepository = oAuthRefreshTokenRepository;
        this.tokenService = tokenService;
        this.credentialsService = credentialsService;
        this.authenticationService = authenticationService;
        this.principalService = principalService;
    }

    @Override
    public String authorize(String clientId, String userAccessToken) {

        if (principalService.getPrincipalByExternalId(UUID.fromString(clientId)).getSubsystemId() != Subsystem.EXTERNAL_SERVICE.getId())
            throw new AuthException("Client has no oauth permissions");

        OAuthCode oAuthCode = new OAuthCode()
                .setExpiresIn(Instant.now().plusSeconds(getDefaultCodeTtlSeconds()))
                .setClientId(clientId)
                .setUserAccessToken(userAccessToken);

        oAuthCodeRepository.save(oAuthCode);

        return oAuthCode.getCode();
    }

    @Override
    public OAuthToken getToken(String clientId, String clientSecret, String authorizationCode) {
        credentialsService.getPrincipal(clientId, clientSecret);

        OAuthCode code = oAuthCodeRepository.findByClientIdAndCode(clientId, authorizationCode)
                .orElseThrow(() -> new AuthException("Wrong authorization code"));

        if (code.getExpiresIn().compareTo(Instant.now()) < 0) {
            throw new AuthException("Authorization code expired");
        }

        TokenEntity token = tokenService.getTokenByPrincipalAndType(code.getUserAccessToken()).orElseThrow(() -> {
            log.info("Incorrect token");
            return new AuthException("Incorrect token");
        });

        OAuthRefreshToken refreshToken = generateRefreshToken(clientId, token.getPrincipalExtId());
        oAuthRefreshTokenRepository.save(refreshToken);

        oAuthCodeRepository.delete(code);

        return new OAuthToken()
                .setAccessToken(token.getValue())
                .setTokenType("Bearer")
                .setExpiresIn(token.getValidTill())
                .setRefreshToken(refreshToken.getRefreshToken());
    }

    @Transactional
    @Override
    public OAuthToken refreshToken(String clientId, String clientSecret, String refreshToken) {
        credentialsService.getPrincipal(clientId, clientSecret);

        OAuthRefreshToken oAuthRefreshToken = oAuthRefreshTokenRepository.findByClientIdAndRefreshToken(clientId, refreshToken)
                .orElseThrow(() -> new AuthException("Wrong refresh token"));

        if (oAuthRefreshToken.getExpiresIn().compareTo(Instant.now()) < 0) {
            throw new AuthException("Refresh token expired");
        }

        PrincipalEntity principal = principalService.getPrincipalByExternalId(oAuthRefreshToken.getPrincipalId());

        FullAuthentication authentication = authenticationService.authenticateAny(principal);

        oAuthRefreshTokenRepository.deleteByPrincipalIdAndClientId(principal.getExtId(), clientId);

        OAuthRefreshToken newRefreshToken = generateRefreshToken(clientId, principal.getExtId());
        oAuthRefreshTokenRepository.save(newRefreshToken);

        return new OAuthToken()
                .setAccessToken(authentication.getAccessToken().getValue())
                .setTokenType("Bearer")
                .setExpiresIn(authentication.getAccessToken().getValidTill())
                .setRefreshToken(newRefreshToken.getRefreshToken());
    }

    private OAuthRefreshToken generateRefreshToken(String clientId, UUID principalId) {
        return new OAuthRefreshToken()
                .setClientId(clientId)
                .setPrincipalId(principalId)
                .setRefreshToken(generateRefreshToken())
                .setExpiresIn(Instant.now().plusSeconds(getDefaultRefreshTokenTtlSeconds()));
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    private long getDefaultCodeTtlSeconds() {
        return Duration.ofDays(DEFAULT_CODE_TTL_DAYS).getSeconds();
    }

    private long getDefaultRefreshTokenTtlSeconds() {
        return Duration.ofDays(DEFAULT_REFRESH_TOKEN_TTL_DAYS).getSeconds();
    }
}
