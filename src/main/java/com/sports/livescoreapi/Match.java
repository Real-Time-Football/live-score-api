package com.sports.livescoreapi;

import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Match extends Aggregate {

    private boolean playing;
    private LocalDateTime date;
    private Team home;
    private Team visitors;
    private Score score;

    public Match(UUID matchId) {
        super(matchId);
        score = new Score();
    }

    public void start() {
        playing = true;
    }

    public void scoreForHome() {
        if (playing) {
            score.incrementHome();
        }
    }

    public void scoreForVisitors() {
        if (playing) {
            score.incrementVisitors();
        }
    }

    public void end() {
        playing = false;
    }

    public void apply(MatchStartedEvent matchStartedEvent) {
        this.date = matchStartedEvent.getDate();
        this.home = Team.of(matchStartedEvent.getTeamHome());
        this.visitors = Team.of(matchStartedEvent.getTeamVisitors());
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
