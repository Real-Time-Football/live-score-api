package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.sports.livescoreapi.fixtures.MatchStartedEventFixture.aMatchStartedEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventBusTest {

    private EventRepository eventStore;
    private MatchEventHandler eventHandler;

    @BeforeEach
    void setUp() {
        eventStore = mock(EventRepository.class);
        eventHandler = mock(MatchEventHandler.class);
        when(eventStore.save(any(Event.class))).thenReturn(null);
    }

    @Test
    void persist_events() {
        EventBus eventBus = new EventBus(eventStore, eventHandler);

        MatchStartedEvent startedEvent = aMatchStartedEvent().build();

        eventBus.post(startedEvent);

        verify(eventStore, times(1)).save(any(MatchStartedEvent.class));
    }
}