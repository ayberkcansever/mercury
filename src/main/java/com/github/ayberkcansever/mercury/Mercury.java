package com.github.ayberkcansever.mercury;

import com.github.ayberkcansever.mercury.cache.CacheHolder;
import com.github.ayberkcansever.mercury.client.MercuryClient;
import com.github.ayberkcansever.mercury.client.MercuryClientHolder;
import com.github.ayberkcansever.mercury.config.MercuryConfig;
import com.github.ayberkcansever.mercury.event.MercuryEventBus;
import com.github.ayberkcansever.mercury.grpc.client.ClientHolder;
import com.github.ayberkcansever.mercury.grpc.client.GRpcClient;
import com.github.ayberkcansever.mercury.grpc.server.GRpcServer;
import com.github.ayberkcansever.mercury.message.event.MessageEvent;
import com.github.ayberkcansever.mercury.message.event.MessageEventType;
import com.google.common.base.Strings;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class Mercury {

    private static Mercury INSTANCE;
    @Getter private static MercuryConfig mercuryConfig = new MercuryConfig();

    @Autowired
    @Getter private GRpcServer gRpcServer;

    @Autowired
    @Getter private CacheHolder cacheHolder;

    @Autowired
    @Getter private MercuryEventBus eventBus = new MercuryEventBus();

    @Autowired
    private ClientHolder clientHolder;

    public static void main(String[] args) {

    }

    public static Mercury init(MercuryConfig config) {
        mercuryConfig = config;
        ConfigurableApplicationContext ctx = SpringApplication.run(Mercury.class);
        INSTANCE = (Mercury) ctx.getBean("mercury");
        return INSTANCE;
    }

    public static Mercury instance() {
        return INSTANCE;
    }

    @Bean(name = "messageSenderTaskExecutor")
    public Executor messageSenderTaskExecutor() {
        ThreadPoolTaskExecutor executor = mercuryConfig.getMessageThreadPoolTaskExecutor();
        if(executor == null) {
            executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(2);
            executor.setMaxPoolSize(10);
            executor.setQueueCapacity(1000);
            executor.setThreadNamePrefix("MessageSender-");
        }
        executor.initialize();
        return executor;
    }

    @Async
    public void routeMessage(String from, String to, String message) {
        String presenceNode = cacheHolder.getPresenceCache().get(to);
        if(!Strings.isNullOrEmpty(presenceNode)) {
            // client connected to local node
            if(mercuryConfig.getServerUrl().equals(presenceNode)) {
                MercuryClient mercuryClient = MercuryClientHolder.getClient(to);
                if(mercuryClient != null) {
                    mercuryClient.send(message);
                    Mercury.instance().getEventBus().postEvent(new MessageEvent(from, to, message, MessageEventType.SENT));
                }
                // client not connected but present in the presence cache
                else {
                    Mercury.instance().getEventBus().postEvent(new MessageEvent(from, to, message, MessageEventType.NOT_SENT));
                }
            }
            // client connected to remote node
            else {
                GRpcClient gRpcClient = clientHolder.getNode(presenceNode);
                if(gRpcClient != null) {
                    String resp = gRpcClient.sendMessage(from, to, message);
                    if("OK".equalsIgnoreCase(resp)) {
                        Mercury.instance().getEventBus().postEvent(new MessageEvent(from, to, message, MessageEventType.SENT));
                    } else {
                        Mercury.instance().getEventBus().postEvent(new MessageEvent(from, to, message, MessageEventType.NOT_SENT));
                    }
                }
                // gRPC node not in the cluster
                else {
                    // todo:
                    Mercury.instance().getEventBus().postEvent(new MessageEvent(from, to, message, MessageEventType.NOT_SENT));
                }
            }
        } else {
            Mercury.instance().getEventBus().postEvent(new MessageEvent(from, to, message, MessageEventType.NOT_SENT));
        }
    }

}