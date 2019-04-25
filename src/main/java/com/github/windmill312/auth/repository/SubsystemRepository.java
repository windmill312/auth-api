package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.SubsystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubsystemRepository extends JpaRepository<SubsystemEntity, Integer> {
    Optional<SubsystemEntity> findByCode(Integer code);
}
