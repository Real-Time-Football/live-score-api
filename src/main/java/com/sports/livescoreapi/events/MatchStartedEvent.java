package com.sports.livescoreapi.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MatchStartedEvent extends Event {

    public MatchStartedEvent() {}

    public MatchStartedEvent(UUID aggregateId, LocalDateTime timeStamp, String userId, String version) {
        super(aggregateId, timeStamp, userId, version);
    }
}
