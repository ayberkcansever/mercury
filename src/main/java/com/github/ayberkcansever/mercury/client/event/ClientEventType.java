package com.github.ayberkcansever.mercury.client.event;

public enum ClientEventType {

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
