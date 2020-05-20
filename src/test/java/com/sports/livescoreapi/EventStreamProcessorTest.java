package com.sports.livescoreapi;

import com.sports.livescoreapi.domain.*;
import com.sports.livescoreapi.events.*;
import com.sports.livescoreapi.queries.MatchRepository;
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

public class EventStreamProcessorTest {

    @Test
    void replay_match_with_teams_configured() {
        EventRepository eventRepository = mock(EventRepository.class);
        MatchRepository matchRepository = mock(MatchRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId)
                .withHome("PALMEIRAS")
                .withVisitors("CORINTHIANS").build();

        when(eventRepository.findByAggregateId(aggregateId))
                .thenReturn(Collections.singletonList(matchStartedEvent));

        when(matchRepository.findByAggregateId(aggregateId))
                .thenReturn(Optional.empty());

        EventStreamProcessor streamProcessor = new EventStreamProcessor(matchRepository, eventRepository);

        Optional<Match> match = streamProcessor.replayMatchEventStream(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("home", "visitors")
                .containsExactly(Team.of("PALMEIRAS"), Team.of("CORINTHIANS"));
    }

    @Test
    void get_match_with_last_state() {
        EventRepository eventRepository = mock(EventRepository.class);
        MatchRepository matchRepository = mock(MatchRepository.class);

        UUID aggregateId = UUID.randomUUID();
        String userId = "";
        String version = "";

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent().withAggregateId(aggregateId).build();
        GoalScoredEvent homeScored1Event = new GoalScoredEvent(aggregateId, LocalDateTime.now(), userId, version, TeamSide.HOME);
        PeriodEndedEvent firstPeriodEndedEvent = new PeriodEndedEvent(aggregateId, LocalDateTime.now(), userId, version);
        PeriodStartedEvent secondPeriodStartedEvent = new PeriodStartedEvent(aggregateId, LocalDateTime.now(), userId, version);
        GoalScoredEvent homeScored2Event = new GoalScoredEvent(aggregateId, LocalDateTime.now(), userId, version, TeamSide.HOME);
        GoalScoredEvent visitorsScored1Event = new GoalScoredEvent(aggregateId, LocalDateTime.now(), userId, version, TeamSide.VISITORS);
        PeriodEndedEvent secondPeriodEndedEvent = new PeriodEndedEvent(aggregateId, LocalDateTime.now(), userId, version);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), userId, version);

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

        when(matchRepository.findByAggregateId(aggregateId))
                .thenReturn(Optional.empty());

        EventStreamProcessor streamProcessor = new EventStreamProcessor(matchRepository, eventRepository);

        Optional<Match> match = streamProcessor.replayMatchEventStream(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "ballInPlay", "currentPeriod").containsExactly(2, 1, false, MatchPeriod.FULL_TIME);
    }
}
