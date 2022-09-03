package com.moon.impl.modules.render;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.IntegerSetting;

@Module.registration(name = "CustomFov", description = "Europa Moment", category = Module.Category.Render)
public class CustomFov extends Module {
    private final Setting<Integer> fov = new IntegerSetting("Fov", 120, 0, 175, this);
    private float oldFov;

    @Override
    public void onEnable() {
        this.oldFov = CustomFov.mc.gameSettings.fovSetting;
    }

    @Override
    public void onUpdate() {
        CustomFov.mc.gameSettings.fovSetting = fov.getValue().floatValue();
    }

    @Override
    public void onDisable() {
        CustomFov.mc.gameSettings.fovSetting = this.oldFov;
    }
}
