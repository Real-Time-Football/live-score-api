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

    @Test
    void get_match_with_teams_configured() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1",
                LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");

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

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");
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

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventRepository);

        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "playing").containsExactly(3, 1, false);
    }

    @Test
    void get_match_with_state_at_30_min() {
        EventRepository eventRepository = mock(EventRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, aMatchTime(21, 30), "usr-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, aMatchTime(21, 40), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, aMatchTime(21, 45), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, aMatchTime(22, 25), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, aMatchTime(22, 40), "usr-m", "1", TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, aMatchTime(22, 50), "usr-m", "1");

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

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, aMatchTime(21, 30), "usr-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, aMatchTime(21, 40), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, aMatchTime(21, 45), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, aMatchTime(22, 25), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, aMatchTime(22, 40), "usr-m", "1", TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, aMatchTime(22, 50), "usr-m", "1");

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