package com.sports.livescoreapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class MatchAggregate extends Aggregate {

    private boolean teamsArePlaying;
    private int homeScore;
    private int visitorsScore;

    public MatchAggregate(String matchId) {
        super(matchId);
    }

    public void start() {
        teamsArePlaying = true;
    }

    public void scoreForHome() {
        if (teamsArePlaying) {
            homeScore += 1;
        }
    }

    public void scoreForVisitors() {
        if (teamsArePlaying) {
            visitorsScore += 1;
        }
    }
}
