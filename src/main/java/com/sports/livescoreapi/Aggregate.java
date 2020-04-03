package com.sports.livescoreapi;

import lombok.Getter;

public abstract class Aggregate {
    @Getter
    private String aggregateId;

    public Aggregate(String aggregateId) {
        this.aggregateId = aggregateId;
    }
}
