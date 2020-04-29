package com.sports.livescoreapi.fixtures;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventFixture {
    public static LocalDateTime anEventTime(int hour, int min) {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, min));
    }
}
