package com.moon.api.utils.misc;

import com.moon.api.utils.IMinecraft;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public class PacketUtils implements IMinecraft {

    public static ArrayList<Packet> noEventPackets = new ArrayList<>();

    public static void sendPacket(Packet pakcet) {
        mc.player.connection.sendPacket(pakcet);
    }

    public static void sendPacketNoEvent(Packet packet) {
        noEventPackets.add(packet);
        sendPacket(packet);
    }

}
