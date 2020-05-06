package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import org.springframework.stereotype.Service;

@Service
public class EventBus {
    
    private final EventRepository eventStore;
    private final MatchEventHandler eventHandler;

    public EventBus(EventRepository eventStore, MatchEventHandler eventHandler) {
        this.eventStore = eventStore;
        this.eventHandler = eventHandler;
    }

    public <T extends Event> void post(T event) {
        persistEvent(event);
        notifyHandler(event);
    }

    private <T extends Event> void notifyHandler(T event) {
        eventHandler.replayMatchEventStream(event.getAggregateId());
    }

    private <T extends Event> void persistEvent(T event) {
        eventStore.save(event);
    }

    public MatchEventHandler getMatchEventHandler() {
        return eventHandler;
    }
}

