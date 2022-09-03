package com.moon.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public interface IMinecraft {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public default EntityPlayer getPlayer() {
        return mc.player;
    }

}
