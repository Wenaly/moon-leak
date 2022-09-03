package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;

import java.util.function.Predicate;

public class IntegerSetting extends Setting<Integer> {

    private final int min;
    private final int max;

    public IntegerSetting(String name, int value, int min, int max, Module parent) {
        super(name, value, parent);

        this.min = min;
        this.max = max;
    }

    public IntegerSetting(String name, int value, int min, int max, Module parent, Predicate<Integer> visible) {
        super(name, value, parent, visible);

        this.min = min;
        this.max = max;
    }

    public Integer getValue() {
        return this.value;
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public double getNumber() {
        return this.value;
    }

    public void setNumber(double value) {
        this.value = Math.toIntExact(Math.round(value));
    }

    public double getMaximumValue() {
        return this.max;
    }

    public double getMinimumValue() {
        return this.min;
    }

    public int getPrecision() {
        return 0;
    }

    public boolean isVisible(){
        if (visible == null){
            return true;
        }
        return visible.test(this.getValue());
    }

    @Override
    public String getType() {
        return "Int";
    }
}