package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.CommandBus;
import com.sports.livescoreapi.commands.MatchCommandHandler;
import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import com.sports.livescoreapi.domain.TeamSide;
import com.sports.livescoreapi.events.EventBus;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandBusTest {

    private final String USER_ID = "user_x";
    private final String VERSION = "1";
    private final String HOME = "PALMEIRAS";
    private final String VISITORS = "CORINTHIANS";

    @Test
    void register_command_handler_starter() {
        EventBus eventBus = mock(EventBus.class);
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        assertTrue(commandBus.getCommandHandlerStarters().containsKey(StartMatchCommand.class));
    }

    @Test
    void create_instance_of_command_handler() throws Throwable {
        EventBus eventBus = mock(EventBus.class);
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        StartMatchCommand startCommand = new StartMatchCommand(USER_ID, VERSION, LocalDateTime.now(), HOME, VISITORS);
        commandBus.send(startCommand);

        assertEquals(1, commandBus.getCommandHandlerInstances().size());
    }

    @Test
    void deliver_command_to_handler() throws Throwable {
        EventBus eventBus = mock(EventBus.class);
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        StartMatchCommand startCommand = new StartMatchCommand(USER_ID, VERSION, LocalDateTime.now(), HOME, VISITORS);
        commandBus.send(startCommand);

        ScoreCommand scoreCommand = new ScoreCommand(startCommand.getAggregateId(), USER_ID, VERSION, TeamSide.HOME);
        commandBus.send(scoreCommand);

        verify(eventBus, times(1)).post(any(MatchStartedEvent.class));
        verify(eventBus, times(1)).post(any(GoalScoredEvent.class));
    }
}