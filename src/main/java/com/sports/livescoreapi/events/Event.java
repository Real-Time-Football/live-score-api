package com.sports.livescoreapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class Event {
    private UUID aggregateId;
    private LocalDateTime timeStamp;
    private String userId;
    private String version;
}
