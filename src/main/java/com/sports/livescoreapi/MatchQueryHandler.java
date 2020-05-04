package com.sports.livescoreapi;

import com.sports.livescoreapi.events.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchQueryHandler {

    private EventRepository eventRepository;

    public MatchQueryHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Match> getMatch(UUID matchId) {

        List<Event> eventStream = eventRepository.findByAggregateId(matchId);

        if (eventStream.isEmpty())
            return Optional.empty();

        Match match = new Match(matchId);

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
