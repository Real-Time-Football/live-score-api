package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import com.sports.livescoreapi.fixtures.MatchStartedEventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class EventRepositoryIntegrationTest {

    @Autowired
    EventRepository eventRepository;

    private MatchStartedEventBuilder matchStartedEventBuilder;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        matchStartedEventBuilder = new MatchStartedEventBuilder();
    }

    private final String USER_ID = "user_x";
    private final String VERSION = "1";

    @Test
    void save_events() {

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = matchStartedEventBuilder
                .withAggregateId(aggregateId)
                .build();

        eventRepository.save(matchStartedEvent);

        assertThat(eventRepository.findAll()).extracting("aggregateId").containsOnly(aggregateId);
    }

    @Test
    void find_all_events_stream() {

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = matchStartedEventBuilder
                .withAggregateId(aggregateId)
                .build();

        GoalScoredEvent goalScoredEvent = new GoalScoredEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION, TeamSide.HOME);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), USER_ID, VERSION);

        eventRepository.save(matchStartedEvent);
        eventRepository.save(goalScoredEvent);
        eventRepository.save(matchEndedEvent);

        List<Event> eventsStream = eventRepository.findByAggregateId(aggregateId);

        assertThat(eventsStream).contains(matchStartedEvent, goalScoredEvent, matchEndedEvent);
    }
}
