package com.sports.livescoreapi.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Event {

    @Id
    private String id;

    private UUID aggregateId;
    private LocalDateTime timeStamp;
    private String userId;
    private String version;

    public Event() {}

    public Event(UUID aggregateId, LocalDateTime timeStamp, String userId, String version) {
        this.aggregateId = aggregateId;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.version = version;
    }
}
