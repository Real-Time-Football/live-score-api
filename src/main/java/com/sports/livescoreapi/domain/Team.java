package com.sports.livescoreapi.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class Team {
    private String name;
}
