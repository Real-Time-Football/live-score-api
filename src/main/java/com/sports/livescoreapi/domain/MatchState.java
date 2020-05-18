package com.sports.livescoreapi.domain;

import com.sports.livescoreapi.events.*;

public abstract class MatchState {
    public void apply(Match match, MatchStartedEvent matchStartedEvent) { }

    public void apply(Match match, GoalScoredEvent goalScoredEvent) { }

    public void apply(Match match, MatchEndedEvent matchEndedEvent) { }

    public void apply(Match match, PeriodEndedEvent periodEndedEvent) { }

    public void apply(Match match, PeriodStartedEvent periodStartedEvent) { }
}
