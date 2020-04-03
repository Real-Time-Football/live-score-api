package com.sports.livescoreapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Aggregate {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String aggregateId;

    public Aggregate(String aggregateId) {
        this.aggregateId = aggregateId;
    }
}
