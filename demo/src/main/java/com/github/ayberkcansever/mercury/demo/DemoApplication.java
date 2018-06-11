package com.github.ayberkcansever.mercury.demo;

import com.github.ayberkcansever.mercury.Mercury;
import com.github.ayberkcansever.mercury.config.MercuryConfig;
import com.github.ayberkcansever.mercury.demo.io.DemoClient;
import com.github.ayberkcansever.mercury.demo.listener.DemoIOEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(200);
		executor.setQueueCapacity(10000);
		executor.setThreadNamePrefix("MessageSender-");

		MercuryConfig mercuryConfig = new MercuryConfig.MercuryConfigBuilder()
				.setGRpcIp("localhost")
				.setGRpcPort(6667)
				.setServerPort(5556)
				.setClientClass(DemoClient.class)
				.setMessageThreadPoolTaskExecutor(executor)
				.build();
		Mercury mercury = new Mercury().init(mercuryConfig);
		mercury.getEventBus().register(new DemoIOEventListener());

		//SpringApplication.run(DemoApplication.class);
	}

}
