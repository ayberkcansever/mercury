package com.github.ayberkcansever.mercury.demo.io;

import com.github.ayberkcansever.mercury.Mercury;
import com.github.ayberkcansever.mercury.io.MercuryClient;

public class DemoClient extends MercuryClient {

    @Override
    protected void handleMessage(String message) {
        System.out.println(getId() + ": " + message);
        if(message.startsWith("id:")) {
            identify(message.split("id:")[1].trim());
        } else if (message.startsWith("send:")) {
            String to = message.split(":")[1];
            String msg = message.split(":")[2];
            Mercury.instance().routeMessage(to, message);
        }
    }

}
