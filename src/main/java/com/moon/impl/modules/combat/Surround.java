package com.moon.impl.modules.combat;

import com.moon.Moon;
import com.moon.api.event.events.MoveEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.priorities.Priorities;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.IntegerSetting;
import com.moon.api.utils.entity.EntityUtils;
import com.moon.api.utils.entity.InventoryUtils;
import com.moon.api.utils.player.PlayerUtils;
import com.moon.api.utils.entity.RotationUtils;
import com.moon.api.utils.world.BlockUtils;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

@Module.registration(name = "Surround", description = "Best surround in the market.", category = Module.Category.Combat, priority = Priorities.High)
public class Surround extends Module {

    public final Setting<Integer> blocksPerTick = new IntegerSetting("Blocks Per Tick", 16, 0, 20, this);
    public final Setting<Boolean> betterSwitch = new BooleanSetting("Better Switch", true, this);
    public final Setting<Boolean> stepCheck = new BooleanSetting("Step Check", true, this);
    public final Setting<Boolean> raytrace = new BooleanSetting("Raytrace", false, this);
    public final Setting<Boolean> cancelYMotion = new BooleanSetting("Cancel Y motion", true, this);
    public final Setting<Boolean> dynamicAntiPhase = new BooleanSetting("DynamicAntiPhase", true, this);
    public final Setting<Boolean> swing = new BooleanSetting("Swing", true, this);
    public final Setting<Boolean> antiPhase = new BooleanSetting("Anti Phase", true, this);
    public final Setting<Boolean> crystalClear = new BooleanSetting("Crystal Clear", true, this);
    public final Setting<Boolean> packetCrystal = new BooleanSetting("Packet Crystal", true, this);
    public final Setting<Boolean> center = new BooleanSetting("Center", false, this);
    public final Setting<Integer> centerPackets = new IntegerSetting("Center Packets", 3, 0, 10, this);
    public final Setting<Boolean> packet = new BooleanSetting("Packet", true, this);
    public final Setting<Boolean> rotate = new BooleanSetting("Rotate", false, this);
    public final Setting<Boolean> yCheck = new BooleanSetting("Y Check", true, this);
    public final Setting<Boolean> floor = new BooleanSetting("Floor", true, this);
    public int blocksPlaced = 0;
    BlockPos startPos = null;

    /** TODO: Make better antiPhase
     * @author Primooctopus33
     * @subauthor DriftyDev
    **/

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            this.disable();
            return;
        }
        int obsidianSlot = InventoryUtils.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = InventoryUtils.findHotbarBlock(BlockEnderChest.class);
        if (obsidianSlot == -1 && eChestSlot == -1) {
            this.disable();
            return;
        }
        if (stepCheck.getValue()) {
            Moon.Modules.getModuleByName("Step").disable();
        }
        startPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (center.getValue()) {
            BlockPos centerPos = new BlockPos(PlayerUtils.getPlayerPosFloored().getX() + 0.5, PlayerUtils.getPlayerPosFloored().getY(), PlayerUtils.getPlayerPosFloored().getZ() + 0.5);
            for (int i = 0; i < centerPackets.getValue(); i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(centerPos.getX() + 0.5f, mc.player.posY, centerPos.getZ() + 0.5f, true));
            }
            mc.player.setPosition(centerPos.getX() + 0.5f, mc.player.posY, centerPos.getZ() + 0.5f);
        }
    }

    @Override
    public void onTick() {
        blocksPlaced = 0;
        ArrayList<BlockPos> blocks = EntityUtils.getPos(0, 0, 0, mc.player);
        if (yCheck.getValue() && mc.player.posY != startPos.getY()) {
            this.disable();
        }
        if (blocks.size() == 1) {
            BlockPos block = blocks.get(0);
            BlockPos[] offsets = {block.north(), block.east(), block.south(), block.west()};

            doSurround(offsets);

            if (canPlaceBlock(block.down())) {
                placeBlock(block.down());
            }
        } else if (blocks.size() == 2) {
            BlockPos block = blocks.get(0);
            BlockPos block2 = blocks.get(1);
            BlockPos[] offsets = {block.north(), block.east(), block.south(), block.west()};
            BlockPos[] offsets2 = {block2.north(), block2.east(), block2.south(), block2.west()};

            doSurround(offsets, offsets2);
        } else if (blocks.size() == 4) {
            BlockPos block = blocks.get(0);
            BlockPos block2 = blocks.get(1);
            BlockPos block3 = blocks.get(2);
            BlockPos block4 = blocks.get(3);
            BlockPos[] offsets = {block.north(), block.east(), block.south(), block.west()};
            BlockPos[] offsets2 = {block2.north(), block2.east(), block2.south(), block2.west()};
            BlockPos[] offsets3 = {block3.north(), block3.east(), block3.south(), block3.west()};
            BlockPos[] offsets4 = {block4.north(), block4.east(), block4.south(), block4.west()};

            doSurround(offsets, offsets2, offsets3, offsets4);
        }
    }

    public void doSurround(BlockPos[]... offsets) {
        int oldSlot = mc.player.inventory.currentItem;
        if (betterSwitch.getValue()) {
            int obsidianSlot = InventoryUtils.findHotbarBlock(BlockObsidian.class);
            int eChestSlot = InventoryUtils.findHotbarBlock(BlockEnderChest.class);
            InventoryUtils.switchSlot(obsidianSlot != -1 ? obsidianSlot : eChestSlot, true);
        }
        for (BlockPos[] offset : offsets) {
            for (BlockPos pos : offset) {
                int obsidianSlot = InventoryUtils.findHotbarBlock(BlockObsidian.class);
                int eChestSlot = InventoryUtils.findHotbarBlock(BlockEnderChest.class);
                if (obsidianSlot == -1 && eChestSlot == -1) return;
                if (canPlaceBlock(pos)) {
                    placeBlock(pos);
                    if (floor.getValue() && canPlaceBlock(pos.down())) {
                        placeBlock(pos.down());
                    }
                }
                if (antiPhase.getValue() && intersectsWithLivingPlayer(pos)) {
                    if (dynamicAntiPhase.getValue()) {
                        doDynamicExtend(intersectingEntityPlayer(pos));
                    } else {
                        doExtend(pos);
                    }
                }
            }
        }
        if (betterSwitch.getValue()) {
            InventoryUtils.switchSlot(oldSlot, true);
            mc.playerController.updateController();
        }
    }

    public void doDynamicExtend(EntityPlayer player) {
        ArrayList<BlockPos> blocks = EntityUtils.getPos(0, 0, 0, player);
        if (blocks.size() == 1) {
            BlockPos block = blocks.get(0);
            BlockPos[] extend = {block.north(), block.east(), block.south(), block.west()};

            doExtend(extend);
        } else if (blocks.size() == 2) {
            BlockPos block = blocks.get(0);
            BlockPos block2 = blocks.get(1);
            BlockPos[] extend = {block.north(), block.east(), block.south(), block.west()};
            BlockPos[] extend2 = {block2.north(), block2.east(), block2.south(), block2.west()};

            doExtend(extend, extend2);
        } else if (blocks.size() == 4) {
            BlockPos block = blocks.get(0);
            BlockPos block2 = blocks.get(1);
            BlockPos block3 = blocks.get(2);
            BlockPos block4 = blocks.get(3);
            BlockPos[] extend = {block.north(), block.east(), block.south(), block.west()};
            BlockPos[] extend2 = {block2.north(), block2.east(), block2.south(), block2.west()};
            BlockPos[] extend3 = {block3.north(), block3.east(), block3.south(), block3.west()};
            BlockPos[] extend4 = {block4.north(), block4.east(), block4.south(), block4.west()};

            doExtend(extend, extend2, extend3, extend4);
        }
    }

    public void doExtend(BlockPos[]... positions) {
        for (BlockPos[] position : positions) {
            for (BlockPos pos : position) {
                if (canPlaceBlock(pos)) {
                    placeBlock(pos);
                }
                if (floor.getValue() && canPlaceBlock(pos.down())) {
                    placeBlock(pos.down());
                }
            }
        }
    }

    public void doExtend(BlockPos pos) {
        BlockPos[] extend = {pos.north(), pos.east(), pos.south(), pos.west()};
        for (BlockPos offsets : extend) {
            if (canPlaceBlock(offsets)) {
                placeBlock(offsets);
            }
            if (floor.getValue() && canPlaceBlock(offsets.down())) {
                placeBlock(offsets.down());
            }
        }
    }

    public boolean canPlaceBlock(BlockPos pos) {
        if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR && !intersectsWithEntity(pos) || mc.world.getBlockState(pos).getBlock() == Blocks.TALLGRASS && !intersectsWithEntity(pos)) {
            if (raytrace.getValue()) {
                return RotationUtils.canSeePosition(pos);
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean intersectsWithLivingPlayer(BlockPos pos) {
        for (Entity entity : mc.world.playerEntities) {
            if (entity.isDead) return false;
            if (!(entity instanceof EntityPlayer)) return false;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

    public EntityPlayer intersectingEntityPlayer(BlockPos pos) {
        for (Entity entity : mc.world.playerEntities) {
            if (entity.isDead) return null;
            if (!(entity instanceof EntityPlayer)) return null;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()) && entity != mc.player) return (EntityPlayer) entity;
        }
        return null;
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

    public void placeBlock(BlockPos pos) {
        int obsidianSlot = InventoryUtils.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = InventoryUtils.findHotbarBlock(BlockEnderChest.class);
        if (obsidianSlot == -1 && eChestSlot == -1) {
            this.disable();
            return;
        }
        if (crystalClear.getValue()) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal) {
                    EntityUtils.attackEntity(entity, packetCrystal.getValue(), false, true);
                }
            }
        }
        if (blocksPlaced < blocksPerTick.getValue()) {
            if (betterSwitch.getValue()) {
                BlockUtils.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), obsidianSlot != -1 ? false : true, swing.getValue(), -1);
            } else {
                BlockUtils.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), obsidianSlot != -1 ? false : true, swing.getValue(), obsidianSlot != -1 ? obsidianSlot : eChestSlot);
            }
            blocksPlaced++;
        }
    }

    @EventHandler
    public void onMove(MoveEvent event) {
        if (event.getStage() != 0) return;
        if (cancelYMotion.getValue()) {
            mc.player.setVelocity(mc.player.motionX, 0, mc.player.motionY);
            event.setY(0);
        }
    }
}
