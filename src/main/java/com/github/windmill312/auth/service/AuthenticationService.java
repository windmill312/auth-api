package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.Authentication;
import com.github.windmill312.auth.model.FullAuthentication;
import com.github.windmill312.auth.model.entity.PrincipalEntity;

public interface AuthenticationService {

    FullAuthentication authenticateAny(PrincipalEntity principal);

    FullAuthentication authenticateService(String serviceId, String serviceSecret);

    Authentication getAuthentication(String token);

    void revokeAuthentication(String token);
}
