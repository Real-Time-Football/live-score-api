package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QueryHandler {

    private EventRepository eventRepository;

    public QueryHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<MatchAggregate> getMatch(UUID matchId) {

        List<Event> eventStream = eventRepository.findByAggregateId(matchId);

        if (eventStream.isEmpty())
            return Optional.empty();

        MatchAggregate matchAggregate = new MatchAggregate(matchId);

        eventStream
                .stream()
                .sorted(Comparator.comparing(Event::getTimeStamp))
                .forEach(event -> {
                    if (event.getClass().isAssignableFrom(MatchStartedEvent.class)) {
                        matchAggregate.apply((MatchStartedEvent) event);
                    }

                    if (event.getClass().isAssignableFrom(GoalScoredEvent.class)) {
                        matchAggregate.apply((GoalScoredEvent) event);
                    }

                    if (event.getClass().isAssignableFrom(MatchEndedEvent.class)) {
                        matchAggregate.apply((MatchEndedEvent) event);
                    }
                });

        return Optional.of(matchAggregate);
    }
}
