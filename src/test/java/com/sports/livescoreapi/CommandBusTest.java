package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandBusTest {

    private final String DEFAULT_USER_ID = "user_x";
    private final String DEFAULT_VERSION = "1";

    @Test
    void register_command_handler_starter() {
        EventBus eventBus = mock(EventBus.class);
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        assertTrue(commandBus.getCommandHandlerStarters().containsKey(StartMatchCommand.class));
    }

    @Test
    void create_instance_of_command_handler() {
        EventBus eventBus = mock(EventBus.class);
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        StartMatchCommand startCommand = new StartMatchCommand(DEFAULT_USER_ID, DEFAULT_VERSION);
        commandBus.send(startCommand);

        assertEquals(1, commandBus.getCommandHandlerInstances().size());
    }

    @Test
    void deliver_command_to_handler() {
        EventBus eventBus = mock(EventBus.class);
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        StartMatchCommand startCommand = new StartMatchCommand(DEFAULT_USER_ID, DEFAULT_VERSION);
        commandBus.send(startCommand);

        ScoreCommand scoreCommand = new ScoreCommand(startCommand.getAggregateId(), DEFAULT_USER_ID, DEFAULT_VERSION, TeamSide.HOME);
        commandBus.send(scoreCommand);

        verify(eventBus, times(1)).send(any(MatchStartedEvent.class));
        verify(eventBus, times(1)).send(any(GoalScoredEvent.class));
    }
}