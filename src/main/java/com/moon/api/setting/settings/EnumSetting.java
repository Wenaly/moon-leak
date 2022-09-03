package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;

import java.util.List;
import java.util.function.Predicate;

public class EnumSetting extends Setting<String> {

    private final List<String> modes;

    public EnumSetting(String name, String value, List<String> modes, Module parent) {
        super(name, value, parent);

        this.modes = modes;
    }

    public EnumSetting(String name, String value, List<String> modes, Module parent, Predicate<String> visible) {
        super(name, value, parent, visible);

        this.modes = modes;
    }

    public List<String> getModes() {
        return this.modes;
    }

    public void setValue(String value) {
        this.value = (this.modes.contains(value) ? value : this.value);
    }

    public void increment() {
        value = modes.get((modes.indexOf(this.value) + 1) % modes.size());
    }

    public String getValue() {
        return this.value;
    }

    public boolean isVisible(){
        if (visible == null){
            return true;
        }
        return visible.test(this.getValue());
    }

    @Override
    public String getType() {
        return "Enum";
    }
}
