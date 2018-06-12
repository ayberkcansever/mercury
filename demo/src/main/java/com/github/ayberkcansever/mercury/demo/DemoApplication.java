package com.github.ayberkcansever.mercury.demo;

import com.github.ayberkcansever.mercury.Mercury;
import com.github.ayberkcansever.mercury.config.MercuryConfig;
import com.github.ayberkcansever.mercury.demo.io.DemoClient;
import com.github.ayberkcansever.mercury.demo.listener.DemoClientEventListener;
import com.github.ayberkcansever.mercury.demo.listener.DemoIOEventListener;
import com.github.ayberkcansever.mercury.demo.listener.DemoMessageEventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

// no need to annotate Spring Boot applications
public class DemoApplication {

	public static void main(String[] args) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(30);
		executor.setQueueCapacity(10000);
		executor.setThreadNamePrefix("MessageSenderThread-");

		MercuryConfig mercuryConfig = new MercuryConfig.MercuryConfigBuilder()
				.setGRpcIp("127.0.0.1")
				.setGRpcPort(6667)
				.setServerPort(5556)
				.setClientClass(DemoClient.class)
				.setMessageThreadPoolTaskExecutor(executor)
				.build();
		Mercury mercury = new Mercury().init(mercuryConfig);

		mercury.getEventBus().register(new DemoIOEventListener());
		mercury.getEventBus().register(new DemoClientEventListener());
		mercury.getEventBus().register(new DemoMessageEventListener());
	}

}
