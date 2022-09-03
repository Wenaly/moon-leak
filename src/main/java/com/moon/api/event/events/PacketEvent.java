package com.moon.api.event.events;

import com.moon.api.event.EventStage;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.NullUtils;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent extends EventStage implements IMinecraft, NullUtils {
    private Packet<?> packet;
    public boolean hasSetPacket;

    public PacketEvent(int stage, Packet<?> packet) {
        super(stage);
        this.packet = packet;
        hasSetPacket = false;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    public void setPacket(Packet<?> packet){
        if(!nullCheck()){
            hasSetPacket = true;
            if(!this.isCancelled()){
                this.setCancelled(true);
            }
            mc.player.connection.sendPacket(packet);
        }
    }

    @Cancelable
    public static class All extends PacketEvent {
        public All(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }

    @Cancelable
    public static class Send extends PacketEvent {
        public Send(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }

    @Cancelable
    public static class Receive extends PacketEvent {
        public Receive(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }
}

