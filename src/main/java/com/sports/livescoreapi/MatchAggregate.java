package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class MatchAggregate extends Aggregate {

    private final EventBus eventBus;
    private MatchState matchState;
    private int homeScore;
    private int visitorsScore;

    public MatchAggregate(EventBus eventBus) {
        this.eventBus = eventBus;
        matchState = MatchState.NOT_STARTED;
    }

    @HandleStarterCommand
    public void on(MatchStartedEvent event) {
        // Set the ID of the saga
        this.setAggregateId(event.getAggregateId());
        matchState = MatchState.STARTED;
    }

    @HandleCommand
    public void on(GoalScoredEvent event) {
        if(matchState != MatchState.STARTED) {
            return;
        }

        switch (event.getTeamSide()) {
            case HOME:
                homeScore += 1;
                break;
            case  VISITORS:
                visitorsScore += 1;
                break;
        }
    }
}
