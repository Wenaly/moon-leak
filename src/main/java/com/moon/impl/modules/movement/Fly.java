package com.moon.impl.modules.movement;

import com.moon.api.event.events.BlockCollisionBoundingBoxEvent;
import com.moon.api.event.events.MoveEvent;
import com.moon.api.event.events.PacketEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.DoubleSetting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.utils.misc.MappingUtils;
import com.moon.api.utils.entity.MovementUtils;
import com.moon.api.utils.misc.TimerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;

import java.lang.reflect.Field;
import java.util.Arrays;

@Module.registration(name = "Flight", description = "Modifies the velocity you take", category = Module.Category.Movement)
public class Fly extends Module {

    private Setting<String> modes = new EnumSetting("Mode", "Verus", Arrays.asList("Vanilla", "Verus"), this);
    private Setting<Double> verusYOfsset = new DoubleSetting("VerusYOffset", 1.5d, 0.0d, 2.0d, this, s -> modes.equals("Verus"));
    private Setting<Double> verusSpeed = new DoubleSetting("VerusSpeed", 1.7d, 0.0d, 5.0d, this, s -> modes.equals("Verus"));
    private Setting<Double> verusHopOffset = new DoubleSetting("VerusHopOffset", 1.7d, 0.0d, 2.0d, this, s -> modes.equals("Verus"));
    private Setting<Double> verusHopMotion = new DoubleSetting("VerusHopMotion", 1.5d, 0.0d, 2.0d, this, s -> modes.equals("Verus"));

    private TimerUtils timer = new TimerUtils();
    private double y;
    private boolean flyable;

    @Override
    public void onEnable() {
        if (nullCheck()) this.toggle();
        y = mc.player.posY;
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + verusYOfsset.getValue().floatValue(), mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));

        mc.player.motionX = 0f;
        mc.player.motionY = 0f;
        mc.player.motionZ = 0f;

        //y += 0.402f;

        flyable = true;

        timer.reset();
    }

    @Override
    public void onUpdate() {
        mc.player.posY = y;
        if (timer.passedMs(verusHopOffset.getValue().intValue() * 1000)) {
            mc.player.motionY = 0f;
            MovementUtils.strafe(0.48f);
            flyable = false;
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + verusHopMotion.getValue(), mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, y, mc.player.posZ, true));
            flyable = true;
            timer.reset();
        }

        if (flyable && timer.passedMs(100)) {
            MovementUtils.strafe(verusSpeed.getValue().floatValue());
            mc.player.motionY = 0f;
        } else if (!timer.passedMs(100)) {
            mc.player.motionX = 0f;
            mc.player.motionY = 0f;
            mc.player.motionZ = 0f;
        }
    }

    @EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (event.getStage() != 0) return;

        if (event.getPacket() instanceof CPacketPlayer && flyable) {
            CPacketPlayer packet = event.getPacket();
            packet.y = y;
            packet.onGround = true;
            mc.player.onGround = true;
            mc.player.motionY = 0f;
            mc.player.setPosition(mc.player.posX, y, mc.player.posZ);
        }
    }

}

