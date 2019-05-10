package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.OAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OAuthCodeRepository extends JpaRepository<OAuthCode, Integer> {

    Optional<OAuthCode> findByClientIdAndCode(UUID clientId, String code);

}
