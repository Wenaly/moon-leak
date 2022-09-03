package com.moon.api.event.handler;

public class Event {
    private boolean isCancelled;

    public Event(){
        isCancelled = false;
    }
    public final boolean isCancelled() {
        return isCancelled;
    }

    public final void setCancelled(final boolean cancelled) {
        isCancelled = cancelled;
    }

    public final void cancel() {
        isCancelled = true;
    }

    public final void unCancel() {
        isCancelled = false;
    }
}