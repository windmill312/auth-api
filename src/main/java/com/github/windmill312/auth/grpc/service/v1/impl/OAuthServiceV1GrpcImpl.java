package com.github.windmill312.auth.grpc.service.v1.impl;

import com.github.windmill312.auth.converter.ModelConverter;
import com.github.windmill312.auth.grpc.model.v1.*;
import com.github.windmill312.auth.grpc.service.v1.OAuthServiceV1Grpc;
import com.github.windmill312.auth.model.OAuthToken;
import com.github.windmill312.auth.security.Grants;
import com.github.windmill312.auth.service.AccessService;
import com.github.windmill312.auth.service.OAuthService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class OAuthServiceV1GrpcImpl extends OAuthServiceV1Grpc.OAuthServiceV1ImplBase {

    private final AccessService accessService;
    private final OAuthService oAuthService;

    @Autowired
    public OAuthServiceV1GrpcImpl(
            AccessService accessService,
            OAuthService oAuthService) {

        this.accessService = accessService;
        this.oAuthService = oAuthService;
    }

    @Override
    public void authorize(
            GOAuthAuthorizeRequest request,
            StreamObserver<GOAuthAuthorizeResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.OAUTH_GRANT.getValue());

        String authorizationCode = oAuthService.authorize(request.getClientId(), request.getUserAccessToken());

        GOAuthAuthorizeResponse response = GOAuthAuthorizeResponse.newBuilder()
                .setAuthorizationCode(authorizationCode)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getToken(
            GOAuthTokenRequest request,
            StreamObserver<GOAuthTokenResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.OAUTH_GRANT.getValue());

        OAuthToken token = oAuthService.getToken(
                request.getClientId(),
                request.getClientSecret(),
                request.getAuthorizationCode());

        GOAuthTokenResponse response = GOAuthTokenResponse.newBuilder()
                .setAccessToken(token.getAccessToken())
                .setTokenType(token.getTokenType())
                .setExpiresIn(token.getExpiresIn().toEpochMilli())
                .setRefreshToken(token.getRefreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void refreshToken(
            GOAuthRefreshTokenRequest request,
            StreamObserver<GOAuthRefreshTokenResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.OAUTH_GRANT.getValue());

        OAuthToken token = oAuthService.refreshToken(
                request.getClientId(),
                request.getClientSecret(),
                request.getRefreshToken());

        GOAuthRefreshTokenResponse response = GOAuthRefreshTokenResponse.newBuilder()
                .setAccessToken(token.getAccessToken())
                .setTokenType(token.getTokenType())
                .setExpiresIn(token.getExpiresIn().toEpochMilli())
                .setRefreshToken(token.getRefreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
