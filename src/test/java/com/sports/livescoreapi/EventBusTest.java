package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.MatchStartedEvent;
import com.sports.livescoreapi.fixtures.MatchStartedEventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventBusTest {

    private EventRepository eventStore;
    private MatchStartedEventBuilder matchStartedEventBuilder;

    @BeforeEach
    void setUp() {
        eventStore = mock(EventRepository.class);
        when(eventStore.save(any(Event.class))).thenReturn(null);
        matchStartedEventBuilder = new MatchStartedEventBuilder();
    }

    @Test
    void persist_events() {
        EventBus eventBus = new EventBus(eventStore);

        MatchStartedEvent startedEvent = matchStartedEventBuilder.build();

        eventBus.post(startedEvent);

        verify(eventStore, times(1)).save(any(MatchStartedEvent.class));
    }
}