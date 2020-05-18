package com.sports.livescoreapi.events;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByAggregateId(UUID aggregateId);
}
