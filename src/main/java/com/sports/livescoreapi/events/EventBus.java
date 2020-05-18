package com.sports.livescoreapi.events;

import org.springframework.stereotype.Service;

@Service
public class EventBus {
    
    private final EventRepository eventStore;
    private final MatchStreamProcessor eventHelper;

    public EventBus(EventRepository eventStore, MatchStreamProcessor eventHelper) {
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

    public MatchStreamProcessor getMatchEventHandler() {
        return eventHelper;
    }
}

