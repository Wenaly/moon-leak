package com.moon.impl.modules.movement;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.DoubleSetting;
import net.minecraft.util.math.BlockPos;

@Module.registration(name = "FastFall", description = "Caer Rapido", category = Module.Category.Movement)
public class FastFall extends Module {

    /*
    @author: iJese
 */

    //private Setting<Double> dist = new DoubleSetting("Min Distance", 3d,0d,25d, this);
    private Setting<Double> speed = new DoubleSetting("Multiplier", 3d,0d,10d, this);

    @Override
    public void onUpdate() {
        if (mc.world.isAirBlock(new BlockPos(mc.player.getPositionVector()))) {
            if (mc.player.onGround &&
                    (!mc.player.isElytraFlying()
                            || !mc.player.capabilities.isFlying))
                mc.player.motionY -= speed.getValue();
        }

    }
}
