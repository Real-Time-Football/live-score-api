package com.sports.livescoreapi;

public class MatchAggregate extends Aggregate {

    private boolean teamsArePlaying;
    private int homeScore;
    private int visitorsScore;

    public MatchAggregate(String matchId) {
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
}
