package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static com.sports.livescoreapi.fixtures.EventFixture.anEventTime;
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

        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);

        when(eventRepository.findByAggregateId(aggregateId)).thenReturn(
                Arrays.asList(
                        matchStartedEvent,
                        goalScoredEvent1,
                        goalScoredEvent2,
                        goalScoredEvent3,
                        matchEndedEvent
                )
        );

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventRepository);

        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "playing").containsExactly(2, 1, false);
    }

    @Test
    void get_match_with_state_at_30_min() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId)
                .withTimeStamp(anEventTime(21, 30))
                .build();

        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, anEventTime(21, 40), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, anEventTime(21, 45), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, anEventTime(22, 25), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, anEventTime(22, 40), USER_ID, VERSION, TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, anEventTime(22, 50), USER_ID, VERSION);

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

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventRepository);

        Optional<Match> match = matchQueryHandler.getMatchAtMinute(aggregateId, 30);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "playing").containsExactly(2, 0, true);
    }

    @Test
    void get_all_events() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId)
                .withTimeStamp(anEventTime(21, 30))
                .build();

        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, anEventTime(21, 40), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, anEventTime(21, 45), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, anEventTime(22, 25), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, anEventTime(22, 40), USER_ID, VERSION, TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, anEventTime(22, 50), USER_ID, VERSION);

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

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventRepository);

        Optional<List<Event>> events = matchQueryHandler.getMatchEvents(aggregateId);

        assertThat(events.isPresent()).isTrue();
        assertThat(events.get()).contains(
                matchStartedEvent,
                goalScoredEvent1,
                goalScoredEvent2,
                goalScoredEvent3,
                goalScoredEvent4,
                matchEndedEvent
        );
    }
}