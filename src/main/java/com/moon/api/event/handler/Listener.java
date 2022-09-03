package com.moon.api.event.handler;

import com.moon.api.priorities.Priorities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;


public final class Listener {
    public final Method method;
    public final Object object;
    public final Class<?> event;
    public final Priorities priority;

    public Listener(@NotNull final Method method, @NotNull final Object object, @NotNull  final Class<?> event, @NotNull  final Priorities priority){
        this.method = method;
        this.object = object;
        this.event = event;
        this.priority = priority;
    }
}
