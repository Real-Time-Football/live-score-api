package com.sports.livescoreapi.domain;

import lombok.Getter;

import java.util.UUID;

public abstract class Aggregate {
    @Getter
    private UUID aggregateId;

    public Aggregate(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }
}
