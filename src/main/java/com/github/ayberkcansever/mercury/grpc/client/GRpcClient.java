package com.github.ayberkcansever.mercury.grpc.client;

import com.github.ayberkcansever.mercury.grpc.Mercury;
import com.github.ayberkcansever.mercury.grpc.MessageServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;

public class GRpcClient {

    private MessageServiceGrpc.MessageServiceBlockingStub stub;
    @Getter private String serverUrl;

    public GRpcClient(String serverUrl) {
        this.serverUrl = serverUrl;
        ManagedChannel channel = ManagedChannelBuilder.forTarget(serverUrl)
                .usePlaintext(true)
                .build();
        stub = MessageServiceGrpc.newBlockingStub(channel);
    }

    public void sendMessage(String to, String message) {
        Mercury.MessageRequest messageRequest = Mercury.MessageRequest.newBuilder().setTo(to).setMessage(message).build();
        stub.send(messageRequest);
    }
}
