package com.sports.livescoreapi.events;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class GoalScoredEvent extends Event {
    private final String team;

    public GoalScoredEvent(String matchId, LocalDateTime timeStamp, String userId, String version, String team) {
        super(matchId, timeStamp, userId, version);
        this.team = team;
    }
}
