package com.moon.api.utils.entity;

import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.NullUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RotationUtils implements NullUtils, IMinecraft {

    public static float getRotationYaw() {
        float rotationYaw = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f)
        {
            rotationYaw += 180.0f;
        }
        float n = 1.0f;
        if (mc.player.moveForward < 0.0f)
        {
            n = -0.5f;
        }
        else if (mc.player.moveForward > 0.0f)
        {
            n = 0.5f;
        }
        if (mc.player.moveStrafing > 0.0f)
        {
            rotationYaw -= 90.0f * n;
        }
        if (mc.player.moveStrafing < 0.0f)
        {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }

    public static boolean canSeePosition(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }

}
