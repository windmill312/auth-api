package com.github.windmill312.auth.service;

import com.github.windmill312.auth.model.entity.SubsystemEntity;

public interface SubsystemService {
    SubsystemEntity getSubsystemByCode(Integer code);

    SubsystemEntity getSubsystemById(Integer id);
}
