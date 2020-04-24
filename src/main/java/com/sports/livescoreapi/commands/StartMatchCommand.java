package com.sports.livescoreapi.commands;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class StartMatchCommand extends Command {

    private final LocalDateTime date;
    private final String teamHome;
    private final String teamVisitors;

    public StartMatchCommand(String userId, String version, LocalDateTime date, String teamHome, String teamVisitors) {
        super(UUID.randomUUID(), LocalDateTime.now(), userId, version);
        this.date = date;
        this.teamHome = teamHome;
        this.teamVisitors = teamVisitors;
    }

}
