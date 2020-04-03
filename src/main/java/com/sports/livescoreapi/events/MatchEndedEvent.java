package com.sports.livescoreapi.events;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchEndedEvent extends Event {
    public MatchEndedEvent(String aggregateId, LocalDateTime timeStamp, String userId, String version) {
        super(aggregateId, timeStamp, userId, version);
    }
}
