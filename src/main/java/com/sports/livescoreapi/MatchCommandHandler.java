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

    public MatchCommandHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @HandleStarterCommand
    public void handle(StartMatchCommand command) {
        this.setAggregateId(command.getAggregateId());

        eventBus.send(new MatchStartedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion()
        ));
    }

    @HandleCommand
    public void handle(ScoreCommand command) {
        eventBus.send(new GoalScoredEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion(),
                ""
        ));
    }
}
