package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.*;
import com.sports.livescoreapi.events.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class MatchCommandHandler extends CommandHandler {

    private final EventBus eventBus;
    private Match match;

    public MatchCommandHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @HandleStarterCommand
    public void handle(StartMatchCommand command) {

        this.setAggregateId(command.getAggregateId());

        match = new Match(this.getAggregateId());

        MatchStartedEvent event = new MatchStartedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion(),
                command.getDate(),
                Team.of(command.getTeamHome()),
                Team.of(command.getTeamVisitors())
        );

        match.apply(event);

        eventBus.post(event);
    }

    @HandleCommand
    public void handle(ScoreCommand command) {

        if (match == null) {
            return;
            //todo apply validation and raise exception event
        }

        GoalScoredEvent event = new GoalScoredEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion(),
                command.getTeamSide()
        );

        match.apply(event);

        eventBus.post(event);
    }

    @HandleCommand
    public void handle(EndMatchCommand command) {

        if (match == null) {
            return;
        }

        MatchEndedEvent event = new MatchEndedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion()
        );

        match.apply(event);

        eventBus.post(event);
    }

    @HandleCommand
    public void handle(EndPeriodCommand command) {
        if (match == null) {
            return;
        }

        PeriodEndedEvent event = new PeriodEndedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion()
        );

        match.apply(event);

        eventBus.post(event);
    }

    @HandleCommand
    public void handle(StartPeriodCommand command) {
        if (match == null) {
            return;
        }

        PeriodStartedEvent event = new PeriodStartedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion()
        );

        match.apply(event);

        eventBus.post(event);
    }
}
