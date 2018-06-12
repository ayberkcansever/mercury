package com.github.ayberkcansever.mercury.demo.io;

import com.github.ayberkcansever.mercury.client.MercuryClient;

public class DemoClient extends MercuryClient {

    @Override
    protected void handleMessage(String message) {
        if(message.startsWith("id:")) {
            identify(message.split("id:")[1].trim());
        } else if (message.startsWith("send:")) {
            String to = message.split(":")[1];
            String msg = message.split(":")[2];
            this.route(to, msg);
        }
    }

}
