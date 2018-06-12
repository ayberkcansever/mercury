package com.github.ayberkcansever.mercury.demo.listener;

import com.github.ayberkcansever.mercury.io.event.IOEvent;
import com.github.ayberkcansever.mercury.io.event.IOEventListener;

public class DemoIOEventListener extends IOEventListener {

    @Override
    protected void handle(IOEvent event) {
        switch (event.getType()) {
            case CLIENT_CONNECTED:
                System.out.println("CONNECTED: " + event.getMercuryClient().getTempId());
                break;
            case CLIENT_DISCONNECTED:
                System.out.println("DISCONNECTED: " + event.getMercuryClient().getTempId());
                break;
        }
    }

}
