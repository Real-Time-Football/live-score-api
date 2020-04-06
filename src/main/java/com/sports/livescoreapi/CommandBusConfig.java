package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.StartMatchCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandBusConfig {

    @Bean
    CommandBus registerStarters(CommandBus commandBus) {
        commandBus.registerCommandHandlerStarter(StartMatchCommand.class, MatchCommandHandler.class);
        return commandBus;
    }
}
