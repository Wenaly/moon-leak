package com.moon.api.utils.entity;

import com.moon.api.utils.IMinecraft;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MovementUtils implements IMinecraft {

    public static boolean isMoving() {
        return mc.player != null && (mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f);
    }

    public static void strafe(float speed) {
        if (!isMoving()) return;

        float yaw = RotationUtils.getRotationYaw();
        mc.player.motionX = -sin(yaw) * speed;
        mc.player.motionZ = cos(yaw) * speed;
    }

}
