package com.moon.impl.modules.combat;

import com.moon.api.module.Module;
import com.moon.api.priorities.Priorities;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.FloatSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.entity.EntityUtils;
import com.moon.api.utils.entity.InventoryUtils;
import com.moon.api.utils.world.BlockUtils;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

@Module.registration(name = "AutoTrap", description = "trap goes br", category = Module.Category.Combat, priority = Priorities.High, bind = Keyboard.KEY_NONE)
public class AutoTrap extends Module {

    /**
     * @author: Primooctopus33
     */

    public Setting<Integer> blocksPerPlace = new IntegerSetting("Blocks Per Place", 16, 0, 20, this);
    public Setting<Boolean> extraTrapTop = new BooleanSetting("Extra Trap Top", true, this);
    public Setting<Float> range = new FloatSetting("Range", 4.5f, 0.0f, 10.0f, this);
    public Setting<Boolean> rotate = new BooleanSetting("Rotate", false, this);
    public Setting<Boolean> packet = new BooleanSetting("Packet", true, this);
    public Setting<Boolean> swing = new BooleanSetting("Swing", true, this);
    public int placed = 0;

    public void onUpdate() {
        placed = 0;
        EntityPlayer target = EntityUtils.getTarget(range.getValue());
        if (target == null || mc.player == null || mc.world == null) return;
        BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
        BlockPos[] trapArray = {targetPos.north(), targetPos.east(), targetPos.south(), targetPos.west(), targetPos.down()};
        BlockPos[] topTrapArray = {targetPos.north().up(), targetPos.east().up(), targetPos.south().up(), targetPos.west().up()};
        for (BlockPos pos : trapArray) {
            placeBlock(pos);
        }
        for (BlockPos pos : topTrapArray) {
            placeBlock(pos);
        }
        if (!BlockUtils.hasNeighbor(targetPos.up().up())) {
            placeHelpingBlocksForTrap(target);
        }
        placeBlock(targetPos.up().up());
        if (extraTrapTop.getValue()) {
            placeBlock(targetPos.up().up().up());
        }
    }

    public void placeHelpingBlocksForTrap(EntityPlayer entityPlayer) {
        if (!BlockUtils.hasNeighbor(entityPlayer.getPosition().up().up())) {
            switch (mc.player.getHorizontalFacing()) {
                case NORTH: {
                    placeBlock(entityPlayer.getPosition().up().up().north());
                }
                case EAST: {
                    placeBlock(entityPlayer.getPosition().up().up().east());
                }
                case SOUTH: {
                    placeBlock(entityPlayer.getPosition().up().up().south());
                }
                case WEST: {
                    placeBlock(entityPlayer.getPosition().up().up().west());
                }
            }
        }
    }

    public void placeBlock(BlockPos pos) {
        int obsidianSlot = InventoryUtils.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = InventoryUtils.findHotbarBlock(BlockEnderChest.class);
        int slot = obsidianSlot != -1 ? obsidianSlot : eChestSlot;
        if (slot != -1 && canPlaceBlock(pos) && placed < blocksPerPlace.getValue() && pos != null) {
            BlockUtils.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true, swing.getValue(), slot);
            placed++;
        }
    }

    public boolean canPlaceBlock(BlockPos pos) {
        if (intersectsWithEntity(pos) || mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
            return false;
        }
        return true;
    }

    public boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityXPOrb) continue;
            if (entity instanceof EntityItem) continue;
            if (entity instanceof EntityEnderCrystal) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

}
