package com.sports.livescoreapi;

import com.sports.livescoreapi.events.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.sports.livescoreapi.fixtures.MatchStartedEventFixture.aMatchStartedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchQueryHandlerTest {

    private final String USER_ID = "user_x";
    private final String VERSION = "1";

    @Test
    void get_match_with_teams_configured() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId)
                .withHome("PALMEIRAS")
                .withVisitors("CORINTHIANS").build();

        when(eventRepository.findByAggregateId(aggregateId))
                .thenReturn(Collections.singletonList(matchStartedEvent));

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventRepository);

        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("home", "visitors")
                .containsExactly(Team.of("PALMEIRAS"), Team.of("CORINTHIANS"));
    }

    @Test
    void get_match_with_last_state() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId).build();

        GoalScoredEvent homeScored1Event = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        PeriodEndedEvent firstPeriodEndedEvent = new PeriodEndedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);
        PeriodStartedEvent secondPeriodStartedEvent = new PeriodStartedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);
        GoalScoredEvent homeScored2Event = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent visitorsScored1Event = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.VISITORS);
        PeriodEndedEvent secondPeriodEndedEvent = new PeriodEndedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);

        when(eventRepository.findByAggregateId(aggregateId)).thenReturn(
                Arrays.asList(
                        matchStartedEvent,
                        homeScored1Event,
                        firstPeriodEndedEvent,
                        secondPeriodStartedEvent,
                        homeScored2Event,
                        visitorsScored1Event,
                        secondPeriodEndedEvent,
                        matchEndedEvent
                )
        );

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventRepository);

        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "ballInPlay", "currentPeriod").containsExactly(2, 1, false, MatchPeriod.FULL_TIME);
    }
}