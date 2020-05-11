package com.sports.livescoreapi;

import com.sports.livescoreapi.events.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Match extends Aggregate {

    private boolean ballInPlay;
    private MatchPlayingStatus status;
    private LocalDateTime date;
    private Team home;
    private Team visitors;
    private Score score;
    private MatchPeriod currentPeriod;
    private Optional<Score> firstPeriodScore;
    private Optional<Score> secondPeriodScore;

    private MatchState currentState;

    public Match(UUID matchId) {
        super(matchId);
        status = MatchPlayingStatus.PENDING;
        score = new Score();
        currentPeriod = MatchPeriod.NONE;
        firstPeriodScore = Optional.empty();
        secondPeriodScore = Optional.empty();
        currentState = new NotStartedEvent();
    }

    public void configure(LocalDateTime date, Team home, Team visitors) {
        this.date = date;
        this.home = home;
        this.visitors = visitors;
    }

    public void setState(MatchState matchState) {
        currentState = matchState;
    }

    public void score(TeamSide teamSide) {
        if (teamSide == TeamSide.HOME) {
            score.incrementHome();
        } else if(teamSide == TeamSide.VISITORS) {
            score.incrementVisitors();
        }
    }

    public void end() {
        ballInPlay = false;
        status = MatchPlayingStatus.ENDED;
    }

    public void startFirstPeriod() {
        status = MatchPlayingStatus.PLAYING;
        currentPeriod = MatchPeriod.FIRST_PERIOD;
        ballInPlay = true;
    }

    public void startSecondPeriod() {
        currentPeriod = MatchPeriod.SECOND_PERIOD;
        ballInPlay = true;
    }

    public void endFirstPeriod() {
        currentPeriod = MatchPeriod.HALF_TIME;
        firstPeriodScore = Optional.of(score);
        ballInPlay = false;
    }

    public void endSecondPeriod() {
        currentPeriod = MatchPeriod.FULL_TIME;
        secondPeriodScore = Optional.of(score);
        ballInPlay = false;
    }

    public void apply(PeriodEndedEvent event) {
        currentState.apply(this, event);
    }

    public void apply(MatchStartedEvent event) {
        currentState.apply(this, event);
    }

    public void apply(GoalScoredEvent event) {
        currentState.apply(this, event);
    }

    public void apply(MatchEndedEvent event) {
        currentState.apply(this, event);
    }

    public void apply(PeriodStartedEvent event) {
        currentState.apply(this, event);
    }
}
