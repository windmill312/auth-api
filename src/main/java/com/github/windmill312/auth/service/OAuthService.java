package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.OAuthToken;

import java.util.UUID;

public interface OAuthService {

    String authorize(UUID clientUid, String userAccessToken);

    OAuthToken getToken(String clientIdentifier, String clientSecret, String authorizationCode);

    OAuthToken refreshToken(String clientIdentifier, String clientSecret, String refreshToken);
}
