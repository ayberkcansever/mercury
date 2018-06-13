package com.github.ayberkcansever.mercury.message.event;

import com.github.ayberkcansever.mercury.event.EventType;

public enum MessageEventType implements EventType {

    SENT("sent"),
    NOT_SENT("not_sent");

    private String type;

    MessageEventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
