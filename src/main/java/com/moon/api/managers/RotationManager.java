package com.moon.api.managers;

import com.moon.Moon;
import com.moon.api.event.events.MotionUpdateEvent;
import com.moon.api.event.events.PacketEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.NullUtils;
import net.minecraft.network.play.client.CPacketPlayer;

public class RotationManager implements NullUtils, IMinecraft {

    private static boolean shouldRotate = false;
    private static int ticks;
    private static float yaw;
    private static float pitch;

    public void resetRotations() {
        shouldRotate = false;
        ticks = -1;
    }

    public void setRotations(float newYaw, float newPitch) {
        yaw = newYaw;
        pitch = newPitch;
        shouldRotate = true;
        ticks = 0;
    }

    public void setRotations(int newYaw, int newPitch) {
        yaw = newYaw;
        pitch = newPitch;
        shouldRotate = true;
        ticks = 0;
    }

    @EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (event.getStage() != 0) return;
        if (event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (shouldRotate) {
                ((CPacketPlayer) event.getPacket()).pitch = pitch;
                ((CPacketPlayer) event.getPacket()).yaw = yaw;
            }
        }
    }

    @EventHandler
    public void invoke(MotionUpdateEvent event) {
        Moon.getLogger().info("motion update event");
        event.setYaw(yaw);
        event.setPitch(pitch);
    }

    public void onTick() {
        if (!nullCheck() && ticks == 0) {
            ticks++;
            //mc.player.cameraPitch = pitch;
            //mc.player.cameraYaw = yaw;
            //mc.player.setRotationYawHead(yaw);
            //mc.player.rotationYawHead = yaw;
            if (ticks > 14) {
                this.resetRotations();
            }
        }
    }


    /*@EventHandler
    public void onPacket(PacketEvent.Send event) {
        Moon.getLogger().info("trigger event");
        if (!shouldRotate) return;
        //event.cancel();

        if (mc.getRenderViewEntity() == mc.player) {
            Moon.getLogger().info("spoofing client side rot");
            mc.player.rotationYawHead = yaw;
        }
        AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
        double l_PosXDifference = mc.player.posX - mc.player.lastReportedPosX;
        double l_PosYDifference = axisalignedbb.minY - mc.player.lastReportedPosY;
        double l_PosZDifference = mc.player.posZ - mc.player.lastReportedPosZ;
        double l_YawDifference = (yaw - mc.player.lastReportedYaw);
        double l_RotationDifference = (pitch - mc.player.lastReportedPitch);
        ++mc.player.positionUpdateTicks;
        boolean l_MovedXYZ = l_PosXDifference * l_PosXDifference + l_PosYDifference * l_PosYDifference + l_PosZDifference * l_PosZDifference > 9.0E-4D || mc.player.positionUpdateTicks >= 20;
        boolean rotated = l_YawDifference != 0.0D || l_RotationDifference != 0.0D;

        if (mc.player.isRiding())
        {
            Moon.getLogger().info("riding");
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, yaw, pitch, mc.player.onGround));
            l_MovedXYZ = false;
        }
        else if (l_MovedXYZ && rotated)
        {
            Moon.getLogger().info("moved & rotated");
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY, mc.player.posZ, yaw, pitch, mc.player.onGround));
        }
        else if (l_MovedXYZ)
        {
            Moon.getLogger().info("moved");
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY, mc.player.posZ, mc.player.onGround));
        }
        else if (rotated)
        {
            Moon.getLogger().info("rotated");
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
        }
        else if (mc.player.prevOnGround != mc.player.onGround)
        {
            Moon.getLogger().info("on ground");
            mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
        }

        if (l_MovedXYZ)
        {
            Moon.getLogger().info("moved reported");
            mc.player.lastReportedPosX = mc.player.posX;
            mc.player.lastReportedPosY = axisalignedbb.minY;
            mc.player.lastReportedPosZ = mc.player.posZ;
            mc.player.positionUpdateTicks = 0;
        }

        if (rotated)
        {
            Moon.getLogger().info("rotation reported");
            mc.player.lastReportedYaw = yaw;
            mc.player.lastReportedPitch = pitch;
        }

        mc.player.prevOnGround = mc.player.onGround;
        mc.player.autoJumpEnabled = mc.player.mc.gameSettings.autoJump;
    }*/


}

