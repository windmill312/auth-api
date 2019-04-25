package com.github.windmill312.auth.converter;

import com.github.windmill312.auth.grpc.model.v1.GAuthentication;
import com.github.windmill312.auth.grpc.model.v1.GCredentials;
import com.github.windmill312.auth.grpc.model.v1.GPrincipalOuterKey;
import com.github.windmill312.auth.grpc.model.v1.GToken;
import com.github.windmill312.auth.model.Authentication;
import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.model.entity.CredentialsEntity;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.model.entity.TokenEntity;
import com.github.windmill312.common.grpc.model.GUuid;

import java.time.Instant;
import java.util.UUID;

public class ModelConverter {

    public static GAuthentication convert(Authentication authentication) {
        return GAuthentication.newBuilder()
                .setToken(convert(authentication.getTokenEntity()))
                .setPrincipal(convert(convert(authentication.getPrincipal(), authentication.getPrincipal().getSubsystemId())))
                .build();
    }

    public static PrincipalOuterKey convert(PrincipalEntity entity, Integer code) {
        return new PrincipalOuterKey(
                code,
                entity.getExtId()
        );
    }

    public static PrincipalEntity convert(PrincipalOuterKey principal, Integer subsystemId) {
        return new PrincipalEntity()
                .setExtId(principal.getPrincipalExtId())
                .setSubsystemId(subsystemId);
    }

    public static String toToken(GAuthentication authentication) {
        return authentication.getToken().getToken();
    }

    public static GUuid convert(UUID uuid) {
        return GUuid.newBuilder()
                .setUuid(String.valueOf(uuid))
                .build();
    }

    public static GToken convert(TokenEntity token) {
        return GToken.newBuilder()
                .setToken(token.getValue())
                .setValidFromMills(token.getValidFrom().toEpochMilli())
                .setValidTillMills(token.getValidTill().toEpochMilli())
                .build();
    }

    public static TokenEntity convert(GToken token) {
        return new TokenEntity()
                .setValue(token.getToken())
                .setValidFrom(Instant.ofEpochMilli(token.getValidFromMills()))
                .setValidTill(Instant.ofEpochMilli(token.getValidTillMills()));
    }

    public static CredentialsEntity convert(GCredentials credentials) {
        return new CredentialsEntity();
    }

    public static GPrincipalOuterKey convert(PrincipalOuterKey principalKey) {
        return GPrincipalOuterKey.newBuilder()
                .setExtId(principalKey.getPrincipalExtId().toString())
                .setSubsystemCode(principalKey.getSubsystemCode().toString())
                .build();
    }

    public static PrincipalOuterKey convert(GPrincipalOuterKey principalKey) {
        return new PrincipalOuterKey(Integer.valueOf(principalKey.getSubsystemCode()), UUID.fromString(principalKey.getExtId()));
    }
}
