package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MatchIntegrationTest {

    @Autowired
    EventBus eventBus;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MatchQueryHandler matchQueryHandler;

    private final String USER_ID = "user_x";
    private final String VERSION = "1";
    private final String HOME = "PALMEIRAS";
    private final String VISITORS = "CORINTHIANS";

    @Test
    void deliver_command_to_handler() throws Throwable {
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        StartMatchCommand startCommand = new StartMatchCommand(USER_ID, VERSION, LocalDateTime.now(), HOME, VISITORS);
        commandBus.send(startCommand);

        ScoreCommand scoreCommand = new ScoreCommand(startCommand.getAggregateId(), USER_ID, VERSION, TeamSide.HOME);
        commandBus.send(scoreCommand);

        Optional<Match> match = matchQueryHandler.getMatch(startCommand.getAggregateId());

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("home", "visitors").containsExactly(Team.of("PALMEIRAS"), Team.of("CORINTHIANS"));
        assertThat(match.get()).extracting("score.home", "score.visitors", "playing").containsExactly(1, 0, true);
    }
}
