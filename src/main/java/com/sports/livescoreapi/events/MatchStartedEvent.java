package com.sports.livescoreapi.events;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class MatchStartedEvent extends Event {
    public MatchStartedEvent(String aggregateId, LocalDateTime timeStamp, String userId, String version) {
        super(aggregateId, timeStamp, userId, version);
    }
}
