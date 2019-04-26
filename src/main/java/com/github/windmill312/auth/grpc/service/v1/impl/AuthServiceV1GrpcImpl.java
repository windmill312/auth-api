package com.github.windmill312.auth.grpc.service.v1.impl;

import com.github.windmill312.auth.converter.ModelConverter;
import com.github.windmill312.auth.grpc.model.v1.GAuthenticateAnyRequest;
import com.github.windmill312.auth.grpc.model.v1.GAuthenticateAnyResponse;
import com.github.windmill312.auth.grpc.model.v1.GAuthenticateServiceRequest;
import com.github.windmill312.auth.grpc.model.v1.GAuthenticateServiceResponse;
import com.github.windmill312.auth.grpc.model.v1.GGenerateTokenRequest;
import com.github.windmill312.auth.grpc.model.v1.GGenerateTokenResponse;
import com.github.windmill312.auth.grpc.model.v1.GGetAuthenticationRequest;
import com.github.windmill312.auth.grpc.model.v1.GGetAuthenticationResponse;
import com.github.windmill312.auth.grpc.model.v1.GRevokeAuthenticationRequest;
import com.github.windmill312.auth.grpc.service.v1.AuthServiceV1Grpc;
import com.github.windmill312.auth.model.Authentication;
import com.github.windmill312.auth.model.FullAuthentication;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.security.Grants;
import com.github.windmill312.auth.service.AccessService;
import com.github.windmill312.auth.service.AuthenticationService;
import com.github.windmill312.auth.service.PrincipalService;
import com.github.windmill312.auth.service.TokenService;
import com.github.windmill312.common.grpc.model.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@GRpcService
public class AuthServiceV1GrpcImpl extends AuthServiceV1Grpc.AuthServiceV1ImplBase {

    private final AccessService accessService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final PrincipalService principalService;

    @Autowired
    public AuthServiceV1GrpcImpl(
            AccessService accessService,
            AuthenticationService authenticationService,
            TokenService tokenService, PrincipalService principalService) {

        this.accessService = accessService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.principalService = principalService;
    }

    @Override
    public void authenticateAny(
            GAuthenticateAnyRequest request,
            StreamObserver<GAuthenticateAnyResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.AUTHORIZATION_GRANT.getValue());

        PrincipalEntity entity = principalService.getPrincipalByExternalId(UUID.fromString(request.getPrincipalKey().getExtId()));

        FullAuthentication authentication = authenticationService.authenticateAny(entity);

        GAuthenticateAnyResponse response = GAuthenticateAnyResponse.newBuilder()
                .setAuthentication(ModelConverter.convert(authentication))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void authenticateService(
            GAuthenticateServiceRequest request,
            StreamObserver<GAuthenticateServiceResponse> responseObserver) {

        FullAuthentication authentication = authenticationService.authenticateService(
                request.getServiceId(),
                request.getServiceSecret());

        GAuthenticateServiceResponse response = GAuthenticateServiceResponse.newBuilder()
                .setAuthentication(ModelConverter.convert(authentication))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAuthentication(
            GGetAuthenticationRequest request,
            StreamObserver<GGetAuthenticationResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.AUTHORIZATION_GRANT.getValue());

        Authentication authentication = authenticationService.getAuthentication(request.getToken());

        GGetAuthenticationResponse response = GGetAuthenticationResponse.newBuilder()
                .setAuthentication(ModelConverter.convert(authentication))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void revokeAuthentication(
            GRevokeAuthenticationRequest request,
            StreamObserver<Empty> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.AUTHORIZATION_GRANT.getValue());

        authenticationService.revokeAuthentication(request.getToken());

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void generateToken(
            GGenerateTokenRequest request,
            StreamObserver<GGenerateTokenResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.AUTHORIZATION_GRANT.getValue());

        GGenerateTokenResponse response = GGenerateTokenResponse.newBuilder()
                .setToken(ModelConverter.convert(tokenService.generateToken()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
