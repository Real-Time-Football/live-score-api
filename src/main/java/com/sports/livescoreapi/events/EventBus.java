package com.sports.livescoreapi.events;

import org.springframework.stereotype.Service;

@Service
public class EventBus {
    
    private final EventRepository eventStore;
    private final EventStreamProcessor streamProcessor;

    public EventBus(EventRepository eventStore, EventStreamProcessor streamProcessor) {
        this.eventStore = eventStore;
        this.streamProcessor = streamProcessor;
    }

    public <T extends Event> void post(T event) {
        persistEvent(event);
        notifyHandler(event);
    }

    private <T extends Event> void notifyHandler(T event) {
        streamProcessor.replayMatchEventStream(event.getAggregateId());
    }

    private <T extends Event> void persistEvent(T event) {
        eventStore.save(event);
    }

    public EventStreamProcessor getMatchEventHandler() {
        return streamProcessor;
    }
}

