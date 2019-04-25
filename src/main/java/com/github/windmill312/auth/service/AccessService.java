package com.github.windmill312.auth.service;

public interface AccessService {

    void checkAccess(String token, String grant);
}
