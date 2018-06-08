package com.github.ayberkcansever.mercury.io.event;

import com.github.ayberkcansever.mercury.event.Event;
import com.github.ayberkcansever.mercury.io.MercuryClient;
import lombok.Getter;
import lombok.Setter;

public class IOEvent extends Event {

    @Getter @Setter private IOEventType type;
    @Getter @Setter private MercuryClient mercuryClient;

    public IOEvent(MercuryClient mercuryClient, IOEventType type) {
        this.type = type;
        this.mercuryClient = mercuryClient;
    }
}
