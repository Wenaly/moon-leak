package com.moon.impl.modules.misc;

import com.moon.Moon;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.FloatSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.misc.PacketUtils;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.registration(name = "AntiAim", description = "", category = Module.Category.Misc)
public class AntiAim extends Module {

    private Setting<Integer> pitch = new IntegerSetting("Pitch", 0, -90, 90, this); // head
    private Setting<Float> rotSpeed = new FloatSetting("Speed", 0f, 0f, 10f, this);
    //private Setting<Integer> pitch = new IntegerSetting("", 0, -360, 360, this, s -> this.mode.equals("Custom")); // body
    private float prevYaw;

    @Override
    public void onEnable() {
        prevYaw = mc.player.rotationYaw;
    }

    @Override
    public void onTick() {
        prevYaw += rotSpeed.getValue();
        if (prevYaw > 360f) {
            prevYaw = -360f + (prevYaw - 359f);
        }
        Moon.getRotationManager().setRotations(prevYaw, pitch.getValue());
        PacketUtils.sendPacket(new CPacketPlayer.Rotation(prevYaw, pitch.getValue(), mc.player.onGround));
    }

    @Override
    public void onDisable() {
        Moon.getRotationManager().resetRotations();
    }

}
