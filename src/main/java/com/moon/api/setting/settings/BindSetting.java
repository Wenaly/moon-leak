package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.util.function.Predicate;


public class BindSetting extends Setting<Integer> {

    public BindSetting(String name, int value, Module parent) {
        super(name, value, parent);
    }

    public BindSetting(String name, int value, Module parent, Predicate<Integer> visible) {
        super(name, value, parent, visible);
    }

    public int getKey() {
        return this.value;
    }

    public void setKey(int key) {
        this.value = key;
    }

    public boolean isDown() {
        if (this.value <= 0) return false;
        return Keyboard.isKeyDown(value);
    }

    public String getKeyName() {
        return Keyboard.getKeyName(this.getKey());
    }

    public boolean isVisible(){
        if (visible == null){
            return true;
        }
        return visible.test(this.getValue());
    }

    @Override
    public String getType() {
        return "Bind";
    }
}
