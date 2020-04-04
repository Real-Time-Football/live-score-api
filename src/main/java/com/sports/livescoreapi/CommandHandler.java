package com.sports.livescoreapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public abstract class CommandHandler {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private UUID aggregateId;
}
