package com.sports.livescoreapi;

import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.domain.Team;
import com.sports.livescoreapi.events.EventStreamProcessor;
import com.sports.livescoreapi.events.MatchStartedEvent;
import com.sports.livescoreapi.queries.MatchQueryHandler;
import com.sports.livescoreapi.queries.MatchRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.sports.livescoreapi.fixtures.MatchStartedEventFixture.aMatchStartedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchQueryHandlerTest {

    @Test
    void get_match_form_repository() {
        EventStreamProcessor eventHelper = mock(EventStreamProcessor.class);
        MatchRepository matchRepository = mock(MatchRepository.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId)
                .withHome("PALMEIRAS")
                .withVisitors("CORINTHIANS").build();

        Match match = new Match(aggregateId);

        match.apply(matchStartedEvent);

        when(matchRepository.findByAggregateId(aggregateId))
                .thenReturn(Optional.of(match));

        MatchQueryHandler matchQueryHandler = new MatchQueryHandler(eventHelper, matchRepository);

        Optional<Match> returnedMatch = matchQueryHandler.getMatch(aggregateId);

        assertThat(returnedMatch.isPresent()).isTrue();
        assertThat(returnedMatch.get()).extracting("home", "visitors")
                .containsExactly(Team.of("PALMEIRAS"), Team.of("CORINTHIANS"));
    }
}