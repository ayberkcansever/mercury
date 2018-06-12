package com.github.ayberkcansever.mercury.message.event;

import com.github.ayberkcansever.mercury.event.Event;
import lombok.Getter;
import lombok.Setter;

public class MessageEvent extends Event {

    @Getter @Setter private String from;
    @Getter @Setter private String to;
    @Getter @Setter private String message;
    @Getter @Setter private MessageEventType type;

    public MessageEvent(String from, String to, String message, MessageEventType type) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                '}';
    }
}
