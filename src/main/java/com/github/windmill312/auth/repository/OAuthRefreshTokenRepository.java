package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.OAuthRefreshToken;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OAuthRefreshTokenRepository extends JpaRepository<OAuthRefreshToken, Integer> {

    Optional<OAuthRefreshToken> findByClientIdAndRefreshToken(String clientId, String refreshToken);

    void deleteByPrincipalIdAndClientId(UUID principal, String clientId);
}
