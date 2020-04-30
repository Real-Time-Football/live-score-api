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
    private MatchPeriod period;

    public Match(UUID matchId) {
        super(matchId);
        score = new Score();
        period = MatchPeriod.NONE;
    }

    public void start() {
        playing = true;
        startPeriod();
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
        this.home = matchStartedEvent.getHome();
        this.visitors = matchStartedEvent.getVisitors();
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

    public void startPeriod() {
        if (period == MatchPeriod.NONE) {
            period = MatchPeriod.FIRST_PERIOD;
        } else if (period == MatchPeriod.HALF_TIME) {
            period = MatchPeriod.SECOND_PERIOD;
        }
    }

    public void stopPeriod() {
        if (period == MatchPeriod.FIRST_PERIOD) {
            period = MatchPeriod.HALF_TIME;
        } else if (period == MatchPeriod.SECOND_PERIOD) {
            period = MatchPeriod.FULL_TIME;
        }
    }
}
