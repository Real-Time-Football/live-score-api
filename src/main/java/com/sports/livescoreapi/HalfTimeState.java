package com.sports.livescoreapi;

import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.PeriodStartedEvent;

public class HalfTimeState extends MatchState {
    @Override
    public void apply(Match match, PeriodStartedEvent event) {
        match.startSecondPeriod();
        match.setState(new SecondPeriodState());
    }

    @Override
    public void apply(Match match, MatchEndedEvent matchEndedEvent) {
        match.end();
        match.setState(new EndedState());
    }
}
