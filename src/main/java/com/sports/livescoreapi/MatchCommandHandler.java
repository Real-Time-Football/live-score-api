package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class MatchCommandHandler extends CommandHandler {

    private final EventBus eventBus;
    private MatchAggregate match;

    public MatchCommandHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @HandleStarterCommand
    public void handle(StartMatchCommand command) {

        this.setAggregateId(command.getAggregateId());

        match = new MatchAggregate(this.getAggregateId());

        match.start();

        eventBus.post(new MatchStartedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion()
        ));
    }

    @HandleCommand
    public void handle(ScoreCommand command) {

        if (match == null) {
            return;
        }

        switch (command.getTeamSide()) {
            case HOME:
                match.scoreForHome();
                break;
            case  VISITORS:
                match.scoreForVisitors();
                break;
        }

        eventBus.post(new GoalScoredEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion(),
                command.getTeamSide()
        ));
    }
}
