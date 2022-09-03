package com.moon.api.mixin.mixins;

import com.moon.Moon;
import com.moon.api.event.events.PacketEvent;
import com.moon.api.utils.misc.PacketUtils;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetworkManager.class})
public class MixinNetworkManager {
    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
        if (packet == null) return;
        PacketEvent.Send event = new PacketEvent.Send(0, packet);
        PacketEvent.All allEvent = new PacketEvent.All(0, packet);
        if (!PacketUtils.noEventPackets.contains(packet)) {
            Moon.getEventProcessor().postEvent(allEvent);
            Moon.getEventProcessor().postEvent(event);
        }
        if (event.isCancelled() || allEvent.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="RETURN")}, cancellable=true)
    private void onSendPacketPost(Packet<?> packet, CallbackInfo info) {
        if (packet == null) return;
        PacketEvent.Send event = new PacketEvent.Send(1, packet);
        PacketEvent.All allEvent = new PacketEvent.All(1, packet);
        if (!PacketUtils.noEventPackets.contains(packet)) {
            Moon.getEventProcessor().postEvent(allEvent);
            Moon.getEventProcessor().postEvent(event);
        }
        if (event.isCancelled() || allEvent.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"channelRead0"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info) {
        if (packet == null) return;
        PacketEvent.Receive event = new PacketEvent.Receive(0, packet);
        PacketEvent.All allEvent = new PacketEvent.All(0, packet);
        if (!PacketUtils.noEventPackets.contains(packet)) {
            Moon.getEventProcessor().postEvent(allEvent);
            Moon.getEventProcessor().postEvent(event);
        }
        if (event.isCancelled() || allEvent.isCancelled()) {
            info.cancel();
        }
    }
}

