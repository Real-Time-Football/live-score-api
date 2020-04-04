package com.sports.livescoreapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MatchAggregateTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void starts_match() {
        UUID matchId = UUID.randomUUID();

        MatchAggregate matchAggregate = new MatchAggregate(matchId);

        matchAggregate.start();

        assertThat(matchAggregate)
                .extracting("aggregateId", "teamsArePlaying")
                .contains(matchId, true);
    }

    @Test
    void scores_goals() {
        UUID matchId = UUID.randomUUID();
        MatchAggregate matchAggregate = new MatchAggregate(matchId);

        matchAggregate.start();
        matchAggregate.scoreForHome();
        matchAggregate.scoreForVisitors();

        assertThat(matchAggregate.getHomeScore()).isEqualTo(1);
        assertThat(matchAggregate.getVisitorsScore()).isEqualTo(1);
    }

    @Test
    void do_not_scores_goal_when_match_is_not_started() {
        UUID matchId = UUID.randomUUID();
        MatchAggregate matchAggregate = new MatchAggregate(matchId);

        matchAggregate.scoreForHome();
        matchAggregate.scoreForVisitors();

        assertThat(matchAggregate.getHomeScore()).isEqualTo(0);
        assertThat(matchAggregate.getVisitorsScore()).isEqualTo(0);
    }

    @Test
    void ends_match() {
        UUID matchId = UUID.randomUUID();
        MatchAggregate matchAggregate = new MatchAggregate(matchId);

        matchAggregate.start();
        matchAggregate.end();

        assertFalse(matchAggregate.areTeamsPlaying());
    }
}