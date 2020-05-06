package com.sports.livescoreapi;

import com.sports.livescoreapi.events.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventBus {
    
    private final EventRepository eventStore;

    public EventBus(EventRepository eventStore) {
        this.eventStore = eventStore;
    }

    public <T extends Event> void post(T event) {
        persistEvent(event);
    }

    private <T extends Event> void persistEvent(T event) {
        eventStore.save(event);
    }

    public Optional<Match> replayMatchEventStream(UUID aggregateId) {
        List<Event> eventStream = eventStore.findByAggregateId(aggregateId);

        if (eventStream.isEmpty())
            return Optional.empty();

        Match match = new Match(aggregateId);

        eventStream
                .stream()
                .sorted(Comparator.comparing(Event::getTimeStamp))
                .forEach(event -> applyFor(match, event));

        return Optional.of(match);
    }

    private void applyFor(Match match, Event event) {
        if (event.getClass().isAssignableFrom(MatchStartedEvent.class)) {
            match.apply((MatchStartedEvent) event);
        }

        if (event.getClass().isAssignableFrom(GoalScoredEvent.class)) {
            match.apply((GoalScoredEvent) event);
        }

        if (event.getClass().isAssignableFrom(MatchEndedEvent.class)) {
            match.apply((MatchEndedEvent) event);
        }

        if (event.getClass().isAssignableFrom(PeriodStartedEvent.class)) {
            match.apply((PeriodStartedEvent) event);
        }

        if (event.getClass().isAssignableFrom(PeriodEndedEvent.class)) {
            match.apply((PeriodEndedEvent) event);
        }
    }
}

