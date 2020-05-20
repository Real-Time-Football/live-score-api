package com.sports.livescoreapi.events;

import com.sports.livescoreapi.queries.MatchRepository;
import com.sports.livescoreapi.domain.Match;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventStreamProcessor {

    private final MatchRepository matchRepository;
    private final EventRepository eventStore;

    public EventStreamProcessor(MatchRepository matchRepository, EventRepository eventStore) {
        this.matchRepository = matchRepository;
        this.eventStore = eventStore;
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

        matchRepository.save(match);

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
