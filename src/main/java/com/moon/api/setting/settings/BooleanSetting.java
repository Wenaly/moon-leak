package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;

import java.util.function.Predicate;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, Boolean value, Module parent) {
        super(name, value, parent);
    }

    public BooleanSetting(String name, boolean value, Module parent, Predicate<Boolean> shown) {
        super(name, value, parent, shown);
    }

    public Boolean getValue() {
        return value;
    }

    public boolean isVisible(){
        if (visible == null){
            return true;
        }
        return visible.test(this.getValue());
    }

    @Override
    public String getType() {
        return "Boolean";
    }
}