package com.sports.livescoreapi;

import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.queries.MatchRepository;
import com.sports.livescoreapi.repositories.MatchCacheRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchRepositoryIntegrationTest {

    @Test
    @Ignore
    void save_match() {

        UUID aggregateId = UUID.randomUUID();

        Match match = new Match(aggregateId);

        MatchRepository matchRepository = new MatchCacheRepository();

        matchRepository.save(match);

        assertThat(matchRepository.findByAggregateId(aggregateId).get().getAggregateId()).isEqualTo(aggregateId);
    }
}
