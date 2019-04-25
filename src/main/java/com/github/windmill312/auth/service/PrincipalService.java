package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.model.entity.PrincipalEntity;

import java.util.UUID;

public interface PrincipalService {
    PrincipalEntity getPrincipalById(Integer id);

    PrincipalEntity getPrincipalByExternalId(UUID externalId);

    PrincipalOuterKey addPrincipal(UUID externalId, Integer subsystemCode);

    void deletePrincipal(UUID externalId);
}
