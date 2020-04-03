package com.sports.livescoreapi;

import com.sports.livescoreapi.events.Event;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class EventBus {
    @Getter
    private final Map<Class<?>, Class<?>> aggregateStarters = new HashMap<>();
    @Getter
    private final Map<String, Object> aggregateInstances = new HashMap<>();

    private final EventRepository eventStore;

    public EventBus(EventRepository eventStore) {
        this.eventStore = eventStore;
    }

    public void registerEvent(Class<?> startEvent, Class<?> event)
    {
        aggregateStarters.putIfAbsent(startEvent, event);
    }

    public <T extends Event> void send(T event) {

        tryStartRegisteredAggregate(event);

        tryDeliverEventToAggregate(event);

        eventStore.save(event);
    }

    private <T extends Event> void tryStartRegisteredAggregate(T event) {
        if (aggregateStarters.containsKey(event.getClass()))
        {
            Class<?> eventType = aggregateStarters.get(event.getClass());
            Aggregate aggregateInstance;

            try {
                aggregateInstance = (Aggregate) eventType.getDeclaredConstructor(EventBus.class).newInstance(this);
            } catch (InstantiationException e) {
                System.out.println(e);
                return;
            } catch (IllegalAccessException e){
                System.out.println(e);
                return;
            } catch (NoSuchMethodException e) {
                System.out.println(e);
                return;
            } catch (InvocationTargetException e) {
                System.out.println(e);
                return;
            }

            if (aggregateInstance == null)
                return;

            Optional<Method> starterMethod = getMethodAnnotatedWith(event, aggregateInstance.getClass(), HandleStarterCommand.class);

            if(!starterMethod.isPresent()) {
                return;
            }

            try {
                starterMethod.get().invoke(aggregateInstance, event);
            } catch (IllegalAccessException e) {
                System.out.println(e);
                return;
            } catch (InvocationTargetException e) {
                System.out.println(e);
                return;
            }

            aggregateInstances.putIfAbsent(aggregateInstance.getAggregateId(), aggregateInstance);
        }
    }

    private <T extends Event> void tryDeliverEventToAggregate(T event) {

        if (aggregateInstances.containsKey(event.getAggregateId()))
        {
            Object aggregate = aggregateInstances.get(event.getAggregateId());

            Optional<Method> handler = getMethodAnnotatedWith(event, aggregate.getClass(), HandleCommand.class);

            if(!handler.isPresent()) {
                return;
            }

            try {
                handler.get().invoke(aggregate, event);
            } catch (IllegalAccessException e) {
                System.out.println(e);
                return;
            } catch (InvocationTargetException e) {
                System.out.println(e);
                return;
            }
        }
    }

    private <T extends Event> Optional<Method> getMethodAnnotatedWith(T event, Class<?> aggregate, Class<? extends Annotation> annotation) {
        return Arrays.stream(aggregate.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation)
                        && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == event.getClass())
                .findFirst();
    }
}

