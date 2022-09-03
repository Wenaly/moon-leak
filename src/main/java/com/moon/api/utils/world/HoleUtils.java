package com.moon.api.utils.world;

import com.moon.api.utils.IMinecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class HoleUtils implements IMinecraft {

    public static boolean isHole(BlockPos pos, boolean above)
    {
        if (!BlockUtils.isAir(pos) || !BlockUtils.isAir(pos.up()) || above && !BlockUtils.isAir(pos.up(2)))
        {
            return false;
        }

        return is1x1(pos);
    }

    public static boolean is1x1(BlockPos pos)
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            if (facing != EnumFacing.UP)
            {
                BlockPos offset = pos.offset(facing);
                IBlockState state = mc.world.getBlockState(offset);
                if (state.getBlock() != Blocks.BEDROCK || state.getBlock() != Blocks.OBSIDIAN || state.getBlock() != Blocks.ENDER_CHEST)
                {
                    return false;
                }
            }
        }
        return true;
    }

}
