package com.moon.impl.modules.misc;

import com.moon.api.event.events.DeathEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

@Module.registration(name = "KillEffect", description = "Efectos al morir", category = Module.Category.Misc)
public class KillEffect extends Module {

    private Setting<Boolean> lightning = new BooleanSetting("Lightning", false, this);
    private Setting<Boolean> lightningSound = new BooleanSetting("LightningSound", false, this);

    @EventHandler
    public void onEntityDeath(DeathEvent event) {
        if (nullCheck()) { return; }
        if (lightning.getValue()) {
            final EntityLightningBolt bolt = new EntityLightningBolt(mc.world, 0, 0, 0, false);
            if (lightningSound.getValue()) {
                mc.world.playSound(event.player.getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 1f, 1f, false);
            }
            bolt.setLocationAndAngles(event.player.posX, event.player.posY, event.player.posZ, 0, 0);
            mc.world.spawnEntity(bolt);
        }
    }
}
