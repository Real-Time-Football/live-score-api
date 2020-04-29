package com.sports.livescoreapi.fixtures;

import com.sports.livescoreapi.Team;
import com.sports.livescoreapi.events.MatchStartedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class MatchStartedEventBuilder {
    private UUID aggregateId = UUID.randomUUID();
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String userId = "user_x";
    private String version = "1";
    private LocalDateTime date = LocalDateTime.of(2020, 4, 28, 21, 30);
    private Team home = Team.of("TIME DA CASA");
    private Team visitors = Team.of("TIME VISITANTE");

    public MatchStartedEventBuilder() {}

    public MatchStartedEventBuilder withAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public MatchStartedEventBuilder withTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public MatchStartedEventBuilder withDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public MatchStartedEventBuilder withHome(String teamName) {
        this.home = Team.of(teamName);
        return this;
    }

    public MatchStartedEventBuilder withVisitors(String teamName) {
        this.visitors = Team.of(teamName);
        return this;
    }

    public MatchStartedEvent build() {
        return new MatchStartedEvent(aggregateId, timeStamp, userId, version, date, home, visitors);
    }

}
