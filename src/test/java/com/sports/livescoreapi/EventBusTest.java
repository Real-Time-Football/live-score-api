package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.sports.livescoreapi.fixtures.MatchStartedEventFixture.aMatchStartedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventBusTest {

    private EventRepository eventStore;

    @BeforeEach
    void setUp() {
        eventStore = mock(EventRepository.class);
        when(eventStore.save(any(Event.class))).thenReturn(null);
    }

    @Test
    void persist_events() {
        EventBus eventBus = new EventBus(eventStore);

        MatchStartedEvent startedEvent = aMatchStartedEvent().build();

        eventBus.post(startedEvent);

        verify(eventStore, times(1)).save(any(MatchStartedEvent.class));
    }

    @Test
    void replay_match_events() {
        EventBus eventBus = new EventBus(eventStore);

        MatchStartedEvent startedEvent = aMatchStartedEvent().build();

        UUID aggregateId = startedEvent.getAggregateId();

        GoalScoredEvent goalScoredEvent = new GoalScoredEvent(aggregateId, LocalDateTime.now(), "", "", TeamSide.HOME);

        when(eventStore.findByAggregateId(aggregateId)).thenReturn(Arrays.asList(startedEvent, goalScoredEvent));

        Optional<Match> match = eventBus.replayMatchEventStream(aggregateId);

        assertThat(match.get()).extracting("score.home", "score.visitors", "ballInPlay").containsExactly(1, 0, true);
    }
}