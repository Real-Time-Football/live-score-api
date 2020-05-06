package com.sports.livescoreapi;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository {
    void save(Match match);

    Optional<Match> findByAggregateId(UUID aggregateId);
}
