package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchQueryHandlerTest {

    private final String USER_ID = "user_x";
    private final String VERSION = "1";
    private final LocalDateTime DATE = LocalDateTime.now();
    private final Team HOME = Team.of("PALMEIRAS");
    private final Team VISITORS = Team.of("CORINTHIANS");

    @Test
    void get_match_with_teams_configured() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION,
                DATE, HOME, VISITORS);

        when(eventRepository.findByAggregateId(aggregateId)).thenReturn(
                Arrays.asList(
                        matchStartedEvent
                )
        );

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

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, DATE, HOME, VISITORS);
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);

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

        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "playing").containsExactly(3, 1, false);
    }

    @Test
    void get_match_with_state_at_30_min() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, aMatchTime(21, 30), USER_ID, VERSION, DATE, HOME, VISITORS);
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, aMatchTime(21, 40), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, aMatchTime(21, 45), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, aMatchTime(22, 25), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, aMatchTime(22, 40), USER_ID, VERSION, TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, aMatchTime(22, 50), USER_ID, VERSION);

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

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, aMatchTime(21, 30), USER_ID, VERSION, DATE, HOME, VISITORS);
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, aMatchTime(21, 40), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, aMatchTime(21, 45), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, aMatchTime(22, 25), USER_ID, VERSION, TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, aMatchTime(22, 40), USER_ID, VERSION, TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, aMatchTime(22, 50), USER_ID, VERSION);

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

    LocalDateTime aMatchTime(int hour, int min) {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, min));
    }
}