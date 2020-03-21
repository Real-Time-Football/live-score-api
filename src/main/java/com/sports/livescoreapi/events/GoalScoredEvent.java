package com.sports.livescoreapi.events;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class GoalScoredEvent extends Event {
    private final String team;

    public GoalScoredEvent(String matchId, LocalDateTime timeStamp, String userId, String version, String team) {
        super(matchId, timeStamp, userId, version);
        this.team = team;
    }
}
