package com.github.ayberkcansever.mercury.demo.listener;

import com.github.ayberkcansever.mercury.message.event.MessageEvent;
import com.github.ayberkcansever.mercury.message.event.MessageEventListener;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoMessageEventListener extends MessageEventListener {

    private static AtomicInteger sentCount = new AtomicInteger(0);
    private static AtomicInteger notSentCount = new AtomicInteger(0);

    @Override
    protected void handle(MessageEvent event) {
        switch (event.getType()) {
            case SENT:
                //System.out.println("SENT: " + event.toString());
                System.out.println("sent " + sentCount.incrementAndGet());
                break;
            case NOT_SENT:
                //System.out.println("NOT SENT: " + event.toString());
                System.out.println("not sent " + notSentCount.incrementAndGet());
                break;
        }
    }

}
