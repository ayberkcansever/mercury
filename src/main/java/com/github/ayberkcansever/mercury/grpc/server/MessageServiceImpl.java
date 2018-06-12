package com.github.ayberkcansever.mercury.grpc.server;

import com.github.ayberkcansever.mercury.client.MercuryClient;
import com.github.ayberkcansever.mercury.client.MercuryClientHolder;
import com.github.ayberkcansever.mercury.grpc.Mercury;
import com.github.ayberkcansever.mercury.grpc.MessageServiceGrpc;

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {

    @Override
    public void send(Mercury.MessageRequest request,
                     io.grpc.stub.StreamObserver<Mercury.MessageResponse> responseObserver) {
        System.out.println(request);
        Mercury.MessageResponse response;
        String to = request.getTo();
        MercuryClient mercuryClient = MercuryClientHolder.getClient(to);
        // send message to connected client
        if(mercuryClient != null) {
            mercuryClient.send(request.getMessage());
            response = Mercury.MessageResponse.newBuilder()
                    .setResp("OK")
                    .build();
        }
        // client not connected but present in the cache
        else {
            // todo:
            response = Mercury.MessageResponse.newBuilder()
                    .setResp("NOK")
                    .build();
        }

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();

    }
}
