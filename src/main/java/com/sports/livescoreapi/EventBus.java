package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import org.springframework.stereotype.Service;

@Service
public class EventBus {
    
    private final EventRepository eventStore;
    private final MatchEventHelper eventHelper;

    public EventBus(EventRepository eventStore, MatchEventHelper eventHelper) {
        this.eventStore = eventStore;
        this.eventHelper = eventHelper;
    }

    public <T extends Event> void post(T event) {
        persistEvent(event);
        notifyHandler(event);
    }

    private <T extends Event> void notifyHandler(T event) {
        eventHelper.replayMatchEventStream(event.getAggregateId());
    }

    private <T extends Event> void persistEvent(T event) {
        eventStore.save(event);
    }

    public MatchEventHelper getMatchEventHandler() {
        return eventHelper;
    }
}

