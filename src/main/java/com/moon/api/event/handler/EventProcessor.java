package com.moon.api.event.handler;

import com.moon.api.priorities.Priorities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public final class EventProcessor {

    private final List<Listener> events;

    public EventProcessor() {
        events = new ArrayList<>();
    }

    public final void addEventListener(final @NotNull Object object) throws IllegalArgumentException {
        getEvents(object);
    }

    public final void removeEventListener(final @NotNull Object object) {
        events.removeIf(listener -> listener.object == object);
    }
    private void getEvents(final @NotNull Object object) {
        final Class<?> clazz = object.getClass();
        Arrays.stream(clazz.getDeclaredMethods()).spliterator().forEachRemaining(method -> {
            if (method.isAnnotationPresent(EventHandler.class)) {
                final Class<?>[] prams = method.getParameterTypes();
                if (prams.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " doesnt have any event parameters");
                }
                if (!Event.class.isAssignableFrom(prams[0])) {
                    throw new IllegalArgumentException("Method " + method + " doesnt have any event parameters only non event parameters");
                }
                this.events.add(new Listener(method, object, prams[0], getPriority(method)));
                this.events.sort(Comparator.comparing(o -> o.priority));
            }
        });
    }

    public final boolean postEvent(final @NotNull Event event) {
        if (events.isEmpty()) return false;
        events.spliterator().forEachRemaining(listener -> {
            if(listener.event == event.getClass()){
                listener.method.setAccessible(true);
                try {
                    listener.method.invoke(listener.object, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private Priorities getPriority(final @NotNull Method method) {
        return method.getAnnotation(EventHandler.class).priority();
    }
}
