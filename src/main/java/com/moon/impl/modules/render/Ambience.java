package com.moon.impl.modules.render;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.ColorSetting;
import com.moon.api.utils.render.ColorUtils;

@Module.registration(name = "Ambience", description = "", category = Module.Category.Render)
public class Ambience extends Module {

    public final Setting<ColorUtils> colorLight = new ColorSetting("Color", new ColorUtils(255, 255, 255, 255), this);

    public static Ambience INSTANCE;

    public Ambience() {
        INSTANCE = this;
    }

}
