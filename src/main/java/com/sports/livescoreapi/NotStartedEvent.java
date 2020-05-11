package com.sports.livescoreapi;

import com.sports.livescoreapi.events.MatchStartedEvent;

public class NotStartedEvent extends MatchState {
    @Override
    public void apply(Match match, MatchStartedEvent event) {
        match.configure(event.getDate(), event.getHome(), event.getVisitors());
        match.startFirstPeriod();
        match.setState(new FirstPeriodState());
    }
}
