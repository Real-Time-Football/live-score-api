package com.sports.livescoreapi.fixtures;

import com.sports.livescoreapi.domain.TeamSide;
import com.sports.livescoreapi.events.GoalScoredEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class GoalScoredEventFixture {

    public static GoalScoredEventBuilder aGoalScoredEvent() {
        return new GoalScoredEventBuilder();
    }

    public static class GoalScoredEventBuilder {
        private UUID aggregateId = UUID.randomUUID();
        private LocalDateTime timeStamp = LocalDateTime.now();
        private String userId = "user_x";
        private String version = "1";
        private TeamSide side = TeamSide.HOME;

        public GoalScoredEventBuilder() {}

        public GoalScoredEventBuilder withAggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public GoalScoredEventBuilder withTimeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public GoalScoredEventBuilder withTeamSide(TeamSide side) {
            this.side = side;
            return this;
        }

        public GoalScoredEvent build() {
            return new GoalScoredEvent(aggregateId, timeStamp, userId, version, side);
        }

    }
}
