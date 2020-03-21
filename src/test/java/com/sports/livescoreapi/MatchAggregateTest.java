package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MatchAggregateTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void starts_match() {
        EventBus eventBus = mock(EventBus.class);
        MatchAggregate matchAggregate = new MatchAggregate(eventBus);

        MatchStartedEvent event = new MatchStartedEvent("123", LocalDateTime.now(), "user", "1.0");

        matchAggregate.startMatch(event);

        assertThat(matchAggregate.getAggregateId()).isEqualTo("123");
    }

    @Test
    void scores_goals() {
        EventBus eventBus = mock(EventBus.class);
        MatchAggregate matchAggregate = new MatchAggregate(eventBus);

        MatchStartedEvent event = new MatchStartedEvent("123", LocalDateTime.now(), "user", "1.0");
        GoalScoredEvent homeGoalEvent = new GoalScoredEvent("123", LocalDateTime.now(), "user", "1.0", "Home");
        GoalScoredEvent visitorsGoalEvent = new GoalScoredEvent("123", LocalDateTime.now(), "user", "1.0", "Visitors");

        matchAggregate.startMatch(event);
        matchAggregate.handle(homeGoalEvent);
        matchAggregate.handle(visitorsGoalEvent);

        assertThat(matchAggregate.getHomeScore()).isEqualTo(1);
        assertThat(matchAggregate.getVisitorsScore()).isEqualTo(1);
    }

    @Test
    void do_not_scores_goal_when_match_is_not_started() {
        EventBus eventBus = mock(EventBus.class);
        MatchAggregate matchAggregate = new MatchAggregate(eventBus);

        GoalScoredEvent homeGoalEvent = new GoalScoredEvent("123", LocalDateTime.now(), "user", "1.0", "Home");

        matchAggregate.handle(homeGoalEvent);

        assertThat(matchAggregate.getHomeScore()).isEqualTo(0);
    }
}