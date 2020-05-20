package com.sports.livescoreapi.queries;

import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.events.EventStreamProcessor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchQueryHandler {

    private final EventStreamProcessor eventHelper;
    private final MatchRepository matchRepository;

    public MatchQueryHandler(EventStreamProcessor eventHelper, MatchRepository matchRepository) {
        this.eventHelper = eventHelper;
        this.matchRepository = matchRepository;
    }

    public Optional<Match> getMatch(UUID matchId) {

        Optional<Match> match = matchRepository.findByAggregateId(matchId);

        if (!match.isPresent()) {
            eventHelper.replayMatchEventStream(matchId);
        }

        return match;
    }
}
