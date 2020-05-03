package com.sports.livescoreapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MatchTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void starts_match() {
        UUID matchId = UUID.randomUUID();

        Match match = new Match(matchId);

        match.start();

        assertThat(match)
                .extracting("aggregateId", "playing")
                .contains(matchId, true);
    }

    @Test
    void scores_goals() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();
        match.scoreForHome();
        match.scoreForVisitors();

        assertThat(match.getScore().getHome()).isEqualTo(1);
        assertThat(match.getScore().getVisitors()).isEqualTo(1);
    }

    @Test
    void do_not_scores_goal_when_match_is_not_started() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.scoreForHome();
        match.scoreForVisitors();

        assertThat(match.getScore().getHome()).isEqualTo(0);
        assertThat(match.getScore().getVisitors()).isEqualTo(0);
    }

    @Test
    void ends_match() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();
        match.end();

        assertFalse(match.isPlaying());
    }

    @Test
    void start_first_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();

        assertThat(match.getPeriod()).isEqualTo(MatchPeriod.FIRST_PERIOD);
    }

    @Test
    void stop_first_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();
        match.endPeriod();

        assertThat(match.getPeriod()).isEqualTo(MatchPeriod.HALF_TIME);
    }

    @Test
    void not_stop_period_when_no_period_started() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.endPeriod();

        assertThat(match.getPeriod()).isEqualTo(MatchPeriod.NONE);
    }

    @Test
    void start_second_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();
        match.endPeriod();
        match.startPeriod();

        assertThat(match.getPeriod()).isEqualTo(MatchPeriod.SECOND_PERIOD);
    }

    @Test
    void not_start_second_period_when_not_at_half_time() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();
        match.startPeriod();

        assertThat(match.getPeriod()).isEqualTo(MatchPeriod.FIRST_PERIOD);
    }

    @Test
    void end_second_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.start();
        match.endPeriod();
        match.startPeriod();
        match.endPeriod();

        assertThat(match.getPeriod()).isEqualTo(MatchPeriod.FULL_TIME);
    }
}