package com.sports.livescoreapi;

import lombok.Value;

import java.time.LocalDate;

@Value(staticConstructor = "of")
public class MatchId {
    private LocalDate date;
    private Team home;
    private Team visitors;
}
