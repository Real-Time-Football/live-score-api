package com.sports.livescoreapi.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MatchStartedEvent extends Event {

    private final LocalDateTime date;
    private final String teamHome;
    private final String teamVisitors;

    public MatchStartedEvent(UUID aggregateId, LocalDateTime timeStamp, String userId, String version,
                             LocalDateTime date, String teamHome, String teamVisitors) {
        super(aggregateId, timeStamp, userId, version);
        this.date = date;
        this.teamHome = teamHome;
        this.teamVisitors = teamVisitors;
    }
}
