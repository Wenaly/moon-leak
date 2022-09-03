package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;

import java.util.function.Predicate;

public class FloatSetting extends Setting<Float> {

    private final float min;
    private final float max;

    public FloatSetting(String name, Float value, Float min, Float max, Module parent) {
        super(name, value, parent);
        this.min = min;
        this.max = max;
    }

    public FloatSetting(String name, Float value, Float min, Float max, Module parent, Predicate<Float> visible) {
        super(name, value, parent, visible);
        this.min = min;
        this.max = max;
    }

    public Float getValue() {
        return this.value;
    }

    public Float getMax() {
        return this.max;
    }

    public Float getMin() {
        return this.min;
    }

    public double getNumber() {
        return this.value;
    }

    public void setNumber(float value) {
        this.value = value;
    }

    public double getMaximumValue() {
        return this.max;
    }

    public double getMinimumValue() {
        return this.min;
    }

    public int getPrecision() {
        return 2;
    }

    public boolean isVisible(){
        if (visible == null){
            return true;
        }
        return visible.test(this.getValue());
    }

    @Override
    public String getType() {
        return "float";
    }
}
