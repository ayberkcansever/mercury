package com.github.ayberkcansever.mercury.client.event;

import com.github.ayberkcansever.mercury.client.MercuryClient;
import com.github.ayberkcansever.mercury.event.Event;
import lombok.Data;

@Data
public class ClientEvent extends Event {

    private MercuryClient mercuryClient;
    private ClientEventType type;

    public ClientEvent(MercuryClient mercuryClient, ClientEventType type) {
        this.mercuryClient = mercuryClient;
        this.type = type;
    }
}
