package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.OAuthToken;

public interface OAuthService {

    String authorize(String clientId, String userAccessToken);

    OAuthToken getToken(String clientId, String clientSecret, String authorizationCode);

    OAuthToken refreshToken(String clientId, String clientSecret, String refreshToken);
}
