package com.moon.impl.modules.movement;

import com.moon.Moon;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

@Module.registration(name = "Step", description = "", category = Module.Category.Movement)
public class Step extends Module {

    private Setting<Boolean> vanilla = new BooleanSetting("Vanilla", false, this);

    @Override
    public void onEnable() {
        if (vanilla.getValue()) {
            mc.player.stepHeight = 2f;
        }
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        if (mc.player.onGround && !mc.player.isInsideOfMaterial(Material.WATER) && !mc.player.isInsideOfMaterial(Material.LAVA) && mc.player.collidedVertically && mc.player.fallDistance == 0.0f && !mc.gameSettings.keyBindJump.pressed && !mc.player.isOnLadder() && !Moon.Modules.isModuleEnabled("Speed")) {
            if (!vanilla.getValue()) {

                float height = (float) getHeight();

                if (height < 0f || height > 2f) return;

                if (height == 2f) {
                    float[] steps = {0.42f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f};

                    for (float i : steps) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + i, mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2f, mc.player.posZ);
                }

                if (height == 1.5f) {
                    float[] steps = {0.41999998688698f, 0.7531999805212f, 1.00133597911214f, 1.16610926093821f, 1.24918707874468f, 1.1707870772188f};

                    for (float i : steps) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + i, mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1f, mc.player.posZ);
                }

                if (height == 1f) {
                    float[] steps = {0.41999998688698f, 0.7531999805212f};

                    for (float i : steps) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + i, mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1f, mc.player.posZ);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;
        mc.player.stepHeight = 0.6f;
    }

    private double getHeight() {
        mc.player.stepHeight = 0.5f;

        double height = -1;
        final AxisAlignedBB grow = mc.player.getEntityBoundingBox().offset(0, 0.05, 0).grow(0.05);
        if (!mc.world.getCollisionBoxes(mc.player, grow.offset(0, 2, 0)).isEmpty()) return -1;
        for (final AxisAlignedBB aabb : mc.world.getCollisionBoxes(mc.player, grow)) {
            if (aabb.maxY > height) {
                height = aabb.maxY;
            }
        }
        return height - mc.player.posY;
    }

}
