package com.sports.livescoreapi.queries;

import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.events.MatchStreamProcessor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchQueryHandler {

    private final MatchStreamProcessor eventHelper;
    private final MatchRepository matchRepository;

    public MatchQueryHandler(MatchStreamProcessor eventHelper, MatchRepository matchRepository) {
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
