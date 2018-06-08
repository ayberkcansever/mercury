package com.github.ayberkcansever.mercury.grpc.server;

import com.github.ayberkcansever.mercury.grpc.Mercury;
import com.github.ayberkcansever.mercury.grpc.MessageServiceGrpc;

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {

    @Override
    public void send(Mercury.MessageRequest request,
                     io.grpc.stub.StreamObserver<Mercury.MessageResponse> responseObserver) {
        System.out.println(request);

        // You must use a builder to construct a new Protobuffer object
        Mercury.MessageResponse response = Mercury.MessageResponse.newBuilder()
                .setResp("OK")
                .build();

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();

    }
}
