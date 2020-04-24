package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
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

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    void save_events() {

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");

        eventRepository.save(matchStartedEvent);

        assertThat(eventRepository.findAll()).extracting("aggregateId").containsOnly(aggregateId);
    }

    @Test
    void find_all_events_stream() {

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");
        GoalScoredEvent goalScoredEvent = new GoalScoredEvent(aggregateId, LocalDateTime.now(), "usr-m", "1", TeamSide.HOME);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, LocalDateTime.now(), "usr-m", "1");

        eventRepository.save(matchStartedEvent);
        eventRepository.save(goalScoredEvent);
        eventRepository.save(matchEndedEvent);

        List<Event> eventsStream = eventRepository.findByAggregateId(aggregateId);

        assertThat(eventsStream).contains(matchStartedEvent, goalScoredEvent, matchEndedEvent);
    }
}
