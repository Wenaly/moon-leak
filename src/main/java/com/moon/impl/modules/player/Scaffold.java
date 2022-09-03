package com.moon.impl.modules.player;

import com.moon.Moon;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.entity.RotationUtils;

import java.util.Arrays;

@Module.registration(name = "Scaffold", description = "", category = Module.Category.Player)
public class Scaffold extends Module {

    private Setting<String> mode = new EnumSetting("Mode", "Custom", Arrays.asList("Custom", "AAC", "Vanilla"), this);
    private Setting<Integer> yaw = new IntegerSetting("", 0, -90, 90, this, s -> this.mode.equals("Custom")); // head
    private Setting<Integer> pitch = new IntegerSetting("", 0, -360, 360, this, s -> this.mode.equals("Custom")); // body
    private Setting<Integer> aacYawOffset = new IntegerSetting("AACYawOffset", 0, 0, 20, this, s -> this.mode.equals("AAC"));

    @Override
    public void onEnable() {
        Moon.getRotationManager().setRotations(yaw.getValue(), pitch.getValue());
    }

    @Override
    public void onUpdate() {
        Moon.getRotationManager().setRotations(yaw.getValue(), pitch.getValue());
    }

    @Override
    public void onDisable() {
        Moon.getRotationManager().resetRotations();
    }

}
