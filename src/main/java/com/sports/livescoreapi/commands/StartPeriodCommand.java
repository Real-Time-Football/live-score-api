package com.sports.livescoreapi.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public class StartPeriodCommand extends Command {
    public StartPeriodCommand(UUID aggregateId, String userId, String version) {
        super(aggregateId, LocalDateTime.now(), userId, version);
    }
}
