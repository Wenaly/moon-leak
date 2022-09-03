package com.moon.impl.modules.client;

import com.moon.api.gui.Gui;
import com.moon.api.module.Module;
import com.moon.api.newgui.NewGui;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.ColorSetting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.render.ColorUtils;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

@Module.registration(name = "ClickGui", description = "Renders Moon's ClickGui.", category = Module.Category.Client, bind = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {

    public static ClickGui Instance;

    private Setting<String> mode = new EnumSetting("gui", "new", Arrays.asList("classic", "new"), this);
    public Setting<ColorUtils> headButtonColor = new ColorSetting("Head Button 1", new ColorUtils(255, 150, 90, 255), this);
    public Setting<ColorUtils> headButtonColor2 = new ColorSetting("Head Button 2", new ColorUtils(255, 150, 90, 255), this);
    public Setting<ColorUtils> buttonColor = new ColorSetting("Button", new ColorUtils(224, 156, 96, 255), this);
    public Setting<ColorUtils> fontColor = new ColorSetting("Font", new ColorUtils(255, 255, 255, 255), this);
    public BooleanSetting customFont = new BooleanSetting("CustomFont", true, this);
    public Setting<Integer> height = new IntegerSetting("Height", 12, 1,18, this);
    public Setting<Integer> width = new IntegerSetting("Width", 115, 1,180, this);

    public ClickGui() {
        Instance = this;
    }

    public void onEnable() {
        mc.displayGuiScreen(new Gui());
    }

}