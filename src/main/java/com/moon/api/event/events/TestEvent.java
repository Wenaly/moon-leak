package com.moon.api.event.events;

import com.moon.api.event.handler.Event;

public class TestEvent extends Event {
    public long startTime;
    public TestEvent(){
        startTime = System.nanoTime();
    }
}
