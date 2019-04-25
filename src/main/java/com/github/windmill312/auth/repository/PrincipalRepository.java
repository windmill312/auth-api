package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.PrincipalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipalRepository extends JpaRepository<PrincipalEntity, Integer> {
    Optional<PrincipalEntity> findByExtId(UUID extId);
    void deleteByExtIdAndSubsystemId(UUID extId, Integer subsystemId);
}
