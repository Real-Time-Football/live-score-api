package com.sports.livescoreapi;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchQueryHandler {

    private final MatchEventHandler eventHandler;
    private final MatchRepository matchRepository;

    public MatchQueryHandler(MatchEventHandler eventHandler, MatchRepository matchRepository) {
        this.eventHandler = eventHandler;
        this.matchRepository = matchRepository;
    }

    public Optional<Match> getMatch(UUID matchId) {

        Optional<Match> match = matchRepository.findByAggregateId(matchId);

        if (!match.isPresent()) {
            eventHandler.replayMatchEventStream(matchId);
        }

        return match;
    }
}
