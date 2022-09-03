package com.moon.impl.modules.misc;

import com.moon.DiscordPresence;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;

@Module.registration(name = "RPC", description = "Skid Moment", category = Module.Category.Misc)
public class RPC extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = new BooleanSetting("ShowIp", true, this);

    public RPC() {
        RPC.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}
