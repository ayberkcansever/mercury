package com.github.ayberkcansever.mercury.config;

import com.github.ayberkcansever.mercury.io.MercuryClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MercuryConfig {

    @Getter @Setter private String gRpcIp = "127.0.0.1";
    @Getter @Setter private int gRpcPort = 7777;
    @Getter @Setter private Class<? extends MercuryClient> clientClass;
    @Getter @Setter private int serverPort = 5555;
    @Getter @Setter private ThreadPoolTaskExecutor messageThreadPoolTaskExecutor;

    public String getServerUrl() {
         return gRpcIp.concat(":").concat(String.valueOf(gRpcPort));
    }

    public static class MercuryConfigBuilder {

        @Getter @Setter private MercuryConfig mercuryConfig = new MercuryConfig();

        public MercuryConfig build() {
            return mercuryConfig;
        }

        public MercuryConfigBuilder setGRpcIp(String gRpcIp) {
            mercuryConfig.setGRpcIp(gRpcIp);
            return this;
        }

        public MercuryConfigBuilder setGRpcPort(int gRpcPort) {
            mercuryConfig.setGRpcPort(gRpcPort);
            return this;
        }

        public MercuryConfigBuilder setClientClass(Class<? extends MercuryClient> clientClass) {
            mercuryConfig.setClientClass(clientClass);
            return this;
        }

        public MercuryConfigBuilder setServerPort(int serverPort) {
            mercuryConfig.setServerPort(serverPort);
            return this;
        }

        public MercuryConfigBuilder setMessageThreadPoolTaskExecutor(ThreadPoolTaskExecutor executor) {
            mercuryConfig.setMessageThreadPoolTaskExecutor(executor);
            return this;
        }

    }

}
