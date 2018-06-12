package com.github.ayberkcansever.mercury.message.event;

import com.github.ayberkcansever.mercury.event.EventListener;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public abstract class MessageEventListener implements EventListener {

    @Subscribe
    @AllowConcurrentEvents
    protected abstract void handle(MessageEvent event);

}
