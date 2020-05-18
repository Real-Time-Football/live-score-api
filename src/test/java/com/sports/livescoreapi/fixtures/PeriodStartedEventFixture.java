package com.sports.livescoreapi.fixtures;

import com.sports.livescoreapi.events.PeriodStartedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class PeriodStartedEventFixture {

    public static PeriodStartedEventBuilder aPeriodStartedEvent() {
        return new PeriodStartedEventBuilder();
    }

    public static class PeriodStartedEventBuilder {
        private UUID aggregateId = UUID.randomUUID();
        private LocalDateTime timeStamp = LocalDateTime.now();
        private String userId = "user_x";
        private String version = "1";

        public PeriodStartedEventBuilder() {}

        public PeriodStartedEventBuilder withAggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public PeriodStartedEventBuilder withTimeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public PeriodStartedEvent build() {
            return new PeriodStartedEvent(aggregateId, timeStamp, userId, version);
        }

    }
}
