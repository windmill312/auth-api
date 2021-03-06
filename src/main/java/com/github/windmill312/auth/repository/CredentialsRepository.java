package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.CredentialsEntity;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Integer> {
    Optional<CredentialsEntity> findByIdentifier(String identifier);

    Optional<CredentialsEntity> findByPrincipalExtId(UUID principalExtId);

    boolean existsByIdentifierAndSecret(String identifier, String secret);

    boolean existsByIdentifier(String identifier);

    void deleteByPrincipal(PrincipalEntity principal);
}
