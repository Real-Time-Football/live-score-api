package com.sports.livescoreapi.domain;

import lombok.Getter;

@Getter
public class Score {
    private int home;
    private int visitors;

    public Score() {
        this.home = 0;
        this.visitors = 0;
    }

    public void incrementHome() {
        home += 1;
    }

    public void incrementVisitors() {
        visitors += 1;
    }
}
