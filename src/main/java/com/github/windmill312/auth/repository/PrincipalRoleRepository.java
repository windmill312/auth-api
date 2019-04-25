package com.github.windmill312.auth.repository;

import com.github.windmill312.auth.model.entity.PrincipalRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRoleRepository extends JpaRepository<PrincipalRoleEntity, Integer> {

}
