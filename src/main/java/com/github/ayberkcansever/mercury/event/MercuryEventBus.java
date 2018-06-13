package com.github.ayberkcansever.mercury.event;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class MercuryEventBus {

    private EventBus eventBus = new EventBus();

    private Map<String, Boolean> LISTENER_REGISTER_STATE_MAP = new HashMap<>();

    public void register(EventListener eventListener) {
        LISTENER_REGISTER_STATE_MAP.put(getListenerEventName(eventListener), true);
        eventBus.register(eventListener);
    }

    private String getListenerEventName(EventListener eventListener) {
        Method[] methods = eventListener.getClass().getDeclaredMethods();
        for(Method method : methods) {
            if("handle".equalsIgnoreCase(method.getName())) {
                return method.getParameters()[0].getType().getSimpleName();
            }
        }
        return null;
    }

    public void postEvent(Event event) {
        String eventName = event.getClass().getSimpleName();
        Boolean state = LISTENER_REGISTER_STATE_MAP.get(eventName);
        if(state != null && state) {
            eventBus.post(event);
        }
    }

}
