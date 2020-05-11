package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.PeriodEndedEvent;

public class SecondPeriodState extends MatchState {
    @Override
    public void apply(Match match, PeriodEndedEvent event) {
        match.endSecondPeriod();
        match.setState(new FullTimeState());
    }

    @Override
    public void apply(Match match, GoalScoredEvent goalScoredEvent) {
        match.score(goalScoredEvent.getTeamSide());
    }

    @Override
    public void apply(Match match, MatchEndedEvent matchEndedEvent) {
        match.end();
        match.setState(new EndedState());
    }
}
