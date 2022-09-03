package com.moon.api.setting;

import com.moon.Moon;
import com.moon.api.module.Module;

import java.util.function.Predicate;

public class Setting<T> {

    private final String name;
    private final Module parent;
    public T value;
    public Predicate<T> visible;

    public Setting(String name, T value, Module parent) {
        this.name = name;
        this.value = value;
        this.parent = parent;

        Moon.Settings.addSetting(this);
    }

    public Setting(String name, T value, Module parent, Predicate<T> visible){
        this.name = name;
        this.value = value;
        this.parent = parent;
        this.visible = visible;
        Moon.Settings.addSetting(this);
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public boolean equals(Object value) {
        return this.value == value;
    }

    public String getType() {
        return "";
    }

    public Module getParent() {
        return this.parent;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Predicate<T> getVisible() {
        return visible;
    }

    public void setVisible(Predicate<T> visible) {
        this.visible = visible;
    }
}