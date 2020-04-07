package com.sports.livescoreapi.commands;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class Command {
    private UUID aggregateId;
    private LocalDateTime timeStamp;
    private String userId;
    private String version;

    public Command(UUID aggregateId, LocalDateTime timeStamp, String userId, String version) {
        this.aggregateId = aggregateId;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.version = version;
    }
}
