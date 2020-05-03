package com.sports.livescoreapi.commands;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class EndPeriodCommand extends Command {
    public EndPeriodCommand(UUID aggregateId, String userId, String version) {
        super(aggregateId, LocalDateTime.now(), userId, version);
    }
}
