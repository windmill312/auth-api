package com.github.windmill312.auth.grpc.service.v1.impl;

import com.github.windmill312.auth.converter.ModelConverter;
import com.github.windmill312.auth.grpc.model.v1.GAddCredentialsRequest;
import com.github.windmill312.auth.grpc.model.v1.GGetPrincipalKeyResponse;
import com.github.windmill312.auth.grpc.model.v1.GGetPrincipalOuterKeyRequest;
import com.github.windmill312.auth.grpc.service.v1.CredentialsServiceV1Grpc;
import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.model.entity.PrincipalEntity;
import com.github.windmill312.auth.security.Grants;
import com.github.windmill312.auth.service.AccessService;
import com.github.windmill312.auth.service.CredentialsService;
import com.github.windmill312.auth.service.SubsystemService;
import com.github.windmill312.common.grpc.model.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GRpcService
@Transactional
public class CredentialsServiceV1GrpcImpl extends CredentialsServiceV1Grpc.CredentialsServiceV1ImplBase {

    private final AccessService accessService;
    private final CredentialsService credentialsService;
    private final SubsystemService subsystemService;

    @Autowired
    public CredentialsServiceV1GrpcImpl(
            AccessService accessService,
            CredentialsService credentialsService,
            SubsystemService subsystemService) {
        this.accessService = accessService;
        this.credentialsService = credentialsService;
        this.subsystemService = subsystemService;
    }

    @Override
    public void getPrincipalKey(
            GGetPrincipalOuterKeyRequest request,
            StreamObserver<GGetPrincipalKeyResponse> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.AUTHORIZATION_GRANT.getValue());

        PrincipalEntity entity = credentialsService.getPrincipal(
                request.getCredentials().getLogin().getValue(),
                request.getCredentials().getSecret().getValue());

        Integer subsystemCode = subsystemService.getSubsystemById(entity.getSubsystemId()).getCode();

        PrincipalOuterKey principalKey = ModelConverter.convert(entity, subsystemCode);

        GGetPrincipalKeyResponse response = GGetPrincipalKeyResponse.newBuilder()
                .setPrincipalKey(ModelConverter.convert(principalKey))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addCredentials(
            GAddCredentialsRequest request,
            StreamObserver<Empty> responseObserver) {

        accessService.checkAccess(
                ModelConverter.toToken(request.getAuthentication()),
                Grants.AUTHORIZATION_GRANT.getValue());
        credentialsService.addCredentials(
                ModelConverter.convert(request.getPrincipalKey()),
                request.getCredentials().getLogin().getValue(),
                request.getCredentials().getSecret().getValue());

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
