package com.moon.impl.modules.movement;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.EnumSetting;

import java.util.Arrays;

public class Speed extends Module {

    private Setting<String> mode = new EnumSetting("Mode", "Strafe", Arrays.asList("Strafe", "NCP"), this);

    @Override
    public void onEnable() {

    }

    @Override
    public void onUpdate() {
        switch (mode.getValue()) {
            case "Strafe":
                break;
            case "NCP":
                break;
        }
    }

}