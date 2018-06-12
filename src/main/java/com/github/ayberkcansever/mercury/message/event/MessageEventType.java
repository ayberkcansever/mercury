package com.github.ayberkcansever.mercury.message.event;

public enum MessageEventType {

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
