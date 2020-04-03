package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class EventBus {
    
    private final EventRepository eventStore;

    public EventBus(EventRepository eventStore) {
        this.eventStore = eventStore;
    }

    public <T extends Event> void post(T event) {
        persistEvent(event);
    }

    private <T extends Event> void persistEvent(T event) {
        eventStore.save(event);
    }
}

