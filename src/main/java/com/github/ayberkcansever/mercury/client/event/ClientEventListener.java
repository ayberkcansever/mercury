package com.github.ayberkcansever.mercury.client.event;

import com.github.ayberkcansever.mercury.event.EventListener;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public abstract class ClientEventListener implements EventListener {

    @Subscribe
    @AllowConcurrentEvents
    protected abstract void handle(ClientEvent event);

}
