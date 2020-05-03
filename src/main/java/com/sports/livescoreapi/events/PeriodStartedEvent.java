package com.sports.livescoreapi.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class PeriodStartedEvent extends Event {
    public PeriodStartedEvent(UUID aggregateId, LocalDateTime timeStamp, String userId, String version) {
        super(aggregateId, timeStamp, userId, version);
    }
}
