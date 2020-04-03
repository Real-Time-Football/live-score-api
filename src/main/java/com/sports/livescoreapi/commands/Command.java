package com.sports.livescoreapi.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public abstract class Command {
    private String aggregateId;
    private LocalDateTime timeStamp;
    private String userId;
    private String version;
}
