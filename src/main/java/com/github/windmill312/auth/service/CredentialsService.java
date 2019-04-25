package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.model.entity.PrincipalEntity;

public interface CredentialsService {
    PrincipalEntity getPrincipal(String login, String secret);

    void addCredentials(PrincipalOuterKey principal, String login, String secret);
}
