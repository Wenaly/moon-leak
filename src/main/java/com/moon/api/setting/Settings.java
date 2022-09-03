package com.moon.api.setting;


import com.moon.api.module.Module;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private final List<Setting> settings;

    public Settings() {
        this.settings = new ArrayList<>();
    }

    public void addSetting(Setting setting) {
        this.settings.add(setting);
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public List<Setting> getSettingFromModule(Module module) {
        List<Setting> settings = new ArrayList<>();
        for (Setting setting : this.settings) {
            if(setting.getParent() == module) {
                settings.add(setting);
            }
        }
        return settings;
    }

    public List<Setting> getSettingFromModule(String module) {
        List<Setting> settings = new ArrayList<>();
        for (Setting setting : this.settings) {
            if(setting.getParent().getName().equalsIgnoreCase(module)) {
                settings.add(setting);
            }
        }
        return settings;
    }

}
