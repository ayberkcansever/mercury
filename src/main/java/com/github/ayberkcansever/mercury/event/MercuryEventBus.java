package com.github.ayberkcansever.mercury.event;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

@Component
public class MercuryEventBus {

    private EventBus eventBus = new EventBus();

    public void register(EventListener eventListener) {
        eventBus.register(eventListener);
    }

    public void unregister(EventListener eventListener) {
        eventBus.unregister(eventListener);
    }

    public void postEvent(Event event) {
        eventBus.post(event);
    }
}
