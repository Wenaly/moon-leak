package com.moon.api.mixin.mixins;

import com.moon.Moon;
import com.moon.api.event.events.DeathEvent;
import com.moon.api.utils.IMinecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(method={"handleEntityMetadata"}, at={@At(value="RETURN")}, cancellable=true)
    private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
        EntityPlayer player;
        Entity entity;
        if (IMinecraft.mc.world != null && (entity = IMinecraft.mc.world.getEntityByID(packetIn.getEntityId())) instanceof EntityPlayer && (player = (EntityPlayer) entity).getHealth() <= 0.0f) {
            Moon.getEventProcessor().postEvent(new DeathEvent(player));
        }
    }
}