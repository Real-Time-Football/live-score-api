package com.sports.livescoreapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public abstract class Event {
    private String aggregateId;
    private LocalDateTime timeStamp;
    private String userId;
    private String version;
}
