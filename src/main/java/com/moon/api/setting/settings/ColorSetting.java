package com.moon.api.setting.settings;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.utils.render.ColorUtils;

import java.awt.*;
import java.util.function.Predicate;

public class ColorSetting extends Setting<ColorUtils> {

    private boolean rainbow;

    public ColorSetting(String name, ColorUtils value, Module parent) {
        super(name, value, parent);
    }

    public ColorSetting(String name, ColorUtils value, Module parent, Predicate<ColorUtils> visible) {
        super(name, value, parent, visible);
    }

    @Override
    public ColorUtils getValue() {
        this.doRainbow();
        return this.value;
    }

    private void doRainbow() {
        if (rainbow) {
            Color c = ColorUtils.fromHSB((System.currentTimeMillis() % (360 * 32)) / (360f * 32), value.getSaturation(), value.getBrightness());
            setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), value.getAlpha()));
        }
    }


    public void setValue(Color value) {
        this.value = new ColorUtils(value);
    }

    public void setValue(int red, int green, int blue, int alpha) {
        this.value = new ColorUtils(red, green, blue, alpha);
    }

    public Color getColor() {
        return this.value;
    }

    public boolean getRainbow() {
        return this.rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean isVisible() {
        if (visible == null){
            return true;
        }
        return visible.test(this.getValue());
    }

    @Override
    public String getType() {
        return "Color";
    }
}
