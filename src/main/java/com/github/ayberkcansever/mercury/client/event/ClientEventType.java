package com.github.ayberkcansever.mercury.client.event;

import com.github.ayberkcansever.mercury.event.EventType;

public enum ClientEventType implements EventType {

    IDENTIFIED("identified");

    private String type;

    ClientEventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
