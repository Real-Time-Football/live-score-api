package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryHandlerTest {

    @Test
    void get_match_with_last_state() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1");
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1");

        when(eventRepository.findByAggregateId(aggregateId)).thenReturn(
                Arrays.asList(
                        matchStartedEvent,
                        goalScoredEvent1,
                        goalScoredEvent2,
                        goalScoredEvent3,
                        goalScoredEvent4,
                        matchEndedEvent
                )
        );

        QueryHandler queryHandler = new QueryHandler(eventRepository);

        Optional<MatchAggregate> match = queryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("homeScore", "visitorsScore", "teamsArePlaying").containsExactly(3, 1, false);
    }
}