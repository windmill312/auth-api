package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.GrantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrantRepository extends JpaRepository<GrantEntity, Integer> {
    List<GrantEntity> findAllByPrincipalId (Integer principalId);

    void deleteAllByPrincipalId(Integer principalId);
}
