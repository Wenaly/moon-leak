package com.moon.api.event.events;

import com.moon.api.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class DeathEvent extends EventStage {
    public EntityPlayer player;

    public DeathEvent(EntityPlayer player) {
        this.player = player;
    }
}

