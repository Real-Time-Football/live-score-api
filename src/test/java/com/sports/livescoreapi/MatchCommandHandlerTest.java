package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.EndMatchCommand;
import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchCommandHandlerTest {

    private final String USER_ID = "user_x";
    private final String VERSION = "1";
    private final LocalDateTime DATE = LocalDateTime.now();
    private final String HOME = "PALMEIRAS";
    private final String VISITORS = "CORINTHIANS";

    @Test
    void handle_start_match() {
        EventBus eventBus = mock(EventBus.class);
        MatchCommandHandler matchCommandHandler = new MatchCommandHandler(eventBus);

        StartMatchCommand startCommand = new StartMatchCommand(USER_ID, VERSION, DATE, HOME, VISITORS);
        matchCommandHandler.handle(startCommand);

        verify(eventBus).post(any(MatchStartedEvent.class));
        assertTrue(matchCommandHandler.getMatch().isPlaying());
    }

    @Test
    void handle_score_goal() {
        EventBus eventBus = mock(EventBus.class);
        MatchCommandHandler matchCommandHandler = new MatchCommandHandler(eventBus);

        StartMatchCommand startCommand = new StartMatchCommand(USER_ID, VERSION, DATE, HOME, VISITORS);
        matchCommandHandler.handle(startCommand);

        ScoreCommand scoreHomeCommand = new ScoreCommand(startCommand.getAggregateId(), USER_ID, VERSION, TeamSide.HOME);
        matchCommandHandler.handle(scoreHomeCommand);

        ScoreCommand scoreVisitorsCommand = new ScoreCommand(startCommand.getAggregateId(), USER_ID, VERSION, TeamSide.VISITORS);
        matchCommandHandler.handle(scoreVisitorsCommand);

        verify(eventBus, times(2)).post(any(GoalScoredEvent.class));
        assertTrue(matchCommandHandler.getMatch().getScore().getHome() == 1);
        assertTrue(matchCommandHandler.getMatch().getScore().getHome() == 1);
    }

    @Test
    void handle_end_match() {
        EventBus eventBus = mock(EventBus.class);
        MatchCommandHandler matchCommandHandler = new MatchCommandHandler(eventBus);

        StartMatchCommand startCommand = new StartMatchCommand(USER_ID, VERSION, DATE, HOME, VISITORS);
        matchCommandHandler.handle(startCommand);

        EndMatchCommand endCommand = new EndMatchCommand(startCommand.getAggregateId(), USER_ID, VERSION);
        matchCommandHandler.handle(endCommand);

        verify(eventBus, times(1)).post(any(MatchEndedEvent.class));
        assertFalse(matchCommandHandler.getMatch().isPlaying());
    }
}