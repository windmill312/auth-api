package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.model.entity.SubsystemEntity;
import com.github.windmill312.auth.repository.SubsystemRepository;
import com.github.windmill312.auth.service.SubsystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SubsystemServiceImpl implements SubsystemService {
    private Logger log = LoggerFactory.getLogger(SubsystemServiceImpl.class);

    private final SubsystemRepository subsystemRepository;

    public SubsystemServiceImpl(SubsystemRepository subsystemRepository) {
        this.subsystemRepository = subsystemRepository;
    }


    @Override
    public SubsystemEntity getSubsystemByCode(Integer code) {
        return subsystemRepository.findByCode(code).orElseThrow(() -> {
            log.info("Not found subsystem with code: {}", code);
            return new AuthException("Not found subsystem with code: " + code);
        });
    }

    @Override
    public SubsystemEntity getSubsystemById(Integer id) {
        return subsystemRepository.findById(id).orElseThrow(() -> {
            log.info("Subsystem not found");
            return new AuthException("Subsystem not found");
        });
    }
}
