package com.sports.livescoreapi;

import lombok.Value;

@Value(staticConstructor = "of")
public class Team {
    private String name;
}
