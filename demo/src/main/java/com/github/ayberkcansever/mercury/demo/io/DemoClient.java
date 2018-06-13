package com.github.ayberkcansever.mercury.demo.io;

import com.github.ayberkcansever.mercury.client.MercuryClient;
import com.github.ayberkcansever.mercury.demo.listener.DemoIOEventListener;

public class DemoClient extends MercuryClient {

    private DemoIOEventListener d = new DemoIOEventListener();

    @Override
    protected void handleMessage(String message) {
        if(message.startsWith("id:")) {
            identify(message.split("id:")[1].trim());
        } else if (message.startsWith("send:")) {
            String to = message.split(":")[1];
            String msg = message.split(":")[2];
            route(to, msg);
        }
    }

}
