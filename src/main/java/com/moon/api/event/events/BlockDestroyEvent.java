package com.moon.api.event.events;

import com.moon.api.event.handler.Event;
import net.minecraft.util.math.BlockPos;

public class BlockDestroyEvent extends Event {
    private BlockPos blockPos;

    public BlockDestroyEvent(BlockPos blockPos) {
        super();
        this.blockPos = blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

}
