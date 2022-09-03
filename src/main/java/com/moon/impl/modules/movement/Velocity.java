package com.moon.impl.modules.movement;

import com.moon.api.event.events.EntityPushEvent;
import com.moon.api.event.events.PacketEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.mixin.mixins.access.ISPacketEntityVelocity;
import com.moon.api.mixin.mixins.access.ISPacketExplosion;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.IntegerSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

import java.util.Arrays;

@Module.registration(name = "Velocity", description = "Modifies the velocity you take", category = Module.Category.Movement)
public class Velocity extends Module {

    private Setting<Integer> vertical = new IntegerSetting("Vertical", 0, 0, 100, this);
    private Setting<Integer> horizontal = new IntegerSetting("Horizontal", 0, 0, 100, this);

    @EventHandler
    public void pushEvent(EntityPushEvent event) {
        if (nullCheck()) return;
        if (event.getStage() == 0) {
            event.x = event.x * 0;
            event.y = event.y * 0;
            event.z = event.z * 0;
        }

        if (event.getStage() == 1) {
            event.setCancelled(true);
        }

        if (event.getStage() == 2 && event.entity == mc.player) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (event.getPacket() instanceof SPacketEntityStatus){
            final SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 31)
            {
                final Entity entity = packet.getEntity(Minecraft.getMinecraft().world);
                if (entity != null && entity instanceof EntityFishHook)
                {
                    final EntityFishHook fishHook = (EntityFishHook) entity;
                    if (fishHook.caughtEntity == Minecraft.getMinecraft().player)
                    {
                        event.cancel();
                    }
                }
            }

        }

        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
            SPacketEntityVelocity packet = event.getPacket();

            ISPacketEntityVelocity inter = (ISPacketEntityVelocity) packet;
            inter.setMotionX(packet.getMotionX() * this.horizontal.getValue().intValue());
            inter.setMotionY(packet.getMotionY() * this.vertical.getValue().intValue());
            inter.setMotionZ(packet.getMotionZ() * this.horizontal.getValue().intValue());
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
        }

        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion packet = event.getPacket();

            ISPacketExplosion inter = (ISPacketExplosion) packet;
            inter.setMotionX(packet.getMotionX() * this.horizontal.getValue().intValue());
            inter.setMotionY(packet.getMotionY() * this.vertical.getValue().intValue());
            inter.setMotionZ(packet.getMotionZ() * this.horizontal.getValue().intValue());

            if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
        }
    }
}