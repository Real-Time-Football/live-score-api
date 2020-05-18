package com.sports.livescoreapi.fixtures;

import com.sports.livescoreapi.domain.TeamSide;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.PeriodEndedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class PeriodEndedEventFixture {

    public static PeriodEndedEventBuilder aPeriodEndedEvent() {
        return new PeriodEndedEventBuilder();
    }

    public static class PeriodEndedEventBuilder {
        private UUID aggregateId = UUID.randomUUID();
        private LocalDateTime timeStamp = LocalDateTime.now();
        private String userId = "user_x";
        private String version = "1";

        public PeriodEndedEventBuilder() {}

        public PeriodEndedEventBuilder withAggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public PeriodEndedEventBuilder withTimeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public PeriodEndedEvent build() {
            return new PeriodEndedEvent(aggregateId, timeStamp, userId, version);
        }

    }
}
