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

    @StartWithMessage
    public void startMatch(StartMatchCommand command) {
        this.setAggregateId(command.getAggregateId());

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion()
        );

        eventBus.send(matchStartedEvent);
    }

    @CanHandleMessage
    public void handle(ScoreCommand command) {
        GoalScoredEvent goalScoredEvent = new GoalScoredEvent(
                command.getAggregateId(),
                command.getTimeStamp(),
                command.getUserId(),
                command.getVersion(),
                ""
        );

        eventBus.send(goalScoredEvent);
    }
}
