package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.exception.DuplicateException;
import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.model.entity.CredentialsEntity;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.SubsystemEntity;
import com.github.windmill312.auth.repository.CredentialsRepository;
import com.github.windmill312.auth.repository.SubsystemRepository;
import com.github.windmill312.auth.service.CredentialsService;
import com.github.windmill312.auth.service.PrincipalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredentialsServiceImpl implements CredentialsService {

    Logger log = LoggerFactory.getLogger(CredentialsServiceImpl.class);

    private final CredentialsRepository credentialsRepository;
    private final SubsystemRepository subsystemRepository;
    private final PrincipalService principalService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CredentialsServiceImpl(
            CredentialsRepository credentialsRepository,
            SubsystemRepository subsystemRepository,
            PrincipalService principalService,
            PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.subsystemRepository = subsystemRepository;
        this.principalService = principalService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PrincipalEntity getPrincipal(String identifier, String secret) {
        return credentialsRepository.findByIdentifier(identifier)
                .filter(c -> passwordEncoder.matches(secret, c.getSecret()))
                .map(c -> principalService.getPrincipalById(c.getPrincipal().getId()))
                .orElseThrow(() -> new AuthException("Invalid credentials"));
    }

    @Override
    public void addCredentials(PrincipalOuterKey principalKey, String identifier, String secret) {
        if (!credentialsRepository.existsByIdentifier(identifier)) {
            SubsystemEntity subsystem = subsystemRepository.findByCode(principalKey.getSubsystemCode()).orElseThrow(() -> {
                log.warn("Subsystem not found with code: {}", principalKey.getSubsystemCode());
                return new AuthException("Subsystem not found with code: " + principalKey.getSubsystemCode());
            });

            PrincipalEntity principal = principalService.getPrincipalByExternalId(principalKey.getPrincipalExtId());

            credentialsRepository.save(new CredentialsEntity()
                    .setPrincipal(principal)
                    .setSubsystem(subsystem)
                    .setIdentifier(identifier)
                    .setSecret(passwordEncoder.encode(secret)));
        } else
            throw new DuplicateException("Such identifier is already exists");
    }

    @Override
    public CredentialsEntity getCredentials(PrincipalEntity principal) {
        return credentialsRepository.findByPrincipalExtId(principal.getExtId()).orElseThrow( () -> {
            log.warn("Credentials not found by principal with uid: {}", principal.getExtId());
            return new AuthException("Credentials not found by principal with uid: {} " + principal.getExtId());
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
