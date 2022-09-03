package com.moon.impl.modules.client;

import com.moon.Moon;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.utils.render.ColorUtils;

import java.awt.*;

@Module.registration(name = "Notifications", description = "Notifications.", category = Module.Category.Client)
public class Notifications extends Module {

    public static Notifications Instance;

    public Setting<Boolean> modules = new BooleanSetting("Modules", true, this);

    public Notifications() {
        Instance = this;
    }

    private Color color =  ColorUtils.getWave(new Color(255, 255, 255, 255), 0);

    @Override
    public void onUpdate() {
        //Moon.Logger.info("New color? (Provided 255, 255, 255, 255) : " + "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ", " + color.getAlpha() + ")");
    }

}
