package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;

public class MatchAggregate extends Aggregate {

    private final EventBus eventBus;

    public MatchAggregate(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @StartWithMessage
    public void startMatch(MatchStartedEvent event) {
        // Set the ID of the saga
        this.setAggregateId(event.getAggregateId());
    }

    @CanHandleMessage
    public void handle(GoalScoredEvent event) {

    }
}
