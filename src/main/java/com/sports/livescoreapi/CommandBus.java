package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.Command;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CommandBus {

    private EventBus eventBus;

    public CommandBus(EventBus eventBus) {

        this.eventBus = eventBus;
    }

    @Getter
    private final Map<Class<?>, Class<?>> commandHandlerStarters = new HashMap<>();

    @Getter
    private final Map<String, Object> commandHandlerInstances = new HashMap<>();

    public void registerCommandHandlerStarter(Class<?> startEvent, Class<?> event)
    {
        commandHandlerStarters.putIfAbsent(startEvent, event);
    }

    public <T extends Command> void send(T command) {
        startRegisteredHandler(command);
        deliverCommandToHandler(command);
    }

    private <T extends Command> void startRegisteredHandler(T command) {
        if (commandHandlerStarters.containsKey(command.getClass()))
        {
            Class<?> commandType = commandHandlerStarters.get(command.getClass());
            CommandHandler handlerInstance;

            try {
                handlerInstance = (CommandHandler) commandType.getDeclaredConstructor(EventBus.class).newInstance(eventBus);
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

            if (handlerInstance == null)
                return;

            Optional<Method> starterMethod = getMethodAnnotatedWith(command, handlerInstance.getClass(), StartWithMessage.class);

            if(!starterMethod.isPresent()) {
                return;
            }

            try {
                starterMethod.get().invoke(handlerInstance, command);
            } catch (IllegalAccessException e) {
                System.out.println(e);
                return;
            } catch (InvocationTargetException e) {
                System.out.println(e);
                return;
            }

            commandHandlerInstances.putIfAbsent(handlerInstance.getAggregateId(), handlerInstance);
        }
    }

    private <T extends Command> void deliverCommandToHandler(T command) {

        if (commandHandlerInstances.containsKey(command.getAggregateId()))
        {
            Object commandHandler = commandHandlerInstances.get(command.getAggregateId());

            Optional<Method> handlerMethod = getMethodAnnotatedWith(command, commandHandler.getClass(), CanHandleMessage.class);

            if(!handlerMethod.isPresent()) {
                return;
            }

            try {
                handlerMethod.get().invoke(commandHandler, command);
            } catch (IllegalAccessException e) {
                System.out.println(e);
                return;
            } catch (InvocationTargetException e) {
                System.out.println(e);
                return;
            }
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

