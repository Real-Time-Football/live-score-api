package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;

public interface EventRepository {
    void save(Event message);
}
