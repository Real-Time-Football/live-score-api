package com.sports.livescoreapi.domain;

import com.sports.livescoreapi.events.GoalScoredEvent;

public class EndedState extends MatchState {
    @Override
    public void apply(Match match, GoalScoredEvent goalScoredEvent) { }
}
