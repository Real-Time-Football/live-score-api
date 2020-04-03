package com.sports.livescoreapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class CommandHandler {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String aggregateId;
}
