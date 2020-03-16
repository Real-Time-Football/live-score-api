package com.sports.livescoreapi.events;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public abstract class Event {
    private String aggregateId;
    private LocalDateTime timeStamp;
    private String userId;
    private String version;

    public Event(@NonNull String aggregateId, LocalDateTime timeStamp, String userId, String version) {
        this.aggregateId = aggregateId;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.version = version;
    }
}
