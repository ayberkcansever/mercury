package com.github.ayberkcansever.mercury.io.event;

import com.github.ayberkcansever.mercury.event.EventListener;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public abstract class IOEventListener implements EventListener {

    @Subscribe
    @AllowConcurrentEvents
    protected abstract void handle(IOEvent event);

}
