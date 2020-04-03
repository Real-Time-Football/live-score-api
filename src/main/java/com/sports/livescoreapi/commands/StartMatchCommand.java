package com.sports.livescoreapi.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public class StartMatchCommand extends Command {

    public StartMatchCommand(String userId, String version) {
        super(UUID.randomUUID().toString(), LocalDateTime.now(), userId, version);
    }

}
