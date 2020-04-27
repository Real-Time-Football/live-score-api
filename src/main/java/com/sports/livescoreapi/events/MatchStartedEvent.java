package com.sports.livescoreapi.events;

import com.sports.livescoreapi.Team;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MatchStartedEvent extends Event {

    private final LocalDateTime date;
    private final Team home;
    private final Team visitors;

    public MatchStartedEvent(UUID aggregateId, LocalDateTime timeStamp, String userId, String version,
                             LocalDateTime date, Team teamHome, Team visitors) {
        super(aggregateId, timeStamp, userId, version);
        this.date = date;
        this.home = teamHome;
        this.visitors = visitors;
    }
}
