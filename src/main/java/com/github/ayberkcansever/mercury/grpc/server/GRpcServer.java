package com.github.ayberkcansever.mercury.grpc.server;

import com.github.ayberkcansever.mercury.cache.CacheHolder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GRpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(GRpcServer.class);

    public static final String GRPC_IP_USER_ATTRIBUTE = "grpcIp";
    public static final String GRPC_PORT_USER_ATTRIBUTE = "grpcPort";

    @Getter private String localGRpcIp;
    @Getter private int localGRpcPort;
    @Getter private String localGRpcServerUrl;

    @Autowired
    private CacheHolder cacheHolder;

    public void init() {
        localGRpcIp = cacheHolder.getIgniteConfigurationUserAttribute(GRPC_IP_USER_ATTRIBUTE).toString();
        localGRpcPort = Integer.parseInt(cacheHolder.getIgniteConfigurationUserAttribute(GRPC_PORT_USER_ATTRIBUTE).toString());
        localGRpcServerUrl = localGRpcIp.concat(":").concat(String.valueOf(localGRpcPort));

        new Thread(() -> {
            try {
                Server server = ServerBuilder.forPort(localGRpcPort)
                        .addService(new MessageServiceImpl())
                        .build();
                server.start();
                LOG.info("gRPC server started at port: " + localGRpcPort);
                server.awaitTermination();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
