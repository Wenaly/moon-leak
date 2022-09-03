package com.moon.impl.modules.player;

import com.moon.api.event.events.PacketEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.misc.MessageUtils;
import com.moon.api.utils.misc.PacketUtils;
import com.moon.api.utils.misc.TimerUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Module.registration(name = "Disabler", description = "", category = Module.Category.Player)
public class Disabler extends Module {

    private final Setting<String> mode = new EnumSetting("Mode", "2b2t", Arrays.asList("2b2t", "NCP", "Verus", "NewVerus", "ULTRA"), this);
    private final Setting<Boolean> cancelKeepAlive = new BooleanSetting("Cancel Keep Alive", true, this);
    private final Setting<Integer> delay = new IntegerSetting("Delay", 1000, 0, 5000, this);
    private final Setting<Boolean> debug = new BooleanSetting("Debug", true, this);

    private final Queue<Packet<?>> Veruspackets = new ConcurrentLinkedQueue();
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue();
    protected final Set<Short> transactionIDs = new HashSet<>();
    private TimerUtils timer = new TimerUtils();
    private int phase = 0;
    private int ultraPhase = 0;

    double x;
    double y;
    double z;


    boolean verusCModify;
    double lastX;
    double lastY;
    double lastZ;

    @Override
    public void onLogout() {
        Veruspackets.clear();
        packets.clear();
        transactionIDs.clear();
    }

    @Override
    public void onEnable() {
        x = 0;
        y = 0;
        z = 0;

        lastX = 0d;
        lastY = 0d;
        lastZ = 0d;

        phase = 0;
        timer.reset();
    }

    @Override
    public void onDisable() {
        for (Packet packet : packets) {
            PacketUtils.sendPacketNoEvent(packets.poll());
        }

        for (Packet packet : Veruspackets) {
            PacketUtils.sendPacketNoEvent(Veruspackets.poll());
        }

        Veruspackets.clear();
        packets.clear();
        transactionIDs.clear();
    }

    /*@EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (!nullCheck() && !mc.isSingleplayer()) {
            Object packet = event.getPacket();

            this.packets.add((Packet<?>) packet);
            event.cancel();
        }
    }*/

    @Override
    public void onUpdate() {
        switch (mode.getValue()) {
            case "NewVerus":
                if (timer.passedMs(490)) {
                    timer.reset();
                    if (!Veruspackets.isEmpty()) {
                        PacketUtils.sendPacketNoEvent(Veruspackets.poll());
                        //debugMessage("RELEASE")
                    } else {
                        //debugMessage("RELEASE BUT EMPTY")
                    }
                }
            case "ULTRA":
                if (timer.passedMs(delay.getValue())) {
                    if (!packets.isEmpty()) {
                        PacketUtils.sendPacketNoEvent(packets.poll());
                        if (debug.getValue()) {
                            MessageUtils.sendMessage("[DEBUG] C0F RELEASE");
                        }
                    } else {
                        if (debug.getValue()) {
                            MessageUtils.sendMessage("[DEBUG] C0F RELEASE BUT EMPTY");
                        }
                    }
                    timer.reset();
                }

                //if (mc.player.ticksExisted % 110 == 0) {
                    //packets.clear();
                //}
        }
    }


    @EventHandler
    public void onPacket(PacketEvent.All event) {
        switch (mode.getValue()) {
            case "ULTRA":
                if (event.getPacket() instanceof CPacketConfirmTransaction) {
                    if (transactionIDs.remove(((CPacketConfirmTransaction) event.getPacket()).getUid()))
                    {
                        return;
                    }

                    packets.add(event.getPacket());
                    event.cancel();
                }

                if (event.getPacket() instanceof CPacketClickWindow) {
                    transactionIDs.add(((CPacketClickWindow) event.getPacket()).getActionNumber());
                }

                if (event.getPacket() instanceof CPacketResourcePackStatus) {
                    packets.add(event.getPacket());
                    event.cancel();
                }

                if (event.getPacket() instanceof CPacketKeepAlive) {
                    packets.add(event.getPacket());
                    event.cancel();
                }
            case "NewVerus":
                if (event.getPacket() instanceof CPacketConfirmTransaction) {
                    Veruspackets.add(event.getPacket());
                    event.cancel();

                    if (Veruspackets.size() > 300) {
                        MessageUtils.sendMessage("Anticheat is disabled.");
                        
                        PacketUtils.sendPacketNoEvent(packets.poll());
                    }

                    PacketUtils.sendPacketNoEvent(Veruspackets.poll());
                }

                if (event.getPacket() instanceof CPacketPlayer) {
                    lastX = ((CPacketPlayer) event.getPacket()).x;
                    lastY = ((CPacketPlayer) event.getPacket()).y;
                    lastZ = ((CPacketPlayer) event.getPacket()).z;
                    if (mc.player.ticksExisted % 40 == 0) {
                        ((CPacketPlayer) event.getPacket()).y -= 11.4514;
                        ((CPacketPlayer) event.getPacket()).onGround = false;
                    }
                }

                if (mc.player != null && mc.player.ticksExisted <= 7) {
                    timer.reset();
                    Veruspackets.clear();
                }

                break;

            case "Verus":
                if (event.getPacket() instanceof CPacketPlayer.Position) {
                    double yPos = (mc.player.posY / 0.015625) * 0.015625;
                    mc.player.setPosition(mc.player.posX, yPos, mc.player.posZ);
                    if (mc.player.ticksExisted % 45 == 0) {
                        PacketUtils.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                        PacketUtils.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 11.725, mc.player.posZ, false));
                        PacketUtils.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                        verusCModify = true;
                    }
                }

                if (event.getPacket() instanceof CPacketConfirmTransaction) {
                    Random random = new Random();
                    for (int i = 0; i < (random.nextInt(5) + 1); i++) {
                        PacketUtils.sendPacket(event.getPacket());
                    }
                }
                break;

            case "2b2t":
                // Disables some movement checks (not done yet)
                if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                    if (phase > 0) {
                        CPacketPlayer.PositionRotation packet = event.getPacket();
                        if (packet.x == x && packet.y == y && packet.z == z) {
                            PacketUtils.sendPacket(new CPacketPlayer.Position(packet.x, packet.y, packet.z, packet.onGround));
                            event.cancel();
                        }
                    }
                    phase += 1;
                }

                if (event.getPacket() instanceof CPacketPlayer.Rotation && mc.player.isRiding()) {
                    PacketUtils.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (event.getPacket() instanceof CPacketInput && mc.player.isRiding()) {
                    PacketUtils.sendPacket(event.getPacket());
                    PacketUtils.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }

                if (event.getPacket() instanceof SPacketPlayerPosLook) {
                    x = ((SPacketPlayerPosLook) event.getPacket()).getX();
                    y = ((SPacketPlayerPosLook) event.getPacket()).getY();
                    z = ((SPacketPlayerPosLook) event.getPacket()).getZ();
                }

                if (event.getPacket() instanceof SPacketRespawn) {
                    phase = 0;
                }
                break;
            case "NCP":
                if (event.getPacket() instanceof CPacketConfirmTransaction || (event.getPacket() instanceof CPacketKeepAlive && cancelKeepAlive.getValue())) {
                    event.cancel();
                }
        }
    }

    @EventHandler
    public void onCPacket(PacketEvent.Send event) {
        if (mode.getValue().toLowerCase() != "newverus") return;
        if (event.getStage() != 1 && !(event.getPacket() instanceof CPacketPlayer)) return;

        if (verusCModify) {
            mc.player.setPosition(lastX, lastY, lastZ);
            verusCModify = false;
        }
    }

}


