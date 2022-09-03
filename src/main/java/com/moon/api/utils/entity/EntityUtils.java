package com.moon.api.utils.entity;

import com.moon.api.utils.IMinecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class EntityUtils implements IMinecraft {

    public static ArrayList<BlockPos> getPos(double posX, double posY, double posZ, Entity entity) {
        ArrayList<BlockPos> block = new ArrayList<>();
        if (entity != null) {
            final AxisAlignedBB bb = entity.ridingEntity != null ? entity.ridingEntity.getEntityBoundingBox().contract(0.0d, 0.0d, 0.0d).offset(posX, posY, posZ) : entity.getEntityBoundingBox().contract(0.01d, 1d, 0.01d).offset(posX, posY, posZ);
            int y = (int) bb.minY;
            for (int x = (int) Math.floor(bb.minX); x < Math.floor(bb.maxX) + 1; x++) {
                for (int z = (int) Math.floor(bb.minZ); z < Math.floor(bb.maxZ) + 1; z++) {
                    block.add(new BlockPos(x, y, z));
                }
            }
        }
        return block;
    }

    public static EntityPlayer getTarget(float range) {
        EntityPlayer finalTarget = null;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer)) continue;
            if (entity.isDead) continue;
            if (entity.getPositionVector().distanceTo(mc.player.getPositionVector()) > range) continue;
            finalTarget = (EntityPlayer) entity;
        }
        return finalTarget;
    }

    public static void attackEntity(Entity entity, boolean packet, boolean swingArm, boolean packetSwing) {
        if (packet) {
            mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        } else {
            mc.playerController.attackEntity(mc.player, entity);
        }
        if (swingArm) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        if (packetSwing) {
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    public static float getHealth(final Entity entity) {
        if (entity.isEntityAlive()) {
            final EntityLivingBase livingBase = (EntityLivingBase) entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }

    public static boolean isInHole(Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        for (BlockPos pos : new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        for (BlockPos pos : new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        for (BlockPos pos : new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN))
                continue;
            return false;
        }
        return true;
    }

    public static boolean holding32k(final EntityPlayer player) {
        return is32k(player.getHeldItemMainhand());
    }

    public static boolean is32k(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getTagCompound() == null) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList) stack.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        int i = 0;
        while (i < enchants.tagCount()) {
            final NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                final int lvl = enchant.getInteger("lvl");
                if (lvl >= 42) {
                    return true;
                }
                break;
            } else {
                ++i;
            }
        }
        return false;
    }


}
