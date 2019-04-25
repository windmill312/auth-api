package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByValue(String value);

    Optional<TokenEntity> findByPrincipalExtId(UUID principalExtId);
}
