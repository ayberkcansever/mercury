package com.github.ayberkcansever.mercury;

import com.github.ayberkcansever.mercury.event.MercuryEventBus;
import com.github.ayberkcansever.mercury.io.MercuryClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Mercury {

    private static Mercury INSTANCE;
    @Getter private static Class<MercuryClient> clientClazz;

    @Autowired
    @Getter private MercuryEventBus eventBus = new MercuryEventBus();

    public static Mercury init(Class clazz) {
        INSTANCE = new Mercury();
        clientClazz = clazz;
        SpringApplication.run(INSTANCE.getClass());
        return INSTANCE;
    }

    public static Mercury instance() {
        return INSTANCE;
    }

}