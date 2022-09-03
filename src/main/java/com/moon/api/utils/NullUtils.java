package com.moon.api.utils;

public interface NullUtils {

    default boolean nullCheck() {
        return IMinecraft.mc.player == null || IMinecraft.mc.world == null;
    }

}
