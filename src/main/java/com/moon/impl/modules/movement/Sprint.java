package com.moon.impl.modules.movement;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.registration(name = "Sprint", description = "Modifies sprinting", category = Module.Category.Movement)
public class Sprint extends Module {

    /*
    @author: iJese
 */

    private Setting<Boolean> multiDirection = new BooleanSetting("Multi Direction", true, this);

    public void onUpdate() {
        EntityPlayerSP player = mc.player;

        if (player != null) {
            player.setSprinting(shouldSprint(player));
        }
    }

    public boolean shouldSprint(EntityPlayerSP player) {
        return !mc.gameSettings.keyBindSneak.isKeyDown()
                && player.getFoodStats().getFoodLevel() > 6
                && !player.isElytraFlying()
                && !mc.player.capabilities.isFlying
                && checkMovementInput(player);
    }

    private boolean checkMovementInput(EntityPlayerSP player) {
        return multiDirection.getValue() ? (player.moveForward != 0.0f || player.moveStrafing != 0.0f) : player.moveForward > 0.0f;
    }
}