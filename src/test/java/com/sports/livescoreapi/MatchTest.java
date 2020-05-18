package com.sports.livescoreapi;

import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.domain.MatchPeriod;
import com.sports.livescoreapi.domain.MatchScheduleStatus;
import com.sports.livescoreapi.domain.TeamSide;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import com.sports.livescoreapi.events.PeriodEndedEvent;
import com.sports.livescoreapi.events.PeriodStartedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.sports.livescoreapi.fixtures.GoalScoredEventFixture.aGoalScoredEvent;
import static com.sports.livescoreapi.fixtures.MatchStartedEventFixture.aMatchStartedEvent;
import static com.sports.livescoreapi.fixtures.PeriodEndedEventFixture.aPeriodEndedEvent;
import static com.sports.livescoreapi.fixtures.PeriodStartedEventFixture.aPeriodStartedEvent;
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

        match.startFirstPeriod();

        assertThat(match)
                .extracting("aggregateId", "ballInPlay", "status")
                .contains(matchId, true, MatchScheduleStatus.PLAYING);
    }

    @Test
    void scores_goals() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        //todo: rewrite all tests to use method apply as public api

        match.startFirstPeriod();
        match.score(TeamSide.HOME);
        match.score(TeamSide.VISITORS);

        assertThat(match.getScore().getHome()).isEqualTo(1);
        assertThat(match.getScore().getVisitors()).isEqualTo(1);
    }

    @Test
    void do_not_scores_goal_when_match_is_not_started() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        GoalScoredEvent goalHomeEvent = aGoalScoredEvent().withAggregateId(matchId).withTeamSide(TeamSide.HOME).build();
        GoalScoredEvent goalVisitorsEvent = aGoalScoredEvent().withAggregateId(matchId).withTeamSide(TeamSide.VISITORS).build();

        match.apply(goalHomeEvent);
        match.apply(goalVisitorsEvent);

        assertThat(match.getScore().getHome()).isEqualTo(0);
        assertThat(match.getScore().getVisitors()).isEqualTo(0);
    }

    @Test
    void ends_match() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();
        match.end();

        assertThat(match)
                .extracting("ballInPlay", "status")
                .contains(false, MatchScheduleStatus.ENDED);
    }

    @Test
    void start_first_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();

        assertThat(match.getCurrentPeriod()).isEqualTo(MatchPeriod.FIRST_PERIOD);
    }

    @Test
    void stop_first_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();
        match.endFirstPeriod();

        assertThat(match.getCurrentPeriod()).isEqualTo(MatchPeriod.HALF_TIME);
    }

    @Test
    void not_stop_period_when_no_period_started() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        PeriodEndedEvent periodEndedEvent = aPeriodEndedEvent().withAggregateId(matchId).build();

        match.apply(periodEndedEvent);

        assertThat(match.getCurrentPeriod()).isEqualTo(MatchPeriod.NONE);
    }

    @Test
    void start_second_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();
        match.endFirstPeriod();
        match.startSecondPeriod();

        assertThat(match.getCurrentPeriod()).isEqualTo(MatchPeriod.SECOND_PERIOD);
    }

    @Test
    void not_start_second_period_when_not_at_half_time() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent().withAggregateId(matchId).build();
        PeriodStartedEvent firstPeriodStartedEvent = aPeriodStartedEvent().withAggregateId(matchId).build();
        PeriodStartedEvent secondPeriodStartedEvent = aPeriodStartedEvent().withAggregateId(matchId).build();

        match.apply(matchStartedEvent);
        match.apply(firstPeriodStartedEvent);
        match.apply(secondPeriodStartedEvent);

        assertThat(match.getCurrentPeriod()).isEqualTo(MatchPeriod.FIRST_PERIOD);
    }

    @Test
    void end_second_period() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();
        match.endFirstPeriod();
        match.startSecondPeriod();
        match.endSecondPeriod();

        assertThat(match.getCurrentPeriod()).isEqualTo(MatchPeriod.FULL_TIME);
    }

    @Test
    void update_first_period_score_at_end() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();
        match.score(TeamSide.HOME);
        match.endFirstPeriod();

        assertThat(match.getFirstPeriodScore().get()).extracting("home", "visitors").contains(1, 0);
    }

    @Test
    void update_second_period_score_at_end() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();
        match.endFirstPeriod();
        match.startSecondPeriod();
        match.score(TeamSide.HOME);
        match.score(TeamSide.HOME);
        match.endSecondPeriod();

        assertThat(match.getSecondPeriodScore().get()).extracting("home", "visitors").contains(2, 0);
    }

    @Test
    void do_not_update_first_period_score() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId);

        match.startFirstPeriod();

        assertFalse(match.getFirstPeriodScore().isPresent());
    }
}