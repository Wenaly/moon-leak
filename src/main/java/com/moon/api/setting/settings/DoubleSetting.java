package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;

import java.util.function.Predicate;

public class DoubleSetting extends Setting<Double> {

    private final double min;
    private final double max;

    public DoubleSetting(String name, Double value, Double min, Double max, Module parent) {
        super(name, value, parent);
        this.min = min;
        this.max = max;
    }

    public DoubleSetting(String name, Double value, Double min, Double max, Module parent, Predicate<Double> visible) {
        super(name, value, parent, visible);
        this.min = min;
        this.max = max;
    }

    public Double getValue() {
        return this.value;
    }

    public Double getMax() {
        return this.max;
    }

    public Double getMin() {
        return this.min;
    }

    public double getNumber() {
        return this.value;
    }

    public void setNumber(double value) {
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
        return "Double";
    }
}
