package com.github.windmill312.auth.service.impl;

import com.github.windmill312.auth.exception.AuthException;
import com.github.windmill312.auth.model.Authentication;
import com.github.windmill312.auth.repository.GrantRepository;
import com.github.windmill312.auth.service.AccessService;
import com.github.windmill312.auth.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessServiceImpl implements AccessService {

    private final GrantRepository grantRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public AccessServiceImpl(
            GrantRepository grantRepository,
            AuthenticationService authenticationService) {

        this.grantRepository = grantRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void checkAccess(String token, String grant) {
        Authentication authentication = authenticationService.getAuthentication(token);

        grantRepository.findAllByPrincipalId(authentication.getPrincipal().getId())
                .stream()
                .filter(a -> grant.equals(a.getValue()))
                .findFirst()
                .orElseThrow(() -> new AuthException("Insufficient permissions"));
    }
}
