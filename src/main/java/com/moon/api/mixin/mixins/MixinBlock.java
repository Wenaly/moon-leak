package com.moon.api.mixin.mixins;

import com.moon.Moon;
import com.moon.api.event.events.BlockCollisionBoundingBoxEvent;
import com.moon.impl.modules.movement.Fly;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Block.class)
public abstract class MixinBlock extends IForgeRegistryEntry.Impl<Block> {

    @Overwrite
    public static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable AxisAlignedBB blockBox) {
        if (blockBox != Block.NULL_AABB) {
            AxisAlignedBB axisalignedbb = blockBox.offset(pos);
            if (entityBox.intersects(axisalignedbb)) {
                collidingBoxes.add(axisalignedbb);
                BlockCollisionBoundingBoxEvent event = new BlockCollisionBoundingBoxEvent(pos);
                Moon.getEventProcessor().postEvent(event);
                //Fly.INSTANCE.onBlockBB(event);
            }
        }

    }


}
