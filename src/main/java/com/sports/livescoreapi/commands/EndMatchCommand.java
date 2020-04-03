package com.sports.livescoreapi.commands;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EndMatchCommand extends Command {

    public EndMatchCommand(String aggregateId, String userId, String version) {
        super(aggregateId, LocalDateTime.now(), userId, version);
    }
}
