package com.sports.livescoreapi.queries;

import com.sports.livescoreapi.domain.Match;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository {
    void save(Match match);

    Optional<Match> findByAggregateId(UUID aggregateId);
}
