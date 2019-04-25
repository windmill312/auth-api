package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.converter.ModelConverter;
import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.model.entity.GrantEntity;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.repository.CredentialsRepository;
import com.github.windmill312.auth.repository.GrantRepository;
import com.github.windmill312.auth.repository.PrincipalRepository;
import com.github.windmill312.auth.repository.SubsystemRepository;
import com.github.windmill312.auth.security.Grants;
import com.github.windmill312.auth.service.CredentialsService;
import com.github.windmill312.auth.service.PrincipalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class PrincipalServiceImpl implements PrincipalService {

    private final Logger logger = LoggerFactory.getLogger(PrincipalServiceImpl.class);

    private final PrincipalRepository principalRepository;
    private final SubsystemRepository subsystemRepository;
    private final GrantRepository grantRepository;
    private final CredentialsRepository credentialsRepository;

    @Autowired
    public PrincipalServiceImpl(
            PrincipalRepository principalRepository,
            SubsystemRepository subsystemRepository,
            GrantRepository grantRepository,
            CredentialsRepository credentialsRepository) {
        this.principalRepository = principalRepository;
        this.subsystemRepository = subsystemRepository;
        this.grantRepository = grantRepository;
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public PrincipalEntity getPrincipalById(Integer id) {
        return principalRepository.findById(id).orElseThrow(() -> {
            logger.info("Not found principal");
            return new AuthException("Not found principal");
        });
    }

    @Override
    public PrincipalEntity getPrincipalByExternalId(UUID externalId) {
        return principalRepository.findByExtId(externalId).orElseThrow(() -> {
            logger.info("Not found principal");
            return new AuthException("Not found principal");
        });
    }

    @Override
    public PrincipalOuterKey addPrincipal(UUID externalId, Integer subsystemCode) {
        Integer subsystemId = subsystemRepository.findByCode(subsystemCode).orElseThrow(() -> {
            logger.info("Not found subsystem");
            return new AuthException("Not found subsystem");
        }).getId();

        PrincipalEntity principal = new PrincipalEntity().setExtId(externalId).setSubsystemId(subsystemId);

        principalRepository.save(principal);

        //TODO refactor
        Grants grant;
        if (subsystemCode.equals(40)) {
            grant = Grants.OAUTH_GRANT;
        }
        else
            grant = Grants.AUTHORIZATION_GRANT;

        grantRepository.save(new GrantEntity()
                .setValue(grant.getValue())
                .setPrincipalId(principal.getId()));

        return ModelConverter.convert(principal, subsystemCode);
    }

    @Override
    public void deletePrincipal(UUID externalId) {
        PrincipalEntity principal = principalRepository.findByExtId(externalId).orElseThrow(() -> {
            logger.info("Not found principal");
            return new AuthException("Not found principal");
        });

        grantRepository.deleteAllByPrincipalId(principal.getId());
        credentialsRepository.deleteByPrincipal(principal);
        principalRepository.deleteByExtIdAndSubsystemId(externalId, principal.getSubsystemId());
    }
}
