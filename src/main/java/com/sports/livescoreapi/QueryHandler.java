package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QueryHandler {

    private EventRepository eventRepository;

    public QueryHandler(EventRepository eventRepository) {
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
                .forEach(event -> {
                    if (event.getClass().isAssignableFrom(MatchStartedEvent.class)) {
                        match.apply((MatchStartedEvent) event);
                    }

                    if (event.getClass().isAssignableFrom(GoalScoredEvent.class)) {
                        match.apply((GoalScoredEvent) event);
                    }

                    if (event.getClass().isAssignableFrom(MatchEndedEvent.class)) {
                        match.apply((MatchEndedEvent) event);
                    }
                });

        return Optional.of(match);
    }

    public Optional<Match> getMatchAtMinute(UUID matchId, int minute) {
        List<Event> eventStream = eventRepository.findByAggregateId(matchId);

        if (eventStream.isEmpty())
            return Optional.empty();

        Match match = new Match(matchId);

        LocalDateTime timeStampAtMinute = eventStream.get(0).getTimeStamp().plusMinutes(minute);

        eventStream
                .stream()
                .sorted(Comparator.comparing(Event::getTimeStamp))
                .filter(event -> event.getTimeStamp().compareTo(timeStampAtMinute) <= 0)
                .forEach(event -> {
                    if (event.getClass().isAssignableFrom(MatchStartedEvent.class)) {
                        match.apply((MatchStartedEvent) event);
                    }

                    if (event.getClass().isAssignableFrom(GoalScoredEvent.class)) {
                        match.apply((GoalScoredEvent) event);
                    }

                    if (event.getClass().isAssignableFrom(MatchEndedEvent.class)) {
                        match.apply((MatchEndedEvent) event);
                    }
                });

        return Optional.of(match);
    }

    public Optional<List<Event>> getMatchEvents(UUID matchId) {

        List<Event> eventStream = eventRepository.findByAggregateId(matchId);

        if (eventStream.isEmpty())
            return Optional.empty();

        return Optional.of(eventStream);
    }
}
