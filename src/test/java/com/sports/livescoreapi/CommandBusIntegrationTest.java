package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static com.sports.livescoreapi.fixtures.MatchStartedEventFixture.aMatchStartedEvent;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommandBusIntegrationTest {

    @Autowired
    EventBus eventBus;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MatchQueryHandler matchQueryHandler;

    private final String USER_ID = "user_x";
    private final String VERSION = "1";

    @Test
    void deliver_command_to_existent_handler() throws Throwable {
        CommandBus commandBus = new CommandBus(eventBus);
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);

        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = aMatchStartedEvent()
                .withAggregateId(aggregateId)
                .withHome("PALMEIRAS")
                .withVisitors("CORINTHIANS")
                .build();

        eventRepository.save(matchStartedEvent);

        ScoreCommand scoreCommand = new ScoreCommand(aggregateId, USER_ID, VERSION, TeamSide.HOME);
        commandBus.send(scoreCommand);

        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        assertThat(match.isPresent()).isTrue();
        assertThat(match.get()).extracting("score.home", "score.visitors", "ballInPlay").containsExactly(1, 0, true);
    }
}
