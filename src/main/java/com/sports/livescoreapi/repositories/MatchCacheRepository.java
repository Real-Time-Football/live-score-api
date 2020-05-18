package com.sports.livescoreapi.repositories;

import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.queries.MatchRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MatchCacheRepository implements MatchRepository {

    private final Map<UUID, Match> cachedMatches = new HashMap<>();

    @Override
    public void save(Match match) {
        cachedMatches.put(match.getAggregateId(), match);
    }

    @Override
    public Optional<Match> findByAggregateId(UUID aggregateId) {
        if (cachedMatches.containsKey(aggregateId)) {
            return Optional.of(cachedMatches.get(aggregateId));
        }

        return Optional.empty();
    }
}
