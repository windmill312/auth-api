package com.github.windmill312.auth.grpc.service.v1.impl;

import com.github.windmill312.auth.converter.ModelConverter;
import com.github.windmill312.auth.grpc.model.v1.GAddPrincipalRequest;
import com.github.windmill312.auth.grpc.model.v1.GAddPrincipalResponse;
import com.github.windmill312.auth.grpc.model.v1.GDeletePrincipalRequest;
import com.github.windmill312.auth.grpc.service.v1.PrincipalServiceV1Grpc;
import com.github.windmill312.auth.model.PrincipalOuterKey;
import com.github.windmill312.auth.service.PrincipalService;
import com.github.windmill312.common.grpc.model.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@GRpcService
public class PrincipalServiceV1GrpcImpl extends PrincipalServiceV1Grpc.PrincipalServiceV1ImplBase {
    private final PrincipalService principalService;

    @Autowired
    public PrincipalServiceV1GrpcImpl(
            PrincipalService principalService) {
        this.principalService = principalService;
    }

    @Override
    public void addPrincipal(
            GAddPrincipalRequest request,
            StreamObserver<GAddPrincipalResponse> responseObserver) {

        PrincipalOuterKey principal = principalService.addPrincipal(
                UUID.fromString(request.getPrincipalExtId()),
                Integer.valueOf(request.getSubsystemCode()));

        GAddPrincipalResponse response = GAddPrincipalResponse.newBuilder()
                .setPrincipalKey(ModelConverter.convert(principal))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePrincipal(
            GDeletePrincipalRequest request,
            StreamObserver<Empty> responseObserver) {

        principalService.deletePrincipal(UUID.fromString(request.getPrincipalExtId()));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
