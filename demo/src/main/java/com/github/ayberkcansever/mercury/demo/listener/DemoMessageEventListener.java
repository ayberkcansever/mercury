package com.github.ayberkcansever.mercury.demo.listener;

import com.github.ayberkcansever.mercury.message.event.MessageEvent;
import com.github.ayberkcansever.mercury.message.event.MessageEventListener;

public class DemoMessageEventListener extends MessageEventListener {

    @Override
    protected void handle(MessageEvent event) {
        switch (event.getType()) {
            case SENT:
                System.out.println("SENT: " + event.toString());
                break;
            case NOT_SENT:
                System.out.println("NOT SENT: " + event.toString());
                break;
        }
    }

}
