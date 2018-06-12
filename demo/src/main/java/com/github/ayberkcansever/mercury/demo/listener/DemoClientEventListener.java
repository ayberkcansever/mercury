package com.github.ayberkcansever.mercury.demo.listener;

import com.github.ayberkcansever.mercury.client.event.ClientEvent;
import com.github.ayberkcansever.mercury.client.event.ClientEventListener;

public class DemoClientEventListener extends ClientEventListener {

    @Override
    protected void handle(ClientEvent event) {
        switch (event.getType()) {
            case IDENTIFIED:
                System.out.println("IDENTIFIED TempId: "
                        + event.getMercuryClient().getTempId()
                        + " RealId: " + event.getMercuryClient().getId());
                break;
        }
    }

}
