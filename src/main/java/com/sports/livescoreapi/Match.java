package com.sports.livescoreapi;

import com.sports.livescoreapi.events.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Match extends Aggregate {

    private boolean ballInPlay;
    private LocalDateTime date;
    private Team home;
    private Team visitors;
    private Score score;
    private MatchPeriod currentPeriod;
    private Optional<Score> firstPeriodScore;
    private Optional<Score> secondPeriodScore;

    public Match(UUID matchId) {
        super(matchId);
        score = new Score();
        currentPeriod = MatchPeriod.NONE;
        firstPeriodScore = Optional.empty();
        secondPeriodScore = Optional.empty();
    }

    public void start() {
        ballInPlay = true;
        startPeriod();
    }

    public void scoreForHome() {
        if (ballInPlay) {
            score.incrementHome();
        }
    }

    public void scoreForVisitors() {
        if (ballInPlay) {
            score.incrementVisitors();
        }
    }

    public void end() {
        ballInPlay = false;
    }

    public void startPeriod() {
        switch (currentPeriod) {
            case NONE:
                currentPeriod = MatchPeriod.FIRST_PERIOD;
                ballInPlay = true;
                break;
            case HALF_TIME:
                currentPeriod = MatchPeriod.SECOND_PERIOD;
                ballInPlay = true;
                break;
        }
    }

    public void endPeriod() {
        switch (currentPeriod) {
            case FIRST_PERIOD:
                currentPeriod = MatchPeriod.HALF_TIME;
                firstPeriodScore = Optional.of(score);
                ballInPlay = false;
                break;
            case SECOND_PERIOD:
                currentPeriod = MatchPeriod.FULL_TIME;
                secondPeriodScore = Optional.of(score);
                ballInPlay = false;
                break;
        }
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

    public void apply(PeriodEndedEvent periodEndedEvent) {
        endPeriod();
    }

    public void apply(PeriodStartedEvent periodStartedEvent) {
        startPeriod();
    }
}
