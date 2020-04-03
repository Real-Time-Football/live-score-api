package com.sports.livescoreapi.events;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchStartedEvent extends Event {
    public MatchStartedEvent(String aggregateId, LocalDateTime timeStamp, String userId, String version) {
        super(aggregateId, timeStamp, userId, version);
    }
}
