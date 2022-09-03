package com.moon.api.event.events;

import com.moon.api.event.EventStage;
import net.minecraft.entity.Entity;

public class EntityPushEvent
        extends EventStage {
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    public EntityPushEvent(Entity entity, double x, double y, double z, boolean airbone) {
        super(0);
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
    }

    public EntityPushEvent(int stage) {
        super(stage);
    }

    public EntityPushEvent(int stage, Entity entity) {
        super(stage);
        this.entity = entity;
    }
}

