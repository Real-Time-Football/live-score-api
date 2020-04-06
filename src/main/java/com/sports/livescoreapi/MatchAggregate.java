package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;

import java.util.UUID;

public class MatchAggregate extends Aggregate {

    private boolean teamsArePlaying;
    private int homeScore;
    private int visitorsScore;

    public MatchAggregate(UUID matchId) {
        super(matchId);
    }

    public boolean areTeamsPlaying() {
        return teamsArePlaying;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getVisitorsScore() {
        return visitorsScore;
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

    public void end() {
        teamsArePlaying = false;
    }

    public void apply(MatchStartedEvent matchStartedEvent) {
        start();
    }

    public void apply(GoalScoredEvent goalScoredEvent) {
        switch (goalScoredEvent.getTeamSide()) {
            case HOME:
                scoreForHome();
                break;
            case  VISITORS:
                scoreForVisitors();
                break;
        }
    }

    public void apply(MatchEndedEvent matchEndedEvent) {
        end();
    }
}
