package com.github.ayberkcansever.mercury.io.event;

import com.github.ayberkcansever.mercury.client.MercuryClient;
import com.github.ayberkcansever.mercury.event.Event;
import lombok.Data;

@Data
public class IOEvent extends Event {

    private IOEventType type;
    private MercuryClient mercuryClient;

    public IOEvent(MercuryClient mercuryClient, IOEventType type) {
        this.type = type;
        this.mercuryClient = mercuryClient;
    }
}
