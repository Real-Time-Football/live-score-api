package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventBusTest {

    private EventRepository eventStore;

    private final String DEFAULT_USER_ID = "user_x";
    private final String DEFAULT_VERSION = "1";

    @BeforeEach
    void setUp() {
        eventStore = mock(EventRepository.class);
        doNothing().when(eventStore).save(any(Event.class));
    }

    @Test
    void register_aggregate_stater() {
        EventBus eventBus = new EventBus(eventStore);
        eventBus.registerEvent(MatchStartedEvent.class, MatchAggregate.class);

        assertTrue(eventBus.getAggregateStarters().containsKey(MatchStartedEvent.class));
    }

    @Test
    void create_instance_of_aggregate() {
        EventBus eventBus = new EventBus(eventStore);
        eventBus.registerEvent(MatchStartedEvent.class, MatchAggregate.class);

        MatchStartedEvent event = new MatchStartedEvent(getNewMatchId(), LocalDateTime.now(), DEFAULT_USER_ID, DEFAULT_VERSION);
        eventBus.post(event);

        assertEquals(1, eventBus.getAggregateInstances().size());
    }

    @Test
    void persist_events() {
        EventBus eventBus = new EventBus(eventStore);

        eventBus.registerEvent(MatchStartedEvent.class, MatchAggregate.class);

        String matchId = getNewMatchId();

        MatchStartedEvent event = new MatchStartedEvent(matchId, LocalDateTime.now(), DEFAULT_USER_ID, DEFAULT_VERSION);
        eventBus.post(event);

        GoalScoredEvent secondEvent = new GoalScoredEvent(matchId, LocalDateTime.now(), DEFAULT_USER_ID, DEFAULT_VERSION, "Home");
        eventBus.post(secondEvent);

        verify(eventStore, times(2)).save(any());
    }

    private String getNewMatchId() {
        return UUID.randomUUID().toString();
    }
}