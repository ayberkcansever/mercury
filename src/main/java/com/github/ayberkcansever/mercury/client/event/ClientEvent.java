package com.github.ayberkcansever.mercury.client.event;

import com.github.ayberkcansever.mercury.client.MercuryClient;
import com.github.ayberkcansever.mercury.event.Event;
import lombok.Getter;
import lombok.Setter;

public class ClientEvent extends Event {

    @Getter @Setter private MercuryClient mercuryClient;
    @Getter @Setter private ClientEventType type;

    public ClientEvent(MercuryClient mercuryClient, ClientEventType type) {
        this.mercuryClient = mercuryClient;
        this.type = type;
    }
}
