package com.sports.livescoreapi.events;

import com.sports.livescoreapi.TeamSide;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class GoalScoredEvent extends Event {
    private final TeamSide teamSide;

    public GoalScoredEvent(String matchId, LocalDateTime timeStamp, String userId, String version, TeamSide teamSide) {
        super(matchId, timeStamp, userId, version);
        this.teamSide = teamSide;
    }
}
