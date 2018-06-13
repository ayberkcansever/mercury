package com.github.ayberkcansever.mercury.io.event;

import com.github.ayberkcansever.mercury.event.EventType;

public enum IOEventType implements EventType {

    CLIENT_CONNECTED("connected"),
    CLIENT_DISCONNECTED("disconnected");

    private String type;

    IOEventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
