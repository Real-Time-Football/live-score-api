package com.sports.livescoreapi.commands;

import com.sports.livescoreapi.events.EventBus;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class CommandBus {

    private final EventBus eventBus;

    public CommandBus(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @Getter
    private final Map<Class<?>, Class<?>> commandHandlerStarters = new HashMap<>();

    @Getter
    private final Map<UUID, Object> commandHandlerInstances = new HashMap<>();

    public void registerCommandHandlerStarter(Class<?> startEvent, Class<?> event)
    {
        commandHandlerStarters.putIfAbsent(startEvent, event);
    }

    public <T extends Command> void send(T command) throws ReflectiveOperationException {
        try {

            createHandlerGivenRegisteredStarter(command);

            createHandlerForAggregateId(command);

            deliverCommandToHandler(command);

        } catch (NoSuchMethodException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw e;
        } catch (InstantiationException e) {
            throw e;
        }

        //todo Log errors with some framework
    }

    private <T extends Command> void createHandlerGivenRegisteredStarter(T command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (commandHandlerStarters.containsKey(command.getClass()))
        {
            Class<?> handlerType = commandHandlerStarters.get(command.getClass());
            CommandHandler handlerInstance = (CommandHandler) handlerType.getDeclaredConstructor(EventBus.class).newInstance(eventBus);

            if (handlerInstance == null)
                return;

            commandHandlerInstances.putIfAbsent(command.getAggregateId(), handlerInstance);
        }
    }

    private <T extends Command> void createHandlerForAggregateId(T command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!commandHandlerInstances.containsKey(command.getAggregateId())) {
            for (Class<?> registeredHandler : commandHandlerStarters.values()) {
                CommandHandler handlerInstance = (CommandHandler) registeredHandler.getDeclaredConstructor(EventBus.class).newInstance(eventBus);

                if (handlerInstance == null)
                    continue;

                Optional<Method> handlerMethod = getMethodAnnotatedWith(command, handlerInstance.getClass(), HandleCommand.class);

                if(handlerMethod.isPresent()) {
                    commandHandlerInstances.putIfAbsent(command.getAggregateId(), handlerInstance);
                }
            }
        }
    }

    private <T extends Command> void deliverCommandToHandler(T command) throws InvocationTargetException, IllegalAccessException {
        if (commandHandlerInstances.containsKey(command.getAggregateId()))
        {
            Object commandHandler = commandHandlerInstances.get(command.getAggregateId());

            Optional<Method> handlerMethod = getMethodAnnotatedWith(command, commandHandler.getClass(), HandleCommand.class);

            if(!handlerMethod.isPresent()) {
                return;
            }

            handlerMethod.get().invoke(commandHandler, command);
        }
    }

    private <T extends Command> Optional<Method> getMethodAnnotatedWith(T command, Class<?> commandHandler, Class<? extends Annotation> annotation) {
        return Arrays.stream(commandHandler.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation)
                        && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == command.getClass())
                .findFirst();
    }
}

