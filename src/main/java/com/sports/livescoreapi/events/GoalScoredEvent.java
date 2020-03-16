package com.sports.livescoreapi.events;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class GoalScoredEvent extends Event {
    public GoalScoredEvent(String matchId, LocalDateTime timeStamp, String userId, String version) {
        super(matchId, timeStamp, userId, version);
    }
}
