package com.moon.api.utils.world;

import com.moon.Moon;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.entity.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockUtils implements IMinecraft {

    public static void clickBlock(final BlockPos position, final EnumFacing side, final EnumHand hand, final boolean packet) {
        if (packet) {
            BlockUtils.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(position.offset(side), side.getOpposite(), hand, 0.5f, 0.5f, 0.5f));
        }
        else {
            BlockUtils.mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, position.offset(side), side.getOpposite(), new Vec3d(position), hand);
        }
    }

    public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean sneaking, boolean swing, int slot) {
        EnumFacing side = BlockUtils.getPlaceableSide(pos);
        if (side == null) {
            return;
        }
        int oldSlot = mc.player.inventory.currentItem;
        float[] rot = getRotations(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        BlockPos neighbour = pos.offset(side);
        EnumFacing facing = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(facing.getDirectionVec()).scale(0.5));
        if (sneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        if (rotate) {
            Moon.getRotationManager().setRotations(rot[0], rot[1]);
        }
        if (slot != -1) {
            mc.player.inventory.currentItem = slot;
        }
        if (packet) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(neighbour, facing, hand, 0.5f, 0.5f, 0.5f));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, facing, hitVec, hand);
        }
        mc.player.inventory.currentItem = oldSlot;
        if (swing) {
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (sneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    public static boolean hasNeighbor(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
                return true;
            }
        }
        return false;
    }

    public static float[] getRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static void placeBlock(final BlockPos blockPos, final boolean swing, final boolean packet) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!mc.world.getBlockState(blockPos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(blockPos)) {
                if (packet) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos.offset(enumFacing), enumFacing.getOpposite(), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
                }
                else {
                    mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, blockPos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(blockPos), EnumHand.MAIN_HAND);
                }
                if (swing) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
                return;
            }
        }
    }

    public static void placeBlock(final BlockPos position, final EnumHand hand, final boolean packet) {
        if (!mc.world.getBlockState(position).getBlock().isReplaceable(mc.world, position)) {
            return;
        }
        if (getPlaceableSide(position) == null) {
            return;
        }
        clickBlock(position, getPlaceableSide(position), hand, packet);
        mc.player.connection.sendPacket(new CPacketAnimation(hand));
    }

    public static boolean isIntercepted(final BlockPos blockPos) {
        for (final Entity entity : BlockUtils.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                continue;
            }
            if (entity instanceof EntityEnderCrystal) {
                continue;
            }
            if (new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (BlockUtils.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtils.mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = BlockUtils.mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }

    public static boolean isPositionPlaceable(final BlockPos position, final boolean entityCheck, final boolean sideCheck) {
        if (!BlockUtils.mc.world.getBlockState(position).getBlock().isReplaceable(BlockUtils.mc.world, position)) {
            return false;
        }
        if (entityCheck) {
            for (final Object entity : BlockUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    return false;
                }
            }
        }
        return !sideCheck || getPlaceableSide(position) != null;
    }

    public static boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

}
