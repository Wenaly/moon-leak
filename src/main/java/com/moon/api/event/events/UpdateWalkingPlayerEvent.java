package com.moon.api.event.events;

import com.moon.api.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class UpdateWalkingPlayerEvent
        extends EventStage {
    public UpdateWalkingPlayerEvent(int stage) {
        super(stage);
    }
}

