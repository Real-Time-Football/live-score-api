package com.sports.livescoreapi.events;

import com.sports.livescoreapi.TeamSide;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class GoalScoredEvent extends Event {
    private TeamSide teamSide;

    public GoalScoredEvent() {}

    public GoalScoredEvent(UUID aggregateId, LocalDateTime timeStamp, String userId, String version, TeamSide teamSide) {
        super(aggregateId, timeStamp, userId, version);
        this.teamSide = teamSide;
    }
}
