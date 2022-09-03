package com.moon.impl.modules.combat;

import com.moon.Moon;
import com.moon.api.event.events.MoveEvent;
import com.moon.api.module.Module;
import com.moon.api.priorities.Priorities;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.entity.CrystalUtils;
import com.moon.api.utils.entity.EntityUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import java.util.Arrays;

@Module.registration(name = "Offhand", description = "puts things in ur offhand", category = Module.Category.Combat, priority = Priorities.Highest)
public class OffHand extends Module {
    public Setting<String> mode = new EnumSetting("Mode", "Totem", Arrays.asList("Totem", "Crystal", "Gapple", "Bed"), this);
    public Setting<Boolean> cancelMovement = new BooleanSetting("CancelMovement", false, this);
    public Setting<Integer> TotemHp = new IntegerSetting("TotemHP", 16, 0, 36, this);
    public Setting<Integer> HoleHP = new IntegerSetting("HoleHP", 16, 0, 36, this);
    public Setting<Boolean> GapSwitch = new BooleanSetting("GapSwap", false, this);
    public Setting<Boolean> GapOnSword = new BooleanSetting("SwordGap", false, this, s -> GapSwitch.getValue());
    public Setting<Boolean> GapOnPick = new BooleanSetting("PickGap", false, this, s -> GapSwitch.getValue());
    public Setting<Boolean> Always = new BooleanSetting("Always", false, this, s -> GapSwitch.getValue());
    public Setting<Boolean> CrystalCheck = new BooleanSetting("CrystalCheck", false, this);
    public Setting<Boolean> check32K = new BooleanSetting("32KCheck", false, this);
    public Setting<Integer> cooldown = new IntegerSetting("Cooldown", 0, 0, 40, this);

    private int timer = 0;

    @Override
    public void onUpdate() {
        if(nullCheck())return;
        if (mc.currentScreen instanceof GuiContainer) {
            return;
        }

        float hp = EntityUtils.getHealth(mc.player);

        if (hp < TotemHp.getValue()) {
            // Stop the player movement so totem can be swapped on 2b2t.
            if (cancelMovement.getValue()) {
                StopPlayerMovement.toggle(true);
            }
            this.swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
            // Stop the cancelling of the MoveEvent after the totem has been swapped.
            if (cancelMovement.getValue()) {
                StopPlayerMovement.toggle(false);
            }
        }
    }

    @Override
    public void onTick() {
        if(nullCheck())return;
        timer = timer + 1;
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {
            float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            if ((hp > TotemHp.getValue() || (EntityUtils.isInHole(mc.player) && hp > HoleHP.getValue())) && lethalToLocalCheck() && Check32K()) {
                if (mode.getValue().equalsIgnoreCase("crystal") && !(((GapOnSword.getValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) || Always.getValue() || (GapOnPick.getValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe)) && mc.gameSettings.keyBindUseItem.isKeyDown() && GapSwitch.getValue())) {
                    swapItems(getItemSlot(Items.END_CRYSTAL));
                    return;
                } else if (((GapOnSword.getValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) || Always.getValue() || (GapOnPick.getValue() && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe)) && mc.gameSettings.keyBindUseItem.isKeyDown() && GapSwitch.getValue()) {
                    swapItems(getItemSlot(Items.GOLDEN_APPLE));
                    if (mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                        mc.playerController.isHittingBlock = true;
                    }
                    return;
                }
                if (mode.getValue().equalsIgnoreCase("totem")) {
                    swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
                    return;
                }
                if (mode.getValue().equalsIgnoreCase("bed")) {
                    swapItems(getItemSlot(Items.BED));
                    return;
                }
                if (mode.getValue().equalsIgnoreCase("gapple")) {
                    swapItems(getItemSlot(Items.GOLDEN_APPLE));
                    return;
                }
                if (mode.getValue().equalsIgnoreCase("bed")) {
                    swapItems(getItemSlot(Items.BED));
                    return;
                }
            } else {
                swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
                return;
            }
            if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
                swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
            }
        }
    }

    private boolean Check32K() {
        if (!check32K.getValue() || mc.world == null || mc.player == null) return true;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.player && Moon.FriendsManager.isFriend(entity.getName()) && entity instanceof EntityPlayer && entity.getDistance(mc.player) < 7) {
                if (EntityUtils.holding32k((EntityPlayer) entity)) return true;
            }
        }
        return true;
    }

    private boolean lethalToLocalCheck() {
        if (!CrystalCheck.getValue()) return true;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && mc.player.getDistance(entity) <= 12) {
                if (CrystalUtils.calculateDamage(new BlockPos(entity.posX, entity.posY, entity.posZ), mc.player, false) >= mc.player.getHealth()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void swapItems(int slot) {
        if (slot == -1 || (timer <= cooldown.getValue()) && mc.player.inventory.getStackInSlot(slot).getItem() != Items.TOTEM_OF_UNDYING) return;
        timer = 0;
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

    private int getItemSlot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for (int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                if (i < 9) {
                    if (input == Items.GOLDEN_APPLE) {
                        return -1;
                    }
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }
    
    public static class StopPlayerMovement {
        private static final StopPlayerMovement stopPlayerMovement = new StopPlayerMovement();

        public static void toggle(boolean on) {
            if (on) {
                Moon.getEventProcessor().addEventListener(stopPlayerMovement);
            } else {
                Moon.getEventProcessor().removeEventListener(stopPlayerMovement);
            }
        }

        public void onMove(MoveEvent event) {
            event.setCancelled(true);
        }
    }
}
