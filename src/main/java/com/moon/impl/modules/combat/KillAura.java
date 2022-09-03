package com.moon.impl.modules.combat;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.DoubleSetting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.FloatSetting;
import com.moon.api.utils.entity.InventoryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Module.registration(name = "KillAura", description = "", category = Module.Category.Combat)
public class KillAura extends Module {

    List<Entity> targets = null;

    private Setting<Float> reach = new FloatSetting("Reach", 3.5f, 0f, 6f, this);
    private Setting<String> autoSwitch = new EnumSetting("AutoSwitch", "Normal", Arrays.asList("Off", "Normal", "Silent"), this);

    public void onEnable() {
        targets = null;
    }

    public void onUpdate() {
        targets = mc.world.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityPlayer && entity != mc.player && entity.getDistance(mc.player) < reach.getValue().floatValue() && !entity.isDead && ((EntityPlayer) entity).getHealth() > 0).collect(Collectors.toList());

        for (Entity entity : targets) {
            if (entity instanceof EntityPlayer) {
                if (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword && !autoSwitch.equals("Silent")) {

                } else if (autoSwitch.equals("Normal") || autoSwitch.equals("Silent")) {
                    int slot = InventoryUtils.findHotbarBlock(ItemSword.class);
                    InventoryUtils.switchSlot(slot, (autoSwitch.equals("Silent") ? true : false));
                }
            }
        }
    }

}
