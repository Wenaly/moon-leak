package com.moon.impl.modules.render;

import com.moon.api.event.events.Render3DEvent;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.DoubleSetting;
import com.moon.api.setting.settings.IntegerSetting;
import net.minecraft.entity.player.EntityPlayer;

@Module.registration(name = "Animations", description = "Europa Moment", category = Module.Category.Render)
public class Animations extends Module {
    public Setting<Boolean> playersDisableAnimations = new BooleanSetting("DisableAnimations", false, this);
    public Setting<Boolean> changeMainhand = new BooleanSetting("ChangeMainhand", false, this);
    public Setting<Double> mainhand = new DoubleSetting("MainHand", 1.0d, 0.0d, 1.0d, this);
    public Setting<Boolean> changeOffhand = new BooleanSetting("ChangeOffhand", false, this);
    public Setting<Double> offhand = new DoubleSetting("OffHand", 1.0d, 0.0d, 1.0d, this);
    public Setting<Boolean> changeSwing = new BooleanSetting("ChangeSwing", true, this);
    public Setting<Integer> swingDelay = new IntegerSetting("Delay", 6, 1, 30, this);

    public Setting<Boolean> rainbowEnchant = new BooleanSetting("rainbowEnchant", false, this);

    public static Animations INSTANCE;

    public Animations() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (playersDisableAnimations.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                player.limbSwing = 0f;
                player.limbSwingAmount = 0f;
                player.prevLimbSwingAmount = 0f;
            }
        }
        if (changeMainhand.getValue() && mc.itemRenderer.equippedProgressMainHand != mainhand.getValue().floatValue()) {
            mc.itemRenderer.equippedProgressMainHand = mainhand.getValue().floatValue();
            mc.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
        }
        if (changeOffhand.getValue() && mc.itemRenderer.equippedProgressOffHand != offhand.getValue().floatValue()) {
            mc.itemRenderer.equippedProgressOffHand = offhand.getValue().floatValue();
            mc.itemRenderer.itemStackOffHand = mc.player.getHeldItemOffhand();
        }
    }

}